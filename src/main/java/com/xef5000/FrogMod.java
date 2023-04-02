package com.xef5000;

import com.google.common.collect.Sets;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.xef5000.commands.FrogModMainCommand;
import com.xef5000.gui.FrogModConfig;
import com.xef5000.features.*;
import com.xef5000.listeners.ChatListener;
import com.xef5000.listeners.RenderEntityListener;
import com.xef5000.listeners.RenderListener;
import com.xef5000.utils.LocationManager;
import com.xef5000.utils.Visual;
import com.xef5000.utils.WaypointsManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


@Mod(modid = "frogmod", version = "1.1-pre2")
public class FrogMod {

    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private final Set<Integer> registeredFeatureIDs = new HashSet<>();

    private FrogModConfig config = null;
    private ConfigValues configValues;
    public static Minecraft mc;
    @Mod.Instance
    public static FrogMod INSTANCE = null;
    private boolean hasSkyblockScoreboard;
    private static final Set<String> SKYBLOCK_IN_ALL_LANGUAGES = Sets.newHashSet("SKYBLOCK", "\u7A7A\u5C9B\u751F\u5B58", "\u7A7A\u5CF6\u751F\u5B58");
    private static final ThreadPoolExecutor THREAD_EXECUTOR = new ThreadPoolExecutor(0, 1, 60L, TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(), new ThreadFactoryBuilder().setNameFormat("FrogMod" + " - #%d").build());



    @Mod.EventHandler
    public void preinit(FMLPreInitializationEvent event) {
        INSTANCE = this;
        configValues = new ConfigValues(event.getSuggestedConfigurationFile());
        configValues.loadValues();

        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(new ChatListener()); // Chat Listeners
        MinecraftForge.EVENT_BUS.register(new TerminalOverlay());
        //MinecraftForge.EVENT_BUS.register(new ThroneFinder());
        //MinecraftForge.EVENT_BUS.register(new XaltFinder());
        MinecraftForge.EVENT_BUS.register(new UnbreakableCobble());
        MinecraftForge.EVENT_BUS.register(new WaypointsManager());
        //MinecraftForge.EVENT_BUS.register(new BarbarianDukeESP());
        MinecraftForge.EVENT_BUS.register(new RenderEntityListener());
        MinecraftForge.EVENT_BUS.register(CrystalScanner.getInstance());
        MinecraftForge.EVENT_BUS.register(LocationManager.getInstance());
        MinecraftForge.EVENT_BUS.register(RenderListener.getInstance());

        //Visual.renderManager = FrogMod.mc.getRenderManager();


    }
    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        config = new FrogModConfig();
        config.preload();
        //ThroneFinder.scannerWidth = config.throneFinderRange;
        ClientCommandHandler.instance.registerCommand(new FrogModMainCommand());
        mc = Minecraft.getMinecraft();

    }

    public FrogModConfig getFrogModConfig() {
        return config;
    }



    public boolean hasSkyblockScoreboard() {
        return hasSkyblockScoreboard;
    }

    // Taken from NEU, which was stolen from SBA
    public void updateSkyblockScoreboard() {
        Minecraft mc = Minecraft.getMinecraft();

        if (mc != null && mc.theWorld != null && mc.thePlayer != null) {
            if (mc.isSingleplayer() || mc.thePlayer.getClientBrand() == null || !mc.thePlayer.getClientBrand().toLowerCase().contains("hypixel")) {
                hasSkyblockScoreboard = false;
                return;
            }

            Scoreboard scoreboard = mc.theWorld.getScoreboard();
            ScoreObjective sidebarObjective = scoreboard.getObjectiveInDisplaySlot(1);
            if (sidebarObjective != null) {
                String objectiveName = sidebarObjective.getDisplayName().replaceAll("(?i)\\u00A7.", "");
                for (String skyblock : SKYBLOCK_IN_ALL_LANGUAGES) {
                    if (objectiveName.startsWith(skyblock)) {
                        hasSkyblockScoreboard = true;
                        return;
                    }
                }
            }

            hasSkyblockScoreboard = false;
        }
    }

    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load event) {
        updateSkyblockScoreboard();
        Visual.showTitle("§aFrogmod", "§fLoading world...", 0, 1, 0); //To make sure that all titles register correctly


    }

    public Set<Integer> getRegisteredFeatureIDs() { return registeredFeatureIDs; }
    public Gson getGson() {return gson;}
    public ConfigValues getConfigValues() {return configValues;}

    public static void runAsync(Runnable runnable) {
        THREAD_EXECUTOR.execute(runnable);
    }





}
