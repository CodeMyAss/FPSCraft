package net.castegaming.plugins.FPSCaste.listener;

import net.castegaming.plugins.FPSCaste.FPSCaste;
import net.castegaming.plugins.FPSCaste.FPSPlayer;
import net.castegaming.plugins.FPSCaste.enums.breakableBlocks;
import net.castegaming.plugins.FPSCaste.playerclass.weapons.Special;
import net.castegaming.plugins.FPSCaste.playerclass.weapons.WeaponContainer;
import net.castegaming.plugins.FPSCaste.util.Explosion;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.util.BlockIterator;

public class EntityDamageListener implements Listener {
	
	FPSCaste plugin = null;
	
	public EntityDamageListener(FPSCaste plugin){
		this.plugin = plugin;
	}
	
	@EventHandler
	public void ArrowDamageEvent(ProjectileHitEvent e){
		if (e.getEntity() instanceof Arrow){
			//Got hit by an arrow
			final Arrow arrow = (Arrow) e.getEntity();
			
			//was the shooter of the arrow a player?
			if (arrow.getShooter() instanceof Player){
				final FPSPlayer player = FPSCaste.getFPSPlayer(((Player) arrow.getShooter()).getName());
				
				//is the player online? (arrow shouldnt fly for 2 minutes 0.o
				if (player != null && player.isIngame()){
					//gets match, and removes the arrow
					
					BlockIterator iterator = new BlockIterator(player.getPlayer().getWorld(), arrow.getLocation().toVector(), arrow.getVelocity().normalize(), 0, 4);
				    Block hitBlock = null;

				    while(iterator.hasNext()) {
				        hitBlock = iterator.next();
				        if(!hitBlock.getType().equals(Material.AIR)){
				            break;
				        }
				    }
				    
				    if (hitBlock != null){
					    if (hitBlock.getType().equals(Material.TNT)){
					    	Special w = (Special) WeaponContainer.getWeapon("Tnt");
					    	new Explosion(hitBlock.getLocation(), w.getRange(), w, player.getName(), w.getDamage(), false, w.getEffecs());
					    	player.getMatch().breakOneBlock(hitBlock);
					    	arrow.remove();
					    } else {
					    	try {
						    	breakableBlocks.valueOf(hitBlock.getType().toString());
						    	player.getMatch().break3Blocks(hitBlock);
						    	arrow.remove();
						    	
						    } catch (IllegalArgumentException Ex){
						    	Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
									
									@Override
									public void run() {
										arrow.remove();
										//player.getMatch().arrows.remove(arrow.getEntityId());
									}
								}, 300);
						    }
					    }
				    } else {
				    	//?
				    	arrow.remove();
				    }
				}
			}
		}
	}
	
	/**
	 * Defines what to do when something gets damage
	 * @param e EntityDamageByEntityEvent e
	 */
	@EventHandler
	public void playerAssistEvent(EntityDamageByEntityEvent e){
		if (e.getEntity() instanceof Player){  
			
			//He who gets attacked is a player, continue.
			String name = ((Player) e.getEntity()).getName();
			FPSPlayer defender = FPSCaste.getFPSPlayer(name);
			if (defender.isIngame()){
				//he is ingame, continue
				
				FPSPlayer attacker;
				WeaponContainer weapon;
				boolean headshot = false;
				double damage = -1;
				
				//check if the attacker is a player or an arrow
				if (e.getDamager() instanceof Player){
					//Fist to fist attack
					if (defender.getPlayer().hasMetadata("FPSexplosion")){
						String[] meta = defender.getPlayer().getMetadata("FPSexplosion").get(0).asString().split(":");
						weapon = WeaponContainer.getWeapon(meta[0]);
						damage = Double.parseDouble(meta[1]);
					} else {
						weapon = WeaponContainer.getWeapon("Knife");
					}
					attacker = FPSCaste.getFPSPlayer(((Player) e.getDamager()).getName());
					
				} else if (e.getDamager() instanceof Arrow){
					//shoot with a bullet
					Arrow arrow = (Arrow) e.getDamager();
					
					if (arrow.getShooter() instanceof Player){
						attacker = FPSCaste.getFPSPlayer(((Player) arrow.getShooter()).getName());
						
						weapon = WeaponContainer.getWeapon(arrow.getMetadata("FPSweapon").get(0).asString());
						double y = arrow.getLocation().getY();
				        double shotY = e.getEntity().getLocation().getY();
				        headshot = y - shotY > 1.35d;
						
					} else {
						//skeleton or dispenser shot it.
						e.setCancelled(true);
						return;
					}
				} else {
					//not currently attacked by our plugin, cancelling
					e.setCancelled(true);
					return;
				}
				
				if (weapon != null){
					if (defender.canDamage(attacker.getName())){
						if (damage == -1){
							damage = weapon.getDamage();
						}
						System.out.println(damage);
						e.setDamage(headshot ? (int) Math.round(damage*1.3) : damage);
						attacker.addAssist(true, name);
						defender.addAssist(false, attacker.getName());
					} else {
						e.setCancelled(true);
					}
				} else {
					//????
				}
			} else {
				//not ingame, so we dont care 
				//e.setCancelled(true);
				return;
			}
		} else {
			//not a player which got shot
			//e.setCancelled(true);
			return;
		}
	}
}
