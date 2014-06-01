package net.castegaming.plugins.FPSCaste.enums;

import org.bukkit.Material;

public enum BreakableBlocks {
	GLASS(Material.GLASS),
	TNT(Material.TNT);
	
	Material material;
	
	BreakableBlocks(Material material){
		this.material = material;
	}
	
}
