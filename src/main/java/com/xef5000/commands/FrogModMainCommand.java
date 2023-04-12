package com.xef5000.commands;

import com.xef5000.FrogMod;
import com.xef5000.features.MilestoneOverlay;
import com.xef5000.gui.LocationsEditGUI;
import com.xef5000.utils.LocationManager;
import gg.essential.api.utils.GuiUtil;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class FrogModMainCommand extends CommandBase {

    private static final String[] SUBCOMMANDS = {"help", "edit", "milestone", "milestonereset"};

    private static final HashMap<String, String> SUBCOMMANDS_HELP = new HashMap<String, String>(){{
        put("edit", "Allows you to edit GUI elements locations");
        put("help", "Shows this help menu");
        put("milestonereset", "Resets your crop spead in case you changed gear. Can also be done manually in .minecraft/config/frogmod_persistent");
        put("milestone", "Shows you the best crop to harvest for the time spent");
    }};


    @Override
    public String getCommandName() {
        return "frog";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/" + getCommandName();
    }
    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }

    @Override
    public ArrayList<String> getCommandAliases() {
        ArrayList<String> al = new ArrayList<String>();
        al.add("frogmod");
        return al;
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        //FrogMod.INSTANCE.openGUI = FrogMod.INSTANCE.config.gui();
        if (args.length == 0) GuiUtil.open(Objects.requireNonNull(FrogMod.INSTANCE.getFrogModConfig().gui()));
        if (args.length >= 1) {
            if (args[0].equalsIgnoreCase("edit")) GuiUtil.open(new LocationsEditGUI());
            if (args[0].equalsIgnoreCase("milestonereset")) MilestoneOverlay.getInstance().resetCropSpeed();
            if (args[0].equalsIgnoreCase("milestone")) {

                for (String crop : MilestoneOverlay.getInstance().getTopCrops().keySet()) {
                    int nextMilestone = FrogMod.INSTANCE.getPersistentValuesManager().getPersistentValues().getMilestone().get(crop);
                    int progression = FrogMod.INSTANCE.getPersistentValuesManager().getPersistentValues().getMilestoneProgression().get(crop);
                    int bestCPM = FrogMod.INSTANCE.getPersistentValuesManager().getPersistentValues().getCropSpeed().get(crop);
                    int minutesLeft = (int) ((nextMilestone - progression) / (bestCPM * 0.90));
                    if(bestCPM != 0)FrogMod.mc.thePlayer.addChatMessage(new ChatComponentText("§e" + crop + " §7- §b" + String.format("%dh%02dm", minutesLeft/60, minutesLeft%60))); else FrogMod.mc.thePlayer.addChatMessage(new ChatComponentText("§e" + crop + " §7- §bN/A" ));
                }
            }
            if (args[0].equalsIgnoreCase("help")) {
                FrogMod.mc.thePlayer.addChatMessage(new ChatComponentText("§a---------------§f FrogMod §a---------------"));
                for (String command : SUBCOMMANDS) {
                    FrogMod.mc.thePlayer.addChatMessage(new ChatComponentText("§e/" + command + " §f" + SUBCOMMANDS_HELP.get(command)));
                }
            }
        }

    }
}
