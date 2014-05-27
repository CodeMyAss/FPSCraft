/**
 * 
 */
package net.castegaming.plugins.FPSCaste.storage;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * @author Brord
 *
 */
public class PlayerStorage {
	/**
	 * Holds the inventory a player had when he teleported to an FPSCaste arena<br/>
	 * 0: Contents<br/>
	 * 1: Armour
	 */
	private ItemStack[][] teleportInventory = new ItemStack[2][];
	
	/**
	 * Holds the other values from that a player had when he teleported to an FP SCaste arena<br/>
	 * Saturation and EXP are x100 and then rounded to an int for easier storage<br/>
	 * 0: Health<br/>
	 * 1: Hunger<br/>
	 * 2: saturation<br/>
	 * 3: Level<br/>
	 * 4: EXP<br/>
	 * 5: gamemode
	 */
	private Double[] teleportStats;
	
	/**
	 * Holds the location this player had when he teleported to an FPSCaste arena
	 */
	private Location teleportPlace;
	
	/**
	 * @param teleportPlace 
	 * @param armor 
	 * @param content 
	 * @param stats 
	 * 
	 */
	public PlayerStorage(Double[] stats, ItemStack[] content, ItemStack[] armor, Location teleportPlace) {
		teleportStats = stats;
		teleportInventory[0] = content;
		teleportInventory[1] = armor;
		this.teleportPlace = teleportPlace;
	}
	
	/**
	 * @param player
	 */
	@SuppressWarnings("deprecation")
	public PlayerStorage(Player player) {
		teleportInventory[0] = player.getInventory().getContents();
		teleportInventory[1] = player.getInventory().getArmorContents();
		teleportPlace = player.getLocation();
		teleportStats = new Double[]{player.getHealth(), (double) player.getFoodLevel(), 
					(double) player.getSaturation(), (double) player.getLevel(), (double) player.getExp(), (double) player.getGameMode().getValue()};
	}

	/**
	 * @return the teleportInventory
	 */
	public ItemStack[] getTeleportInventory() {
		return teleportInventory[0];
	}
	
	/**
	 * @return the teleportInventory
	 */
	public ItemStack[] getTeleportArmory() {
		return teleportInventory[1];
	}
	
	/**
	 * @return the teleportPlace
	 */
	public Location getTeleportPlace() {
		return teleportPlace;
	}
	
	public double getHealth(){
		return teleportStats[0];
	}

	/**
	 * @return
	 */
	public int getFood() {
		return (int) Math.round((teleportStats[1]));
	}

	/**
	 * @return
	 */
	public float getSaturation() {
		return Float.parseFloat((teleportStats[2] / 100) + "");
	}

	/**
	 * @return
	 */
	public int getLevel() {
		return (int) Math.round(teleportStats[3]);
	}

	/**
	 * @return
	 */
	public float getExp() {
		return Float.parseFloat(((teleportStats[4]) / 100) + "");
	}

	/**
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public GameMode getGameMode() {
		return GameMode.getByValue((int) Math.round(teleportStats[5]));
	}

	/**
	 * @return the teleportStats
	 */
	public Double[] getTeleportStats() {
		return teleportStats;
	}
}
