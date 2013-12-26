package net.castegaming.plugins.FPSCaste.guns;

public class ak47 extends AbstractGun{

	@SuppressWarnings("static-access")
	public ak47(String gunholder) {
		super(gunholder);
		super.amountOfGun = 1;
		super.clipSize = 30;
		super.gunItemID = gunName.AK47.gunID;
		super.gunName = gunName.AK47;
		super.maxAmmo = 60;
		super.slotSpot = 0;
		super.distance = 30;
		super.delay = 20L;
		super.damage = 4;
	}
}
