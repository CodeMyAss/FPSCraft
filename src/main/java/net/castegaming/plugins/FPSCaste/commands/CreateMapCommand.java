package net.castegaming.plugins.FPSCaste.commands;

import net.castegaming.plugins.FPSCaste.FPSCaste;
import net.castegaming.plugins.FPSCaste.config.Config;
import net.castegaming.plugins.FPSCaste.exceptions.NoOnlinePlayerException;
import net.castegaming.plugins.FPSCaste.exceptions.NotAdminException;
import net.castegaming.plugins.FPSCaste.exceptions.NotIngameException;
import net.castegaming.plugins.FPSCaste.map.MapPreset;

import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

/**
 * @author Brord
 *
 */
public class CreateMapCommand extends FPSAdminCommand{

	public CreateMapCommand(CommandSender sender, String[] args) throws NotAdminException, NotIngameException, NoOnlinePlayerException {
		super(sender, args, true);
	}
	
	@Override
	public boolean handle() {
		if (args.length> 0){
			YamlConfiguration map = Config.createMapConfig(args[0]);
			Location l = ((Player) getSender()).getLocation();
			
			map.set("name", args[0]);
			map.set("main", l.getX() + " " + l.getY() + " " + l.getZ() + " " + l.getYaw() + " " + l.getPitch());
			Config.saveMapConfig(args[0], map);
			
			MapPreset preset = new MapPreset(args[0], new Float[]{(float) l.getX(), (float) l.getY(), (float) l.getZ(), l.getYaw(), l.getPitch()});
			preset.setName(args[0]);
			
			FPSCaste.getFPSPlayer(getName()).setSelectedMap(preset);
			goodMsg("Started creating the map: " + args[0]);
			goodMsg(preset.check());
			return true;
		} 
		return false;
	}

}
