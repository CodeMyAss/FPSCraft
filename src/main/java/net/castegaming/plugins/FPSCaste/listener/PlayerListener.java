package net.castegaming.plugins.FPSCaste.listener;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import net.castegaming.plugins.FPSCaste.FPSCaste;
import net.castegaming.plugins.FPSCaste.FPSPlayer;
import net.castegaming.plugins.FPSCaste.enums.gameState;
import net.castegaming.plugins.FPSCaste.enums.teamName;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.event.player.PlayerToggleSprintEvent;

public class PlayerListener implements Listener {
	
	FPSCaste plugin = null;
	
	private HashMap<String, Integer> runners = new HashMap<String, Integer>();
	
	public PlayerListener(FPSCaste plugin){
		this.plugin = plugin;
	}
	
	private List<Material> rightclickablesList = Arrays.asList(Material.WOOD_DOOR, Material.WOODEN_DOOR, Material.WOOD_BUTTON, Material.STONE_BUTTON);
	
	
	@EventHandler
	public void playerInteractEvent(PlayerInteractEvent e){
		String name = e.getPlayer().getName();
		FPSPlayer player = FPSCaste.getFPSPlayer(name);
		if (player.isIngame()){
			if (!player.isFrozen()){
				if (player.getTeam() != teamName.SPECTATOR){
					//right clicked
					if (e.getAction().equals(Action.RIGHT_CLICK_AIR) || e.getAction().equals(Action.RIGHT_CLICK_BLOCK)){
						//clicking a door or something?
						if (e.getClickedBlock() != null && rightclickablesList.contains(e.getClickedBlock().getType())){
							return;
						}
						
						//prevent shooting/trowing things
						if (e.getItem() != null){
							Material type = e.getItem().getType();
							if (type.equals(Material.EGG) || type.equals(Material.ENDER_PEARL) || type.equals(Material.SNOW_BALL) || type.equals(Material.PAINTING)){
								e.setCancelled(true);
							}
						}
						
						//handle right click
						player.handleRightClick();
					//left clicked
					} else if (e.getAction().equals(Action.LEFT_CLICK_AIR) || e.getAction().equals(Action.LEFT_CLICK_BLOCK)){
						if (!player.canBuild()){
							if(e.getClickedBlock() != null){
								if (e.getClickedBlock().getType().equals(Material.GLASS)){
									player.getMatch().break3Blocks(e.getClickedBlock());
								} else {
									player.useLeft();
								}
							} else {
								player.useLeft();
							}
						}
					}
					e.getPlayer().updateInventory();
				}
			} else {
				e.setCancelled(true);
			}
		}
	}
	
	@EventHandler
	public void playerMoveEvent(PlayerMoveEvent e){
		String name = e.getPlayer().getName();
		FPSPlayer player = FPSCaste.getFPSPlayer(name);
		
		if (player.isFrozen()){
			double xfrom = e.getFrom().getX(); 
		    double zfrom = e.getFrom().getZ();
		        
		    double xto = e.getTo().getX();
		    double zto = e.getTo().getZ();
		        
		    if (!(xfrom == xto && zfrom==zto)){
		        player.getPlayer().teleport(player.getPlayer());
		    }
		} else {
			if (player.isIngame()){
				player.handleMove();
			}
		}
	}
	
	@EventHandler
	public void place(BlockPlaceEvent e){
		if (FPSCaste.getFPSPlayer(e.getPlayer().getName()).isIngame() && !FPSCaste.getFPSPlayer(e.getPlayer().getName()).canBuild()){
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void respawn(PlayerRespawnEvent e){
		FPSPlayer p = FPSCaste.getFPSPlayer(e.getPlayer().getName());
		if (p.isIngame()){
			if (p.getMatch().getState().equals(gameState.PREGAME) || p.getTeam().equals(teamName.SPECTATOR)){
				p.spawn();
			} else {
				p.respawn();
			}
		} else {
			e.setRespawnLocation(Bukkit.getServer().getWorld("world").getSpawnLocation());
		}
	}
	
	@EventHandler
	public void sprint(PlayerToggleSprintEvent e){
		final String name = e.getPlayer().getName();
		FPSPlayer player = FPSCaste.getFPSPlayer(name);
		
		if (player.isIngame() && player.getMatch().getState().equals(gameState.PLAYING)){
			player.zoomOut();
			
			if (runners.containsKey(name)){
				Bukkit.getServer().getScheduler().cancelTask(runners.get(name));
				runners.remove(name);
			}
			if (e.isSprinting()){
				//scheduleStamina(name, 5, false);
				runners.put(name, Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(plugin,new Runnable() {
					@Override
					public void run() {
						if (Bukkit.getServer().getPlayer(name) != null && Bukkit.getServer().getPlayer(name).getFoodLevel() == 6){
							Bukkit.getServer().getScheduler().cancelTask(runners.get(name));
							runners.remove(name);
						} else {
							Bukkit.getServer().getPlayer(name).setFoodLevel(Bukkit.getServer().getPlayer(name).getFoodLevel()-1);
						}
					} 
				}, 5, 5));
			} else {
				//scheduleStamina(name, 10, true);
				runners.put(name, Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(plugin,new Runnable() {
					@Override
					public void run() {
						if (Bukkit.getServer().getPlayer(name) != null && Bukkit.getServer().getPlayer(name).getFoodLevel() == 20){
							Bukkit.getServer().getScheduler().cancelTask(runners.get(name));
							runners.remove(name);;
						} else {
							Bukkit.getServer().getPlayer(name).setFoodLevel(Bukkit.getServer().getPlayer(name).getFoodLevel()+1);
						}
					}
				}, 10, 10));
			}
		}
	}
	
	private void scheduleStamina(final String name, final int delay, final boolean full){
		runners.put(name, Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
			@Override
			public void run() {
				if (Bukkit.getServer().getPlayer(name) != null){
					if (Bukkit.getServer().getPlayer(name).getFoodLevel() > 20 || Bukkit.getServer().getPlayer(name).getFoodLevel() < 6){
						Bukkit.getServer().getScheduler().cancelTask(runners.get(name));
						Bukkit.getServer().getPlayer(name).setFoodLevel(full ? 20 : 6);
						runners.remove(name);
					} else {
						Bukkit.getServer().getPlayer(name).setFoodLevel(Bukkit.getServer().getPlayer(name).getFoodLevel()+(full ? 1 : -1));
					}
				} else {
					Bukkit.getServer().getScheduler().cancelTask(runners.get(name));
				}
			}
		}, 0, delay));
	}
	
	@EventHandler
	public void sneak(PlayerToggleSneakEvent e){
		//TODO make weapon pickup
	}
}
