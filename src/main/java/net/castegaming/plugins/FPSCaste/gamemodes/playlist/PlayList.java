/**
 * 
 */
package net.castegaming.plugins.FPSCaste.gamemodes.playlist;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import net.castegaming.plugins.FPSCaste.enums.GameModes;
import net.castegaming.plugins.FPSCaste.gamemodes.GameMode;
import net.castegaming.plugins.FPSCaste.map.MapPreset;

/**
 * @author Brord
 *
 */
public class PlayList {

	private static HashMap<String, PlayList> playlists = new HashMap<String, PlayList>();
	
	/**
	 * @param string
	 * @return
	 */
	public static PlayList getNew(String string) {
		if (!playlists.containsKey(string)) return null;
		
		PlayList list = playlists.get(string);
		return new PlayList(list.name, list.maps, list.random);
	}
	
	private static Random r = new Random();
	
	List<String> maps = new LinkedList<String>();
	GameModes mode;
	
	
	private boolean random;
	private String name;
	private int index = -1;
	
	
	public PlayList(String name, List<String> maps, boolean random){
		this(name, maps, random, false);
	}
	
	/**
	 * 
	 */
	public PlayList(String name, List<String> maps, boolean random, boolean add) {
		this.name = name;
		this.random = random;
		
		for (String s : maps){
			if (MapPreset.exists(s)){
				this.maps.add(s);
			} else if (GameModes.get(s) != null){
				maps.add(s);
			}
		}
		
		if (add) playlists.put(name, this);
	}
	
	public GameMode getMode(){
		return mode.getMode();
	}
	
	public String getMap(){
		return maps.get(index > maps.size() ? maps.size()-1 : index);
	}
	
	public void next(){
		boolean done = false;
		while(!done){
			int index = random ? r.nextInt(maps.size()-1) : this.index+1 >= maps.size() ? 0 : this.index+1;
			if (MapPreset.exists(maps.get(index))){
				this.index = index;
				done = true;
			} else {
				//gamemode
				mode = GameModes.get(maps.get(index));
			}
		}
		
	}
}
