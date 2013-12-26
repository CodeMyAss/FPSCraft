package net.castegaming.plugins.FPSCaste.gamemodes;

import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.block.Block;

import net.castegaming.plugins.FPSCaste.FPSCaste;
import net.castegaming.plugins.FPSCaste.Match;
import net.castegaming.plugins.FPSCaste.enums.Points;
import net.castegaming.plugins.FPSCaste.enums.gameType;
import net.castegaming.plugins.FPSCaste.map.Map;

public class Demolition extends GameMode{

	public Demolition() {
		super(gameType.DEM, 10, 4);
	}

	@Override
	protected HashMap<Block, Material> checkGenerate(Map map) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void handleNear(String name) {
		FPSCaste.getFPSPlayer(name).goodMsg("You are near an objective!");
	}

	@Override
	public void handleNotNear(String name) {
		FPSCaste.getFPSPlayer(name).goodMsg("You are not near an objective!");
	}

	@Override
	public void handleRightClick(String name) {
		FPSCaste.getFPSPlayer(name).goodMsg("You are rightclciked near an objective!");
	}

	@Override
	public boolean isFFA() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isHardcore() {
		return false;
	}

	@Override
	public void init(Map map) {
		super.init(map);
	}

	@Override
	public int handleKill(String attacker, String defender) {
		return Points.KILL_TDM;
	}

	@Override
	public void handleAssist(String attacker, String defender) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void second(Match match) {
		// TODO Auto-generated method stub
		
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
