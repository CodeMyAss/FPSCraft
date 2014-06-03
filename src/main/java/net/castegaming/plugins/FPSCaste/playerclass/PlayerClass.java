package net.castegaming.plugins.FPSCaste.playerclass;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.bukkit.configuration.ConfigurationSection;

import net.castegaming.plugins.FPSCaste.FPSCaste;
import net.castegaming.plugins.FPSCaste.config.Config;
import net.castegaming.plugins.FPSCaste.playerclass.weapons.Weapon;
import net.castegaming.plugins.FPSCaste.playerclass.weapons.WeaponContainer;
import net.castegaming.plugins.FPSCaste.util.Parse;

public class PlayerClass {
	
	public static int maxAllowedKillstreaks = 3;
	
	public static HashMap<String, PlayerClass> defaultClasses = new HashMap<String, PlayerClass>();
	
	public static String getDefaultClasses(){
		return Parse.ArrayToString(defaultClasses.keySet().toArray(new String[0]));
	}
	
	/**
	 * Returns the default PlayerClass<br/>
	 * Returns null if it doesnt exist
	 * @param classname the name of the class to get
	 * @return The default class or null
	 */
	public static PlayerClass getDefaultClass(String classname){
		classname = getDefaultClassName(classname);
		return defaultClasses.get(classname);
	}
	
	/**
	 * Returns the name of the class.<br/>
	 * Automatically checks for uppercase etc.
	 * @param classname
	 * @return The class found
	 */
	public static String getDefaultClassName(String classname){
		for (String name : defaultClasses.keySet()){
			if (name.equals(classname)){
				return classname;
			} else if (classname.toUpperCase().equals(name.toUpperCase())){
				return name;
			}
		}
		for (String name : defaultClasses.keySet()){
			if (name.startsWith(classname)){
				return classname;
			} else if (classname.toUpperCase().equals(name.toUpperCase())){
				return name;
			}
		}
		return "";
	}
	
	////////////////////////////////////// class vars //////////////////////////////////////////
	
	private List<String> perks;
	
	private Weapon[] initWeapons = new Weapon[9];
	
	private Weapon[] weapons = new Weapon[9];
	
	private List<String> killstreaks;
	
	private String classname;
	
	private int minlevel;
	
	/**
	 * Loads a new {@link PlayerClass}<br/>
	 * If there is a defaultclass with this name, a copy will be(loaded and) returned<br/>
	 * If there exists a player with this name, His current class will be loaded
	 * @param name
	 */
	public PlayerClass(String name) {
		if (defaultClasses.containsKey(name)){
			//do we want a new instance, to give away?
			loadCopy(name);
		} else {
			if (Config.getConfig("defaultclasses") != null && Config.getConfig("defaultclasses").contains(name)){
				load(Config.getConfig("defaultclasses").getConfigurationSection(name), true);
			} else {
				//did they pass just a playername?
				if (Config.hasPlayerConfig(name)){
					if (Config.getPlayerConfig(name).getCurrentClassSection() != null){
						load(Config.getPlayerConfig(name).getCurrentClassSection(), false);
					} else if (defaultClasses.containsKey(Config.getPlayerConfig(name).getSelectedClass())){
						loadCopy(Config.getPlayerConfig(name).getSelectedClass());
					} else {
						loadCopy(defaultClasses.keySet().toArray(new String[0])[0]);
					}
				}
				//load empty class
			}
		}
	}

	/**
	 * Load a class from a player
	 * @param name the player
	 * @param classname the class from the player.
	 */
	public PlayerClass(String player, String classname) {
		load(Config.getPlayerConfig(player).getClassSection(classname), false);
	}
	
	/**
	 * Loads all the class options form config into a {@link PlayerClass}
	 * @param config the {@link ConfigurationSection} the class is in
	 * @param defaultClass if this class is a default class(true), or not (false)
	 */
	private void load(ConfigurationSection config, boolean defaultClass){
		perks = new LinkedList<String>();
		if (config == null){
			//final safeguard, config still doesnt exist, snag first default config
			loadCopy(defaultClasses.keySet().toArray(new String[0])[0]);
			return;
		}
		if (config != null){
			//section is not null, so class exists.
			classname = config.getName();
			minlevel = config.getInt("level", 1);
			
			//load weapons
			ConfigurationSection section = config.getConfigurationSection("weapons");
			for (String weaponName : section.getKeys(false)){
				loadWeapon(section, weaponName);
			}
			
			//load killstreaks
			killstreaks = config.getStringList("killstreaks");
			if (killstreaks == null){
				killstreaks = Arrays.asList("","","");
			}
			
			//load perks
			perks = config.getStringList("perks");
			if (perks == null){
				perks = Arrays.asList("", "", "");
				
			}
			
			if (defaultClass){
				defaultClasses.put(classname, this);
			}
		}
	}

	/**
	 * @param config
	 * @param weaponName
	 * @return
	 */
	private String loadWeapon(ConfigurationSection config, String weaponName) {
		weaponName = weaponName.replace(',', '.');
		WeaponContainer container = WeaponContainer.getWeapon(weaponName);
		if (container == null){
			FPSCaste.log("playerclass " + classname + ": Could not find weapon " + weaponName);
			container = WeaponContainer.getWeapon("Default");
		}
		Weapon w = new Weapon(container);
		w.addAttachments(config.getStringList(weaponName));
		weapons[w.getSlot()] = w;
		initWeapons[w.getSlot()] = w;
		return weaponName;
	}
	
	/**
	 * !!!WARNING!!!<br/>
	 * This is only used to make a new empty class, and copy the values from the old.<br/>
	 * Please dont use this for other intentions.
	 */
	private PlayerClass() {}

	public PlayerClass copy(){
		PlayerClass p =  new PlayerClass();
		p.setKillstreaks(getKillstreaks());
		p.weapons = getWeapons();
		p.setName(classname);
		return p;
	}
	
	public void loadCopy(String classname){
		PlayerClass defaultClass = defaultClasses.get(classname);
		
		if (defaultClass == null) return;
		minlevel = defaultClass.getRequiredLevel();
		setName(defaultClass.getName());
		setKillstreaks(defaultClass.getKillstreaks());
		setPerks(defaultClass.getPerks());
		initWeapons = defaultClass.getWeapons();
		weapons = initWeapons.clone();
	}

	private PlayerClass setName(String classname) {
		this.classname = classname;
		return this;
	}

	private List<String> getPerks() {
		return perks;
	}
	
	private PlayerClass setPerks(List<String> perks){
		this.perks = perks;
		return this;
	}

	private PlayerClass setKillstreaks(List<String> killstreaks) {
		this.killstreaks = killstreaks;
		return this;
	}

	private List<String> getKillstreaks() {
		return killstreaks;
	}

	public Weapon[] getWeapons() {
		return weapons;
	}
	
	public void setWeapons(Weapon[] weapons){
		this.weapons = weapons;
	}
	
	public void setWeapon(Weapon w, int slot) {
		weapons[slot] = w;
	}

	public Weapon getWeapon(int slot) {
		return weapons[slot];
	}

	public String getName() {
		return classname;
	}

	public void reset() {
		weapons = initWeapons.clone();
	}

	public int getRequiredLevel() {
		return minlevel;
	}

	/**
	 * Sets the weapon in the slot to null<br/>
	 * the weapon in initweapons stays
	 * @param heldItemSlot
	 */
	public void removeWeapon(int heldItemSlot) {
		weapons[heldItemSlot] = null;
	}
	
	/**
	  * @see java.lang.Object#toString()
	  */
	@Override
	public String toString() {
		return classname;
	}
}
