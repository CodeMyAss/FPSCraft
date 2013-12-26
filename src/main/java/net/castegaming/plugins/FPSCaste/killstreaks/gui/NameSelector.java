package net.castegaming.plugins.FPSCaste.killstreaks.gui;

import java.util.Arrays;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class NameSelector extends GUI{
	
	public static HashMap<String, Integer> pages = new HashMap<String, Integer>();

	public NameSelector(String player, String name, String text) {
		super(player, name, text);
		
	}

	@Override
	public Inventory getInventory() {
		Inventory inv = super.getInventory();
		ItemStack item = selectoritem;
		ItemMeta meta = item.getItemMeta();
		meta.setLore(Arrays.asList(getClicktext()));
		
		Player[] players = Bukkit.getServer().getOnlinePlayers();
		int start = 0;
		if (players.length > max_slots-2){
			if (pages.containsKey(getPlayer())){
				start = pages.get(getPlayer())*max_slots;
			}
		}

		if (start != 0){
			//prevous
			meta.setDisplayName(ChatColor.GOLD + "Previous");
			item.setItemMeta(meta);
			inv.setItem(max_slots-8, item);
		} 
		if (players.length > start+max_slots-2){
			meta.setDisplayName(ChatColor.GOLD + "Next");
			item.setItemMeta(meta);
			inv.setItem(max_slots, item);
		}
		
		for (int i=start; i<start+max_slots; i++){
			if (players.length <= i) break;
			//display start or end, so no player
			if (!((start != 0 && i==max_slots-9) || (players.length > start+max_slots && i==max_slots))){
				meta.setDisplayName(players[i].getName());
				item.setItemMeta(meta);
				inv.setItem(i, item);
			}
		} 
		return inv;
	}
}
