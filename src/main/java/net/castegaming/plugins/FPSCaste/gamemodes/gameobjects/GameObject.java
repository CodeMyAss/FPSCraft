package net.castegaming.plugins.FPSCaste.gamemodes.gameobjects;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import net.castegaming.plugins.FPSCaste.FPSCaste;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

public abstract class GameObject {
	
	public static final double MAX_REACH = 5;

	private Location position;
	private String name;
	protected HashMap<Block, Material> blocks = new HashMap<Block, Material>(); 
	
	public GameObject(Location position, String name) {
		this.position = position;
		this.name = name;
	}

	/**
	 * @return the name of this flag
	 */
	public String getName() {
		return name;
	}
	
	public void setLocation(Location location) {
		position = location;
	}
	
	public Location getLocation() {
		return position;
	}

	/**
	 * Contains the blocks needed for this object.<br/>
	 * @return HashMap<Block, Material> With the blocks used
	 */
	public HashMap<Block, Material> getBlocks() {
		return blocks;
	}
	
	/**
	 * Returns whether the player defiend is near thiss flag
	 * @param name The player to scan
	 * @return True if he is near, otherwise false
	 */
	public boolean isNear(String name){
		if (Bukkit.getServer().getPlayer(name) != null && FPSCaste.getFPSPlayer(name).isIngame() && !Bukkit.getServer().getPlayer(name).isDead()){
			Location l = Bukkit.getServer().getPlayer(name).getLocation();
			try {
				return l.distanceSquared(position) < MAX_REACH;
			} catch (IllegalArgumentException e){
				return false;
			}
		}
		return false;
	}
	
	/**
	 * Returns all the active players
	 * @param matchID the ID of the match
	 * @return A Set<String> containing all the playernames
	 */
	public Set<String> getNearPlayers(int matchID){
		HashSet<String> names = new HashSet<String>();
		for (String name : FPSCaste.getMatch(matchID).getPlayers().keySet()){
			if (Bukkit.getServer().getPlayer(name) != null && !Bukkit.getServer().getPlayer(name).isDead()){
				if (isNear(name)){
					names.add(name);
				}
			}
		}
		return names;
	}
	
	@Override
	public String toString() {
		return name;
	}
	
	public abstract void build();
}
