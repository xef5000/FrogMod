package com.xef5000.gui;


import com.xef5000.Feature;
import gg.essential.api.utils.GuiUtil;
import gg.essential.vigilance.Vigilant;
import gg.essential.vigilance.data.*;
import org.jetbrains.annotations.NotNull;


import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Comparator;


public class FrogModConfig extends Vigilant {
    //@Property(
    //        type = PropertyType.SWITCH, name = "Test Text",
    //        description = "test text",
    //        category = "Test", subcategory = "Test"
    //)
    public boolean testText = true;


    // *
    // * Mining
    // *
    @Property(
            type = PropertyType.SWITCH, name = "Crystal Scanner",
            description = "Main toggle for the crystal scanner feature. Check the locations you want to be scanned down below.",
            category = "Mining", subcategory = "Structure Finder"
    )
    public boolean crystalScanner = false;

    @Property(
            type = PropertyType.SLIDER, name = "Crystal Scanner Delay",
            description = "Delay between each map scan, in ticks (1s = 20 ticks). Range is about 100-150 blocks, not changeable.",
            category = "Mining", subcategory = "Structure Finder", min = 20, max = 300
    )
    public int crystalScannerDelay = 100;

    @Property(
            type = PropertyType.CHECKBOX, name = "Main Structures",
            description = "Areas: Jungle Temple, Divan's Mines, Precursor City, Goblin King, Goblin Queen and Bal.",
            category = "Mining", subcategory = "Structure Finder"
    )
    public boolean crystalScannerMainStructures1 = false;

    @Property(
            type = PropertyType.CHECKBOX, name = "Important Structures",
            description = "Areas: Corleone, Grotto and Throne",
            category = "Mining", subcategory = "Structure Finder"
    )
    public boolean crystalScannerImportantStructures2 = false;

    @Property(
            type = PropertyType.CHECKBOX, name = "Other Structures",
            description = "Areas: Xalx, Pete and Odawa",
            category = "Mining", subcategory = "Structure Finder"
    )
    public boolean crystalScannerOtherStructures3 = false;

    @Property(
            type = PropertyType.SWITCH, name = "Unbreakable Cobble",
            description = "This will make all cobblestone unbreakable. Useful when making platforms in the crystal hollows",
            category = "Mining", subcategory = "Crystal Hollows"
    )
    public boolean unbreakableCobble = false;

    @Property(
            type = PropertyType.SWITCH, name = "Worm/Scatha alert",
            description = "Alerts you of worm/scathas",
            category = "Mining", subcategory = "Crystal Hollows"
    )
    public boolean scathaAlert = false;



    // *
    // * Dungeons
    // *
    @Property(
            type = PropertyType.TEXT, name = "Toxic death message",
            description = "Will send a message when a player dies. Example: \"player you should download FrogMod to stop dying!\"",
            category = "Dungeons", subcategory = "Death Message"
    )
    public String deathMessageMessage = "player you should download FrogMod to stop dying!";

    @Property(
            type = PropertyType.SWITCH, name = "Toxic death message toggle",
            description = "Toggle the feature",
            category = "Dungeons", subcategory = "Death Message"
    )
    public boolean toxicDeathMessage = true;

    @Property(
            type = PropertyType.SWITCH, name = "Watcher Ready",
            description = "Toggle the feature",
            category = "Dungeons", subcategory = "Watcher"
    )
    public boolean watcherReady = true;
    @Property(
            type = PropertyType.SWITCH, name = "Watcher Cleared",
            description = "Toggle the feature",
            category = "Dungeons", subcategory = "Watcher"
    )
    public boolean watcherCleared = true;

    @Property(
            type = PropertyType.SWITCH, name = "Special 270 score message",
            description = "It will send a special message when others say that the 270 score has been reached.",
            category = "Dungeons", subcategory = "Score"
    )
    public boolean special270Score = true;

    @Property(
            type = PropertyType.SWITCH, name = "Special 300 score message",
            description = "It will send a special message when others say that the 300 score has been reached.",
            category = "Dungeons", subcategory = "Score"
    )
    public boolean special300Score = true;

    @Property(
            type = PropertyType.SWITCH, name = "Show extra stats",
            description = "At the end of a dungeon, it will run /showextrastats to show you extra stats about the run.",
            category = "Dungeons", subcategory = "General"
    )
    public boolean showExtraStats = true;

    @Property(
            type = PropertyType.SWITCH, name = "Terminal Overlay",
            description = "You will see terminals through walls, allowing you to know which ones are not completed",
            category = "Dungeons", subcategory = "Terminal Overlay"
    )
    public boolean terminalOverlay = true;

    // *
    // * General
    // *

    @Property(
            type = PropertyType.SWITCH, name = "Better Trade Menu",
            description = "Adds a trade menu button in the skyblock menu",
            category = "General", subcategory = "QOL"
    )
    public boolean betterTradeMenu = true;

    @Property(
            type = PropertyType.SWITCH, name = "Full NPC Price",
            description = "Shows you the money you can make if you sold all item of that type in the trade menu",
            category = "General", subcategory = "QOL"
    )
    public boolean fullNpcPrice = true;

    @Property(
            type = PropertyType.SWITCH, name = "Artificial Ferocity",
            description = "It will do ferocity sounds when you hit mobs (melee/bows) even if you don't have ferocity. SOUND ONLY.",
            category = "General", subcategory = "Combat"
    )
    public boolean artificialFerocity = true;

    @Property(
            type = PropertyType.SWITCH, name = "Barbarian Duke ESP",
            description = "Allows you to see barbarian duke through walls",
            category = "General", subcategory = "Combat"
    )
    public boolean barbarianDukeESP = false;

    @Property(
            type = PropertyType.SWITCH, name = "Milestone Display",
            description = "Displays information about your current milestone",
            category = "General", subcategory = "Farming"
    )
    public boolean milestoneDisplay = true;

    @Property(
            type = PropertyType.SWITCH, name = "Visitor Notifier",
            description = "Notifies you when a visitor joins you",
            category = "General", subcategory = "Farming"
    )
    public boolean visitorNotifier = true;

    @Property(
            type = PropertyType.SWITCH, name = "1x1x1 Crops",
            description = "Makes all crops 1x1x1",
            category = "General", subcategory = "Farming"
    )
    public boolean oneByOneCrops = true;


    //
    // Other
    //
    @Property(
            type = PropertyType.BUTTON, name = "Join Discord",
            description = "Join discord to get updates, notifications and news about the mod",
            category = "Other", subcategory = "Discord", placeholder = "Join"
    )
    void joinDiscord() throws URISyntaxException, IOException {
        java.awt.Desktop.getDesktop().browse(
                new URI("https://discord.gg/Xaw7btnMka")
        );
    }
    @Property(
            type = PropertyType.BUTTON, name = "Edit Locations",
            description = "Edit the locations of the GUI elements",
            category = "Other", subcategory = "GUI", placeholder = "Edit"
    )
    void editGUI() {
        GuiUtil.open(new LocationsEditGUI());
    }



    public FrogModConfig() {
        super(new File("./config/frogmod.toml"), "FrogMod", new JVMAnnotationPropertyCollector(), new FrogModSortingBehavior());
        setCategoryDescription("Automatic Messages", "Here you can add/customize the automatic messages that will be sent in chat.");
        initialize();

    }






}