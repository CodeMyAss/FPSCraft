package net.castegaming.plugins.FPSCaste.config;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;

import org.bukkit.configuration.file.YamlConfiguration;

import net.castegaming.plugins.FPSCaste.FPSCaste;
import net.castegaming.plugins.FPSCaste.Match;
import net.castegaming.plugins.FPSCaste.enums.Configs;
import net.castegaming.plugins.FPSCaste.enums.GunClass;
import net.castegaming.plugins.FPSCaste.enums.Points;
import net.castegaming.plugins.FPSCaste.enums.Rank;
import net.castegaming.plugins.FPSCaste.gamemodes.playlist.PlayList;
import net.castegaming.plugins.FPSCaste.listener.RestListener;
import net.castegaming.plugins.FPSCaste.playerclass.PlayerClass;
import net.castegaming.plugins.FPSCaste.playerclass.weapons.Gun;
import net.castegaming.plugins.FPSCaste.playerclass.weapons.InitWeapons;
import net.castegaming.plugins.FPSCaste.playerclass.weapons.Special;
import net.castegaming.plugins.FPSCaste.playerclass.weapons.WeaponContainer;
import net.castegaming.plugins.FPSCaste.util.TimeUtil;

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

	private void register(){
		if (Config.getConfig(Configs.CONFIG.toString()) == null){
			plugin.saveDefaultConfig();
			FPSCaste.log("Created " + Configs.CONFIG.toString() + "!", Level.INFO);
		} else {
			checkConfig(Configs.CONFIG.toString());
		}
		
		Configs[] configsStrings = Configs.values();
		String[] folderStrings = {"players", "maps", "weapons"};
		
		for (Configs s : configsStrings){
			if (s.equals(Configs.CONFIG)) continue;
			
			if (Config.getConfig(s.toString()) == null){
				Config.createConfig(s.toString());
				FPSCaste.log("Created " + s + ".yml!", Level.INFO);
			} else {
				if (!Config.getConfig(s.toString()).getBoolean("checked")){
					checkConfig(s.toString());
					setChecked(s);
				}
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
	public boolean checkConfig(String configname) {
		if (!configname.endsWith(".yml")) configname += ".yml";
		if (plugin.getResource(configname) == null)return true;
		boolean changed = false;
		
		YamlConfiguration config = YamlConfiguration.loadConfiguration(plugin.getResource(configname));
		YamlConfiguration fconfig = YamlConfiguration.loadConfiguration(new File(plugin.getDataFolder() + File.separator + configname));
		for (String key : config.getKeys(true)){
			if (!fconfig.contains(key)){
				fconfig.set(key, config.get(key));
				FPSCaste.log("Added the config value: " + key + " to " + configname);
				changed = true;
			}
		}
		
		if (changed){
			try {
				fconfig.save(plugin.getDataFolder() + File.separator + configname);
			} catch (IOException e) {e.printStackTrace();}
		}
		
		return !changed;
	}
	
	private void setChecked(Configs c){
		YamlConfiguration conf = Config.getConfig(c.toString());
		conf.set("checked", true);
		Config.saveConfig(c.toString(), conf);
	}
	
	private void loadConfigValues() {
		Match.switchWaidTime = plugin.getConfig().getInt("switchWaidTime", -1);
		RestListener.allowedCommands = plugin.getConfig().getStringList("allowedCommands");
	}
	
	private void setBroadCastTimes() {
		List<String> list = plugin.getConfig().getStringList("broadcastTimes");
		List<Integer> ints = new LinkedList<Integer>();
		for (String string : list){
			long number = TimeUtil.getTimeMillis(string);
			if (number > -1){
					ints.add(ints.size(), (int)number/1000);
			} else {
				FPSCaste.log(string + "(" + number +  ") is no valid number! Check broadcastTimes in your config ", Level.WARNING);
			}
		}
		Match.broadCastValues = ints;
	}
	
	private void loadRanks(){
		YamlConfiguration ranks = Config.getConfig(Configs.RANK.toString());
		if (ranks == null){
			ranks = Config.createConfig(Configs.RANK.toString());
		}
		
		for (String key : ranks.getKeys(false)){
			if (isInt(ranks.getString(key))){
				if (!Rank.ranks.isEmpty() && Integer.parseInt(ranks.getString(key)) < (Integer) Rank.ranks.keySet().toArray()[Rank.ranks.size()-1]){
					FPSCaste.log("Warning! The rank: " + key + " has a lower requirement(" + Integer.parseInt(ranks.getString(key)) + ") then the rank below!" + Rank.ranks.keySet().toArray()[Rank.ranks.size()-1] + ") Will not add" , Level.INFO);
				} else {
					Rank.ranks.put(Integer.parseInt(ranks.getString(key)), key);
				}
				
			} else {
				FPSCaste.log("Rank: " + key + " is invalid! Check the xp required.", Level.INFO);
			}
		}
		FPSCaste.log("Ranks loaded.", Level.INFO);
	}
	
	private void loadWeapons() {
		//TODO load from file
		new Gun(WeaponContainer.getNextIDAvailable(), "Default weapon", GunClass.PRIMARY, "NO-GROUP", 0, 1.0, new HashMap<String, Object>());
		new Special(WeaponContainer.getNextIDAvailable(), "Tnt", GunClass.SPECIAL, "NO-GROUP", 0, 30.0, 0, 0, new HashMap<String, Object>());
		new InitWeapons(plugin);
	}
	
	private void loadDefaultClasses() {
		YamlConfiguration classes = Config.getConfig(Configs.DEFAULTCLASSES.toString());
		for (String classname : classes.getKeys(false)){
			if (classname.equals("checked")) continue;
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
