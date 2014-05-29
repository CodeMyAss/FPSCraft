package net.castegaming.plugins.FPSCaste.config;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;

import net.castegaming.plugins.FPSCaste.FPSCaste;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
 
public class Config {
		
        public static FPSCaste plugin = FPSCaste.getInstance();
 
        /**
         * Creates the specified config<br/>
         * If this config has an embedded resource in the jar, that one will be copied
         * @param name The config to create
         * @return The YamlConfiguration which you just made
         */
        public static YamlConfiguration createConfig(String name) {
                if (!name.endsWith(".yml")) {
                	name += ".yml";
                }
                
                if (plugin.getResource(name) != null){
                	plugin.saveResource(name, false);
                	return getConfig(name);
                } else {
                	File file = new File(plugin.getDataFolder(),name);
                    if (!file.exists()) {
                            plugin.getDataFolder().mkdir();
                            try {
                                    file.createNewFile();
                            } catch (IOException e) {
                                    e.printStackTrace();
                            }
                    }
                    return YamlConfiguration.loadConfiguration(file);// returns the newly created configuration object.
            }
        }
                
 
        /**
         * Saves the specified config to the main plugin dir.<br/>
         * The string is the name of the config to save
         * @param name The name the config should have
         * @param config The config to  save
         * @return nothing
         */
        public static YamlConfiguration saveConfig(String name, YamlConfiguration config) {
                if (!name.endsWith(".yml")) {
                	name += ".yml";
                }
                
                try {
                        config.save(plugin.getDataFolder() + File.separator + name);
                } catch (IOException e) {
                       FPSCaste.log("Could not save: " + name + ", is the disk full?", Level.WARNING);
                }
                return config;
        }

        /**
         * Gets the specified config from the main plugin dir
         * @param name The config to get
         * @return The YamlConfiguration or null if it doesnt exist
         */
		public static YamlConfiguration getConfig(String name) {
            if (!name.endsWith(".yml")) {
            	name += ".yml";
	        }
            
            File file = new File(plugin.getDataFolder(), name);
            if (!file.exists()){
				return null;
			} else {
				return YamlConfiguration.loadConfiguration(file);
			}
		}
		
		/******************************Player configs***************************************/
		
		/**
		 * Creates a player config, and saves it to disk
		 * @param name name of the config/player
		 */
		public static YamlConfiguration createPlayerConfig(String name) {
			if (!name.endsWith(".yml")) {
				name += ".yml";
			}
			
	        File file = new File(plugin.getDataFolder() + File.separator + "players", name);
	        if (!file.exists()) {
	            try {
	                 file.createNewFile();
	            } catch (IOException e) {
	            	FPSCaste.log("Could not save: " + name + ", is the disk full?", Level.WARNING);
	            }
	        }
	        YamlConfiguration playerfile = getPlayerYMLConfig(name);
	        FileConfiguration defaultplayer = getConfig("defaultplayer");
	        
	        for (String key : defaultplayer.getKeys(false)){
	        	playerfile.set(key, defaultplayer.get(key));
	        }
	        
	        savePlayerConfig(name, playerfile);
	        return playerfile;
		}
		
		/**
		 * Saves the player config to disk
		 * @param name the name of the player/config
		 * @param config the Player his config to save
		 */
		public static void savePlayerConfig(String name, FileConfiguration config) {
            if (!name.endsWith(".yml")) {
            	name += ".yml";
            }
            File file = new File(plugin.getDataFolder() + File.separator + "players", name);
            try {
                config.save(file);
            } catch (IOException e) {
            	FPSCaste.log("Could not save: " + name + ", is the disk full?", Level.WARNING);
            }
		}
		
		/**
		 * Gets the player his configuration
		 * @param name the name of the player/config
		 * @return the config
		 */
		public static YamlConfiguration getPlayerYMLConfig(String name) {
			if (!name.endsWith(".yml")) {
				name += ".yml";
			}
			
			File file = new File(plugin.getDataFolder() + File.separator + "players", name);
			
			if (!file.exists()){
				return createPlayerConfig(name);
			} else {
				return YamlConfiguration.loadConfiguration(file);
			}
		}
		
		/**
		 * Returns the playerconfiguration file from this player.<br/>
		 * Will create a new one if it doesnt exist
		 * @param name the player to get the config from
		 * @return the {@link PlayerConfiguration}
		 */
		public static PlayerConfiguration getPlayerConfig(String name){
			return new PlayerConfiguration(name);
		}

		
		/**
		 * Copys values from a config inside the plugin jar
		 * @param name the name of the config to copy
		 * @return the freshly made config
		 */
		public static YamlConfiguration copy(String name){
			if (!name.endsWith(".yml")) {
				name += ".yml";
			}
			
			YamlConfiguration config = getConfig(name);
			
			//Does this config exist?
			if (config == null){
				config = YamlConfiguration.loadConfiguration(new File(plugin.getDataFolder()+ ""));
			}
			
			InputStream defaultStream = plugin.getResource(name);
			//does the raw config exist?
			if (defaultStream != null){
					
				//laod the stream into a config
				YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defaultStream);
					
				//add the defaults to the config
				config.addDefaults(defConfig);
					
				//save the config to disk
				saveConfig(name, defConfig);
			}
			
			return config;
		}
		
		public static YamlConfiguration getMap(String mapname){
			if (!mapname.endsWith(".yml")) {
				mapname += ".yml";
			}
			
			File file = new File(plugin.getDataFolder() + File.separator + "maps", mapname);
			
			if (!file.exists()){
				return createMapConfig(mapname);
			} else {
				return YamlConfiguration.loadConfiguration(file);
			}
		}
		
		public static YamlConfiguration createMapConfig(String name) {
			if (!name.endsWith(".yml")) {
				name += ".yml";
			}
			
			File file = new File(plugin.getDataFolder() + File.separator + "maps", name);
            if (!file.exists()) {
                    plugin.getDataFolder().mkdir();
                    try {
                        file.createNewFile();
                    } catch (IOException e) {
                    	FPSCaste.log("Could not create map file: " + name + ", is the disk full?", Level.WARNING);
                    }
             }
            
            return YamlConfiguration.loadConfiguration(file);
		}
		
		/**
		 * Saves the map config
		 * @param name the name of the config
		 * @param config the FileConfiguration to save
		 */
		public static void saveMapConfig(String name, FileConfiguration config) {
            if (!name.endsWith(".yml")) {
            	name += ".yml";
            }
        
            File file = new File(plugin.getDataFolder() + File.separator + "maps", name);
            try {
                    config.save(file);
            } catch (IOException e) {
            	FPSCaste.log("Could not save map: " + name + ", is the disk full?", Level.WARNING);
            }
		}

		/**
		 * Returns if this player has a config.<br/>
		 * No config means never joined before
		 * @param name the player to look for
		 * @return False if he has no config, otherwise true
		 */
		public static boolean hasPlayerConfig(String name) {
			return new File(plugin.getDataFolder() + File.separator + "players", name + ".yml").exists();
		}
}
