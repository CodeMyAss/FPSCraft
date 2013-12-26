package net.castegaming.plugins.FPSCaste.killstreaks.gui;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public abstract class GUI {
	
	public static int max_slots = 54;
	public static ItemStack selectoritem = new ItemStack(Material.SKULL_ITEM);
	
	private String player;
	private String invname;
	private String clicktext;
	
	public GUI(String player, String invname, String clicktext) {
		this.player = player;
		this.invname = invname;
		this.clicktext = clicktext;
	}
	
	public Inventory getInventory(){
		return Bukkit.getServer().createInventory(getPlayer(), max_slots, invname);
	}
	
	public String getName(){
		return player;
	}
	
	public Player getPlayer(){
		return Bukkit.getServer().getPlayer(player);
	}
	
	public String getInvName(){
		return invname;
	}

	public String getClicktext() {
		return clicktext;
	}
}
