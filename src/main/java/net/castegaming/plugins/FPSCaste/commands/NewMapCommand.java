package net.castegaming.plugins.FPSCaste.commands;

import net.castegaming.plugins.FPSCaste.Match;
import net.castegaming.plugins.FPSCaste.config.Config;
import net.castegaming.plugins.FPSCaste.exceptions.NoOnlinePlayerException;
import net.castegaming.plugins.FPSCaste.exceptions.NotAdminException;
import net.castegaming.plugins.FPSCaste.exceptions.NotIngameException;
import net.castegaming.plugins.FPSCaste.map.Map;
import net.castegaming.plugins.FPSCaste.map.MapPreset;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class NewMapCommand extends FPSAdminCommand {

	public NewMapCommand(CommandSender sender, String[] args) throws NotAdminException, NotIngameException, NoOnlinePlayerException {
		super(sender, args, true);
	}

	@Override
	public boolean handle() {
		if (args.length > 0){
			if (!MapPreset.exists(args[0])){
				this.badMsg("The mapname you supplied is not configued, or has been typed wrong (" + args[0] + ")");
				this.badMsg("You need to pick one of these: " + MapPreset.getAll());
				return true;
			}
			Player player = Bukkit.getServer().getPlayer(this.getSender().getName());
			Location loc = player.getLocation();
			YamlConfiguration maps = Config.getConfig("maps");
			int configID = maps.getKeys(false).size() + 1;
			
			maps.set(configID + ".mapname", this.args[0].toUpperCase().replace(" ", ""));
			maps.set(configID + ".world", loc.getWorld().getName());
			maps.set(configID + ".x", loc.getX());
			maps.set(configID + ".y", loc.getY());
			maps.set(configID + ".z", loc.getZ());
			maps.set(configID + ".yaw", loc.getYaw());
			maps.set(configID + ".pitch", loc.getPitch());
			
			Config.saveConfig("maps", maps);
			
			new Map(args[0] ,player.getPlayer().getLocation());
			
			if (Match.currentMatches.size() == 0){
				new Match();
			}
			
			this.goodMsg("The map " + args[0] + " has been created!");
			
			return true;
		} else {
			return false;
		}
	}

}
