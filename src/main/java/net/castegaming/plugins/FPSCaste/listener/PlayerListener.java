package net.castegaming.plugins.FPSCaste.listener;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import net.castegaming.plugins.FPSCaste.FPSCaste;
import net.castegaming.plugins.FPSCaste.FPSPlayer;
import net.castegaming.plugins.FPSCaste.enums.BreakableBlocks;
import net.castegaming.plugins.FPSCaste.enums.gameState;
import net.castegaming.plugins.FPSCaste.enums.teamName;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
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
						
						if (!player.canBuild()) {
							e.setCancelled(true);
							
							//handle right click
							player.handleRightClick();
						}
					//left clicked
					} else if (e.getAction().equals(Action.LEFT_CLICK_AIR) || e.getAction().equals(Action.LEFT_CLICK_BLOCK)){
						if (!player.canBuild()){
							if(e.getClickedBlock() != null){
								try { 
									BreakableBlocks.valueOf(e.getClickedBlock().getType().name());
									player.getMatch().break3Blocks(e.getClickedBlock());
								} catch (IllegalArgumentException ex){ player.useLeft();}
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
		
		if (player.isIngame()){
			if (player.isFrozen()){
				double xfrom = e.getFrom().getX(); 
			    double zfrom = e.getFrom().getZ();
			        
			    double xto = e.getTo().getX();
			    double zto = e.getTo().getZ();
			        
			    if (!(xfrom == xto && zfrom==zto)){
			        player.getPlayer().teleport(player.getPlayer());
			    }
			} else {
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
			p.spawn();
		} else {
			e.setRespawnLocation(Bukkit.getServer().getWorlds().get(0).getSpawnLocation());
		}
	}
	
	@EventHandler
	public void sprint(PlayerToggleSprintEvent e){
		final String name = e.getPlayer().getName();
		FPSPlayer player = FPSCaste.getFPSPlayer(name);
		if (player.isFrozen()) {
			e.setCancelled(true);
			return;
		}
		
		if (player.isIngame() && player.getMatch().getState().equals(gameState.PLAYING)){
			if (runners.containsKey(name)) removeRunning(name);
			
			player.zoomOut();
			scheduleStamina(name, e.isSprinting() ? 5 : 10, !e.isSprinting());
		}
	}
	
	/**
	 * Schedules a stamina task
	 * @param name the name from he {@link Player}
	 * @param delay the delay for each task
	 * @param full to go up or down
	 */
	private void scheduleStamina(final String name, final int delay, final boolean full){
		runners.put(name, Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
			@Override
			public void run() {
				Player p = Bukkit.getServer().getPlayer(name);
				if (p != null){
					if (p.getFoodLevel() > 20 || p.getFoodLevel() < 6){
						p.setFoodLevel(full ? 20 : 6);
						removeRunning(name);
					} else {
						p.setFoodLevel(p.getFoodLevel()+(full ? 1 : -1));
					}
				} else {
					removeRunning(name);
				}
			}
		}, full ? delay : 0, delay));
	}
	
	/**
	 * Make this {@link Player} stop running
	 * @param name the name from the {@link Player}
	 */
	void removeRunning(String name){
		Bukkit.getServer().getScheduler().cancelTask(runners.get(name));
		runners.remove(name);
	}
	
	@EventHandler
	public void sneak(PlayerToggleSneakEvent e){
		//TODO make weapon pickup
	}
}
