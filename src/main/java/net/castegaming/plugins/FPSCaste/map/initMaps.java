package net.castegaming.plugins.FPSCaste.map;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;

import net.castegaming.plugins.FPSCaste.FPSCaste;
import net.castegaming.plugins.FPSCaste.config.Config;

public class initMaps {
	public initMaps(){
		YamlConfiguration MapConfig = Config.getConfig("maps");
		
		for (String key : MapConfig.getKeys(false)){
			if (!MapConfig.isConfigurationSection(key)) continue;
			
			String map = MapConfig.getString(key + ".mapname").toLowerCase();
			World world = Bukkit.getServer().getWorld(MapConfig.getString(key + ".world"));
			if (world == null){
				FPSCaste.log("Warning world " + MapConfig.getString(key + ".world") + " cannot be found for map " + map);
				continue;
			}
			
			double x = MapConfig.getDouble(key + ".x");
			double y = MapConfig.getDouble(key + ".y");
			double z = MapConfig.getDouble(key + ".z");
			float pitch = (float) MapConfig.getDouble(key + ".pitch");
			float yaw = (float) MapConfig.getDouble(key + ".yaw");
			
			Location loc = new Location(world, x, y, z, yaw, pitch);
			
			new Map(map, loc);
		}
	}
}
