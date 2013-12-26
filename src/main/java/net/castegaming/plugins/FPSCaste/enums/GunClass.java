package net.castegaming.plugins.FPSCaste.enums;

import net.castegaming.plugins.FPSCaste.playerclass.weapons.Equipment;
import net.castegaming.plugins.FPSCaste.playerclass.weapons.Gun;
import net.castegaming.plugins.FPSCaste.playerclass.weapons.Special;
import net.castegaming.plugins.FPSCaste.playerclass.weapons.WeaponContainer;

/**
 * @author Brord
 *
 */
public enum GunClass {
	PRIMARY(0, Gun.class), 
	SECONDARY(1, Gun.class),
	SPECIAL(8, Special.class),
	EQUIPMENT(9, Equipment.class);
	
	int slot;
	Class<? extends WeaponContainer> instance;
	
	private GunClass(int slot, Class<? extends WeaponContainer> instance ) {
		this.slot = slot;
		this.instance = instance;
	}
	
	/**
	 * Returns the slot this type is attached too
	 * @return int slot
	 */
	public int getSlot() {
		return slot;
	}
	
	/**
	 * Returns an user friendly string<br/>
	 * First letter is capitalized.
	 */
	@Override
	public String toString() {
		String name = name().toLowerCase();
		char[] stringArray = name.toCharArray();
		stringArray[0] = Character.toUpperCase(stringArray[0]);
		return new String(stringArray);
	}
	
	public Class<? extends WeaponContainer> getGunClass(){
		return instance;
	}

	public WeaponContainer newInstance() {
		try {
			return instance.newInstance();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}

}
