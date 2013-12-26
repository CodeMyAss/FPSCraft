/**
 * 
 */
package net.castegaming.plugins.FPSCaste.killstreaks;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import net.castegaming.plugins.mcadmindev.killstreaks.Killstreaks;
import net.castegaming.plugins.mcadmindev.killstreaks.gui.NameSelector;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Result;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * @author Brord
 *
 */
public class Nuke extends Killstreak implements Listener{

	public HashMap<String, List<String>> nukers;
	public static String clicktext = "Click me to nuke me.";
	public static int amount = 10;
	public static double damage = 20;

	/**
	 * @param name
	 * @param kills
	 * @param material
	 */
	public Nuke(String name, int kills, String material) {
		super(name, kills, material);
		nukers = new HashMap<String, List<String>>();
	}

	@EventHandler
	public void itemclick(PlayerInteractEvent e){
		if (e.getAction().equals(Action.LEFT_CLICK_AIR) || e.getAction().equals(Action.LEFT_CLICK_BLOCK)){
			if (isItem(e.getItem())){
				e.setCancelled(true);
				e.getPlayer().openInventory(new NameSelector(e.getPlayer().getName(), getName(), clicktext).getInventory());
			}
		}
	}
	
	@EventHandler
	public void inventoryclick(InventoryClickEvent e){
		if (e.getInventory().getName().equals(getName()) && e.getCurrentItem().hasItemMeta() && e.getWhoClicked() instanceof Player){
			if (e.getClick().isLeftClick()){
				final String name = e.getCurrentItem().getItemMeta().getDisplayName();
				if (!(name.equals("Next") || name.equals("Previous"))){
					ItemMeta m = e.getCurrentItem().getItemMeta();
					List<String> lore = m.getLore();
					if (addPlayer(e.getWhoClicked().getName(), name)){
						lore.add(ChatColor.AQUA + "Selected");
					} else {
						if (lore.size() > 1){
							lore.remove(lore.size()-1);
						}
					}
					m.setLore(lore);
					e.getCurrentItem().setItemMeta(m);
				}
			}
			e.setCancelled(true);
			e.setResult(Result.DENY);
		}
	}
	
	@EventHandler
	public void invclose(InventoryCloseEvent e){
		if (e.getInventory().getName().equals(getName()) && e.getPlayer() instanceof Player){
			Player p = (Player) e.getPlayer();
			if (nukers.containsKey(p.getName()) && nukers.get(p.getName()).size() > 0){
				p.getInventory().setItem(p.getInventory().first(getItem()), takeItem(p, p.getInventory().getItem(p.getInventory().first(getItem()))));
				for (String s : nukers.get(p.getName())){
					Player player = Bukkit.getPlayer(ChatColor.stripColor(s));
					if (player != null){
						if (damage(p, player, DamageCause.ENTITY_EXPLOSION, damage)){
							player.getWorld().createExplosion(player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ(), 0, false, false);
							player.sendMessage(Killstreaks.prefix + "You have been nuked by " + p.getName());
						}
					}
				}
			} else {
				p.sendMessage(Killstreaks.prefix + "Please select " + amount + " " +  (amount > 1 ? "players" : "player") + " to nuke!");
			}
			
			nukers.remove(e.getPlayer().getName());
		}
	}
	
	private boolean addPlayer(String player, String toadd){
		List<String> selected;
		if (!nukers.containsKey(player)){
			selected = new LinkedList<String>();
		} else {
			selected = nukers.get(player);
		}
		if (selected.size() >= 10) return false;
		
		boolean add = true;
		if (selected.contains(toadd)){
			add = false;
			selected.remove(selected.indexOf(toadd));
		} else {
			selected.add(toadd);
		}
		nukers.put(player, selected);
		return add;
	}
	
}
