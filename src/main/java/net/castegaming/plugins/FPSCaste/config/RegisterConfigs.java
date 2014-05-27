package net.castegaming.plugins.FPSCaste.config;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;

import org.bukkit.configuration.file.YamlConfiguration;

import net.castegaming.plugins.FPSCaste.FPSCaste;
import net.castegaming.plugins.FPSCaste.Match;
import net.castegaming.plugins.FPSCaste.enums.GunClass;
import net.castegaming.plugins.FPSCaste.enums.Points;
import net.castegaming.plugins.FPSCaste.enums.Rank;
import net.castegaming.plugins.FPSCaste.gamemodes.playlist.PlayList;
import net.castegaming.plugins.FPSCaste.listener.RestListener;
import net.castegaming.plugins.FPSCaste.playerclass.PlayerClass;
import net.castegaming.plugins.FPSCaste.playerclass.weapons.Gun;
import net.castegaming.plugins.FPSCaste.playerclass.weapons.InitWeapons;
import net.castegaming.plugins.FPSCaste.playerclass.weapons.WeaponContainer;
import net.castegaming.plugins.FPSCaste.util.TimeUtil;
import net.castegaming.plugins.FPSCaste.util.Util;

public class RegisterConfigs {
	
	public FPSCaste plugin;
	
	public RegisterConfigs(FPSCaste plugin){
		this.plugin = plugin;
		register();
		checkConfig();
		loadConfigValues();
		setBroadCastTimes();
		loadRanks();
		loadPlayLists();
		loadWeapons();
		loadDefaultClasses();
		Points.load();
	}

	private void register(){
		if (Config.getConfig("config") == null){
			plugin.saveDefaultConfig();
			FPSCaste.log("Created config.yml!", Level.INFO);
		}
		
		String[] configsStrings = {"maps", "defaultplayer", "points", "ranks", "defaultclasses", "playlists"};
		String[] folderStrings = {"players", "maps", "weapons"};
		
		for (String s : configsStrings){
			if (Config.getConfig(s) == null){
				Config.createConfig(s);
				FPSCaste.log("Created " + s + ".yml!", Level.INFO);
			}
		}
		for (String s : folderStrings){
			File folder = new File(plugin.getDataFolder(), s + File.separator);
			if (!folder.exists()){
				FPSCaste.log("Created the " + s + " folder!", Level.INFO);
				folder.mkdir();
			}
		}
	}
	
	/**
	 * Checks the config for possible mistakes or needed updates.
	 */
	public void checkConfig(){
		YamlConfiguration config = YamlConfiguration.loadConfiguration(plugin.getResource("config.yml"));
		YamlConfiguration fconfig = YamlConfiguration.loadConfiguration(new File(plugin.getDataFolder() + File.separator + "config.yml"));
		
		boolean save = false;
		for (String key : config.getKeys(true)){
			if (!fconfig.contains(key)){
				plugin.getConfig().set(key, config.get(key));
				FPSCaste.log("Added the config value: " + key);
				save = true;
			}
		}
		if (save) plugin.saveConfig();
	}
	
	private void loadConfigValues() {
		Match.switchWaidTime = plugin.getConfig().getInt("switchWaidTime", -1);
		RestListener.allowedCommands = plugin.getConfig().getStringList("allowedCommands");
	}
	
	private void setBroadCastTimes() {
		List<String> list = plugin.getConfig().getStringList("broadcastTimes");
		List<Integer> ints = new LinkedList<Integer>();
		for (String string : list){
			String intString = string.substring(0, string.length() - 1);
			long number = TimeUtil.getTimeMillis(intString);
			if (number > -1){
					ints.add(ints.size(), (int)number/1000);
			} else {
				FPSCaste.log(string + " is no valid number! Check broadcastTimes in your config ", Level.WARNING);
			}
		}
		Match.broadCastValues = ints;
	}
	
	private void loadRanks(){
		YamlConfiguration ranks = Config.getConfig("ranks");
		if (ranks == null){
			ranks = Config.createConfig("ranks");
		}
		
		for (String key : ranks.getKeys(false)){
			if (isInt(ranks.getString(key))){
				if (!Rank.ranks.isEmpty() && Integer.parseInt(ranks.getString(key)) < (Integer) Rank.ranks.keySet().toArray()[Rank.ranks.size()-1]){
					FPSCaste.log("Warning! The rank: " + key + " has a lower requirement(" + Integer.parseInt(ranks.getString(key)) + ") then the rank below!" + Rank.ranks.keySet().toArray()[Rank.ranks.size()-1] + ") Will not add" , Level.INFO);
				} else {
					Rank.ranks.put(Integer.parseInt(ranks.getString(key)), key);
				}
				
			} else {
				FPSCaste.log("Rank: " + key + " is invalid! Check teh xp required.", Level.INFO);
			}
		}
	}
	
	/**
	 * 
	 */
	private void loadPlayLists() {
		YamlConfiguration lists = Config.getConfig("playlist");
		boolean save = false;
		for (String list : lists.getKeys(false)){
			if (!lists.contains(list + ".options")){
				FPSCaste.log("playlist " + list + " does not have options defined!" );
				lists.set(list + ".options", new LinkedList<String>(Arrays.asList(new String[]{"TDM", "MAPNAMEHEREs"})));
				save = true;
				continue;
			}
			
			Boolean random = (Boolean) lists.get(list + ".random", null);
			if (random == null){
				lists.set(list + ".random", false);
				save = true;
				random = false;
			}
			 new PlayList(list, lists.getStringList(list + ".options"), random, true);
			 FPSCaste.log("Loaded playlist: " + list);
		}
		
		if (save) Config.saveConfig("playlist", lists);
	}
	
	private void loadWeapons() {
		new Gun(WeaponContainer.getNextIDAvailable(), "Default weapon", GunClass.PRIMARY, "NO-GROUP", 5, 1.0, 1, new HashMap<String, Object>());
		new InitWeapons(plugin);
	}
	
	private void loadDefaultClasses() {
		YamlConfiguration classes = Config.getConfig("defaultclasses");
		for (String classname : classes.getKeys(false)){
			new PlayerClass(classname);
			FPSCaste.log("Created default class: " + classname);
		}
	}
	
	private boolean isInt(String number){
		try {
			Integer.parseInt(number);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
}
