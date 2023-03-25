package com.xef5000.commands;

import com.xef5000.FrogMod;
import com.xef5000.features.CrystalScanner;
import com.xef5000.utils.LocationManager;
import gg.essential.api.utils.GuiUtil;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

import javax.xml.stream.Location;
import java.util.Objects;

public class CHScanCommand extends CommandBase {

    @Override
    public String getCommandName() {
        return "chscan";
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
    public void processCommand(ICommandSender sender, String[] args) {
        //FrogMod.mc.thePlayer.addChatMessage(new ChatComponentText(EnumChatFormatting.GREEN + "FrogMod -> " + EnumChatFormatting.WHITE + "Starting scan..."));
        //FrogMod.mc.thePlayer.addChatMessage(new ChatComponentText(EnumChatFormatting.GREEN + "FrogMod -> " + EnumChatFormatting.WHITE + LocationManager.getInstance().getLocation()));
        //FrogMod.mc.thePlayer.addChatMessage(new ChatComponentText(EnumChatFormatting.GREEN + "FrogMod -> " + EnumChatFormatting.WHITE + FrogMod.INSTANCE.hasSkyblockScoreboard()));
        //ThroneFinder.CHScan();
        //FrogMod.mc.thePlayer.addChatMessage(new ChatComponentText(EnumChatFormatting.GREEN + "FrogMod -> " + EnumChatFormatting.WHITE + "Starting scan..."));
        //CrystalScanner.getInstance().scan();

        //FrogMod.mc.thePlayer.addChatMessage(new ChatComponentText(EnumChatFormatting.GREEN + "FrogMod -> " + EnumChatFormatting.WHITE + "Finished scan..."));
        //CrystalScanner.isitxalx(521, 109, 611);

    }
}
