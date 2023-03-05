package com.xef5000.listeners;

import com.xef5000.FrogMod;
import com.xef5000.features.TerminalOverlay;
import com.xef5000.utils.Visual;
import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;


public class ChatListener {

    @SubscribeEvent
    public void onClientChatReceived(ClientChatReceivedEvent event) {
        String stripped = EnumChatFormatting.getTextWithoutFormattingCodes(event.message.getUnformattedText());

        if (FrogMod.INSTANCE.getFrogModConfig().toxicDeathMessageBol) {
            if(stripped.contains("☠") && stripped.endsWith("became a ghost.") && !stripped.contains(":")) {
                String deadPlayer = stripped.split(" ")[2];
                if(deadPlayer.equals("You")) return;
                Minecraft.getMinecraft().thePlayer.sendChatMessage("/pc " + FrogMod.INSTANCE.getFrogModConfig().toxicDeathMessageStr.replace("player", deadPlayer));

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
        if (stripped.toLowerCase().contains("abcdef")) {
            System.out.println("Found abcdef in in chat");
        }
        if (stripped.contains("[BOSS] Goldor")) {
            TerminalOverlay.scanMap();
        }
        if (stripped.contains("activated a terminal")) {
            TerminalOverlay.scanMap();
        }
        if (stripped.contains("[BOSS] Goldor: Little ants, plotting and scheming, thinking they are invincible…")) {
            TerminalOverlay.phase3 = true;
        }
        if (stripped.contains("[BOSS] Necron: Goodbye.")) {
            TerminalOverlay.phase3 = false;
            TerminalOverlay.resetRenderQueues();
        }




    }


}
