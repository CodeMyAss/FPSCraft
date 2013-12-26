package net.castegaming.plugins.FPSCaste.config;

import net.castegaming.plugins.FPSCaste.util.Parse;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

public class PlayerConfiguration {
	
	public static String DEFAULT_CLASS = "Grenadier";
	
	private YamlConfiguration config;
	
	private String name;
	
	public PlayerConfiguration(String name) {
		config = Config.getPlayerYMLConfig(name);
		this.name = name;
	}
	
	public int getXP(){
		return config.getInt("xp", 0);
	}
	
	public void setXP(int xp){
		config.set("xp", xp);
		save();
	}
	
	public int getKills(){
		return config.getInt("kills", 0);
	}
	
	public void setKills(int kills){
		config.set("kills", kills);
		save();
	}
	
	public int getDeaths(){
		return config.getInt("deaths", 0);
	}
	
	public void setDeaths(int deaths){
		config.set("deaths", deaths);
		save();
	}
	
	public int getAssists(){
		return config.getInt("assists", 0);
	}
	
	public void setAssists(int assists){
		config.set("assists", assists);
		save();
	}
	
	public boolean isAdmin(){
		return config.getBoolean("isadmin", false);
	}
	
	public void setAdmin(boolean isadmin){
		config.set("isadmin", isadmin);
		save();
	}
	
	public String getSelectedClass(){
		return config.getString("class", DEFAULT_CLASS);
	}
	
	/**
	 * returns the configurationsetion from the class passed through
	 * @param className the clas to grab
	 * @return the {@link ConfigurationSection}
	 */
	public ConfigurationSection getClassSection(String className){
		return config.getConfigurationSection("customclasses." + className);
	}
	
	/**
	 * Returns the configurationsetion fromt he player his current class<br/>
	 * If this is a default class, null will be returned
	 * @return the {@link ConfigurationSection}
	 */
	public ConfigurationSection getCurrentClassSection(){
		return getClassSection(getSelectedClass());
	}
	
	public void setSelectedClass(String clas){
		config.set("class", clas);
		save();
	}
	
	public void save(){
		Config.savePlayerConfig(name, config);
	}
	
	public ConfigurationSection currentClassSection(){
		return currentClassSection(getSelectedClass());
	}
	
	public ConfigurationSection currentClassSection(String clas){
		return config.getConfigurationSection("customclass." + clas);
	}

	public void set(String key, Object object) {
		config.set(key, object);
	}

	public YamlConfiguration getConfig(){
		return config;
	}

	/**
	 * Returns the name of a created class
	 * @param className the class to look for
	 * @return the correct name, with capitalization and all
	 */
	public String getCreatedClass(String className) {
		if (config.contains("customclasses." + className)){
			return className;
		} else {
			for (String name : config.getConfigurationSection("customclasses").getKeys(false)){
				if (name.toUpperCase().equals(className.toUpperCase())){
					return name;
				}
			}
		}
		return "";
	}

	/**
	 * Returns the name of all the custom classes by this player
	 * @return the names in a strng 
	 */
	public String getCustomclasses() {
		if (!hasCustomClasses()){
			return "";
		}
		return Parse.ArrayToString(config.getConfigurationSection("customclasses").getKeys(false).toArray(new String[0]));
	}

	public boolean hasCustomClasses() {
		return config.contains("customclasses") && config.getConfigurationSection("customclasses").getKeys(false).size() > 0;
	}
}
