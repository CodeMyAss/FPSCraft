package net.castegaming.plugins.FPSCaste.enums;

/**
 * Project FPSCraft<br/>
 * Class net.castegaming.plugins.FPSCaste.enums.Configs.java<br/>
 * @author Brord
 * @since 27 mei 2014, 13:09:40
 */
public enum Configs {
	CONFIG("config"),
	DEFAULTCLASSES("defaultclasses"),
	DEFAULTPLAYER("defaultplayer"),
	PLAYLIST("playlist"),
	POINTS("points"),
	RANK("ranks"),
	MAPS("maps");

	String name;
	
	Configs(String name){
		this.name = name;
	}
	
	@Override
	public String toString() {
		return name;
	}
}
