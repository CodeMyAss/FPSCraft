package net.castegaming.plugins.FPSCaste.listener;

import java.util.logging.Level;

import net.castegaming.plugins.FPSCaste.FPSCaste;
import net.castegaming.plugins.FPSCaste.FPSPlayer;
import net.castegaming.plugins.FPSCaste.config.Config;
import net.castegaming.plugins.FPSCaste.enums.teamName;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class ConnectionListener implements Listener {
	
	FPSCaste plugin = null;
	
	public ConnectionListener(FPSCaste plugin){
		this.plugin = plugin;
	}
	
	@EventHandler
	public void PlayerLoginEvent(PlayerJoinEvent e){
		String name = e.getPlayer().getName();
		FPSPlayer player;
		
		if (!Config.hasPlayerConfig(name)){
			Config.createPlayerConfig(name);
			
			player = new FPSPlayer(name);
			player.goodMsg("Welcome to FPSCaste! An awesome First Person shooter minecraft server powered by CasteGaming!");
		} else {
			//player has joined before, so we need to check stuff
			//part to check if he relogged
			if (FPSCaste.getFPSPlayer(name) == null){
				//he didnt relog back in 2 minutes, so we need to create a new FPSPlayer, and load stats
				player = new FPSPlayer(name);
			} else {
				player = FPSCaste.getFPSPlayer(name);
				if (player.isIngame()){
					if (player.getTeam().equals(teamName.SPECTATOR)){
						player.spawn();
					}
				}
			}
		}
	}
	
	@EventHandler 
	public void LogoutEvent(PlayerQuitEvent e){
		final String eplayer = e.getPlayer().getName();
		if (!FPSCaste.getFPSPlayer(eplayer).isIngame()){
			return;
		}
		FPSCaste.log("Trigegered game leave on logout", Level.INFO);
		
		e.setQuitMessage(null);
		//create schedueled event. if doenst log back in in 2 minutes, remove his FPSPlayer from the hashmap
		//and store the data inside of his player config
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
    		public void run() {
    			FPSCaste.log("Inside the schedule for " + eplayer, Level.INFO);
    			if (Bukkit.getServer().getPlayerExact(eplayer) == null){
    				//He is not online
    				FPSPlayer player = FPSCaste.getFPSPlayer(eplayer);
    				if (player != null){
    					
    					/*this is needed because he could logout, login, and logout again
    					so if he logged out a second time, this will trigger 2 times
    					but the first time, it will remove the FPSPlayer, so
    					getFPSPlayer will return null*/
    					
    					FPSCaste.log(eplayer + " Has left the game", Level.INFO);
    					player.remove();
    				}
    			}
    		}
    	//2 minutes in ticks
    	} , 2400L);
	}
}
