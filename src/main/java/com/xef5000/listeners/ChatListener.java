package com.xef5000.listeners;

import com.xef5000.FrogMod;
import com.xef5000.features.TerminalOverlay;
import com.xef5000.utils.LocationManager;
import com.xef5000.utils.Visual;
import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class ChatListener {

    @SubscribeEvent
    public void onClientChatReceived(ClientChatReceivedEvent event) {
        String stripped = EnumChatFormatting.getTextWithoutFormattingCodes(event.message.getUnformattedText());

        if (FrogMod.INSTANCE.getFrogModConfig().toxicDeathMessage) {
            if(stripped.contains("☠") && stripped.endsWith("became a ghost.") && !stripped.contains(":")) {
                String deadPlayer = stripped.split(" ")[2];
                if(deadPlayer.equals("You") || deadPlayer.equals(FrogMod.mc.thePlayer.getName())) return;
                Minecraft.getMinecraft().thePlayer.sendChatMessage("/pc " + FrogMod.INSTANCE.getFrogModConfig().deathMessageMessage.replace("player", deadPlayer));

            }
        }
        if (FrogMod.INSTANCE.getFrogModConfig().watcherReady) {
            if (stripped.equals("[BOSS] The Watcher: That will be enough for now.")) {
                Visual.showTitle("§cWATCHER READY", "", 5, 45, 5);
                Minecraft.getMinecraft().thePlayer.playSound("random.orb", 1, 6);
                Minecraft.getMinecraft().thePlayer.playSound("game.potion.smash", 1, 4);
            }
        }
        if (FrogMod.INSTANCE.getFrogModConfig().watcherCleared) {
            if (stripped.equals("[BOSS] The Watcher: You have proven yourself. You may pass.")) {
                Visual.showTitle("§4WATCHER CLEARED", "", 5, 45, 5);
                Minecraft.getMinecraft().thePlayer.playSound("random.orb", 1, 6);
                Minecraft.getMinecraft().thePlayer.playSound("mob.cat.meow", 1, 4);
            }
        }
        if(FrogMod.INSTANCE.getFrogModConfig().special270Score) {
            if (stripped.toLowerCase().contains("270 score") && !stripped.contains(Minecraft.getMinecraft().thePlayer.getName()) && stripped.contains("Party") && stripped.contains(":")) {
                Minecraft.getMinecraft().thePlayer.sendChatMessage("/pc ゜゛゜゛゜゛゜゛゜゛゜゛゜゛゜゛゜゛゜゛゜゛゜゛゜゛゜゛゜゛゜゛゜゛゜ ゛゜゛████゜████゛████゜゛゜゛゜゛゜゛゜゛゜゛゜゛゜゛゜゛ ゜゛゜゛゜゛█゛゜゛゜█゜█゜゛█゛゜゛゜゛゜゛゜゛゜゛゜゛゜゛゜゛゜ ゛゜゛゜██゛゜゛゜█゜゛█゛゛█゜゛゜゛゜゛゜゛゜゛゜゛゜゛゜゛゜゛ ゜゛゜█゜゛゜゛゜█゜゛゜█゜゛█゛゜゛゜゛゜゛゜゛゜゛゜゛゜゛゜゛゜ ゛゜゛████゜゛█゛゜゛████゜゛゜゛゜゛゜゛゜゛゜゛゜゛゜゛゜゛ ゜゛゜゛゜゛゜゛゜゛゜゛゜゛゜゛゜゛゜゛゜゛゜");
            }
        }
        if(FrogMod.INSTANCE.getFrogModConfig().special300Score) {
            if (stripped.toLowerCase().contains("300 score") && !stripped.contains(Minecraft.getMinecraft().thePlayer.getName()) && stripped.contains("Party") && stripped.contains(":")) {
                Minecraft.getMinecraft().thePlayer.sendChatMessage("/pc ゜゛゜゛゜゛゜゛゜゛゜゛゜゛゜゛゜゛゜゛゜゛゜゛゜゛゜゛゜゛゜゛゜゛゜ ゛゜゛████゜████゛████゜゛゜゛゜゛゜゛゜゛゜゛゜゛゜゛゜゛ ゜゛゜゛゜゛█゛█゛゜█゜█゜゛█゛゜゛゜゛゜゛゜゛゜゛゜゛゜゛゜゛゜ ゛゜゛████゜█゜゛█゛█゛゛█゜゛゜゛゜゛゜゛゜゛゜゛゜゛゜゛゜゛ ゜゛゜゛゜゛█゛█゛゜█゜█゜゛█゛゜゛゜゛゜゛゜゛゜゛゜゛゜゛゜゛゜ ゛゜゛████゜████゛████゜゛゜゛゜゛゜゛゜゛゜゛゜゛゜゛゜゛ ゜゛゜゛゜゛゜゛゜゛゜゛゜゛゜゛゜゛゜゛゜゛゜");
            }
        }
        if(FrogMod.INSTANCE.getFrogModConfig().showExtraStats) {
            if (stripped.toLowerCase().contains("> extra stats <") && !stripped.contains(":")) {
                Minecraft.getMinecraft().thePlayer.sendChatMessage("/showextrastats");
            }
        }
        if (stripped.contains("[BOSS] Goldor") && FrogMod.INSTANCE.getFrogModConfig().terminalOverlay) {
            TerminalOverlay.scanMap();
        }
        if (stripped.contains("activated a terminal") && FrogMod.INSTANCE.getFrogModConfig().terminalOverlay) {
            TerminalOverlay.scanMap();
        }
        if (stripped.contains("[BOSS] Goldor: Little ants, plotting and scheming, thinking they are invincible…") && FrogMod.INSTANCE.getFrogModConfig().terminalOverlay) {
            TerminalOverlay.phase3 = true;
        }
        if (stripped.contains("[BOSS] Necron: Goodbye.") && FrogMod.INSTANCE.getFrogModConfig().terminalOverlay) {
            TerminalOverlay.phase3 = false;
            TerminalOverlay.resetRenderQueues();
        }
        if (stripped.equals("You hear the sound of something approaching...") && FrogMod.INSTANCE.getFrogModConfig().scathaAlert) {
            Visual.showTitle("§cWORM SPAWNED!", "", 5, 45, 5);
            Minecraft.getMinecraft().thePlayer.playSound("random.orb", 1, 3);
        }
        if (stripped.contains("has arrived on your Garden!") && FrogMod.INSTANCE.getFrogModConfig().visitorNotifier && LocationManager.getInstance().getLocation().equals("garden")) {
            String truevisitor = stripped.split("has arrived on your Garden!")[0];
            Visual.showTitle("§cNEW VISITOR", "§3" + truevisitor, 5, 45, 5);
            Minecraft.getMinecraft().thePlayer.playSound("random.orb", 1, 6);
        }




    }


}
