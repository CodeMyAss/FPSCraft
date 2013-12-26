package net.castegaming.plugins.FPSCaste.commands;

import java.util.Random;

import net.castegaming.plugins.FPSCaste.FPSCaste;
import net.castegaming.plugins.FPSCaste.FPSPlayer;
import net.castegaming.plugins.FPSCaste.enums.gameState;
import net.castegaming.plugins.FPSCaste.enums.teamName;
import net.castegaming.plugins.FPSCaste.exceptions.IngameException;
import net.castegaming.plugins.FPSCaste.exceptions.NoOnlinePlayerException;
import net.castegaming.plugins.FPSCaste.exceptions.NotIngameException;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

public class JoinCommand extends FPSCommandBase{

	public JoinCommand(CommandSender sender, String[] args) throws NotIngameException, IngameException, NoOnlinePlayerException {
		super(sender, args, false, true);
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
		String sendername = this.getSender().getName();
		FPSPlayer player = FPSCaste.getFPSPlayer(sendername);
		if (!player.isIngame()){
			if (this.args.length > 0){
				if (Bukkit.getServer().getPlayer(args[0]) != null){
					String name = Bukkit.getServer().getPlayer(args[0]).getName();
					
					if (name == null && FPSCaste.getFPSPlayer(args[0]) != null){
						name = args[0];
					}
					
					if (FPSCaste.getFPSPlayer(name).isIngame()){
						player.joinMatch(FPSCaste.getFPSPlayer(name).getMatch().getMatchID());
					} else {
						this.badMsg(name + " is not in a game!");
					}
				} else {
					this.badMsg(args[0] + " could not be found. Is he online?");
				}
			} else {
				int MatchID = FPSCaste.randomMatch();
				if (MatchID > 0){
					player.joinMatch(MatchID);
				} else {
					this.badMsg("There is no available match. :(  Please wait a minute and try again");
				}
			}
		} else {
			if (player.getMatch().getState().equals(gameState.ENDING)){
				player.badMsg("You cannot join the match when it has ended, sorry :(");
			} else {
				teamName teamname = teamName.ALONE;
				int team = new Random().nextInt(2);
				
				if (!player.getMatch().getMode().isFFA()){
					if (player.getMatch().getAxis() > player.getMatch().getAllies()){
						teamname = teamName.ALLIES;
					} else if (player.getMatch().getAllies() > player.getMatch().getAxis()){
						teamname = teamName.AXIS;
					} else {
						if (team == 0){
							teamname = teamName.ALLIES;
						} else if (team == 1){
							teamname = teamName.AXIS;
						}
					}
				}
				player.join(teamname);
			}
		}
		return true;
	}
}
