package net.castegaming.plugins.FPSCaste.commands;

import java.util.Arrays;
import java.util.logging.Level;

import net.castegaming.plugins.FPSCaste.FPSCaste;
import net.castegaming.plugins.FPSCaste.exceptions.IngameException;
import net.castegaming.plugins.FPSCaste.exceptions.NoOnlinePlayerException;
import net.castegaming.plugins.FPSCaste.exceptions.NotAdminException;
import net.castegaming.plugins.FPSCaste.exceptions.NotIngameException;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class FPSCasteCommandHandler{

	public void handle(CommandSender sender, Command command, String[] args) {
		FPSCommandBase cmdClass = null;
		String label = command.getLabel();
		
		if (label.startsWith("fps") || label.startsWith("FPS")){
			if (args.length > 0){
				label = args[0];
				try {
					command = Bukkit.getServer().getPluginCommand(label);
				} catch(Exception e){
					FPSCaste.log("An error occured while handling command " + label + "(" + command.getName() + ") Exception:" + e.getClass().toString() + " " + e.getMessage(), Level.WARNING);
					//whoups command didnt exist!
				}
				
				if (args.length > 1){
					args = Arrays.copyOfRange(args, 1, args.length);
				} else {
					args = new String[0];
				}
			} else {
				FPSCaste.msg(sender, ChatColor.GRAY + "---------------" + FPSCaste.NamePrefix + ChatColor.GRAY + "---------------");
				FPSCaste.msg(sender, ChatColor.GOLD + "Welcome to " + ChatColor.DARK_RED + "FPS" + ChatColor.DARK_GREEN + "Caste!");
				FPSCaste.msg(sender, ChatColor.GOLD + "FPSCaste is a plugin made JUST for minecraft!");
				FPSCaste.msg(sender, ChatColor.GOLD + "For a list of all our commands, typ /help FPSCaste");
				FPSCaste.msg(sender, ChatColor.GOLD + "Version: " + FPSCaste.getInstance().getDescription().getVersion());
				FPSCaste.msg(sender, ChatColor.GOLD + "Author: " + FPSCaste.getInstance().getDescription().getAuthors());
				return;
			}
		}
		
		try {
			if (label.equals("join")) {
				cmdClass = new JoinCommand(sender, args);
			} else if (label.equalsIgnoreCase("leave")) {
				cmdClass = new LeaveCommand(sender, args);
			} else if (label.equalsIgnoreCase("team")) {
				cmdClass = new TeamChatCommand(sender, args);
			} else if (label.equalsIgnoreCase("all")) {
				cmdClass = new AllChatCommand(sender, args);
			} else if (label.equalsIgnoreCase("stopgame")){
				cmdClass = new StopGameCommand(sender, args);
			} else if (label.equalsIgnoreCase("setadmin")){
				cmdClass = new SetAdminCommand(sender, args);
			} else if (label.equalsIgnoreCase("showgames")){
				cmdClass = new ShowgamesCommand(sender, args);
			} else if (label.equalsIgnoreCase("allies")){
				cmdClass = new AlliesCommand(sender, args);
			} else if (label.equalsIgnoreCase("axis")){
				cmdClass = new AxisCommand(sender, args);
			} else if (label.equalsIgnoreCase("respawn")){
				cmdClass = new RespawnCommand(sender, args);
			} else if (label.equalsIgnoreCase("newmap") || label.equalsIgnoreCase("nm")){ 
				cmdClass = new NewMapCommand(sender, args);
			} else if (label.equalsIgnoreCase("buildingmode") || label.equalsIgnoreCase("buildmode")){
				cmdClass = new SetBuildingModeCommand(sender, args);
			} else if (label.equalsIgnoreCase("stats")){
				cmdClass = new StatsCommand(sender, args);
			} else if (label.equalsIgnoreCase("class") || label.equalsIgnoreCase("clas")){
				cmdClass = new ChangeClassCommand(sender, args);
			} else if (label.equalsIgnoreCase("createmap") || label.equalsIgnoreCase("createm") || label.equalsIgnoreCase("cmap") || label.equalsIgnoreCase("cm")){
				cmdClass = new CreateMapCommand(sender, args);
			} else if (label.equalsIgnoreCase("em") || label.equalsIgnoreCase("editmap") || label.equalsIgnoreCase("editm")  || label.equalsIgnoreCase("emap")){
				cmdClass = new EditMapCommand(sender, args);
			} else if (label.startsWith("CH") || label.startsWith("ch")){
				cmdClass = new ChatCommand(sender, args);
			}
		// we are not responsible for any other command
		} catch (NotAdminException e) {
			FPSCaste.badMsg(sender, "You can't do this if you are not an admin (permission fpscaste.admin, or OP).");
		} catch (NotIngameException e){
			FPSCaste.badMsg(sender, "You can't do this if you are not in-game");
		} catch (IngameException e){
			FPSCaste.badMsg(sender, "You can't do this if you are in-game! Try /leave first.");
		} catch (NoOnlinePlayerException e){
			FPSCaste.badMsg(sender, "You need to be online to use this command!");
		} catch (Exception e) {
			FPSCaste.log("An error occured while handling command " + label + ". Exception:" + e.getClass().toString() + " " + e.getMessage(), Level.WARNING);
			e.printStackTrace();
		}
		
		if (cmdClass != null){
			boolean handled = cmdClass.handle();
			if (!handled){
				FPSCaste.msg(sender, command.getUsage());
			}
		}
	}
}
