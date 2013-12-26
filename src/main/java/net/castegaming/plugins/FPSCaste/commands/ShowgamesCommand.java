package net.castegaming.plugins.FPSCaste.commands;

import java.util.HashMap;

import net.castegaming.plugins.FPSCaste.FPSCaste;
import net.castegaming.plugins.FPSCaste.FPSPlayer;
import net.castegaming.plugins.FPSCaste.Match;
import net.castegaming.plugins.FPSCaste.exceptions.NoOnlinePlayerException;
import net.castegaming.plugins.FPSCaste.exceptions.NotIngameException;

import org.bukkit.command.CommandSender;

public class ShowgamesCommand extends FPSCommandBase{

	public ShowgamesCommand(CommandSender sender, String[] args) throws NotIngameException, NoOnlinePlayerException {
		super(sender, args, false, false);
	}

	@Override
	public boolean handle() {
		//show all matches with their ID
		HashMap<String, FPSPlayer> players = FPSPlayer.customPlayers;
		for (String name : players.keySet()){
			if (players.get(name).isIngame()){
				msg(name + " is in match " + players.get(name).getMatch().getMatchID() + " team: " + players.get(name).getTeam());
			} else {
				msg(name + " is not ingame");
			}
		}
		
		HashMap<Integer, Match> matches = Match.currentMatches;
		for (Integer ID : matches.keySet()){
			msg(FPSCaste.getMatch(ID).toString());
		}
		return true;
	}
}
