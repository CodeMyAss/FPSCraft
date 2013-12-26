package net.castegaming.plugins.FPSCaste.playerclass.weapons.custom;

import java.util.Map;

import net.castegaming.plugins.FPSCaste.enums.GunClass;
import net.castegaming.plugins.FPSCaste.playerclass.weapons.Gun;

public class Ak47 extends Gun{
	
	public Ak47(int id, String name, GunClass type, String group, int delay, double damage, int level, int amount, Map<String, Object> args) {
		super(id, name, type, group, delay, damage, level, amount, args);
	}
}
