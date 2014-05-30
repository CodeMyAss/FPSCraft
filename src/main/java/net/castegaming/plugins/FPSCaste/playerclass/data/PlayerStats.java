package net.castegaming.plugins.FPSCaste.playerclass.data;

/**
 * Project FPSCraft<br/>
 * Class net.castegaming.plugins.FPSCaste.playerclass.data.PlayerData.java<br/>
 * @author Brord
 * @since 30 mei 2014, 01:51:39
 */
public abstract class PlayerStats {
	
	private String name;

	/**
	 * 
	 */
	public PlayerStats(String name) {
		this.name = name;
	}
	
	abstract void load();
}
