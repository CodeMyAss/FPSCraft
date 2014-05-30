package net.castegaming.plugins.FPSCaste.gamemodes;

import java.util.LinkedList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import net.castegaming.plugins.FPSCaste.enums.gameType;
import net.castegaming.plugins.FPSCaste.gamemodes.gameobjects.Flag;
import net.castegaming.plugins.FPSCaste.gamemodes.gameobjects.GameObject;

/**
 * Project FPSCraft<br/>
 * Class net.castegaming.plugins.FPSCaste.gamemodes.FlagGameMode.java<br/>
 * @author Brord
 * @since 30 mei 2014, 12:25:09
 */
public abstract class FlagGameMode extends GameMode {

	/**
	 * @param type
	 * @param minutes
	 * @param scoreMax
	 */
	public FlagGameMode(gameType type, int minutes, int scoreMax) {
		super(type, minutes, scoreMax);
	}

	/**
	 * Returns the clsoest flag or null.<br/>
	 * Only null if the player is offline, or there are no flags. (this is impossible)<br/>
	 * Previous flags have priority over the next one if the distance is exacly the same.
	 * @param name
	 * @return
	 */
	public Flag getClosest(String name){
		if (Bukkit.getServer().getPlayer(name) != null){
			Location l = Bukkit.getServer().getPlayer(name).getLocation();
			Flag flag = null;
			double distance = 9999;
			
			for (GameObject o : gameObjects){
				if (o instanceof Flag && l.getWorld().equals(o.getLocation().getWorld()) && l.distance(o.getLocation()) <= distance){
					distance = l.distance(o.getLocation());
					flag = (Flag) o;
				}
			}
			return flag;
		}
		return null;
	}
	
	public List<Flag> getFlags(){
		List<Flag> flags = new LinkedList<Flag>();
		for (GameObject o : gameObjects){
			if (o instanceof Flag) {
				
				flags.add((Flag) o);
			}
		}
		return flags;
	}
}
