package net.castegaming.plugins.FPSCaste.killstreaks;

import java.util.HashMap;

import net.castegaming.plugins.FPSCaste.FPSCaste;
import net.castegaming.plugins.FPSCaste.enums.PredatorState;
import net.castegaming.plugins.FPSCaste.killstreaks.gui.NameSelector;
import net.castegaming.plugins.FPSCaste.util.BossHealthUtil;
import net.castegaming.plugins.FPSCaste.util.Util;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Result;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

/**
 * Predator-Missle (10): The item will be a book<br/>
 * Opens a GUI with names then u click on a name and you get teleported to the player but up really high,<br/>
 * so when you click a torch that says launch then it will start firing u down and you will be able to control with WASD.<br/>
 * u can click shift to boost and we will have a boss bar displaying the count of your boost. <br/>
 * <br/>
 *  name: '&2Predator Missile'<br/>
    kills: 10<br/>
    item: BOOK<br/>
    launch-item: TORCH<br/>
    launch-text: "Launch"<br/>
 * 
 * @author Brord
 * 
 */
public class Predator extends Killstreak{
	
	public static ItemStack launchitem = new ItemStack(Material.TORCH);
	public static int minimumheight = 50;
	public static int range = 6;
	public static boolean damageall = false;
	public static boolean teleportback = true;
	public static int damage;
	public static String clicktext = "  Click me to launch the predator at this player";
	
	public static void createLaunchItem(String string, ItemStack transformItem) {
		ItemMeta m = transformItem.getItemMeta();
		m.setDisplayName(string);
		transformItem.setItemMeta(m);
		launchitem = transformItem;
	}
	
	private HashMap<String, PredatorState> predators;
	private HashMap<String, Location> locations;
	
	public Predator(String name, int kills, String material) {
		super(name, kills, material);
		predators = new HashMap<String, PredatorState>();
		locations = new HashMap<String, Location>();
	}
	
	/** 
	 * @see net.castegaming.plugins.mcadmindev.killstreaks.killstreaks.Killstreak#onStop()
	 */
	@Override
	public void onStop() {
		Player p;
		for (String name : predators.keySet()){
			p = Bukkit.getServer().getPlayer(name);
			if (p != null){
				if (predators.get(name).equals(PredatorState.TELEPORTED)){
					ItemStack[] items = p.getInventory().getContents();
					for (int j=0; j<items.length; j++){
						ItemStack i = items[j];
						if (i != null && i.getType().equals(launchitem.getType()) && i.hasItemMeta() && i.getItemMeta().getDisplayName().equals(launchitem.getItemMeta().getDisplayName())){
							p.getInventory().setItem(j, null);
							break;
						}
					}
				}
				
				if (teleportback){
					p.teleport(locations.get(name));
					locations.remove(name);
				} else {
					p.teleport(Util.blockBelow(p.getLocation()));
				}
			}
			
			predators.remove(name);
			locations.remove(name);
		}
	}
	
	@EventHandler
	public void itemclick(PlayerInteractEvent e){
		if (e.getAction().equals(Action.LEFT_CLICK_AIR) || e.getAction().equals(Action.LEFT_CLICK_BLOCK)){
			if (isItem(e.getItem())){
				if (predators.containsKey(e.getPlayer().getName())) return;
				e.setCancelled(true);
				e.getPlayer().openInventory(new NameSelector(e.getPlayer().getName(), getName(), clicktext).getInventory());
			} else if (predators.containsKey(e.getPlayer().getName()) && e.getItem() != null && e.getItem().getType().equals(launchitem.getType()) && e.getItem().hasItemMeta() && e.getItem().getItemMeta().getDisplayName().equals(launchitem.getItemMeta().getDisplayName())){
				//fire the player\
				e.setCancelled(true);
				e.getPlayer().setItemInHand(null);
				e.getPlayer().setAllowFlight(false);
				e.getPlayer().setFlying(false);
				e.getPlayer().setFallDistance(0);
				predators.put(e.getPlayer().getName(), PredatorState.FIRED);
			}
		}
	}
	
	@EventHandler
	public void drag(InventoryDragEvent e){
		if (predators.containsKey(e.getWhoClicked().getName())){
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void inventoryclick(InventoryClickEvent e){
		if (e.getInventory().getName().equals(getName())){
			if (e.getClick().isLeftClick()){
				if (e.getCurrentItem() != null && e.getCurrentItem().hasItemMeta()){
					if (e.getWhoClicked() instanceof Player){
						final String name = e.getCurrentItem().getItemMeta().getDisplayName();
						if (!(name.equals("Next") || name.equals("Previous"))){
							final Player clicker = (Player)e.getWhoClicked();
							final Player p;
							
							if ((p = Bukkit.getServer().getPlayer(ChatColor.stripColor(name))) != null){
								if (distanceUp(p.getLocation()) >= minimumheight){
									Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(FPSCaste.getInstance(), new Runnable(){
	
										@SuppressWarnings("deprecation")
										@Override
										public void run() {
											clicker.closeInventory();
											if (clicker.getInventory().containsAtLeast(getItem(), 1)){
												ItemStack[] items = clicker.getInventory().getContents();
												for (int i=0; i<items.length; i++){
													ItemStack item = items[i];
													if (isItem(item)){
														if (clicker.getInventory().firstEmpty() == -1){
															FPSCaste.badMsg(clicker, "You have no room in your inventory!");
															return;
														}
														
														clicker.getInventory().setItem(i, takeItem(p, item));
														clicker.getInventory().setItem(clicker.getInventory().firstEmpty(), launchitem);
														break;
													}
												}
											} else {
												return;
											}
											
											clicker.updateInventory();
											
											predators.put(clicker.getName(), PredatorState.TELEPORTED);
											locations.put(clicker.getName(), clicker.getLocation());
											
											clicker.setMetadata("aimedplayer", new FixedMetadataValue(FPSCaste.getInstance(), name));
											clicker.setMetadata("speed", new FixedMetadataValue(FPSCaste.getInstance(), 10));
											
											BossHealthUtil.displayTextBar("Hold sneak to boost", p);
											
											Location l = p.getLocation().add(0, minimumheight, 0);
											l.setPitch(90);
											
											clicker.teleport(l);
											clicker.setAllowFlight(true);
											clicker.setFlying(true);
											FPSCaste.goodMsg(clicker, "You have been teleported " + minimumheight + " above " + name + ". Click the " + launchitem.getType().toString().toLowerCase() + " to launch! Hold sneak to boost");
										}
									});
								} else {
									FPSCaste.badMsg(clicker, "This player does not have " + minimumheight + " blocks of air above him.");
								}
							} else {
								FPSCaste.badMsg(clicker, "This player is no longer online!");
							}
						}
					}
				}
			} 
			e.setCancelled(true);
			e.setResult(Result.DENY);
		} else if (predators.containsKey(e.getWhoClicked().getName())){
			if (!isItem(e.getCurrentItem())) e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void damage(EntityDamageEvent e){
		if (!(e.getEntity() instanceof Player))return;
		Player p = (Player) e.getEntity();
		String name = p.getName();
		if (predators.containsKey(name)){
			e.setCancelled(true);
		}
	}

	@EventHandler
	public void move(PlayerMoveEvent e){
		Player p = e.getPlayer();
		String name = p.getName();
		if (predators.containsKey(name)){
			if (predators.get(name).equals(PredatorState.TELEPORTED)){
				double yfrom = e.getFrom().getY(); 
			    double yto = e.getTo().getY();
			          
			    if (!(Util.isEqual(e.getFrom(), e.getTo()) && yfrom == yto)){
			    	e.setTo(e.getFrom());
			    }
			} else if (predators.get(name).equals(PredatorState.FIRED)){
				////check if he has landed
				if ((e.getFrom().getY() >= e.getTo().getY())){
					if (!(e.getTo().getBlock().getRelative(BlockFace.DOWN).isEmpty() || e.getTo().getBlock().getRelative(BlockFace.DOWN).isLiquid())){
						predators.remove(name);
						String target = p.getMetadata("aimedplayer").get(0).asString();
						p.getWorld().createExplosion(e.getTo().getX(), e.getTo().getY(), e.getTo().getZ(), 0f, false, false);
						
						for (Entity ent : p.getNearbyEntities(range, range, range)){
							if (ent instanceof LivingEntity && e != p){
								if (damageall || (!damageall && (ent instanceof Player) && ((Player) ent).getName().equals(target))){
									damage(p, (LivingEntity) e, DamageCause.ENTITY_EXPLOSION, damage);
								}
							}
						}
						
						BossHealthUtil.displayTextBar("EXPLODED", p);
						p.getVelocity().zero();
						
						if (e.getPlayer().hasMetadata("boosttaskid")){
							Bukkit.getServer().getScheduler().cancelTask(e.getPlayer().getMetadata("boosttaskid").get(0).asInt());
							e.getPlayer().removeMetadata("boosttaskid", FPSCaste.getInstance());
						}
						
						if (teleportback){
							p.teleport(locations.get(name));
							locations.remove(name);
						}
						
						predators.put(name, PredatorState.EXPLODED);
						
					}
				} else {
					//not landed, prevent him from dying tho ;)
					p.setFallDistance(0);
					
					//between 0 and 100, goes up by 2.5
					int speed = e.getPlayer().getMetadata("speed").get(0).asInt();
					double vec = 0.05 * speed;
					e.getPlayer().setVelocity(new Vector(0, -vec ,0));
				}
			} else if (predators.get(name).equals(PredatorState.EXPLODED)){
				p.removeMetadata("aimedplayer", FPSCaste.getInstance());
				p.removeMetadata("speed", FPSCaste.getInstance());
				predators.remove(name);
				BossHealthUtil.sendPacket(p, BossHealthUtil.getDestroyEntityPacket());
			}
		}
	}
	
	@EventHandler
	public void sneak(PlayerToggleSneakEvent e){
		final String name = e.getPlayer().getName();
		if (predators.containsKey(name)){
			if (predators.get(name).equals(PredatorState.FIRED)){
				if (e.isSneaking()){
					e.getPlayer().setMetadata("boosttaskid", new FixedMetadataValue(FPSCaste.getInstance(), new BukkitRunnable(){
						@Override
						public void run() {
							Player p = Bukkit.getServer().getPlayer(name);
							if (p == null) {cancel(); return;}
							if (!predators.containsKey(name)) {cancel(); p.removeMetadata("boosttaskid", FPSCaste.getInstance()); return;}
							
							int speed = p.getMetadata("speed").get(0).asInt();
							if (speed < 100){
								p.setMetadata("speed", new FixedMetadataValue(FPSCaste.getInstance(), speed+2.5));
								
								BossHealthUtil.updateTextBar(p, "Hold sneak to boost", speed);
							} else {
								cancel();
								p.removeMetadata("boosttaskid", FPSCaste.getInstance());
								return;
							}
						}
					}.runTaskTimer(FPSCaste.getInstance(), 0, 1).getTaskId()));
				} else {
					if (e.getPlayer().hasMetadata("boosttaskid")){
						int task = e.getPlayer().getMetadata("boosttaskid").get(0).asInt();
						Bukkit.getServer().getScheduler().cancelTask(task);
					}
				}
			}
		}
	}
}
