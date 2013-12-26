package net.castegaming.plugins.FPSCaste.playerclass.weapons;

import java.util.Map;

import net.castegaming.plugins.FPSCaste.enums.GunClass;

public class RocketGun extends Gun {
	
	double radius = 2;

	public RocketGun(int id, String name, GunClass type, String group, int delay, double damage, int level, int amount, Map<String, Object> args) {
		super(id, name, type, group, delay, damage, level, amount, args);
		// TODO Auto-generated constructor stub
	}

}
