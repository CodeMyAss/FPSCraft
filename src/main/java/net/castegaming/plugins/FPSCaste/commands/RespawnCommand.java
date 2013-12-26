/**
 * 
 */
package net.castegaming.plugins.FPSCaste.commands;

import net.castegaming.plugins.FPSCaste.FPSCaste;
import net.castegaming.plugins.FPSCaste.FPSPlayer;
import net.castegaming.plugins.FPSCaste.enums.gameState;
import net.castegaming.plugins.FPSCaste.exceptions.NoOnlinePlayerException;
import net.castegaming.plugins.FPSCaste.exceptions.NotIngameException;

import org.bukkit.command.CommandSender;

/**
 * @author Brord
 *
 */
public class RespawnCommand extends FPSCommandBase { 

	public RespawnCommand(CommandSender sender, String[] args) throws NotIngameException, NoOnlinePlayerException {
		super(sender, args, true, true);
	}

	/* (non-Javadoc)
	 * @see net.castegaming.plugins.FPSCaste.commands.FPSCommandBase#handle()
	 */
	@Override
	public boolean handle() {
		FPSPlayer player = FPSCaste.getFPSPlayer(getName());
		if (!(player.getMatch().getState().equals(gameState.PREGAME) || player.getMatch().getState().equals(gameState.ENDING))){
			player.addDeath(getName());
			player.respawn();
		} else {
			player.badMsg("Cannot respawn if the game has not started, or has allready ended.");
		}
		return true;
	}

}
