package net.castegaming.plugins.FPSCaste.playerclass.weapons.custom;

import java.util.Map;

import net.castegaming.plugins.FPSCaste.enums.GunClass;
import net.castegaming.plugins.FPSCaste.playerclass.weapons.Special;

public class Stun extends Special{
	public Stun(int id, String name, GunClass type, String group, int delay, double damage, int amount, int level, Map<String, Object> args) {
		super(id, name, type, group, delay, damage, level, amount, args);
	}
}
