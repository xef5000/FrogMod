package com.xef5000;

import com.xef5000.commands.FrogModMainCommand;
import com.xef5000.config.FrogModConfig;
import com.xef5000.features.TerminalOverlay;
import com.xef5000.listeners.ChatListener;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.init.Blocks;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

@Mod(modid = "frogmod", version = "1.0.0")
public class FrogMod {


    public GuiScreen openGUI = null;
    private FrogModConfig config = null;
    public static Minecraft mc;
    @Mod.Instance
    public static FrogMod INSTANCE = null;


    @Mod.EventHandler
    public void preinit(FMLPreInitializationEvent event) {
        INSTANCE = this;
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(new ChatListener()); // Chat Listeners
        MinecraftForge.EVENT_BUS.register(new TerminalOverlay());
        mc = Minecraft.getMinecraft();


    }
    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        config = new FrogModConfig();
        config.preload();
        ClientCommandHandler.instance.registerCommand(new FrogModMainCommand());

    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if(event.phase != TickEvent.Phase.START) {
            return;
        }
        if (Minecraft.getMinecraft().thePlayer == null) {
            openGUI = null;
            return;
        }
        if (openGUI != null) {
            if (Minecraft.getMinecraft().thePlayer.openContainer != null) {
                Minecraft.getMinecraft().thePlayer.closeScreen();
            }
            Minecraft.getMinecraft().displayGuiScreen(openGUI);
            openGUI = null;
        }
    }

    public FrogModConfig getFrogModConfig() {
        return config;
    }
}
