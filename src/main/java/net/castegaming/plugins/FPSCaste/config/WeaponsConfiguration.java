package net.castegaming.plugins.FPSCaste.config;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.castegaming.plugins.FPSCaste.playerclass.weapons.WeaponContainer;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.potion.PotionEffectType;

public class WeaponsConfiguration{
	
	YamlConfiguration config;
	
	public WeaponsConfiguration(String path) {
		this(new File(path));
	}

	public WeaponsConfiguration(File f) {
		if (f != null && f.exists()){
			config = YamlConfiguration.loadConfiguration(f);
		} else {
			new FileNotFoundException(f.getAbsolutePath()).printStackTrace();
		}
	}
	
	/**
	 * Returns the name of this weapon.<br/>
	 * If this is not defined, the name of the config will be returned, without the extension
	 * @return The name of this weapon
	 */
	public String getName() {
		return config.getString("name", "NO-NAME");
	}

	/**
	 * Returns the amount of this weapon. <br/>
	 * This will return 1 if its not defined.
	 * @return The amount or 1.
	 */
	public int getAmount() {
		return config.getInt("amount", 1);
	}

	/**
	 * Returns the amount of bullets for this gun.<br/>
	 * This will return 0 if its not defined.
	 * @return Returns the bullets or 0
	 */
	public int getBullets() {
		return config.getInt("bullets", 0);
	}

	/**
	 * Sends the ID of the item to use for this weapon.<br/>
	 * This will return an unused id if its not defined
	 * @return The ID or a new, unused one.
	 */
	public int getID() {
		return config.getInt("itemid", WeaponContainer.getNextIDAvailable());
	}

	/**
	 * Returns a list of potioneffects used for this trowable.<br/>
	 * This will return an empty array if its not defined.
	 * @return An array filled with the effects used on exploding. Possibly empty
	 */
	public List<PotionEffectType> getEffects() {
		List<PotionEffectType> effectTypes = new LinkedList<PotionEffectType>(); 
		return effectTypes;
	}

	/**
	 * Gets the delay for a weapon to shoot between 2 bullets.<br/>
	 * This will return 10 ticks if its not defined.
	 * @return The delay or 10
	 */
	public int getDelay() {
		return config.getInt("delay", 10);
	}
	
	/**
	 * Returns the required level from the config.
	 * @return The level, or 1 if its not found.
	 */
	public int getLevel(){
		return config.getInt("level", 1);
	}

	public Map<String, Object> getOptions() {
		if (config.getConfigurationSection("options") == null){
			return (new HashMap<String, Object>());
		}
		return config.getConfigurationSection("options").getValues(false);
	}

	/**
	 * Returns the damage from the config.
	 * @return The damage, or 1 if its not found.
	 */
	public double getDamage() {
		return config.getDouble("damage", 1);
	}
}
