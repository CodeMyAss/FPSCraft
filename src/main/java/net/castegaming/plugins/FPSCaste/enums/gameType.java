package net.castegaming.plugins.FPSCaste.enums;

/**
 * An enum containing all the different gamemodes.
 * @author Brord
 *
 */
public enum gameType {
	TDM("Team Deathmatch", 7500, 11),
	CTF("Capture the flag", 2, 10),
	FFA("Free for all", 7500, 10),
	DOM("Domination", 200, 10),
	DEM("Demolition", 1, 10);
	
	/**
	 * Better name
	 */
	private String name;
	
	/**
	 * Max points
	 */
	private int maxPoints;
	
	/**
	 * Matchtime in ticks
	 */
	public int matchTime;
	 
	private gameType(String name, int maxPoints, int matchTime) {
		this.name = name;
		this.maxPoints = maxPoints;
		this.matchTime = matchTime*60*20;
	}
	
	/**
	 * Returns the proper name of this gamemode
	 */
	@Override
	public String toString() {
		return name;
	}
	
	public int getMatchTime() {
		return matchTime;
	}
	
	public int getMaxPoints() {
		return maxPoints;
	}
}
