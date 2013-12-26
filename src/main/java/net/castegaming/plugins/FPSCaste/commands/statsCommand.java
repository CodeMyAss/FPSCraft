package net.castegaming.plugins.FPSCaste.commands;

import net.castegaming.plugins.FPSCaste.FPSCaste;
import net.castegaming.plugins.FPSCaste.FPSPlayer;
import net.castegaming.plugins.FPSCaste.exceptions.NoOnlinePlayerException;
import net.castegaming.plugins.FPSCaste.exceptions.NotIngameException;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class statsCommand extends FPSCommandBase{

	public statsCommand(CommandSender sender, String[] args) throws NotIngameException, NoOnlinePlayerException {
		super(sender, args, false, false);
	}

	@Override
	public boolean handle() {
		
		FPSPlayer stats;
		FPSPlayer asker = FPSCaste.getFPSPlayer(getSender().getName());
		
		if (args.length > 0){
			if (Bukkit.getServer().getPlayer(args[0]) != null){
				stats = FPSCaste.getFPSPlayer(Bukkit.getServer().getPlayer(args[0]).getName());
			} else {
				return false;
			}
		} else {
			if (this.getSender() instanceof Player){
				stats = asker;
			} else {
				return false;
			}
		}
		
		int kills;
		int deaths;
		int assists;
		int killstreak;
		int xp;
		
		if (stats.isIngame()){
			kills = stats.getKills();
			deaths = stats.getDeaths();
			assists = stats.getAssists();
			killstreak = stats.getKillstreak();
			xp = stats.getPoints();
		} else {
			kills = stats.getTotalKills();
			deaths = stats.getTotalDeaths();
			assists = stats.getTotalAssists();
			killstreak = stats.getmaxKillstreak();
			xp = stats.getXPTotal();
		}
		
		if (stats != asker){
			asker.goodMsg("---" + stats.getName() + " his stats: ---");
		} else {
			asker.goodMsg("----- Your stats: -----");
		}
		asker.goodMsg("Kills: " + kills);
		asker.goodMsg("Deaths: " + deaths);
		asker.goodMsg("Assists: " + assists);
		asker.goodMsg("Killstreak: " + killstreak);
		asker.goodMsg("XP: " + xp);
		asker.goodMsg("-----------------------");
		
		return true;
	}

}
