 /**
 * 
 */
package net.castegaming.plugins.FPSCaste.killstreaks;

import java.util.LinkedList;
import java.util.List;

import net.castegaming.plugins.FPSCaste.util.Util;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * @author Brord
 *
 */
public abstract class Killstreak implements Listener{
	
	/**
	 *  ///////////////////////////// STATIC METHODS
	 */
	
	public static List<Killstreak> killstreaks = new LinkedList<Killstreak>();
	
	/**
	 * Checks for a {@link Killstreak} which needs the amount of kills defined
	 * @param kills the amount
	 * @return the {@link Killstreak} or <code>null</code>
	 */
	public static Killstreak forKills(int kills) {
		for (Killstreak k : killstreaks){
			if (k.getKills() == kills) return k;
		}
		return null;
	}
	
	/**
	 * Checks for a {@link Killstreak} by the name supplied.
	 * @param name the name of this {@link Killstreak}
	 * @return the {@link Killstreak} or <code>null</code>
	 */
	public static Killstreak forName(String name) {
		for (Killstreak k : killstreaks){
			if (ChatColor.stripColor(k.getName()).equalsIgnoreCase(name)) return k;
			else if (ChatColor.stripColor(k.getName()).startsWith(name)) return k;
			else if (ChatColor.stripColor(k.getName()).contains(name)) return k;
		}
		return null;
	}
	
	/**
	 * @return a list of {@link Killstreak}s possible
	 */
	public static String getList() {
		String list = "";
		for (Killstreak k : killstreaks){
			list += k.getName() + ChatColor.RESET + "(" + k.getKills() + ")" + ", ";
		}
		return list.length() > 0 ? list.substring(0, list.length()-1) : "";
	}
	
	/**
	 *  ///////////////////////////// CLASS METHODS
	 */
	
	private String name;
	private int kills;
	private ItemStack item;
	
	/**
	 * 
	 * @param name
	 * @param kills
	 * @param material
	 */
	public Killstreak(String name, int kills, String material) {
		this.kills = kills;
		this.name = ChatColor.translateAlternateColorCodes('&', name);
		item = Util.transformItem(material);
		
		initItem();
		killstreaks.add(this);
	}

	public int getKills() {
		return kills;
	}
	
	private void initItem(){
		ItemMeta m = item.getItemMeta();
		m.setDisplayName(getName());
		LinkedList<String> lore = new LinkedList<String>();
		lore.add("Awarded for having a " + kills + " killstreak!");
		lore.add("Left click to activate");
		m.setLore(lore);
		item.setItemMeta(m);
	}
	
	public ItemStack getItem(){
		return item;
	}

	/**
	 * Returns the name of this {@link Killstreak}
	 * @return a string
	 */
	public String getName() {
		return name;
	}
	 
	/**
	 * Checks if this {@link ItemStack} is equal to the {@link Killstreak} one, 
	 * has the correct {@link ItemMeta} and correct displayname 
	 * @param i the {@link ItemStack} to check
	 * @return true if it is correct, otherwise false
	 */
	public boolean isItem(ItemStack i){
		return i != null && i.getType().equals(item.getType()) && i.hasItemMeta() && i.getItemMeta().getDisplayName().equals(getName());
	}
	
	public void onStop(){
		
	}
	
	/**
	 * Takes one from the {@link ItemStack} from the {@link Player}
	 * @param p the {@link Player}
	 * @param i the {@link ItemStack}
	 * @return the modified {@link ItemStack} or <code>null</code>
	 */
	public ItemStack takeItem(Player p, ItemStack i){
		if (p != null && i != null){
			
			if (i.getAmount() > 1){
				i.setAmount(i.getAmount()-1);
			} else {
				i = null;
			}
			return i;
		} else {
			return null;
		}
	}
	
	/**
	 * Returns the distance till the next {@link Block} above this {@link Location}
	 * @param l the {@link Location}
	 * @return the distance
	 */
	protected double distanceUp(Location l){
		int y = l.getBlockY();
		return Util.emptySpace(l).getBlockY() - y;
	}
	
	/**
	 * Returns the distance till the next {@link Block} below this {@link Location}
	 * @param l the {@link Location}
	 * @return the distance
	 */
	protected double distanceDown(Location l){
		int y = l.getBlockY();
		return y - Util.blockBelow(l).getBlockY();
	}
	
	/**
	 * Damage a {@link LivingEntity} from the {@link Entity} passed.
	 * @param entity The damager
	 * @param damagee The one getting damage
	 * @param cause The cause
	 * @param damage The damage
	 * @return if the damage was not cancelled(true) otherwise false
	 */
	public boolean damage(Entity entity, LivingEntity damagee, DamageCause cause, double damage){
		EntityDamageByEntityEvent event = new EntityDamageByEntityEvent(entity, damagee, cause, damage);
		if (!event.isCancelled()){
			System.out.println(damagee.getHealth() + " " + damage);
			damagee.damage(damage > damagee.getHealth() ? damagee.getHealth() : damage, entity);
		}
		return !event.isCancelled();
	}
}
