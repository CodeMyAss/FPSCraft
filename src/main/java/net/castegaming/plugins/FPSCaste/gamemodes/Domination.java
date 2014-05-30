package net.castegaming.plugins.FPSCaste.gamemodes;

import java.util.HashMap;

import net.castegaming.plugins.FPSCaste.FPSCaste;
import net.castegaming.plugins.FPSCaste.Match;
import net.castegaming.plugins.FPSCaste.enums.Points;
import net.castegaming.plugins.FPSCaste.enums.gameType;
import net.castegaming.plugins.FPSCaste.gamemodes.gameobjects.DominationFlag;
import net.castegaming.plugins.FPSCaste.gamemodes.gameobjects.GameObject;
import net.castegaming.plugins.FPSCaste.map.Map;
import net.castegaming.plugins.FPSCaste.enums.teamName;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.scheduler.BukkitRunnable;

public class Domination extends FlagGameMode {

	public Domination() {
		super(gameType.DOM, 20, 200);
	}

	@Override
	protected HashMap<Block, Material> checkGenerate(Map map) {
		addObject(new DominationFlag(map.domA, "A"));
		addObject(new DominationFlag(map.domB, "B"));
		addObject(new DominationFlag(map.domC, "C"));
		HashMap<Block, Material> blocks = new HashMap<Block, Material>();
		for (GameObject o : gameObjects){
			blocks.putAll(o.getBlocks());
		}
		return blocks;
	}

	@Override
	public boolean isFFA() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void handleNear(String name) {
		DominationFlag f = (DominationFlag) getClosest(name);
		f.takeOver(100, FPSCaste.getFPSPlayer(name).getTeam(), name, "Capturing flag " + f.getName());
	}

	@Override
	public void handleNotNear(String name) {
		
	}

	@Override
	public void handleRightClick(String name) {
		
	}

	@Override
	public void init(Map map) {
		super.init(map);
		new BukkitRunnable() {
			@Override
			public void run() {
				for (GameObject o : gameObjects){
					if (o instanceof DominationFlag){
						DominationFlag flag = (DominationFlag) o;
						if (flag.getHolder() != teamName.SPECTATOR){
							addScore(1, flag.getHolder());
						}
						
						if (ended){
							cancel();
						}
					}
				}
			}
		}.runTaskTimer(FPSCaste.getInstance(), 0, 100);
	}

	@Override
	public int handleKill(String attacker, String defender) {
		return Points.KILL_DOM;
	}

	@Override
	public boolean isHardcore() {return false;}
	
	@Override
	public void handleAssist(String attacker, String defender) {}

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
