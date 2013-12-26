package net.castegaming.plugins.FPSCaste.commands;

import net.castegaming.plugins.FPSCaste.FPSCaste;
import net.castegaming.plugins.FPSCaste.exceptions.NoOnlinePlayerException;
import net.castegaming.plugins.FPSCaste.exceptions.NotIngameException;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Represents an FPSCaste command
 *
 * @author Brord van Wierst
 */	
	public abstract class FPSCommandBase {
	
		/**
		 * The sender of this command
		 *
		 * @var	sender
		 */
		private CommandSender sender;
	
		/**
		 * The arguments of this command
		 *
		 * @var	args
		 */
		protected String[] args;
	
		/**
		 * 
		 * @param sender The sender of the command
		 * @param args the Command's arguments
		 * @param requiresIngame whether this command can be ran ONLY when the player is in-game or not
		 * @param requiresOnline whether this command can be ran with or without the player beeing online
		 * @throws NotIngameException if the player is not ingame, but it is required
		 * @throws NoOnlinePlayerException if the sender is not a player, but he needs to be
		 */
		public FPSCommandBase(CommandSender sender, String[] args, boolean requiresIngame, boolean requiresOnline) throws NotIngameException, NoOnlinePlayerException{
			if (requiresOnline && !(sender instanceof Player)){
				throw new NoOnlinePlayerException();
			}
			
			if (requiresIngame && !FPSCaste.getFPSPlayer(sender.getName()).isIngame()){
				throw new NotIngameException();
			}  
			
			this.setSender(sender);
			this.args = args;
		}
	
		/**
		 * Handles the command
		 *
		 * @return true if command was used the right way
		 */
		abstract public boolean handle();
	
		/**
		 * Sends a message
		 *
		 * @param 	message	message to send
		 */
		public void msg(String message) {
			FPSCaste.msg(sender, message);
		}
		
		/**
		 * Sends a good message
		 *
		 * @param 	message	message to send
		 */
		public void goodMsg(String message) {
			FPSCaste.goodMsg(sender, message);
		}
	
	
		/**
		 * Sends a failure message
		 *
		 * @param 	message	message to send
		 */
		public void badMsg(String message) {
			FPSCaste.badMsg(sender, message);
		}
	
		/**
		 * Changes the command-sender
		 *
		 * @param 	sender	new sender
		 */
		public void setSender(CommandSender sender) {
			this.sender = sender;
		}
	
		/**
		 * Gets the command-sender
		 *
		 * @return	Command-Sender
		 */
		public CommandSender getSender() {
			return sender;
		}
		
		public String getName(){
			return sender.getName();
		}
	}