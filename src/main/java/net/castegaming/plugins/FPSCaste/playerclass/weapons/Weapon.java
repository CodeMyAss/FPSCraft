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
	
	private int slot;
	
	private ItemStack takenWeapon;
	
	private List<String> attachements;
	
	private int reloadTask = -1;
	
	/**
	 * 
	 * @param weapon
	 */
	public Weapon(WeaponContainer weapon) {
		if (weapon != null){
			this.weapon = weapon;
			slot = weapon.getType().getSlot();
		} else {
			throw new NullPointerException("Weapon cannot be null");
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
			getFPSPlayer().getPlayer().updateInventory();
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
	
	/**
	 * Gets the amount of magazines from
	 * @param item
	 * @return
	 */
	public int getMagezines(ItemStack item){
		if (weapon instanceof Gun && item.getItemMeta().hasLore() && item.getItemMeta().getLore().size() > 0){
			List<String> lore = item.getItemMeta().getLore();
			for (String l : lore) {
				if (l.endsWith("magezines left")) {
					return Integer.parseInt(l.split(" ")[1]);
				}
			}
		}
		return -1;
	}
	
	/**
	 * Sets the amount of magazines to the passed itemStack
	 * @param magezines
	 * @param item
	 * @return the changed item
	 */
	public ItemStack setMagezines(int magezines, ItemStack item){
		if (weapon instanceof Gun){
			System.out.println("setting magezines " + magezines);
			ItemMeta m = item.getItemMeta();
			List<String> lore = setMagezinesLore(m.getLore(), magezines);
			m.setLore(lore);
			item.setItemMeta(m);
		}
		return item;
	}
	
	/**
	 * Sets the magezines from the getItem() item
	 * @param magezines
	 */
	public void setMagezines(int magezines){
		setMagezines(magezines, getItem());
	}
	
	/**
	 * Checks the lore for a string ending with "magazines left" and edits that one
	 * @param lore
	 * @param magezines
	 * @return the edited lore
	 */
	private List<String> setMagezinesLore(List<String> lore, int magezines){
		if (lore == null) {
			lore = new LinkedList<String>(Arrays.asList(new String[]{"Has " + magezines + " magezines left"}));
		} else {
			for (String l : lore){
				if (l.endsWith("magezines left"))lore.set(lore.indexOf(l), "Has " + magezines + " magezines left"); break;
			}
		}
		return lore;
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
						takenWeapon = setMagezines(getMagezines(takenWeapon)-1, constructItem());
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
			reloadTask = -1;
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

	/**
	 * @return
	 */
	public boolean needsReload() {
		return getAmount() != weapon.getAmount() || isEmpty();
	}

	/**
	 * Adds " - Empty" to the weapon name
	 */
	public void setEmpty() {
		if (getItem() == null) return;
		ItemMeta m = getItem().getItemMeta();
		m.setDisplayName(m.getDisplayName() + " - Empty");
		getItem().setItemMeta(m);
	}
	
	/**
	 * Checks if the weapon is empty
	 * @return
	 */
	public boolean isEmpty(){
		return getItem() != null && getItem().getItemMeta().getDisplayName().endsWith("Empty");
	}
}
