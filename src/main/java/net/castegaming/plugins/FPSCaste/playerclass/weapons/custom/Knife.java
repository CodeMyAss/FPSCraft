package net.castegaming.plugins.FPSCaste.playerclass.weapons.custom;

import net.castegaming.plugins.FPSCaste.FPSPlayer;
import net.castegaming.plugins.FPSCaste.enums.GunClass;
import net.castegaming.plugins.FPSCaste.playerclass.weapons.WeaponContainer;

/**
 * Project FPSCraft<br/>
 * Class net.castegaming.plugins.FPSCaste.playerclass.weapons.custom.Knife.java<br/>
 * @author Brord
 * @since 3 jun. 2014, 18:34:01
 */
public class Knife extends WeaponContainer {

	

	/**
	 * @param nextIDAvailable
	 * @param string
	 * @param rest
	 * @param string2
	 * @param i
	 * @param d
	 */
	public Knife(int nextIDAvailable, String string, GunClass rest, String string2, int i, double d) {
		super(nextIDAvailable, string, rest, string2, i, d);
	}

	/**
	 * @see net.castegaming.plugins.FPSCaste.playerclass.weapons.WeaponContainer#useLeft(net.castegaming.plugins.FPSCaste.FPSPlayer)
	 */
	@Override
	public void useLeft(FPSPlayer p) {
	}

	/**
	 * @see net.castegaming.plugins.FPSCaste.playerclass.weapons.WeaponContainer#useRight(net.castegaming.plugins.FPSCaste.FPSPlayer)
	 */
	@Override
	public void useRight(FPSPlayer p) {
	}

}
