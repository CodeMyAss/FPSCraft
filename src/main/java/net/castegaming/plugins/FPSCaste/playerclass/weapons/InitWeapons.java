package net.castegaming.plugins.FPSCaste.playerclass.weapons;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.bukkit.ChatColor;

import net.castegaming.plugins.FPSCaste.FPSCaste;
import net.castegaming.plugins.FPSCaste.config.WeaponsConfiguration;
import net.castegaming.plugins.FPSCaste.config.RegisterConfigs;
import net.castegaming.plugins.FPSCaste.enums.GunClass;

public class InitWeapons {
	private static boolean reChecked = false;
	
	public InitWeapons(FPSCaste p){
		File weaponsDir = new File(p.getDataFolder() + File.separator + "weapons" + File.separator);
		if (!weaponsDir.exists()){
			if (!reChecked){
				//config doest exist, reload the configs
				new RegisterConfigs(p);
				
				//retry to load.
				reChecked = true;
				new InitWeapons(p);	
				return;
			} else {
				new FileNotFoundException(File.separator + "FPSCaste" + File.separator + "weapons" + File.separator).printStackTrace();
			}
		}
		
		//   /weapons/primary/assault/ak47.yml
		//all folders in /weapons/
		for (File folder : weaponsDir.listFiles()){
			GunClass type = GunClass.valueOf(folder.getName().toUpperCase()); 
			FPSCaste.log(ChatColor.RED + "Found type: " + type);
			if (folder.isDirectory() && type != null){
				//gunClass
				for (File group : folder.listFiles()){
					//guns group
					if (group.isDirectory()){
						String groupName = group.getName();
						FPSCaste.log(ChatColor.GREEN + " Found group: " + groupName);
						for (File f : group.listFiles()){
							//gun name
							if (!f.isDirectory()){
								loadWeapon(f, type, groupName);
							}
						}
					} else if (group.getName().endsWith(".yml")){
						loadWeapon(group, type, "");
					}
				}
			}
		}
	}
	
	public void loadWeapon(File f, GunClass type, String group){
		WeaponsConfiguration weaponsConfig = new WeaponsConfiguration(f);
		if (weaponsConfig != null){
			String weaponsName = f.getName().substring(0, f.getName().lastIndexOf(".")).toLowerCase();
			weaponsName = Character.toString(weaponsName.charAt(0)).toUpperCase() + weaponsName.substring(1);
			
			int id = weaponsConfig.getID();
			String name = weaponsConfig.getName();
			int delay = weaponsConfig.getDelay();
			double damage = weaponsConfig.getDamage();
			int level = weaponsConfig.getLevel();
			int amount = weaponsConfig.getAmount();
			
			Constructor<?> c = null;
			
			try {
				if (Class.forName("net.castegaming.plugins.FPSCaste.playerclass.weapons.custom." + weaponsName).getConstructors().length > 0){
					c = Class.forName("net.castegaming.plugins.FPSCaste.playerclass.weapons.custom." + weaponsName).getConstructors()[0];
				}
			} catch (SecurityException e) {
			} catch (ClassNotFoundException e) {
			} catch (IllegalArgumentException e) {
			} catch (NullPointerException e) {
			}
			
			if (c == null){
				Class<? extends WeaponContainer> i = type.getGunClass();
				c = i.getConstructors()[1];
			}
			
			if (c != null){
				try {
					WeaponContainer w = (WeaponContainer) c.newInstance(id, name, type, group, delay, damage, level, amount, weaponsConfig.getOptions());
					FPSCaste.log(ChatColor.BLUE + "  Found and loaded weapon: " + w.getName() + "(ID: " + w.getItemID() + ")");
				} catch (InstantiationException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
