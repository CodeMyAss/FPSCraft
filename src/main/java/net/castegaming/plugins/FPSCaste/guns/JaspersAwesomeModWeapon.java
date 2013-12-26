package net.castegaming.plugins.FPSCaste.guns;

public class JaspersAwesomeModWeapon extends AbstractGun{

	@SuppressWarnings("static-access")
	public JaspersAwesomeModWeapon(String gunholder) {
		super(gunholder);
		super.amountOfGun = 1;
		super.clipSize = 3000;
		super.gunItemID = gunName.MP5.gunID;
		super.gunName = gunName.MP5;
		super.maxAmmo = 60000;
		super.slotSpot = 0;
		super.distance = 300000;
		super.delay = 1L;
		super.damage = 300000;
	}
}
