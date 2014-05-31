package net.castegaming.plugins.FPSCaste.util;
import java.util.LinkedList;
import java.util.List;

import net.castegaming.plugins.FPSCaste.FPSCaste;
import net.castegaming.plugins.FPSCaste.FPSPlayer;
import net.castegaming.plugins.FPSCaste.playerclass.weapons.WeaponContainer;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
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
	
	public Explosion(Location loc, int radius, WeaponContainer w, String name, double damage, boolean breakblocks, List<PotionEffect> effects) {
		double damagePerBlock = damage / radius;
		
		//TODO set of other blocks
		
		if (breakblocks){
			for (int x = loc.getBlockX()-Math.round(radius/2); x <= loc.getBlockX() + Math.round(radius/2); x ++){
				for (int y = loc.getBlockY()-Math.round(radius/2); y <= loc.getBlockY() + Math.round(radius/2); y ++){
					for (int z = loc.getBlockZ()-Math.round(radius/2); z <= loc.getBlockZ() + Math.round(radius/2); z ++){
						FPSCaste.getFPSPlayer(name).getMatch().breakOneBlock(loc.getWorld().getBlockAt(x,y,z));
					}
				}
			}
		}
		
		loc.getWorld().createExplosion(loc.getX(), loc.getY(), loc.getZ(), 0f, false, false);
		List<Entity> entities = Util.getNearEntities(loc, radius);
		
		for(Entity ent : entities) {
			if (ent instanceof Player){
				//he is a player
				FPSPlayer player = FPSCaste.getFPSPlayer(((Player) ent).getName());
				if (player.isIngame() && FPSCaste.getFPSPlayer(name).isIngame()){
					//both are ingamew
					if (!player.getPlayer().isDead() && FPSCaste.getFPSPlayer(name).canDamage(player.getName())){
						if(Util.canSeeThrough(Util.blocksInLine(loc, player.getPlayer().getLocation(), radius))){
							double totalDamage = damage - (player.getPlayer().getLocation().distance(loc)*damagePerBlock);
							if (totalDamage < 0) totalDamage = 0;
							
							if (player.getPlayer().getHealth() - totalDamage <= 0) {
								totalDamage = player.getPlayer().getHealth();
							}
							
							player.getPlayer().addPotionEffects(effects);
							
							player.getPlayer().setMetadata("FPSexplosion", new FixedMetadataValue(FPSCaste.getInstance(), w + ":" + totalDamage + ":" + name));
							player.getPlayer().damage(totalDamage, Bukkit.getServer().getPlayer(name));
						}
	                }
				}
			}
        }
	}
	
	public void playSound(Sound sound, float volume, float pitch, Location loc) {
		loc.getWorld().playSound(loc, sound, volume, pitch);
	}

}
