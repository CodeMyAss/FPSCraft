package net.castegaming.plugins.FPSCaste.enums;

public enum className {
	ASSAULT("Assault"),
	GRENADIER("Grenadier"),
	HEAVY("Heavy"),
	SMG("Submachine gunner"),
	SNIPER("Sniper"),
	CUSTOM("Custom");
	
	private String name;
	
	/**
	 * guns, starting in slot 1
	 */
	private gunName[] weapons;
	
	
	private int[] ammo;	
	
	className(String name){
		this.name = name;
		handle();
	}
	
	public static className fromString(String text) {
	    if (text != null) {
	      for (className clas: className.values()) {
	        if (text.equalsIgnoreCase(clas.name)) {
	          return clas;
	        }
	      }
	    }
	    return null;
	  }
	
	public void handle() {
		if (name().equals("ASSAULT")){
			weapons = new gunName[]{
				gunName.AK47, 
				gunName.M1911,
				gunName.KNIFE,
				null,
				null,
				null,
				null,
				gunName.STUN, 
				gunName.GRENADE
			};
			ammo = new int[]{140,271,271,271,271,271,271,2,2}; 
		} else if (name().equals("GRENADIER")){
			weapons = new gunName[]{
					gunName.AK47, 
					gunName.M1911,
					gunName.KNIFE,
					null,
					null,
					null,
					null,
					gunName.STUN, 
					gunName.GRENADE
				};
			ammo = new int[]{140,271,271,271,271,271,271,2,2}; 
		} else if (name().equals("HEAVY")){
			weapons = new gunName[]{
					gunName.AK47, 
					gunName.M1911,
					gunName.KNIFE,
					null,
					null,
					null,
					null,
					gunName.STUN, 
					gunName.GRENADE
				};
			ammo = new int[]{140,271,271,271,271,271,271,2,2}; 
		} else if (name().equals("SMG")){
			weapons = new gunName[]{
					gunName.AK47, 
					gunName.M1911,
					gunName.KNIFE,
					null,
					null,
					null,
					null,
					gunName.STUN, 
					gunName.GRENADE
				};
			ammo = new int[]{140,271,271,271,271,271,271,2,2}; 
		} else if (name().equals("SNIPER")){
			weapons = new gunName[]{
					gunName.INTERVENTION, 
					gunName.M1911,
					gunName.KNIFE,
					null,
					null,
					null,
					null,
					gunName.STUN, 
					gunName.GRENADE
				};
			ammo = new int[]{140,271,271,271,271,271,271,3,1}; 
		} else if (name().equals("CUSTOM")){
			weapons = new gunName[]{
					gunName.AK47, 
					gunName.M1911,
					gunName.KNIFE,
					null,
					null,
					null,
					null,
					gunName.STUN, 
					gunName.GRENADE
				};
			ammo = new int[]{140,271,271,271,271,271,271,2,2}; 
		} else {
			System.out.print(name());
		}
	}
	
	public gunName[] getWeapons(){
		return weapons;
	}
	
	public int[] getAmmo(){
		return ammo;
	}
	
	/**
	 * Returns the friendly name of this class
	 * @return
	 */
	public String getName(){
		return name;
	}
	
	@Override
	public String toString(){
		return getName();
	}
}
