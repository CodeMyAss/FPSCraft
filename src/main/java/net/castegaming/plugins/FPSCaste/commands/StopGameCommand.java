package net.castegaming.plugins.FPSCaste.commands;

import java.util.logging.Level;

import net.castegaming.plugins.FPSCaste.FPSCaste;
import net.castegaming.plugins.FPSCaste.Match;
import net.castegaming.plugins.FPSCaste.exceptions.NoOnlinePlayerException;
import net.castegaming.plugins.FPSCaste.exceptions.NotAdminException;
import net.castegaming.plugins.FPSCaste.exceptions.NotIngameException;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class StopGameCommand extends FPSAdminCommand{

	public StopGameCommand(CommandSender sender, String[] args) throws NotAdminException, NotIngameException, NoOnlinePlayerException {
		super(sender, args, false);
	}

	@Override
	public boolean handle() {
		Match match;
		
		if (args.length > 0){
			try {
				match = FPSCaste.getMatch(Integer.parseInt(args[0]));
			} catch (Exception e){
				return false;
			}
		} else {
			if (getSender() instanceof Player){
				if (FPSCaste.getFPSPlayer(getSender().getName()).isIngame()){
					match = FPSCaste.getFPSPlayer(getSender().getName()).getMatch();
				} else{
					return false;
				}
			} else {
				return false;
			}
		}
		
		if (match != null){
			match.endGame("Your game has been stopped by an administrator! stats are saved. playtime: " + match.getPlayTime(), false);
			FPSCaste.log("Your game has been stopped by an administrator! stats are saved. playtime: " + match.getPlayTime(), Level.INFO);
			return true;
		} else {
			return false;
		}
	}

}
