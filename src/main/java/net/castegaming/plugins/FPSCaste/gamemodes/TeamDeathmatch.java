package net.castegaming.plugins.FPSCaste.gamemodes;

import java.util.HashMap;

import net.castegaming.plugins.FPSCaste.FPSCaste;
import net.castegaming.plugins.FPSCaste.Match;
import net.castegaming.plugins.FPSCaste.enums.Points;
import net.castegaming.plugins.FPSCaste.enums.gameType;
import net.castegaming.plugins.FPSCaste.map.Map;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;

public class TeamDeathmatch extends GameMode {

	public TeamDeathmatch() {
		super(gameType.TDM, 10, 7500);
	}

	@Override
	protected HashMap<Block, Material> checkGenerate(Map map) {
		return new HashMap<Block, Material>();
	}

	@Override
	public boolean isFFA() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void handleNear(String name) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void handleNotNear(String name) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void handleRightClick(String name) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int handleKill(String attacker, String defender) {
		addScore(Points.KILL_TDM, FPSCaste.getFPSPlayer(attacker).getTeam());
		updateBoards(FPSCaste.getFPSPlayer(attacker).getTeam());
		if (ended) endedReason = "The game has ended!  Winning kill by "
				+ ChatColor.BOLD + attacker + ChatColor.RESET + "! He killed " + ChatColor.BOLD + defender; 
		return Points.KILL_TDM;
	}

	@Override
	public void handleAssist(String attacker, String defender) {
		//addScore(Points.ASSIST, FPSCaste.getFPSPlayer(attacker).getTeam());
	}

	@Override
	public void second(Match match) {
		
	}

	@Override
	public void playerLeave(String name) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void playerJoin(String name) {
		// TODO Auto-generated method stub
		
	}
}
