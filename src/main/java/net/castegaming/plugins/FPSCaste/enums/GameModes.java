/**
 * 
 */
package net.castegaming.plugins.FPSCaste.enums;

import net.castegaming.plugins.FPSCaste.gamemodes.CaputeTheFlag;
import net.castegaming.plugins.FPSCaste.gamemodes.Demolition;
import net.castegaming.plugins.FPSCaste.gamemodes.Domination;
import net.castegaming.plugins.FPSCaste.gamemodes.FreeForAll;
import net.castegaming.plugins.FPSCaste.gamemodes.GameMode;
import net.castegaming.plugins.FPSCaste.gamemodes.TeamDeathmatch;

/**
 * @author Brord
 *
 */
public enum GameModes {
	TDM("Capture the flag", TeamDeathmatch.class),
	DEM("Demolition", Demolition.class),
	FFA("Free for all", FreeForAll.class),
	DOM("Domination", Domination.class),
	CTF("Capture the flag", CaputeTheFlag.class);
	
	private String name;
	private Class<? extends GameMode> clazz;
	
	GameModes(String name, Class<? extends GameMode> clazz){
		this.name = name;
		this.clazz = clazz;
	}
	
	public GameMode getMode(){
		try {
			return clazz.newInstance();
		} catch (InstantiationException e) {
		} catch (IllegalAccessException e) {
		}
		
		return null;
	}
	
	@Override
	public String toString() {
		return name;
	}
	
	public static GameModes byName(String name){
		for (GameModes mode : values()){
			if (mode.toString().endsWith(name)){
				return mode;
			}
		}
		return null;
	}

	/**
	 * @param string
	 * @return
	 */
	public static GameModes get(String s) {
		try {
			GameModes m = valueOf(s.toUpperCase());
			return m != null ? m : byName(s);
		} catch (IllegalArgumentException e){
			return null;
		}
	}
}
