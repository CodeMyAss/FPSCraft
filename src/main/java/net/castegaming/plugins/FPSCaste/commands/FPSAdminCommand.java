package net.castegaming.plugins.FPSCaste.commands;

import net.castegaming.plugins.FPSCaste.FPSCaste;
import net.castegaming.plugins.FPSCaste.exceptions.NoOnlinePlayerException;
import net.castegaming.plugins.FPSCaste.exceptions.NotAdminException;
import net.castegaming.plugins.FPSCaste.exceptions.NotIngameException;

import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public abstract class FPSAdminCommand extends FPSCommandBase{

	/**
	 * 
	 * @param sender The name of the sender
	 * @param args The args of the command
	 * @param requiresOnlinePlayer whether this command requires the player to be online or not
	 * @throws NotAdminException if the player is no admin
	 * @throws NotIngameException if the player is not ingame, but he should be
	 * @throws NoOnlinePlayerException if the player is not online, but he should be
	 */
	public FPSAdminCommand(CommandSender sender, String[] args, boolean requiresOnlinePlayer) throws NotAdminException, NotIngameException, NoOnlinePlayerException {
		super(sender, args, false, requiresOnlinePlayer);

		if (!isSenderAdmin()) {
			throw new NotAdminException();
		}		
	}
	
	/**
	 * Checks whether the user is admin or not
	 * @return the admin status (true or false)
	 */
	public boolean isSenderAdmin() {
		if (this.getSender() instanceof Player) {
			return FPSCaste.getFPSPlayer(getName()).isAdmin();
		} else if (!(this.getSender() instanceof ConsoleCommandSender)) {
			return false;
		} else {
			// ConsoleCommandSender is admin
			return true;
		}
		
	}

}
