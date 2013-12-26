package net.castegaming.plugins.FPSCaste.util;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class Util {
	/**
	 * Transforms name:damage into an {@link ItemStack}
	 * @param material the name
	 * @return
	 */
	public static ItemStack transformItem(String material){
		String[] itemnames = material.split(":");
		Material mat = Material.getMaterial(itemnames[0]);
		
		ItemStack i = new ItemStack(mat);
		if (itemnames.length > 1){
			i.setDurability((short) Parse.parseInt(itemnames[1], 0));
		}
		return i;
	}
	
	public static boolean isEqual(Location l1, Location l2){
		if (!l1.getWorld().getName().equals(l2.getWorld().getName())) return false;
		
		double xfrom = l1.getX(); 
	    double zfrom = l1.getZ();
	        
	    double xto = l2.getX();
	    double zto = l2.getZ();
	        
	    return (xfrom == xto && zfrom==zto);
	}
	
	public static Location emptySpace(Location l){
		if (l != null){
			Location locabove = l;
			locabove.add(0, 1, 0);
			if (locabove.getBlock().getType().equals(Material.AIR)){
				locabove = emptySpace(locabove);
			}
			return locabove;
		} else {
			return null;
		}
	}
	
	public static Location blockBelow(Location l){
		if (l != null){
			Location locbelow = l;
			locbelow.add(0, -1, 0);
			if (locbelow.getBlock().getType().equals(Material.AIR)){
				locbelow = blockBelow(locbelow);
			}
			return locbelow;
		} else {
			return null;
		}
	}
}
