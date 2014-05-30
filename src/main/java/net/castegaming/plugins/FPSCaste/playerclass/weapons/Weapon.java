/**
 * 
 */
package net.castegaming.plugins.FPSCaste.playerclass.weapons;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import net.castegaming.plugins.FPSCaste.FPSCaste;
import net.castegaming.plugins.FPSCaste.FPSPlayer;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * @author Brord
 *
 */
public class Weapon {
	private WeaponContainer weapon;
	
	private String owner;
	
	///////////// MAYBE
	private int slot;
	///////////// MAYBE

	private long shootingTime;
	
	private ItemStack takenWeapon;
	
	private List<String> attachements;
	
	private int reloadTask = -1;
	
	public Weapon(WeaponContainer weapon) {
		if (weapon != null){
			this.weapon = weapon;
			slot = weapon.getType().getSlot();
		} else {
			System.out.println("Weapon cannot be null!!");
		}
	}
	
	/**
	 * returns the item from the slot this weapon is preassigned for
	 * @return
	 */
	public ItemStack getItem(){
		return getFPSPlayer().getPlayer().getInventory().getItem(slot);
	}
	
	/**
	 * Gives the player the weapon
	 * @param newItem if the item should be new, or given back.
	 */
	public void give(boolean newItem){
		if (getFPSPlayer() != null){
			if (newItem){
				getFPSPlayer().getPlayer().getInventory().setItem(slot,constructItem());
			} else {
				getFPSPlayer().getPlayer().getInventory().setItem(slot, takenWeapon);
				takenWeapon = null;
			}
		}
	}
	
	private ItemStack constructItem(){
		ItemStack itemStack = weapon.constructItem();
		if (isGun()){
			ItemMeta meta = itemStack.getItemMeta();
			List<String> lore = meta.getLore();
			for (String s : attachements){
				lore.add(ChatColor.AQUA + "Attachement" + ChatColor.GRAY + ": " + ChatColor.GOLD + s);
			}
			meta.setLore(lore);
			itemStack.setItemMeta(meta);
		}
		return itemStack;
	}
	
	/**
	 * THis method detects if a gun has been taken.<br/>
	 * If its not, a new is created, otherwise old is given back.
	 */
	public void give() {
		if(takenWeapon == null) { give(true); } else { give(false);}
	}
	
	/**
	 * Takes the weapon from the player.<br/>
	 * If takenWeapon allready is used, old will be given back first
	 */
	public void take(){
		if (owner != null && owner != "" ){
			if (takenWeapon != null) giveBack();
			
			takenWeapon = getFPSPlayer().getPlayer().getInventory().getItem(slot);
			getFPSPlayer().getPlayer().getInventory().setItem(slot, null);
		}
	}
	
	public int getBullets(){
		if (weapon instanceof Gun && getItem() != null){
			return getItem().getAmount();
		}
		return -1;
	}
	
	public FPSPlayer getFPSPlayer() {
		return FPSCaste.getFPSPlayer(owner);
	}
	
	public int getMagezines(){
		return getMagezines(getItem());
	}
	
	public int getMagezines(ItemStack item){
		if (weapon instanceof Gun){
			if (item.getItemMeta().hasLore()){
				if (item.getItemMeta().getLore().size() > 0){
					return Integer.parseInt(item.getItemMeta().getLore().get(0).split(" ")[1]);
				}
			} 
		}
		return -1;
	}
	
	public ItemStack setMagezines(int magezines, ItemStack item){
		if (weapon instanceof Gun){
			ItemMeta m = item.getItemMeta();
			m.setLore(new LinkedList<String>(Arrays.asList(new String[]{"Has " + magezines + " magezines left"})));
			item.setItemMeta(m);
		}
		return item;
	}
	
	public void setMagezines(int magezines){
		if (weapon instanceof Gun){
			ItemMeta m = getItem().getItemMeta();
			m.setLore(new LinkedList<String>(Arrays.asList(new String[]{"Has " + (getMagezines()-1) + " magezines left"})));
			getItem().setItemMeta(m);
		}
	}
	 public Weapon() {
		// TODO Auto-generated constructor stub
	}
	
	public void useRight(){
		weapon.useRight(owner);
	}
	
	public void shoot(){
		weapon.useRight(owner);
	}

	public List<String> getAttachments() {
//		List<Attachment> attachments = new LinkedList<Attachment>();
//		if (weapon instanceof Gun){
//			if (getItem().getItemMeta().hasLore()){
//				if (getItem().getItemMeta().getLore().size() > 1){
//					for (String s : getItem().getItemMeta().getLore().get(1).replaceAll(",", "").split(" ")){
//						if (Attachment.getAttachment(s) != null){
//							attachments.add(Attachment.getAttachment(s));
//						}
//					}
//				}
//			} 
//		}
		return attachements;
	}
	
	public String getName(){
		return weapon.getName();
	}
	
	public int getID(){
		return weapon.getItemID();
	}
	
	public int getSlot(){
		return weapon.getType().getSlot();
	}
	
	public String getOwner() {
		return owner;
	}
	
	public void setOwner(String owner) {
		this.owner = owner;
	}
	
	public void setOwner(Player owner) {
		setOwner(owner.getName());
	}

	public boolean canReload() {
		return weapon instanceof Gun && ((Gun)weapon).isReloadable();
	}

	/**
	 * Reloads the weapon.
	 * @return the task ID for the reload
	 */
	public void reload() {
		if (canReload()){
			take();
			reloadTask =  Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(FPSCaste.getInstance(), new Runnable() {
				
				@Override
				public void run() {
					if (takenWeapon !=null){
						takenWeapon.setAmount(weapon.getAmount());
						takenWeapon = setMagezines(getMagezines(takenWeapon)-1, takenWeapon);
						reloadTask = -1;
						giveBack();
					}
				}
			}, ((Gun) weapon).getReloadTime());
		} 
	}
	
	public void stopReload() {
		if (isReloading()){
			Bukkit.getServer().getScheduler().cancelTask(reloadTask);
			giveBack();
		}
	}
	
	public boolean isReloading(){
		return reloadTask > -1;
	}

	public void giveBack() {
		give(false);
	}
	
	public void giveNew() {
		give(true);
	}

	public void addAttachments(List<String> attachments) {
		this.attachements = attachments;
	}

	public boolean canZoom() {
		//every gun should have an iron sight
		return weapon instanceof Gun || weapon.canZoom();
	}

	public int getMaxZoomLevel() {
		if (!canZoom()) return 0;
		if (getAttachments().contains("Red dot")){
			return 2;
		} else {
			return 1;
		}
	}
	
	@Override
	public String toString() {
		return weapon.getName();
	}

	public void setBullets(int bullets) {
		if (weapon instanceof Gun){
			getItem().setAmount(bullets);
		}
	}

	public boolean isGun() {
		return weapon instanceof Gun;
	}

	/**
	 * Returns the amount of uses for this<br/>
	 * Mainly used for stun etc
	 * @return
	 */
	public int getAmount() {
		return getItem().getAmount();
	}

	public void setAmount(int amount) {
		getItem().setAmount(amount);
	}

	public int getDelay() {
		return weapon.getDelay();
	}
}
