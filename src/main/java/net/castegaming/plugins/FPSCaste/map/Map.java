package net.castegaming.plugins.FPSCaste.map;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;

import net.castegaming.plugins.FPSCaste.FPSCaste;
import net.castegaming.plugins.FPSCaste.Match;
import net.castegaming.plugins.FPSCaste.enums.gameState;
import net.castegaming.plugins.FPSCaste.enums.teamName;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

public class Map {
	
	/**
	 * Holds all the maps which exist
	 * @param Integer the unique map ID
	 * @param map The map instance
	 */
	public static HashMap<Integer, Map> maps = new HashMap<Integer, Map>();
	
	/**
	 * Holds the mapID from those maps which arent currently in use
	 * @param Integer the unique map ID
	 */
	public static LinkedList<Integer> mapAvailable = new LinkedList<Integer>();
	
	/**
	 * Checks if a certain map is avaiable<br/>
	 * If there is one, this returns the mapID, otherwise -1
	 * @param mapname
	 * @return 
	 */
	public static Integer isMapAvailable(String mapname) {
		for (Integer i : mapAvailable){
			if (maps.get(i).getMapName().toUpperCase().equals(mapname.toUpperCase())) return i;
		}
		return -1;
	}
	
	/**
	 * Adds a map to the available list
	 * @param mapID
	 */
	public static void addAvailable(int mapID) {
		if (maps.containsKey(mapID)) mapAvailable.add(mapID);
	}

	/**
	 * Removes a mapID from the available list
	 * @param mapID
	 */
	public static void removeAvailable(int mapID) {
		mapAvailable.remove(mapAvailable.indexOf(mapID));
	}
	
	//private boolean inUse = false;
	private int mapID;
	private String mapName;
	
	private List<Location> spawnsList = new LinkedList<Location>();
	public Location mainSpawn;
	public Location axisSpawn;
	public Location alliesSpawn;
	
	public Location bombA;
	public Location bombB;
	
	public Location domA;
	public Location domB;
	public Location domC;
	
	public int test;
	public int test1;
	
	private int matchID;
	
	
	/**
	 * Creates a new map
	 */
	@SuppressWarnings("all")
	public Map(String map, Location mapLocation){
		MapPreset preset = MapPreset.presets.get(map);
		if (preset != null){
			
			
			try {
				final World world = mapLocation.getWorld();
				final double x = mapLocation.getX();
				final double y = mapLocation.getY();
				final double z = mapLocation.getZ();
				final float pitch = mapLocation.getPitch();
				final float yaw = mapLocation.getYaw();
						
				final Float[] alliesBase = preset.getSpawnAllies();
				final Float[] axisBase = preset.getSpawnAxis();
				
				final Float[] bombA = preset.getBombA();
				final Float[] bombB = preset.getBombB();
				
				final Float[] domA = preset.getDomA();
				final Float[] domB = preset.getDomB();
				final Float[] domC = preset.getDomC();
				
				mainSpawn = mapLocation;
				alliesSpawn = new Location(
						world, 
						x + alliesBase[0], 
						y + alliesBase[1], 
						z + alliesBase[2],
						alliesBase[3], 
						alliesBase[4]);
				
				axisSpawn = new Location(
						world, 
						x + axisBase[0], 
						y + axisBase[1], 
						z + axisBase[2],
						axisBase[3], 
						axisBase[4]);
				
				this.bombA = new Location(
						world, 
						x + bombA[0], 
						y + bombA[1], 
						z + bombA[2],
						bombA[3], 
						bombA[4]);
				
				this.bombB = new Location(
						world, 
						x + bombB[0], 
						y + bombB[1], 
						z + bombB[2],
						bombB[3], 
						bombB[4]);
				
				this.domA = new Location(
						world, 
						x + domA[0], 
						y + domA[1], 
						z + domA[2],
						domA[3], 
						domA[4]);
				
				this.domB = new Location(
						world, 
						x + domB[0], 
						y + domB[1], 
						z + domB[2],
						domB[3], 
						domB[4]);
				
				this.domC = new Location(
						world, 
						x + domC[0], 
						y + domC[1], 
						z + domC[2],
						domC[3], 
						domC[4]);
				
				for (Float[] spawnpoint : preset.getSpawnpoints()){
					spawnsList.add(new Location(
							world, 
							x + spawnpoint[0], 
							y + spawnpoint[1], 
							z + spawnpoint[2], 
							spawnpoint[3], 
							spawnpoint[4]));
				}
				
				
				mapID = maps.size()+1;
				mapName = map;
				mapAvailable.add(mapID);
				maps.put(maps.size()+1, this);
			} catch (Exception e){
				FPSCaste.log("Something went wrong! disabling map: " + map, Level.WARNING);
			}
		} else {
			FPSCaste.log("Could not initialise the Map " + map, Level.WARNING);
		}
	}
	
	/*
	public boolean isMapInUse(){
		return inUse;
	}
	
	public void setInuse(Boolean inuse){
		this.inUse = inuse;
	}*/
	
	/**
	 * Gets the nice name of this map like 'Terminal'
	 * @return the name, in a string
	 */
	public String getMapName(){
		return mapName;
	}
	
	/**
	 * Gets the current Map instance its ID
	 * @return the MapID
	 */
	public int getMapID(){
		return mapID;
	}
	
	/**
	 * Returns this map its main spawn location
	 * @return The main spawn
	 */
	public Location mainSpawn(){
		return mainSpawn;
	}
	
	/**
	 * Returns this map its spawn for the axis
	 * @return The axis spawnpoint
	 */
	public Location axisSpawn(){
		return axisSpawn;
	}
	
	/**
	 * Returns this map its spawn for the allies
	 * @return The allies spawnpoint
	 */
	public Location alliesSpawn(){
		return alliesSpawn;
	}
	
	/**
	 * Sets this maps current match
	 * @param id The matchID
	 */
	public void setMatchID(int id){
		matchID = id;
	}

	/**
	 * Returns a location for someone to go to.
	 * @param team
	 * @return
	 */
	public Location spawn(teamName team, Match m) {
		if (team == teamName.ALLIES && m.getState().equals(gameState.PREGAME)){
			return alliesSpawn;
		} else if (team == teamName.AXIS && m.getState().equals(gameState.PREGAME)){
			return axisSpawn;
		} else if (team == teamName.SPECTATOR){
			return mainSpawn;
		} else if (team == teamName.ALONE){
			return randomSpawn(m);
		} else {
			return randomSpawn(team, m);
		}
	}
	
	/**
	 * Gets the best location for the alone team 
	 * @return
	 */
	public Location randomSpawn(Match m) {
		return randomSpawn(teamName.ALONE, m);
	}
	
	/**
	 * Returns the closest location based on enemy team players
	 * @param team The team for which you want a spawn
	 * @param m The match. Needed for the players
	 * @return The best location, or a random one if there is no good loc
	 */
	public Location randomSpawn(teamName team, Match m) {
		double distance = 0;
		int index = 0;
		boolean edited = false;
		
		final HashMap<String, teamName> players = new HashMap<String, teamName>(m.getPlayers());
		if (players.size()> 1){
			for (Location spawn : spawnsList){
				
				//closest player to location
				double pDistance = 999;
				for (String name : players.keySet()){
					if (players.get(name) != team || team.equals(teamName.ALONE)){
						if (Bukkit.getServer().getPlayer(name) != null){
							if (Bukkit.getServer().getPlayer(name).getLocation().getWorld().toString().equals(spawn.getWorld().toString())){
								if (Bukkit.getServer().getPlayer(name).getLocation().distance(spawn) < pDistance){
									pDistance = Bukkit.getServer().getPlayer(name).getLocation().distance(spawn);
								}
							}
						}
					}
				}
				
				if (pDistance < 999 && pDistance > distance){
					//is edited? < 999, is not closer then 10  blocks?
					//the closest player is further away then the old distance
					distance = pDistance;
					index = spawnsList.indexOf(spawn);
					edited = true;
				}
				//TODO make it check for a location near teammates
			}
		} else {
			index = new Random().nextInt(spawnsList.size());
		}
		
		if (!edited){
			
		}
		return spawnsList.get(index);
	}

	@Override
	public String toString() {
		return getMapName() + "(" +mapID + ")"; 
	}

	/**
	 * Gets the world out of the main spawnpoint
	 * @return Thename of the world
	 */
	public String getWorld() {
		return mainSpawn.getWorld().toString();
	}

	public Match getCurrentMatch() {
		return FPSCaste.getMatch(matchID);
	}
	
	/**
	 * Returns the id of the current match running this map
	 * @return the matchID
	 */
	public int getMatchID(){
		return matchID;
	}
}
