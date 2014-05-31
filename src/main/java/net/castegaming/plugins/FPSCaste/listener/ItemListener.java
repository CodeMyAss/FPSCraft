package net.castegaming.plugins.FPSCaste.listener;

import net.castegaming.plugins.FPSCaste.FPSCaste;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;

public class ItemListener implements Listener {
	
	FPSCaste plugin = null;
	
	public ItemListener(FPSCaste plugin){
		this.plugin = plugin;
	}
	
	
	@EventHandler
	public void EditInventoryEvent(InventoryClickEvent e){
		if (FPSCaste.getFPSPlayer(e.getWhoClicked().getName()).isIngame() && !FPSCaste.getFPSPlayer(e.getWhoClicked().getName()).canBuild()){
			e.getWhoClicked().closeInventory();
			e.setCancelled(true);
		}
		
	}
	
	@EventHandler
	public void ItemDropEvent(PlayerDropItemEvent e){  
		if (FPSCaste.getFPSPlayer(e.getPlayer().getName()).isIngame()){
			if (FPSCaste.getFPSPlayer(e.getPlayer().getName()).isFrozen()){
				e.setCancelled(true); return;
			}
			
			if (e.getPlayer().getItemInHand().getType().equals(Material.AIR)){
				e.getPlayer().getInventory().setItemInHand(e.getItemDrop().getItemStack());
			} else {
				e.getPlayer().getItemInHand().setAmount(e.getPlayer().getItemInHand().getAmount()+1);
			}
			e.getItemDrop().remove();
			FPSCaste.getFPSPlayer(e.getPlayer().getName()).reload();
		}
	}
	
	@EventHandler
	public void onItemPickUp(PlayerPickupItemEvent e){
		if (FPSCaste.getFPSPlayer(e.getPlayer().getName()).isIngame() && !FPSCaste.getFPSPlayer(e.getPlayer().getName()).canBuild()){
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void itemChangeEvent(PlayerItemHeldEvent e){
		if (FPSCaste.getFPSPlayer(e.getPlayer().getName()).isIngame()){
			FPSCaste.getFPSPlayer(e.getPlayer().getName()).zoomOut();
		}
	}
}
