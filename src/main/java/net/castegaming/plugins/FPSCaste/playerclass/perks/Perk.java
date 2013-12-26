/**
 * 
 */
package net.castegaming.plugins.FPSCaste.playerclass.perks;

import java.util.HashMap;

/**
 * @author Brord
 *
 */
public abstract class Perk {
	
	public static HashMap<String, Perk> perks = new HashMap<String, Perk>();
	
	private int levelrequired;
	private String group;
	private String name;
	
	public Perk(String name, String group, int levelrequired) {
		this.name = name;
		this.group = group;
		this.levelrequired = levelrequired;
		
		perks.put(name, this);
	}
	
	public void onSpawn(){}
	
	public void onDeath(){}
	
	public void onDamage(){}
	
	public void onMove(){}
	
	public void onWeaponUse(){}

	/**
	 * @return the levelrequired
	 */
	public int getLevelrequired() {
		return levelrequired;
	}

	/**
	 * @return the group
	 */
	public String getGroup() {
		return group;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
}
