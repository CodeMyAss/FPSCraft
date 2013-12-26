package net.castegaming.plugins.FPSCaste.listener;

import net.castegaming.plugins.FPSCaste.FPSCaste;
import net.castegaming.plugins.FPSCaste.FPSPlayer;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.kitteh.tag.PlayerReceiveNameTagEvent;

public class ProtocolListener implements Listener {
	
	FPSCaste plugin = null;
	
	public ProtocolListener(FPSCaste plugin){
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onNameTag(PlayerReceiveNameTagEvent e) {
		//e.getPlayer() he receives the tag
		//e.getNamedPlayer() the sender of the tag
		//e.getTag() tag he receives
		
		FPSPlayer receiver = FPSCaste.getFPSPlayer(e.getPlayer().getName());
		FPSPlayer sender = FPSCaste.getFPSPlayer(e.getNamedPlayer().getName());
		
		if (receiver != null && receiver.isIngame() && sender != null && sender.isIngame()){
			if (receiver.isEnemyTo(sender.getName())){
				if (receiver.canSee(sender.getName())){
					e.setTag(ChatColor.DARK_RED + e.getTag());
				} else {
					e.setTag("§§§§");
				}
			} else {
				e.setTag(ChatColor.GREEN + e.getTag());
			}
		}
	}
}
