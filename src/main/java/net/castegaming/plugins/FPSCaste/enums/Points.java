package net.castegaming.plugins.FPSCaste.enums;

import java.lang.reflect.Field;
import java.util.HashMap;

import org.bukkit.configuration.file.YamlConfiguration;

import net.castegaming.plugins.FPSCaste.FPSCaste;
import net.castegaming.plugins.FPSCaste.config.Config;

public class Points {
	public static int KILL_TDM; 
	public static int KILL_DOM; 
	public static int DEATH; 
	public static int ASSIST; 
	public static int HEADSHOT;
	public static int FIRST_BLOOD;
	public static int PAYBACK; 
	public static int FLAG_GRAB; 
	public static int FLAG_RETURN; 
	public static int FLAG_CAPTURE; 
	public static int CAPTURE_NEUTRAL; 
	public static int CAPTURE_ENEMY; 
	public static int CAPTURE_ASSIST; 
	public static int MATCH_WIN; 
	public static int AVENGE; 
	public static int BUZZKILL; 
	
	public static HashMap<String, Integer> getAllPoints(){
		HashMap<String, Integer> values = new HashMap<String, Integer>();
		for (Field f : Points.class.getFields()) {
			try {
				values.put(f.getName(), f.getInt(f.getName()));
			} catch (IllegalArgumentException e) {
			} catch (IllegalAccessException e) {
			}
		}
		return values;
	}
	
	public static void load(){
		YamlConfiguration config = Config.getConfig("points");
		
		if (config == null){
			config = Config.copy("points");
		}
		
		for (String key : config.getKeys(false)){
			String fNameString = key.toUpperCase();
			int value;
			try {
				value = config.getInt(key);
			} catch (Exception e) {
				//not a valid int
				//System.out.println(FPSCaste.NamePrefix + key + ": " + config.get(key) + " is not a valid number!");
				value = YamlConfiguration.loadConfiguration(FPSCaste.getInstance().getResource("points")).getInt(key);
			}
			
			Field f = null;
			try {
				//f = Points.class.getDeclaredField("public static int net.castegaming.plugins.FPSCaste.enums.Points." + fNameString);
				f = Points.class.getDeclaredField(fNameString);
			} catch (NoSuchFieldException e) {
			} catch (SecurityException e) {
			}
			
			if (f != null){
				try {
					f.set(null, value);
					FPSCaste.log("Set " + key + " to: " + value);
				} catch (IllegalArgumentException e) {
				} catch (IllegalAccessException e) {
				}
			}
			
		}
	}
}
