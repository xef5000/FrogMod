package com.xef5000.config;


import gg.essential.vigilance.Vigilant;
import gg.essential.vigilance.data.Property;
import gg.essential.vigilance.data.PropertyType;


import java.io.File;


public class FrogModConfig extends Vigilant {


    @Property(
            type = PropertyType.SWITCH, name = "Throne Finder",
            description = "Will try to find thrones in the crystals hollows.",
            category = "Mining", subcategory = "Structure Finder"
    )
    public boolean throneFinder = false;

    @Property(
            type = PropertyType.SLIDER, name = "Throne Finder Range",
            description = "Range for throne finder in blocks. Recommended is 110 or under",
            category = "Mining", subcategory = "Structure Finder", min = 15, max = 150
    )
    public int throneFinderRange = 30;


    @Property(
            type = PropertyType.SWITCH, name = "Xalx Finder",
            description = "Will try to find xalx in the crystals hollows. Uses hypixel's render distance, so around 2 chunks.",
            category = "Mining", subcategory = "Structure Finder"
    )
    public boolean xaltFinder = false;


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



    public FrogModConfig() {
        super(new File("./config/frogmod.toml"), "FrogMod");
        setCategoryDescription("Automatic Messages", "Here you can add/customize the automatic messages that will be sent in chat.");
        initialize();
    }



}