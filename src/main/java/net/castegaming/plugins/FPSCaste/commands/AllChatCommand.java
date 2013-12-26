package net.castegaming.plugins.FPSCaste.commands;

import net.castegaming.plugins.FPSCaste.exceptions.NoOnlinePlayerException;
import net.castegaming.plugins.FPSCaste.exceptions.NotIngameException;

import org.bukkit.command.CommandSender;

public class AllChatCommand extends FPSCommandBase{

	public AllChatCommand(CommandSender sender, String[] args) throws NotIngameException, NoOnlinePlayerException {
		super(sender, args, true, true);
	}

	@Override
	public boolean handle() {
		
		return true;
	}

}
