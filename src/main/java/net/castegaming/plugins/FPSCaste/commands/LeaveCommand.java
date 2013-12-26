package net.castegaming.plugins.FPSCaste.commands;

import net.castegaming.plugins.FPSCaste.FPSCaste;
import net.castegaming.plugins.FPSCaste.exceptions.NoOnlinePlayerException;
import net.castegaming.plugins.FPSCaste.exceptions.NotIngameException;

import org.bukkit.command.CommandSender;

public class LeaveCommand extends FPSCommandBase{

	public LeaveCommand(CommandSender sender, String[] args) throws NotIngameException, NoOnlinePlayerException {
		super(sender, args, true, true);
	}

	@Override
	public boolean handle() {
		FPSCaste.getFPSPlayer(this.getSender().getName()).leave();
		return true;
	}

}
