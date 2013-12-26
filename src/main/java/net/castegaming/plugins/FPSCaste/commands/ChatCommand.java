package net.castegaming.plugins.FPSCaste.commands;

import java.util.Arrays;

import net.castegaming.plugins.FPSCaste.FPSCaste;
import net.castegaming.plugins.FPSCaste.FPSPlayer;
import net.castegaming.plugins.FPSCaste.enums.ChatChannel;
import net.castegaming.plugins.FPSCaste.exceptions.NoOnlinePlayerException;
import net.castegaming.plugins.FPSCaste.exceptions.NotIngameException;

import org.bukkit.command.CommandSender;

public class ChatCommand extends FPSCommandBase{

	public ChatCommand(CommandSender sender, String[] args) throws NotIngameException, NoOnlinePlayerException {
		super(sender, args, true, true);
	}

	@Override
	public boolean handle() {
		FPSPlayer p = FPSCaste.getFPSPlayer(getName());
		if (args.length > 0){
			try {
				ChatChannel c = ChatChannel.valueOf(args[0].toUpperCase());
				p.setChatChannel(c);
				return true;
			} catch(Exception e){
				badMsg("Please choose one of these values: " + Arrays.toString(ChatChannel.values()).replace("[", "").replace("]", ""));
				return false;
			}
		} else {
			p.setChatChannel(p.getChatChannel().next());
			return true;
		}
	}

}
