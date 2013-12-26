package net.castegaming.plugins.FPSCaste.guns;

public class handgun_1911 extends AbstractGun{

	@SuppressWarnings("static-access")
	public handgun_1911(String gunholder) {
		super(gunholder);
		super.amountOfGun = 1;
		super.clipSize = 8;
		super.gunItemID = gunName.M1911.gunID;
		super.gunName = gunName.M1911;
		super.maxAmmo = 60;
		super.slotSpot = 1;
		super.distance = 10;
		super.delay = 25L;
		super.damage = 7;
	}
}
