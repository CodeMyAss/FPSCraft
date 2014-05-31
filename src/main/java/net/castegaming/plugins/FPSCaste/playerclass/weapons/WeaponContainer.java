package net.castegaming.plugins.FPSCaste.playerclass.weapons;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.castegaming.plugins.FPSCaste.FPSCaste;
import net.castegaming.plugins.FPSCaste.FPSPlayer;
import net.castegaming.plugins.FPSCaste.enums.GunClass;

public abstract class WeaponContainer {
	
	private static List<Integer> disallowed_ids = Arrays.asList(
			256,257,258,259,267,268,269,270,271,272,273,274,275,276,277,278,279,283,284,285,286,290,291,
			292,293,294,298,299,300,301,302,303,304,305,306,307,308,309,310,311,312,313,314,315,316,317);
	
	/**
	 * Holds all the weapons in this game.
	 * The Key, Integer, is the itemID
	 */
	private static HashMap<Integer, WeaponContainer> weapons = new HashMap<Integer, WeaponContainer>();
	
	/**
	 * This function will return the weapon based on its item ID.<br/>
	 * If the weapon is not found, null will be returned
	 * @param ID the item ID to look for
	 * @return The Weapon, or null if it isnt found
	 */
	public static WeaponContainer getWeapon(Integer ID) {
		return weapons.get(ID);
	}
	
	/**
	 * This function will return the weapon based on its name.<br/>
	 * If the weapon is not found, it will check if this name is PART of a gun<br/>
	 * still not? null will be returned
	 * @param name The name to look for
	 * @return The Weapon, or null if it isnt found
	 */
	public static WeaponContainer getWeapon(String name){
		for (WeaponContainer weapon : weapons.values()){
			if (weapon.getName().equalsIgnoreCase(name)){
				return weapon;
			}
		}
		for (WeaponContainer weapon : weapons.values()){
			if (weapon.getName().contains(name)){
				return weapon;
			}
		}
		return null;
	}
	
	/**
	 * Returns thenext itemID available
	 * @return
	 */
	public static int getNextIDAvailable() {
		int id = 256;
		while (id < 421){
			int amountgood = 0;
			if (!disallowed_ids.contains(id)){
				for (int w : weapons.keySet()){
					if (w == id){
						break;
					} else {
						amountgood++;
					}
				}
			}
			if (amountgood == weapons.size()){
				break;
			} else {
				id++;
			}
		}
		return id;
	}
		
	
	/*******************************Class variables**********************************/
	
	private int itemID;
	
	private String name = "Unnamed";
	
	private int amount = 1;
	
	private GunClass type = GunClass.PRIMARY;

	private String groupname;
	
	private int delay;

	private int level;
	
	private double damage;
	
	protected WeaponContainer(String name, GunClass type, String group, int delay, int level, int amount) {
		this(getNextIDAvailable(), name, type, group, delay, level, amount);
	}
	
	protected WeaponContainer(String name, GunClass type, String group, int delay, int amount) {
		this(getNextIDAvailable(), name, type, group, delay, 1, amount);
	}
	
	protected WeaponContainer(int id, String name, GunClass type, String group, int delay, double damage, int amount) {
		this(id, name, type, group, delay, damage, 1, amount);
	}
	
	protected WeaponContainer(int id, String name, GunClass type, String group, int delay, double damage) {
		this(id, name, type, group, delay, damage, 1, 1);
	}
	
	protected WeaponContainer(int id, String name, GunClass type, String group, int delay, double damage, int level, int amount) {
		this.itemID = id;
		this.name = name;
		this.type = type;
		this.groupname = group;
		this.delay = delay;
		this.level = level;
		this.damage = damage;
		this.amount = amount;
		weapons.put(itemID, this);
	}
	
	public void give(String name){
		give(FPSCaste.getFPSPlayer(name));
	}
	
	public void give(Player p){
		if (p != null){
			give(FPSCaste.getFPSPlayer(p.getName()));
		}
	}
	
	public void give(FPSPlayer p){
		if (p != null && p.isOnline() && p.isIngame()){
			p.getPlayer().getInventory().setItem(type.getSlot(), constructItem());
		}
	}
	
	public ItemStack constructItem() {
		ItemStack item = new ItemStack(itemID, amount);
		ItemMeta m = item.getItemMeta();
		m.setDisplayName(ChatColor.WHITE + "" + ChatColor.BOLD + name);
		item.setItemMeta(m);
		return item;
	}
	
	/**
	 * Returns the default damage done by this gun
	 * @return
	 */
	public double getDamage() {
		return damage;
	}
	
	/**
	 * Returns the name used for this gun.
	 * @return The name
	 */
	public String getName(){
		return name;
	}
	
	/**
	 * Sends the item ID used for this weapon
	 * @return The itemID
	 */
	public int getItemID(){
		return itemID;
	}

	/**
	 * @return the amount
	 */
	public int getAmount() {
		return amount;
	} 
	
	/**
	 * @return the groupname
	 */
	public String getGroup() {
		return groupname;
	}

	/**
	 * @return the type
	 */
	public GunClass getType() {
		return type;
	}

	/**
	 * @return the delay
	 */
	public int getDelay() {
		return delay;
	}
	
	/**
	 * Returns the rewuired level for this weapon
	 * @return
	 */
	public int getLevel(){
		return level;
	}
	
	public void useLeft(String name){
		useLeft(FPSCaste.getFPSPlayer(name));
	}
	
	public void useLeft(Player p){
		useLeft(FPSCaste.getFPSPlayer(p.getName()));
	}
	
	public abstract void useLeft(FPSPlayer p);
	
	public void useRight(String name){
		useRight(FPSCaste.getFPSPlayer(name));
	}
	
	public void useRight(Player p){
		useRight(FPSCaste.getFPSPlayer(p.getName()));
	}
	
	public abstract void useRight(FPSPlayer p);
	
	@Override
	public String toString() {
		return name;
	}

	public boolean canZoom() {
		return false;
	}
}
