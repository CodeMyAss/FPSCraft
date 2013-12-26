package net.castegaming.plugins.FPSCaste.enums;

/**
 * Enum containing all the team options<br/>
 * Use toString() to get the fancy name!
 * @author Brord
 * 
 */
public enum teamName {
	ALLIES("Allies"),
	AXIS("Axis"),
	SPECTATOR("Spectator"),
	ALONE("Alone");
	
	private String name;
	
	private teamName(String name) {
		this.name = name;
	}
	
	@Override
	public String toString() {
		return name;
	}
}
