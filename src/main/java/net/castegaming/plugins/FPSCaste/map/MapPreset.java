/**
 * 
 */
package net.castegaming.plugins.FPSCaste.map;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * Contains all the info for map presets
 * @author Brord
 *
 */
public class MapPreset {
	
	/**
	 * All the  available presets
	 */
	public static HashMap<String, MapPreset> presets = new HashMap<String, MapPreset>();

	/*===================== Class variables ===================*/
	
	/**
	 * Holds all the spawnpoints on this map, including the 2 main spawnpoints
	 */
	private List<Float[]> spawnpoints = new LinkedList<Float[]>();
	
	/*===================== Main spawns ===================*/
	
	/**
	 * Main spawnpoint.
	 * Used as lobby and reference points.
	 */
	private Float[] spawn;
	
	/**
	 * Main spawnpoit for the axis<br/>
	 * Also the point where the flag stands in CTF
	 */
	private Float[] spawnAxis;
	
	/**
	 * Main spawnpoit for the allies<br/>
	 * Also the point where the flag stands in CTF
	 */
	private Float[] spawnAllies;
	
	/*===================== Domination flags ===================*/
	
	/**
	 * Location of the Point A in the gametype: Domination
	 */
	private Float[] domA;
	
	/**
	 * Location of the Point B in the gametype: Domination
	 */
	private Float[] domB;
	
	/**
	 * Location of the Point C in the gametype: Domination
	 */ 
	private Float[] domC;
	
	/*===================== Locations for the demolition bombs  ===================*/
	
	/**
	 * Location A for the bomb in gamemode Demolition
	 */
	private Float[] bombA;
	
	/**
	 * Location B for the bomb in gamemode Demolition 
	 */
	private Float[] bombB;
	
	/**
	 * Holds the short name of the preset
	 */
	private String name;
	
	private boolean finished;

	/**
	 * HOlds the extended name of the preset
	 */
	private String description;
	
	
	public MapPreset(String name, Float[] floats) {
		setName(name);
		setSpawn(floats);
		finished = false; 
		presets.put(name, this);
	}
	
	@Override
	public String toString() {
		return getName();
	}
	
	/**
	 * Checks if you miss anything, and puts it in a nice string.
	 * @return A string with the left things todo
	 */
	public String check() {
		String needed = "You still need: /n";
		boolean missing = false;
		if (getDomA() == null){
			missing = true;
			needed += "- Domination flag A /n";
		}
		if (getDomB() == null){
			missing = true;
			needed += "- Domination flag B /n";
		}
		if (getDomC() == null){
			missing = true;
			needed += "- Domination flag C /n";
		}
		if (getBombA() == null){
			missing = true;
			needed += "- Demolition bomb A /n";
		}
		if (getBombB() == null){
			missing = true;
			needed += "- Demolition bomb B /n";
		}
		if (getSpawnAllies() == null){
			missing = true;
			needed += "- Spawn for Allies /n";
		}
		if (getSpawnAllies() == null){
			missing = true;
			needed += "- Spawn for Axis /n";
		}
		if (spawnpoints.size()<1){
			missing = true;
			needed += "- Spawnpoints /n";
		}
		
		if (missing) return needed;
		
		finished = true;
		return "Youve got everything! You can always add more spawnpoints!!";
	}

	public List<Float[]> getSpawnpoints() {
		return spawnpoints;
	}

	/**
	 * Adds a spawnpoint. <br/>
	 * Only if there is no spawn at this point
	 * @param spawnpoint
	 * @return 
	 */
	public boolean addSpawnpoint(Float[] spawnpoint) {
		if (!spawnpoints.contains(spawnpoint)){
			spawnpoints.add(spawnpoint);
			return true;
		}
		return false;
	}

	/**
	 * @return the main spawn of this map
	 */
	public Float[] getSpawn() {
		return spawn;
	}

	/**
	 * @param The location to set the main spawn to.
	 */
	public void setSpawn(Float[] ds) {
		this.spawn = ds;
	}

	/**
	 * @return the bombB
	 */
	public Float[] getBombB() {
		return bombB;
	}

	/**
	 * @param bombB the bombB to set
	 */
	public void setBombB(Float[] bombB) {
		this.bombB = bombB;
	}

	/**
	 * @return the bombA
	 */
	public Float[] getBombA() {
		return bombA;
	}

	/**
	 * @param bombA the bombA to set
	 */
	public void setBombA(Float[] bombA) {
		this.bombA = bombA;
	}

	/**
	 * @return the domC
	 */
	public Float[] getDomC() {
		return domC;
	}

	/**
	 * @param domC the domC to set
	 */
	public void setDomC(Float[] domC) {
		this.domC = domC;
	}

	/**
	 * @return the domB
	 */
	public Float[] getDomB() {
		return domB;
	}

	/**
	 * @param domB the domB to set
	 */
	public void setDomB(Float[] domB) {
		this.domB = domB;
	}

	/**
	 * @return the domA
	 */
	public Float[] getDomA() {
		return domA;
	}

	/**
	 * @param domA the domA to set
	 */
	public void setDomA(Float[] domA) {
		this.domA = domA;
	}

	/**
	 * @return the spawnAllies
	 */
	public Float[] getSpawnAllies() {
		return spawnAllies;
	}

	/**
	 * @param spawnAllies the spawnAllies to set
	 */
	public void setSpawnAllies(Float[] spawnAllies) {
		this.spawnAllies = spawnAllies;
	}

	/**
	 * @return the spawnAxis
	 */
	public Float[] getSpawnAxis() {
		return spawnAxis;
	}

	/**
	 * @param spawnAxis the spawnAxis to set
	 */
	public void setSpawnAxis(Float[] spawnAxis) {
		this.spawnAxis = spawnAxis;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	public void setDescription(String desc) {
		description = desc;
	}
	
	/**
	 * Returns the extended version of this preset
	 * @return The extended name
	 */
	public String getDescription(){
		return description;
	}

	public static String getAll() {
		return presets.keySet().toArray().toString().replace("[", "").replace("]", "");
	}
	
	public static boolean exists(String map){
		if (presets.containsKey(map)) return true;
		return false;
	}
}
