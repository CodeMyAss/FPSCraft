package net.castegaming.plugins.FPSCaste.guns;

import java.util.LinkedList;

import net.castegaming.plugins.FPSCaste.FPSCaste;
import net.castegaming.plugins.FPSCaste.FPSPlayer;
import net.castegaming.plugins.FPSCaste.enums.gunName;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

/**
 * Represents a war command
 *
 * @author Brord van Wierst
 */	
	public abstract class AbstractGun {
		
		/**
		 * The damage 1 arrow does to a player
		 */
		public int damage = 0;
		
		/**
		 * The maximum ammo the gun is allowed to have<br/>
		 * 1 by default
		 * 
		 * @var int Maximum ammo capacity
		 */
		public int maxAmmo = 1;
		
		/**
		 * The clip size of a full magazine
		 * @var int clipSize
		 */
		public short clipSize;
		
		/**
		 * The slot the gun should be in.<br/>
		 * Starts at 0!
		 * 
		 * @var int spot of the slot
		 */
		public int slotSpot;
		
		/**
		 * The user of this gun
		 *
		 * @var	Player sender
		 */
		public FPSPlayer user;
		
		/**
		 * The user of this gun
		 *
		 * @var	Player sender
		 */
		public Player player;
		
		/**
		 * The name of this gun
		 * 
		 * @var gunName String
		 */
		public gunName gunName;
		
		/**
		 * The item ID of the gun its original item
		 * @var int gunItemID
		 */
		public Integer gunItemID = 0;
		
		/**
		 * The item ID of the ammo its original item
		 * @var int AmmoItemID
		 */
		public int AmmoItemID = 0;
		
		/**
		 * Defines the amount of this gun given <br/>
		 * Mostly used for special and lethal grenades
		 * @var Integer amountOfGun
		 */
		public Integer amountOfGun = 1;
		
		//////////////////////
		///// Gun stats //////
		//////////////////////
		/**
		 * 
		 */
		public int recoil = 0;

		/**
		 * The "distance" a gun has
		 */
		public int distance = 0;
		
		/**
		 * The delay a gun has before shooting again<br/>
		 * In miliseconds
		 */
		public long delay = 0;;

		/**
		 * Use for the guns<br/>
		 * Note: user and player will be null
		 */
		public AbstractGun() {
		}
	
		public AbstractGun(String sender) {
			user = FPSCaste.getFPSPlayer(sender);
			player = user.getPlayer();
		}
		
		public void giveGun() {
			if (isGunLoaded()){
				if (amountOfGun == null){
					amountOfGun = 1;
				}
				
				//creates the gun
				ItemStack gun = new ItemStack(gunItemID, amountOfGun, clipSize);
				
				//sets the ammo
				gun.setDurability(clipSize);
				
				//adds the name
				ItemMeta meta = gun.getItemMeta();
				meta.setDisplayName(gunName.name);
				
				gun.setItemMeta(meta);
				
				//adds the ClipSize
				gun = setClipSize(gun);
				
				//gives the gun
				player.getInventory().setItem(slotSpot, gun);
				
				//code to give ammo for the gun
			}
			
		}

		public void shootGun() {
			Vector velocity = player.getEyeLocation().getDirection().multiply(distance/10);
			
			Arrow arrow = player.launchProjectile(Arrow.class);
			arrow.setVelocity(velocity);
			
			//System.out.print("getShooter() = " + arrow.getShooter());
			//returns player.getName()
			
			arrow.setVelocity(velocity);
			
			//add arrow to the match its arrows list
			user.getMatch().arrows.put(arrow.getUniqueId(), gunName);
			
			//new bullet(arrow, user.getName(), gunName);
			//setNewBullets();
			
			ItemStack item = player.getItemInHand();
			short ammo = item.getDurability();
			if (ammo != 1){
				item.setDurability((short) (ammo-1));
				setNewBullets();
			} else {
				//get new clip
				//check if new clip available
				reload();
					//remove full stack of ammo
					
					//no
					//any available?
						//yes set clip to remaining
						//no send no ammo
						//gun with ammo available?
							//yes switch to that
							//no send go get a new gun boi
			}
		}
		
		public ItemStack setClipSize(ItemStack gun){
			ItemMeta meta = gun.getItemMeta();
			if (amountOfGun == 1){
				LinkedList<String> Lore = new LinkedList<String>();
				
				short Damage = gun.getDurability();
				Lore.add("Has " + Damage + " ammo left in clip");
				meta.setLore(Lore);
			}	
			gun.setItemMeta(meta);
			return gun;
		}

		public void setNewBullets() {
			ItemStack item = player.getItemInHand();
			item = setClipSize(item);
			player.getInventory().setItem(slotSpot, item);
		}

		/**
		 * Reloads the gun<br/>
		 * Sends "Allready reloading!" if he is reloading
		 */
		public void reload() {
			if (user.isReloading()){
				user.badMsg("Allready reloading!");
			} else {		
				user.getPlayer().setItemInHand(null);
				//user.setReloading();
				
				user.Msg("reloading....");
				Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(FPSCaste.getInstance(), (new Runnable(){
						public void run(){
							//user.unsetReloading();
							user.Msg("Reloading of your " + gunName + " is done!");
							giveGun();
						}
				}), 80L);
			}
		}

		
		public ItemStack setSpecialsName(ItemStack special) {
			ItemMeta meta = special.getItemMeta();
			meta.setDisplayName(gunName.getName());
			special.setItemMeta(meta);
			return special;
		}

		/**
		 * Returns the item to the player
		 * @param itemDrop Item he dropped on pressing Q
		 */
		public void giveBack(Item itemDrop) {
			ItemStack item = setClipSize(itemDrop.getItemStack());
			player.getInventory().setItem(slotSpot, item);
		}
		
		/**
		 * Checks if the gun stats are loaded<br/>
		 * Should never be false
		 * @return true if the stats are loaded
		 */
		public boolean isGunLoaded(){
			boolean isLoaded = true;
			if (user == null || gunName == null || gunItemID == null){
				isLoaded = false;
			}
			return isLoaded;
		}

		/**
		 * 
		 * @param sound
		 * @param volume
		 * @param pitch
		 */
		public void playSound(Sound sound, float volume, float pitch) {
			player.getWorld().playSound(player.getLocation(), sound, volume, pitch);
		}
	}