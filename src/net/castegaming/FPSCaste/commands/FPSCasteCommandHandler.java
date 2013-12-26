package net.castegaming.FPSCaste.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class FPSCasteCommandHandler implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (command.equals("join")) {
			new JoinCommand(sender, args);
			return true;
		} else if (command.equals("leave") || command.equals("warzones")) {
			new LeaveCommand(sender, args);
			return true;
		} else if (command.equals("stopgame")){
			new StopGameCommand(sender, args);
			return true;
		} else if (command.equals("showgames")){
			new ShowgamesCommand(sender, args);
			return true;
		} else if (command.equals("allies")){
			new AlliesCommand(sender, args);
			return true;
		} else if (command.equals("axis")){
			 new AxisCommand(sender, args);
			return true;
		} else if (command.equals("chat") || command.equals("c")){
			
		}
		return false;
	}

}
