package net.castegaming.plugins.FPSCaste.playerclass.weapons;

import java.util.Map;

import net.castegaming.plugins.FPSCaste.FPSPlayer;
import net.castegaming.plugins.FPSCaste.enums.GunClass;

public class Equipment extends WeaponContainer{

	public Equipment(int id, String name, GunClass type, String group, int delay, double damage, int level, int amount, Map<String, String> args) {
		super(id, name, type, group, delay, damage, level, amount);
	}

	@Override
	public void useRight(FPSPlayer p) {
		// TODO Auto-generat ed method stub
		
	}

	@Override
	public void useLeft(FPSPlayer p) {
		// TODO Auto-generated method stub
		
	}
}