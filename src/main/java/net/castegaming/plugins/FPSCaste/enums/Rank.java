package net.castegaming.plugins.FPSCaste.enums;

import java.util.LinkedHashMap;
import java.util.Map;

public class Rank {
	/**
	 * Holds all the ranks.<br/>
	 * First element is rank 1, last is the last rank<br/>
	 * Key is the XP required, Value is the name of this rank
	 */
	public static Map<Integer, String> ranks = new LinkedHashMap<Integer, String>();
	
	/**
	 * Returns the xp required for this level
	 * @param level The number of the level
	 * @return The xp
	 */
	public static int getLevelXP(int level){
		return Integer.parseInt(getLevelStats(level, true));
	}
	
	/**
	 * Returns the name of this level
	 * @param level The number of the level
	 * @return The name
	 */
	public static String getLevelName(int level){
		return getLevelStats(level, false);
	}
	
	/**
	 * Gets a value from the rank
	 * @param level The rank loooking for
	 * @param what True for XP, false for name
	 * @return whatever you required 
	 */
	public static String getLevelStats(int level, boolean what){
		if (level < 1){
			return "";
		}
		
		//last level?
		if (level == Integer.parseInt(ranks.keySet().toArray()[ranks.size()-1] + "")){
			if (what){ 
				return Integer.MAX_VALUE + "";
			} else {
				return ranks.values().toArray()[level-1] + "";
			}
		}
		
		if (what){ 
			return ranks.keySet().toArray()[level-1] + "";
		} else {
			return ranks.values().toArray()[level-1] + "";
		}
	}
	
	/**
	 * Gets the level closest to this XP
	 * @param currentxp the xp to check with
	 * @param high True to get the highest, false for the lowest, null for the centered, up if even
	 * @return The level number
	 */
	public static int getclosestLevel(int currentxp, Boolean high){
		//last level?
		if (currentxp >= Integer.parseInt(ranks.keySet().toArray()[ranks.size()-1] + "")){
			//return the same name, for speed
			return ranks.size();
		}
		
		int level = 0;												 	
		for (int xp : ranks.keySet()){
			if (xp <= currentxp){
				level++;
			} else {
				if (high == null){
					if ((XPBetween(level, level+1) / 2 < (currentxp - getLevelXP(level)) || (XPBetween(level, level+1) / 2 == (currentxp - getLevelXP(level))))){
						level++;
					}
				} else if (high){
					level++;
				}
				return level;
			}
		}
		return level;
	}
	
	/**
	 * Gets the xp left untill the next level
	 * @param level the level 
	 * @return
	 */
	public static int getXPLeft(int xp){
		if (getclosestLevel(xp, true) == ranks.size()){
			return Integer.MAX_VALUE;
		}
		
		int closest = getclosestLevel(xp, true);
		return getLevelXP(closest) - xp;
	}
	
	/**
	 * Returns the xp between 2 ranks<br/>
	 * This is always positive
	 * @param level1 the first rank, preferably lowest
	 * @param level2 the second rank
	 * @return the distance in XP
	 */
	public static int XPBetween(int level1, int level2){
		if (level1 == level2){
			return 0;
		}
		return Math.abs(getLevelXP(level1) - getLevelXP(level2));
	}
}
