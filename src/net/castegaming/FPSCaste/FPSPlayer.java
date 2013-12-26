package net.castegaming.FPSCaste;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class FPSPlayer {

    private Player player;
    
    private static Map<String, FPSPlayer> customplayers = new HashMap<String, FPSPlayer>();
    
    private FPSPlayer(Player player) {
        this.player = player;
        customplayers.put(player.getName(), this);
    }
    
    // Return a running instance (or create a new one)
    public static FPSPlayer getFPSPlayer(Player player) {
        if(!customplayers.containsKey(player.getName())) {
            return new FPSPlayer(player);
        } else {
            return customplayers.get(player.getName());
        }
    }
    
    // Your special (non-static) methods defined here:
    public String getSpecialName() {
        if(player.getName().equalsIgnoreCase("notch")) {
            return ChatColor.GREEN + player.getName();
        }
        return player.getName();
    }
    
}

