package net.castegaming.plugins.FPSCaste.gamemodes;

import java.util.HashMap;

import net.castegaming.plugins.FPSCaste.FPSCaste;
import net.castegaming.plugins.FPSCaste.Match;
import net.castegaming.plugins.FPSCaste.enums.gameType;
import net.castegaming.plugins.FPSCaste.enums.Points;
import net.castegaming.plugins.FPSCaste.enums.teamName;
import net.castegaming.plugins.FPSCaste.gamemodes.gameobjects.CTFFlag;
import net.castegaming.plugins.FPSCaste.gamemodes.gameobjects.Flag;
import net.castegaming.plugins.FPSCaste.gamemodes.gameobjects.GameObject;
import net.castegaming.plugins.FPSCaste.map.Map;

import org.bukkit.Material;
import org.bukkit.block.Block;

public class CaputeTheFlag extends FlagGameMode {
	
	public CaputeTheFlag() {
		super(gameType.CTF, 10, -1);
	}

	@Override
	protected HashMap<Block, Material> checkGenerate(Map map) {
		addObject(new CTFFlag(map.alliesSpawn, "Allies").setHolders(teamName.ALLIES));
		addObject(new CTFFlag(map.axisSpawn, "Axis").setHolders(teamName.AXIS));
		HashMap<Block, Material> blocks = new HashMap<Block, Material>();
		for (GameObject o : gameObjects){
			blocks.putAll(o.getBlocks());
		}
		return blocks;
	}

	@Override
	public boolean isFFA() {
		return false;
	}
	 

	@Override
	public void handleNear(String name) {
		CTFFlag f = (CTFFlag) getClosest(name);
		teamName team = FPSCaste.getFPSPlayer(name).getTeam();
		if (!f.getHolder().equals(team)){
			if (!f.hasRunner()) f.takeOver(40, team, name, "Capturing flag " + f.getName());
		} else {
			//own flag. 
			if (f.getLocation().distanceSquared(f.getOriginLocation()) > 0.5){
				//not at own location
				f.takeOver(20, team, name, "Capturing flag " + f.getName());
			} else {
				//flag at own location, is the runner holding a flag?
				CTFFlag flag = getHolding(name);
				if (flag != null){
					//near own flag, with an enemy flag.
					flag.respawn();
					addScore(1, team);
					FPSCaste.getFPSPlayer(name).givePoints(Points.FLAG_CAPTURE);
					FPSCaste.getFPSPlayer(name).getMatch().broadcastTeamGood(name + " captured the flag!", team);
				}
			}
		}
	}

	private CTFFlag getHolding(String name) {
		for (Flag f : getFlags()){
			if (((CTFFlag) f).getRunner().equals(name)){
				return (CTFFlag) f;
			}
		}
		return null;
	}

	@Override
	public void handleNotNear(String name) {
		//who cares....
	}

	@Override
	public void handleRightClick(String name) {
		//not needed
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
		CTFFlag flag = getHolding(defender);
		if (flag != null){
			flag.drop();
		}
		return super.handleKill(attacker, defender);
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
		for (GameObject o : gameObjects){
			if (o instanceof CTFFlag){
				if (((CTFFlag) o).getRunner().equals(name)){
					((CTFFlag) o).drop();
					return;
				}
			}
		}
	}

	@Override
	public void playerJoin(String name) {
		// TODO Auto-generated method stub
		
	}

}
