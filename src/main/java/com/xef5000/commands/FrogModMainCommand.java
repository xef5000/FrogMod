package com.xef5000.commands;

import com.xef5000.FrogMod;
import com.xef5000.gui.LocationsEditGUI;
import gg.essential.api.utils.GuiUtil;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FrogModMainCommand extends CommandBase {


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
        }

    }
}
