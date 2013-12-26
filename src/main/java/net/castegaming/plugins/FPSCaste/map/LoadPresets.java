package net.castegaming.plugins.FPSCaste.map;

import java.io.File;
import java.util.List;
import java.util.logging.Level;

import org.bukkit.configuration.file.YamlConfiguration;

import net.castegaming.plugins.FPSCaste.FPSCaste;
import net.castegaming.plugins.FPSCaste.config.Config;

public class LoadPresets {

	public LoadPresets() {
		File folder = new File(FPSCaste.getInstance().getDataFolder() + File.separator + "maps");
		for (File f : folder.listFiles()){
			if (f.isFile()){
				String name = f.getName();
				if (name.endsWith("yml") || name.endsWith(".YML")){
					String realName = name.replace(".yml", "").replace(".YML", "").toLowerCase();
					
					YamlConfiguration c = Config.getMap(realName);
					if (c.getShortList("main") != null){
						String[] s = c.getString("main").split(" ");
						Float[] coordinates = new Float[5];
						try {
							for (int i=0; i<s.length; i++){
								coordinates[i] = Float.parseFloat(s[i]);
							}
							
							MapPreset preset = new MapPreset(realName, coordinates);
							
							for (String key : c.getKeys(false)){
								String keyString = c.getString(key);
								
								if (key.equalsIgnoreCase("domA")){
									preset.setDomA(parse(keyString));
								} else if (key.equalsIgnoreCase("domB")){
									preset.setDomB(parse(keyString));
								} else if (key.equalsIgnoreCase("domC")){
									preset.setDomC(parse(keyString));
								} else if (key.equalsIgnoreCase("BombA")){
									preset.setBombA(parse(keyString));
								} else if (key.equalsIgnoreCase("BombB")){
									preset.setBombB(parse(keyString));
								} else if (key.equalsIgnoreCase("allies")){
									preset.setSpawnAllies(parse(keyString));
								} else if (key.equalsIgnoreCase("axis")){
									preset.setSpawnAxis(parse(keyString));
								} else if (key.equalsIgnoreCase("main")){
									preset.setSpawn(parse(keyString));
								} else if (key.equalsIgnoreCase("description")){
									preset.setDescription(keyString);
								} else if (key.equalsIgnoreCase("name")){
									preset.setName(keyString);
								} else if (key.equals("spawnpoints")){
									List<String> spawns = c.getStringList(key);
									for (String s1 : spawns){
										Float[] coordinates1 = parse(s1);
										if (coordinates1 != null){
											preset.addSpawnpoint(coordinates1);
										} else {
											FPSCaste.log("Found an incorrect spawnpoint! " + s1, Level.WARNING);
										}
									}
								}
							}
							
							FPSCaste.log("Registered preset: " + realName, Level.INFO);
						} catch (Exception e){
							FPSCaste.log("Failed to parse the map: " + realName, Level.WARNING);
						}
					}
				}
			}
		}
	}
	
	public Float[] parse(String s){
		String[] intS = s.split(" ");
		Float[] coordinates = new Float[5];
		
		try {
			for (int i=0; i<intS.length; i++){
				coordinates[i] = Float.parseFloat(intS[i]);
			}
			return coordinates;
		} catch (Exception e) {
			return null;
		}
	}
}
