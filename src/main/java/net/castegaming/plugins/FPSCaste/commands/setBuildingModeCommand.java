package net.castegaming.plugins.FPSCaste.commands;

import net.castegaming.plugins.FPSCaste.FPSCaste;
import net.castegaming.plugins.FPSCaste.FPSPlayer;
import net.castegaming.plugins.FPSCaste.exceptions.NoOnlinePlayerException;
import net.castegaming.plugins.FPSCaste.exceptions.NotAdminException;
import net.castegaming.plugins.FPSCaste.exceptions.NotIngameException;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class setBuildingModeCommand extends FPSAdminCommand{

	public setBuildingModeCommand(CommandSender sender, String[] args) 
							 throws NotAdminException, NotIngameException, NoOnlinePlayerException {
		super(sender, args, false);
	}

	@Override
	public boolean handle() {
		Player argsPlayer;
		
		//set player variable
		if (args.length > 0){
			argsPlayer = Bukkit.getServer().getPlayer(args[0]);
		} else {
			if (getSender() instanceof Player){
				argsPlayer = Bukkit.getServer().getPlayer(getName());
			} else {
				argsPlayer = null;
			}
		}
		
		if (argsPlayer != null){
			FPSPlayer player = FPSCaste.getFPSPlayer(argsPlayer.getName());
			if (player != null){
				player.setBuilding(!player.canBuild());
			}
			player.goodMsg("Your building mode has been set to: " + player.canBuild());
		} else {
			if (args.length > 0){
				FPSCaste.badMsg(getSender(), "Could not find \"" + args[0] + "\" Is he online?");
			} else {
				FPSCaste.badMsg(getSender(), "You cant set the console to building mode...");
			}
		}
		return true;
	}

}
