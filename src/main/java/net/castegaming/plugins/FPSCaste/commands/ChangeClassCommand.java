package net.castegaming.plugins.FPSCaste.commands;

import net.castegaming.plugins.FPSCaste.FPSCaste;
import net.castegaming.plugins.FPSCaste.FPSPlayer;
import net.castegaming.plugins.FPSCaste.exceptions.NoOnlinePlayerException;
import net.castegaming.plugins.FPSCaste.exceptions.NotIngameException;
import net.castegaming.plugins.FPSCaste.playerclass.PlayerClass;
import net.castegaming.plugins.FPSCaste.util.Parse;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class ChangeClassCommand extends FPSCommandBase{

	public ChangeClassCommand(CommandSender sender, String[] args) throws NotIngameException, NoOnlinePlayerException {
		super(sender, args, false, true);
	}

	@Override
	public boolean handle() {
		FPSPlayer player = FPSCaste.getFPSPlayer(getSender().getName());
		if (args.length > 0){
			String name = Parse.ArrayToString(args, " ");
			String tempname = "";
			if ((tempname = player.getConfig().getCreatedClass(name)) != ""){
				//a custom class
				name = tempname;
			} else if ((tempname = PlayerClass.getDefaultClassName(name)) != ""){
				//its a default class
				PlayerClass defaultClass = PlayerClass.getDefaultClass(tempname);
				if (defaultClass != null){
					if (defaultClass.getRequiredLevel() <= player.getRank()){
						//required level accepted
						name = tempname;
					} else {
						//too low
						badMsg("This class requires level " + defaultClass.getRequiredLevel() + ", but you are level " + player.getRank() + ". Please choose another");
						sendOptions(player);
						return true;
					}
				}
				name = tempname;
			} else {
				msg("Wrong class name! pick one of these: ");
				sendOptions(player);
				return true;
			}
			if (name != ""){
				if (!player.getclass().getName().equals(name)){
					player.setClass(name);
					goodMsg("Succesfully set your class to: " + name.toString() + "./n" +
							"Changes will take effect in the next spawn.");
				} else {
					badMsg("You allready have that class!");
				}
			}
		} else {
			msg("Pick one of these classes: ");
			sendOptions(player);
		}
		return true;
	}
	
	public void sendOptions(FPSPlayer player){
		player.sendClearMessage(ChatColor.GOLD + "You currently have " + player.getclass());
		player.sendClearMessage(ChatColor.GOLD + "Default classes" + ChatColor.RESET + ": " + PlayerClass.getDefaultClasses());
		if (player.hasCustomClasses()){
			player.sendClearMessage(ChatColor.GOLD + "Custom classes" + ChatColor.RESET + ": " + player.getCustomclassnames());
		}
	}
}
