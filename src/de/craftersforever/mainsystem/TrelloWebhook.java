package de.craftersforever.mainsystem;

import com.google.gson.JsonObject;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import javax.jws.soap.SOAPBinding;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.logging.Level;

public class TrelloWebhook {
    private boolean enabled;
    private URL url;
    private String key, token, listID;

    public TrelloWebhook(String listID, String key, String token) {
        this.listID = listID;
        this.key = key;
        this.token = token;
        try {
            url = new URL("https://api.trello.com/1/cards");
        } catch (MalformedURLException e) {
            Bukkit.getServer().getLogger().log(Level.SEVERE, "Failed to register Trello Webhook! Is the given url " +
                    "correct?");
            enabled = false;
        }
    }

    public boolean createNewCard(String creator, String text, Location location) {
        Date dt = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(dt);
        c.add(Calendar.DATE, 2);
        dt = c.getTime();
        TimeZone tz = TimeZone.getTimeZone("UTC");
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'"); // Quoted "Z" to indicate UTC, no timezone offset
        df.setTimeZone(tz);
        String nowAsISO = df.format(dt);

        String name = creator + "'s Ticket";
        String description = "*" + text + "*\n\n" + location.getWorld().getName() +
                " " + location.getBlockX() + " " + location.getBlockY() + " " + location.getBlockZ();
        JsonObject obj = new JsonObject();
        obj.addProperty("idList", listID);
        obj.addProperty("key", key);
        obj.addProperty("token", token);
        obj.addProperty("name", name);
        obj.addProperty("desc", description);
        obj.addProperty("due", nowAsISO);
        HttpURLConnection postConnection;
        try {
            postConnection = (HttpURLConnection) url.openConnection();
        } catch (IOException e) {
            Bukkit.getLogger().log(Level.SEVERE, "Cannot connect to Trello API");
            return false;
        }
        try {
            postConnection.setRequestMethod("POST");
        } catch (ProtocolException e) {
            Bukkit.getLogger().log(Level.SEVERE, "Protocol error on trello API");
            return false;
        }
        postConnection.setRequestProperty("Content-Type", "application/json");
        postConnection.setDoOutput(true);
        OutputStream os;
        InputStream is;
        try {
            os = postConnection.getOutputStream();
            os.write(obj.toString().getBytes());
            os.flush();
            os.close();
            int responseCode = postConnection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                return true;
            } else {
                Bukkit.getLogger().log(Level.SEVERE, responseCode + "Failed to create Ticket");
                return false;
            }

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

    }
}
