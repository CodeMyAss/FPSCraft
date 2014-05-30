package net.castegaming.plugins.FPSCaste.commands;

import net.castegaming.plugins.FPSCaste.FPSCaste;
import net.castegaming.plugins.FPSCaste.FPSPlayer;
import net.castegaming.plugins.FPSCaste.exceptions.NoOnlinePlayerException;
import net.castegaming.plugins.FPSCaste.exceptions.NotAdminException;
import net.castegaming.plugins.FPSCaste.exceptions.NotIngameException;

import org.bukkit.command.CommandSender;

/**
 * @author Brord
 *
 */
public class SetAdminCommand extends FPSAdminCommand{

	public SetAdminCommand(CommandSender sender, String[] args) throws NotAdminException, NotIngameException, NoOnlinePlayerException {
		super(sender, args, false);
	}
	
	

	@Override
	public boolean handle() {
		if (args.length == 2){
			if ( FPSCaste.getFPSPlayer(args[0]) != null){
				FPSPlayer player = FPSCaste.getFPSPlayer(args[0]);
				String name = player.getPlayer().getName();
				
				if (args[1].charAt(0) == 't' || args[1].charAt(0) == 'T'){
					if (!player.isAdmin()){
						player.setAdmin(true);
						this.goodMsg(name +  " his admin status has been set to true");
						FPSCaste.goodMsg(player.getPlayer(), "You are now an FPSCaste game admin!");
					} else {
						this.msg(name + " is already an admin.");
					}
				} else if (args[1].charAt(0) == 'f' || args[1].charAt(0) == 'F'){
					if (player.isAdmin()){
						player.setAdmin(false);
						this.goodMsg(name + " his admin status has been set to false");
						FPSCaste.badMsg(player.getPlayer(), "You are no longer an FPSCaste game admin!");
					} else {
						this.msg(name + " is no admin already.");
					}
				} else {
					this.badMsg("Please define whether you want him to be an admin(true), or not.(false)");
				}
			} else {
				this.badMsg("The entered player could not be found. Is he online?");
			}
			return true;
		} else {
			return false;
		}
		
	}

}
