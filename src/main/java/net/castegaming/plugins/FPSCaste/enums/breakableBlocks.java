package net.castegaming.plugins.FPSCaste.enums;

import org.bukkit.Material;

public enum breakableBlocks {
	GLASS(Material.GLASS),
	TNT(Material.TNT);
	
	Material material;
	
	breakableBlocks(Material material){
		this.material = material;
	}
	
}
