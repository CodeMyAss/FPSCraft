package net.castegaming.plugins.FPSCaste.guns;

public class mp5 extends AbstractGun{

	@SuppressWarnings("static-access")
	public mp5(String gunholder) {
		super(gunholder);
		super.amountOfGun = 1;
		super.clipSize = 30;
		super.gunItemID = gunName.MP5.gunID;
		super.gunName = gunName.MP5;
		super.maxAmmo = 60;
		super.slotSpot = 0;
		super.distance = 30;
		super.delay = 10L;
		super.damage = 3;
	}
}
