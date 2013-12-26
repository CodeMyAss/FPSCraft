package net.castegaming.plugins.FPSCaste.enums;

public enum gunName {
	//lethals/specials have a direct impact damage
	
	AK47(318, "AK-47", 4), //Flint
	M1911(321, "M1911", 6), //Painting
	MP5(323, "MP-5", 3),  //Sign
	GRENADE(368, "Grenade", 1), //EnderPearl
	STUN(344, "Stun", 1), //egg
	FLASH(332, "Flash", 1), //Snowball
	KNIFE(280, "Knife", 20), //Stick
	INTERVENTION(290, "Intervention", 17),  //stone hoe
	TNT(-1, "TNT", 0);

	public int gunID;
	public String name;
	public int damage;

	gunName(int gunID, String name, int damage){
		this.gunID = gunID;
		this.name = name;
		this.damage = damage;
	}
	
	public static gunName fromID(int ID) {
		for (gunName gun : gunName.values()) {
	        if (ID == gun.gunID) {
	          return gun;
	        }
	    }
		return null;
	}

	public String getName() {
		return name;
	}
	
	//@Override
	//public String toString(){
	//	return name;
	//}
}
