package net.castegaming.plugins.FPSCaste.config;

import java.io.File;
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
import net.castegaming.plugins.FPSCaste.listener.RestListener;
import net.castegaming.plugins.FPSCaste.playerclass.PlayerClass;
import net.castegaming.plugins.FPSCaste.playerclass.weapons.Gun;
import net.castegaming.plugins.FPSCaste.playerclass.weapons.InitWeapons;
import net.castegaming.plugins.FPSCaste.playerclass.weapons.WeaponContainer;

public class RegisterConfigs {
	
	public FPSCaste plugin;
	
	public RegisterConfigs(FPSCaste plugin){
		this.plugin = plugin;
		register();
		loadConfigValues();
		setBroadCastTimes();
		loadRanks();
		loadWeapons();
		loadDefaultClasses();
		Points.load();
	}
	
	private void loadWeapons() {
		new Gun(WeaponContainer.getNextIDAvailable(), "Default weapon", GunClass.PRIMARY, "NO-GROUP", 5, 1.0, 1, new HashMap<String, Object>());
		new InitWeapons(plugin);
	}
	
	private void register(){
		if (Config.getConfig("config") == null){
			plugin.saveDefaultConfig();
			FPSCaste.log("Created config.yml!", Level.INFO);
		}
		
		String[] configsStrings = {"maps", "defaultplayer", "points", "ranks", "defaultclasses"};
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
	
	private void loadDefaultClasses() {
		YamlConfiguration classes = Config.getConfig("defaultclasses");
		for (String classname : classes.getKeys(false)){
			new PlayerClass(classname);
			FPSCaste.log("Created default class: " + classname);
		}
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
	
	private void loadConfigValues() {
		try {
			Match.switchWaidTime = plugin.getConfig().getInt("switchWaidTime");
		} catch (Exception e){
			Match.switchWaidTime = 10;
			FPSCaste.log("Created the switchWaidTime config node!", Level.INFO);
			plugin.getConfig().set("switchWaidTime", 10);
			plugin.saveConfig();
		}
		
		try {
			RestListener.allowedCommands = plugin.getConfig().getStringList("allowedCommands");
		} catch (Exception e){
			RestListener.allowedCommands = new LinkedList<String>();
			FPSCaste.log("Created the allowedCommands config node!", Level.INFO);
			plugin.getConfig().set("allowedCommands", new String[]{"seen", "whois", "help"});
			plugin.saveConfig();
		}
		
		PlayerClass.maxAllowedKillstreaks = plugin.getConfig().getInt("killstreakrewards", 3);
	}
	
	private void setBroadCastTimes() {
		List<String> list = plugin.getConfig().getStringList("broadcastTimes");
		List<Integer> ints = new LinkedList<Integer>();
		for (String string : list){
			String intString = string.substring(0, string.length() - 1);
			int number;
			try {
				if (string.endsWith("s")){
					number = Integer.parseInt(intString)*20;
					
				} else if (string.endsWith("m")){
					number = Integer.parseInt(intString)*60*20;
				} else {
					number = 0;
					FPSCaste.log(FPSCaste.NamePrefix + string + " has no correct time modifier! (m, s) Check broadcastTimes in your config ", Level.WARNING);
				}
				if (number > 0)
					ints.add(ints.size(), number);
			} catch (NumberFormatException e){
				FPSCaste.log(FPSCaste.NamePrefix + string + " is no valid number! Check broadcastTimes in your config ", Level.WARNING);
			}
		}
		Match.broadCastValues = ints;
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
