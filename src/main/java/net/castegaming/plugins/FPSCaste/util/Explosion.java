package net.castegaming.plugins.FPSCaste.util;

import java.util.LinkedList;
import java.util.List;

import net.castegaming.plugins.FPSCaste.FPSCaste;
import net.castegaming.plugins.FPSCaste.FPSPlayer;
import net.castegaming.plugins.FPSCaste.playerclass.weapons.WeaponContainer;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;

public class Explosion {

	public Explosion(Location loc, final int radius, WeaponContainer w, final String name, final double damage, final boolean breakblocks) {
		this(loc, radius, w, name, damage, breakblocks, new LinkedList<PotionEffect>());
	}
	
	public Explosion(Entity ent, final int radius, WeaponContainer w, final String name, final double damage, final boolean breakblocks) {
		this(ent.getLocation(), radius, w, name, damage, breakblocks, new LinkedList<PotionEffect>());
	}
	
	public Explosion(Location loc, int radius, WeaponContainer w, String name, double damage, boolean breakBlocks, List<PotionEffect> effects) {
		double damagePerBlock = damage / radius;
		
		//TODO set of other blocks
		if (breakBlocks)breakBlocks(loc, radius, name);
		
		loc.getWorld().createExplosion(loc.getX(), loc.getY(), loc.getZ(), 0f, false, false);
		List<Entity> entities = Util.getNearEntities(loc, radius);
		FPSPlayer attacker = FPSCaste.getFPSPlayer(name);
		
		for(Entity ent : entities) {
			if (ent instanceof Player){
				//he is a player
				FPSPlayer player = FPSCaste.getFPSPlayer(((Player) ent).getName());
				if (player.isIngame() && attacker.isIngame() && player.getMatch().equals(attacker.getMatch())){
					//both are ingame, same game
					if (!player.getPlayer().isDead() && attacker.canDamage(player.getName())){
						if(Util.canSeeThrough(Util.blocksInLine(loc, player.getPlayer().getLocation(), radius))){
							double totalDamage = damage - (player.getPlayer().getLocation().distance(loc)*damagePerBlock) + damagePerBlock;
							if (totalDamage < 0) totalDamage = 0;
							damagePlayer(w, attacker, effects, player, totalDamage);
						}
	                }
				}
			}
        }
	}

	/**
	 * @param w
	 * @param attacker
	 * @param effects
	 * @param player
	 * @param totalDamage
	 */
	private void damagePlayer(WeaponContainer w, FPSPlayer attacker, List<PotionEffect> effects, FPSPlayer player, double totalDamage) {
		if (player.getPlayer().getHealth() - totalDamage <= 0) {
			totalDamage = player.getPlayer().getHealth();
		} else {
			//dies otherwise, so effects are of no use
			player.getPlayer().addPotionEffects(effects);
		}
		
		player.getPlayer().setMetadata("FPSexplosion", new FixedMetadataValue(FPSCaste.getInstance(), w + ":" + totalDamage + ":" + attacker));
		player.getPlayer().damage(totalDamage, attacker.getPlayer());
	}

	/**
	 * @param loc
	 * @param radius
	 * @param name
	 * @param breakblocks
	 */
	private void breakBlocks(Location loc, int radius, String name) {
		for (int x = loc.getBlockX() - Math.round(radius / 2); x <= loc.getBlockX() + Math.round(radius / 2); x++) {
			for (int y = loc.getBlockY() - Math.round(radius / 2); y <= loc.getBlockY() + Math.round(radius / 2); y++) {
				for (int z = loc.getBlockZ() - Math.round(radius / 2); z <= loc.getBlockZ() + Math.round(radius / 2); z++) {
					FPSCaste.getFPSPlayer(name).getMatch().breakOneBlock(loc.getWorld().getBlockAt(x, y, z));
				}
			}
		}
	}
}
