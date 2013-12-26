package net.castegaming.plugins.FPSCaste.guns;

public class Intervention extends AbstractGun{

	@SuppressWarnings("static-access")
	public Intervention(String gunholder) {
		super(gunholder);
		super.amountOfGun = 1;
		super.clipSize = 12;
		super.gunItemID = gunName.INTERVENTION.gunID;
		super.gunName = gunName.INTERVENTION;
		super.maxAmmo = 60;
		super.slotSpot = 0;
		super.distance = 60;
		super.delay = 1500L;
		super.damage = 17;
	}
}
