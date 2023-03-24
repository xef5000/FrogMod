package com.xef5000.utils;



import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;



//CLASS STOLEN FROM NEU
public class LocationManager {

    private static final LocationManager INSTANCE = new LocationManager();

    private static final Pattern JSON_BRACKET_PATTERN = Pattern.compile("^\\{.+}");
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private JsonObject locraw = null;
    public String mode = null;

    @SubscribeEvent(priority = EventPriority.LOW, receiveCanceled = true)
    public void onChatMessage(ClientChatReceivedEvent event) {
        Matcher matcher = JSON_BRACKET_PATTERN.matcher(event.message.getUnformattedText());
        if (matcher.find()) {
            try {
                JsonObject obj = gson.fromJson(matcher.group(), JsonObject.class);
                if (obj.has("server")) {
                    if (obj.has("gametype") && obj.has("mode") && obj.has("map")) {
                        locraw = obj;
                        setLocation(locraw.get("mode").getAsString());
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void setLocation(String location) {
        this.mode = location;
    }
    public String getLocation() {
        return mode;
    }

    public static LocationManager getInstance() {
        return INSTANCE;
    }

}
