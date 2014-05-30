/**
 * 
 */
package net.castegaming.plugins.FPSCaste.gamemodes.playlist;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import net.castegaming.plugins.FPSCaste.FPSCaste;
import net.castegaming.plugins.FPSCaste.enums.GameModes;
import net.castegaming.plugins.FPSCaste.exceptions.WrongPlayListException;
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
		try {
			return new PlayList(list.name, list.getOptions(), list.random);
		} catch (WrongPlayListException e) {
			return null;
		}
	}

	private static Random r = new Random();
	
	List<String> maps = new LinkedList<String>();
	GameModes mode;
	
	private boolean onemode;
	private boolean random;
	private String name;
	private int index = -1;
	
	
	/**
	 * 
	 * @param name
	 * @param maps
	 * @param random
	 * @throws WrongPlayListException 
	 */
	public PlayList(String name, List<String> maps, boolean random) throws WrongPlayListException{
		this(name, maps, random, false);
	}
	
	/**'
	 * 
	 * @param name
	 * @param maps
	 * @param random
	 * @param add
	 * @throws WrongPlayListException 
	 */
	public PlayList(String name, List<String> maps, boolean random, boolean add) throws WrongPlayListException {
		this.name = name;
		this.random = random;
		
		int nummodes = 0;
		boolean map = false, gamemodes = false;
		
		for (String s : maps){
			if (MapPreset.exists(s)){
				map = true;
				this.maps.add(s);
			} else if (GameModes.get(s) != null){
				gamemodes = true;
				nummodes++;
				this.maps.add(s);
			} else {
				FPSCaste.log("Found unknown playlist tag: " + s);
			}
		}
		
		//cehcks for wrong definitions
		if (!map) throw new WrongPlayListException("No map preset found, please define atleast one!");
		if (!gamemodes) throw new WrongPlayListException("No gamemodes found, please define atleast one!");
		
		//check for one gamemode only
		if (nummodes == 1){
			onemode = true;
			String mode = null;
			for (String s : maps){
				if (GameModes.get(s) != null) {
					mode = s; break;
				}
			}
			this.mode = GameModes.get(mode);
			this.maps.remove(mode);
		}
		
		if (add) playlists.put(name, this);
	}
	
	/**
	 * @return
	 */
	private List<String> getOptions() {
		List<String> options = maps;
		if (onemode)options.add(mode.name());
		return options;
	}
	
	public GameMode getMode(){
		return mode.getMode();
	}
	
	public String getMap(){
		int i = index > maps.size() ? maps.size()-1 : index;
		return maps.get(i);
	}
	
	public void next(){
		boolean done = false;
		while(!done){
			//done = true;
			int index = random ? r.nextInt(maps.size()-1) : this.index+1 >= maps.size() ? 0 : this.index+1;
			String option = maps.get(index);
			if (MapPreset.exists(option)){
				this.index = index;
				done = true;
			} else {
				//gamemode
				this.index = (this.index >= maps.size()-1) ? 0 : this.index+1;
				mode = GameModes.get(option);
			}
		}
		
	}

	/**
	 * @return the total maps in this {@link PlayList}
	 */
	public int totalMaps() {
		int amount = 0;
		for (String s : maps){
			if (MapPreset.exists(s)) amount++;
		}
		
		return amount;
	}
}
