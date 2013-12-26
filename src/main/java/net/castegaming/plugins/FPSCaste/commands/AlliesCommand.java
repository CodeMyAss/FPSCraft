package net.castegaming.plugins.FPSCaste.commands;

import net.castegaming.plugins.FPSCaste.FPSCaste;
import net.castegaming.plugins.FPSCaste.enums.teamName;
import net.castegaming.plugins.FPSCaste.exceptions.IngameException;
import net.castegaming.plugins.FPSCaste.exceptions.NoOnlinePlayerException;
import net.castegaming.plugins.FPSCaste.exceptions.NotIngameException;

import org.bukkit.command.CommandSender;

public class AlliesCommand extends FPSCommandBase{

	public AlliesCommand(CommandSender sender, String[] args) throws NotIngameException, NoOnlinePlayerException, IngameException {
		super(sender, args, true, false);
		if (FPSCaste.getFPSPlayer(sender.getName()).getTeam() != null){
			//joined a game
			if (FPSCaste.getFPSPlayer(sender.getName()).getTeam() != teamName.SPECTATOR){
				//cannot be ingame if you want to /join
				throw new IngameException();
			}
		}
	}

	@Override
	public boolean handle() {
		//TODO make a check for team size
		FPSCaste.getFPSPlayer(this.getSender().getName()).join(teamName.ALLIES);
		return true;
	}

}
