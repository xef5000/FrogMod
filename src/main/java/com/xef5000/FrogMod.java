package com.xef5000;

import com.xef5000.commands.FrogModMainCommand;
import com.xef5000.config.FrogModConfig;
import com.xef5000.features.TerminalOverlay;
import com.xef5000.listeners.ChatListener;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;


@Mod(modid = "frogmod", version = "1.1.0")
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

    public FrogModConfig getFrogModConfig() {
        return config;
    }
}
