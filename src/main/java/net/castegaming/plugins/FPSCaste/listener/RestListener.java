package net.castegaming.plugins.FPSCaste.listener;

import java.util.LinkedList;
import java.util.List;
//import java.util.Set;

import net.castegaming.plugins.FPSCaste.FPSCaste;
import net.castegaming.plugins.FPSCaste.FPSPlayer;
import net.castegaming.plugins.FPSCaste.enums.ChatChannel;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
//import org.bukkit.event.player.PlayerTeleportEvent;
//import org.bukkit.event.server.RemoteServerCommandEvent;
//import org.bukkit.event.server.ServerCommandEvent;

public class RestListener implements Listener {
	
	public static List<String> allowedCommands;
	
	FPSCaste plugin = null;
	//private boolean stopping = false;
	
	public RestListener(FPSCaste plugin){
		this.plugin = plugin;
	}

	@EventHandler
	public void blockPlaceEvent(BlockBreakEvent e){
		if (FPSCaste.getFPSPlayer(e.getPlayer().getName()).isIngame() && !FPSCaste.getFPSPlayer(e.getPlayer().getName()).canBuild()){
			e.setCancelled(true);
		}
	}
	
//	@EventHandler
//	public void serverCommand(ServerCommandEvent e){
//		if (e.getCommand().equals("stop")){
//			System.out.println("Registered server stop command"); 
//			if (!stopping){
//				System.out.println("Blocked it for the first time");
//				e.setCommand("say --[ Stopping in 5 seconds!]--"); 
//				stopping = true;
//				plugin.unLoadFPSCaste();
//				Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
//					
//					@Override
//					public void run() {
//						System.out.println("Send second stop command");
//						Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "stop");
//					}
//				}, 100);
//			}  else {
//				System.out.println("Stopping server ");
//			}
//		}
//	}
	
	@EventHandler(priority = EventPriority.HIGH)
	public void command(PlayerCommandPreprocessEvent e){
//		if (e.getMessage().equals("/stop") || e.getMessage().equals("/reload")){
//			e.setCancelled(true);
//			System.out.println("Cancelled stop from player side");
//			Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), e.getMessage().substring(1, e.getMessage().length()));
//			return;
//		}
		
		if (FPSCaste.getFPSPlayer(e.getPlayer().getName()).isIngame() && !FPSCaste.getFPSPlayer(e.getPlayer().getName()).isAdmin()){
			String cmd = e.getMessage().substring(1, e.getMessage().length());
			if (!Bukkit.getServer().getPluginManager().getPlugin("FPSCaste").getDescription().getCommands().keySet().contains(cmd) && !allowedCommands.contains(cmd)){
				FPSCaste.getFPSPlayer(e.getPlayer().getName()).badMsg("You are not allowed to use \"" + e.getMessage() + "\" at the moment!");
				e.setCancelled(true);
			}
		}
	}
	
	@EventHandler(priority = EventPriority.HIGH)
	public void chat(AsyncPlayerChatEvent e){
		try {
			FPSPlayer sender = FPSCaste.getFPSPlayer(e.getPlayer().getName());
			List<Player> retainPlayers = new LinkedList<Player>();
			for (Player p : Bukkit.getServer().getOnlinePlayers()){
				FPSPlayer temp = FPSCaste.getFPSPlayer(p.getName());
				if (sender.getName().equals(temp.getName())){
					retainPlayers.add(p);
				} else if (sender.isIngame() && temp.isIngame()){
					//both ingame
					if (sender.getMatch().getMatchID() == temp.getMatch().getMatchID()){
						//same match
						if (sender.getTeam().equals(temp.getTeam())){
							//same team
							if ((sender.getChatChannel().equals(ChatChannel.ALL) && temp.getChatChannel().equals(ChatChannel.ALL)) || !sender.getChatChannel().equals(ChatChannel.ALL)){
								//both all, or sender not all
								retainPlayers.add(p);
							}
						} else {
							//different team
							if (sender.getChatChannel().equals(ChatChannel.MATCH) ){
								//different team, but he sends in match, so all good
								retainPlayers.add(p);
							} else if (sender.getChatChannel().equals(ChatChannel.ALL) && temp.getChatChannel().equals(ChatChannel.ALL)){
								//both talking in all, its good
								retainPlayers.add(p);
							}
						}
					} else {
						//different match
						if (sender.getChatChannel().equals(ChatChannel.ALL) && temp.getChatChannel().equals(ChatChannel.ALL)){
							//different match, both on all so its ok
							retainPlayers.add(p);
						}
					}
				} else if (sender.isIngame()){
					//sender is ingame
					if (sender.getChatChannel().equals(ChatChannel.ALL)){
						//all good, he sends to all
						retainPlayers.add(p);
					}
				} else if (temp.isIngame()){
					//receiver is ingame
					if (temp.getChatChannel().equals(ChatChannel.ALL)){
						//all good, he sends to all
						retainPlayers.add(p);
					}
				}
				
				if (sender.getChatChannel().equals(ChatChannel.TEAM)){
					e.setMessage(ChatColor.AQUA + e.getMessage());
				}
			}
			e.getRecipients().retainAll(retainPlayers);
		} catch(UnsupportedOperationException ex){
			//could not modify the messages recipients
		}
		if (FPSCaste.getFPSPlayer(e.getPlayer().getName()).isIngame() && !FPSCaste.getFPSPlayer(e.getPlayer().getName()).isAdmin() && Bukkit.getServer().getPluginManager().getPlugin("Factions") != null){
			if (e.getMessage().startsWith("f ") || e.getMessage().startsWith("F ") || e.getMessage().equalsIgnoreCase("f")){
				e.setCancelled(true);
				FPSCaste.getFPSPlayer(e.getPlayer().getName()).badMsg("Please dont use faction commands if you are ingame! Was this no factions command? Try not to start with f");
			}
		}
	}
}
