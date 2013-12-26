package net.castegaming.plugins.FPSCaste.guns;

import net.castegaming.plugins.FPSCaste.enums.gunName;

import org.bukkit.entity.Item;

public class FPSCasteGunHandler {

	public AbstractGun gunClass;
	
	/**
	 * This give the player his requested gun
	 * @param player playername
	 * @param weapons Itemstack
	 *//*
	public FPSCasteGunHandler(Player player, int weapons) {
		
		AbstractGun gunClass = null;
		gunClass = getGunClass(player, weapons);
		
		//give gun and ammo in correct slot
		gunClass.giveGun();
	}*/

	/**
	 * Shoots the gun
	 * @param player  The holder of the gun
	 * @return 
	 */
	public void shootGun(){
		gunClass.shootGun();
	}

	/**
	 * Main constructor, fills the abstract class with values
	 * @param player user
	 * @param gunName name of the gun (gunName Enum)
	 */
	public FPSCasteGunHandler(String player, gunName gun) {
		if (gun == gunName.AK47) {
			gunClass = new ak47(player);
		} else if (gun == gunName.M1911) {
			gunClass = new handgun_1911(player);
		} else if (gun == gunName.GRENADE) {
			gunClass = new grenade(player);
		} else if (gun == gunName.STUN) {
			gunClass = new stun(player); 
		} else if (gun == gunName.MP5) {
			gunClass = new mp5(player); 
		} else if (gun == gunName.INTERVENTION) {
			gunClass = new Intervention(player); 
		} else {
			//doo nuthing!
		}
		
		if(gunClass == null){
			//give a handgun
			//returnClass = new handgun_1911(player);
		}
	}
	
	public void reload(){
		gunClass.reload();
	}

	public void giveGun() {
		if (gunClass != null){
			gunClass.giveGun();
		}
	}

	public void giveBack(Item itemDrop) {
		gunClass.giveBack(itemDrop);
	}

	public long getWaitTime() {
		return gunClass.delay;
	}

	public void updateGun() {
		gunClass.setNewBullets();
	}
}
