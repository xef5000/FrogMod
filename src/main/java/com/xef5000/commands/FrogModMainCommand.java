package com.xef5000.commands;

import com.xef5000.FrogMod;
import gg.essential.api.utils.GuiUtil;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;

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
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        //FrogMod.INSTANCE.openGUI = FrogMod.INSTANCE.config.gui();
        GuiUtil.open(Objects.requireNonNull(FrogMod.INSTANCE.getFrogModConfig().gui()));
    }
}
