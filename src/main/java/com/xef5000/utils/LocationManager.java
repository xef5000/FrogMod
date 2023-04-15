package com.xef5000.utils;



import com.xef5000.FrogMod;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.gson.JsonObject;
import net.minecraftforge.fml.common.gameevent.TickEvent;


//CLASS STOLEN FROM NEU
public class LocationManager {

    private static final LocationManager INSTANCE = new LocationManager();

    private static final Pattern JSON_BRACKET_PATTERN = Pattern.compile("^\\{.+}");

    private long lastLocRaw = -1;
    public long joinedWorld = -1;

    private JsonObject locraw = null;
    public String mode = null;

    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load event) {
        setLocation(null);
        locraw = null;
        lastLocRaw = System.currentTimeMillis();
        joinedWorld = System.currentTimeMillis();
    }

    @SubscribeEvent(priority = EventPriority.LOW, receiveCanceled = true)
    public void onChatMessage(ClientChatReceivedEvent event) {
        Matcher matcher = JSON_BRACKET_PATTERN.matcher(event.message.getUnformattedText());
        if (matcher.find()) {
            try {
                JsonObject obj = FrogMod.INSTANCE.getGson().fromJson(matcher.group(), JsonObject.class);
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

    @SubscribeEvent
    public void onTick(TickEvent event) {
        long currentTime = System.currentTimeMillis();

        if (FrogMod.mc.thePlayer != null && FrogMod.mc.theWorld != null && locraw == null &&
                (currentTime - joinedWorld) > 1000 &&
                (currentTime - lastLocRaw) > 2500) {
            lastLocRaw = System.currentTimeMillis();
            FrogMod.mc.thePlayer.sendChatMessage("/locraw");
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
