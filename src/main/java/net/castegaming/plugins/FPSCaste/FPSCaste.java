package net.castegaming.plugins.FPSCaste;

import java.util.Random;
import java.util.logging.Level;

import net.castegaming.plugins.FPSCaste.commands.FPSCasteCommandHandler;
import net.castegaming.plugins.FPSCaste.config.Config;
import net.castegaming.plugins.FPSCaste.config.RegisterConfigs;
import net.castegaming.plugins.FPSCaste.gamemodes.playlist.PlayList;
import net.castegaming.plugins.FPSCaste.listener.ConnectionListener;
import net.castegaming.plugins.FPSCaste.listener.DeathListener;
import net.castegaming.plugins.FPSCaste.listener.EntityDamageListener;
import net.castegaming.plugins.FPSCaste.listener.ItemListener;
import net.castegaming.plugins.FPSCaste.listener.PlayerListener;
import net.castegaming.plugins.FPSCaste.listener.ProtocolListener;
import net.castegaming.plugins.FPSCaste.listener.RestListener;
import net.castegaming.plugins.FPSCaste.map.Map;
import net.castegaming.plugins.FPSCaste.map.initMaps;
import net.castegaming.plugins.FPSCaste.map.LoadPresets;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;

public class FPSCaste extends JavaPlugin{
	
	/**
	 * Main plugin instance
	 */
	private static FPSCaste plugin;
	
	/**
	 * The ProtocolLib Manager
	 */
	private static ProtocolManager protocolManager;
	
	/**
	 * The base prefix of messages <br/>
	 * Looks like this: [FPSCaste]
	 */
	public static final String NamePrefix = ChatColor.GRAY + "[" + ChatColor.DARK_RED + "FPS" + ChatColor.DARK_GREEN + "Caste" + ChatColor.GRAY + "] " + ChatColor.RESET;
	
	/**
	 * FPSCaste command handler
	 */
	FPSCasteCommandHandler commandHandler = new FPSCasteCommandHandler();
	
	@Override
	public void onEnable(){
		loadFPSCaste();
	}

	@Override
	public void onDisable(){
		unLoadFPSCaste();
	}
	
	/**
	 * Sends all the commands to their own handler
	 */
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
		commandHandler.handle(sender, cmd, args);
		return true;
	}
	
	/**
	 * Load FPSCaste
	 */
	private void loadFPSCaste() {
		if (Bukkit.getServer().getPluginManager().getPlugin("Multiverse-Core") != null){
			FPSCaste.log("--------------- " + NamePrefix + "---------------");
			long time = System.currentTimeMillis();
			
			FPSCaste.log("Loading all maps, configs, and creating a match");
			
			plugin = this;
			protocolManager = ProtocolLibrary.getProtocolManager();
			
			//load the Config handler
			new RegisterConfigs(this);
			
			//loads all gamemode presets
			new LoadPresets();
			
			//registers all the maps available
			new initMaps();
			
			loadMatches();
			
			if (getServer().getOnlinePlayers().length > 0){
				log("Parsing all online players!");
				for (Player p : getServer().getOnlinePlayers()){
					new FPSPlayer(p.getName());
				}
			}

			PluginManager PM = Bukkit.getServer().getPluginManager();
			registerListeners(PM);
			
			if (getServer().getPluginManager().getPlugin("TagAPI") != null){
				PM.registerEvents(new ProtocolListener(this), this);
			} else {
				log(ChatColor.DARK_RED + "TagAPI has not been found!/n" + ChatColor.DARK_RED + "Disabling team colored name tags", Level.WARNING);
			}
			
			if (getServer().getPluginManager().getPlugin("TabAPI") == null){
				log(ChatColor.DARK_RED + "TabAPI has not been found!/n" + ChatColor.DARK_RED + "Disabling tab playerlist", Level.WARNING);
			}
			
			FPSCaste.log("Loading done. Took " + (System.currentTimeMillis()-time) + " miliseconds.");
			FPSCaste.log("----------------------------------------");
		} else {
			FPSCaste.log(ChatColor.DARK_RED + "Multiverse has not been found!/n" + ChatColor.DARK_RED + "We NEED this! stopping FPSCaste", Level.SEVERE);
			Bukkit.getServer().getPluginManager().disablePlugin(this);
		}
	}

	/**
	 * Unload FPSCaste
	 */
	public void unLoadFPSCaste() {
		getServer().broadcastMessage(ChatColor.GOLD + "The server is shutting down or reloading!");
		Integer[] matches = Match.currentMatches.keySet().toArray(new Integer[0]);
		for (Integer match : matches){
			if (match != null && match > 0){
				FPSCaste.log("Stopped match " + match + " becasuse of a reboot/stop", Level.INFO);
				FPSCaste.getMatch(match).endGame("The game has ended becauase of a server restart/reload! stats saved.", false);
			}
		}
	}
	
	/**
	 * 
	 */
	private void registerListeners(PluginManager PM) {
		PM.registerEvents(new ConnectionListener(this), this);
		PM.registerEvents(new DeathListener(this), this);
		PM.registerEvents(new EntityDamageListener(this), this);
		PM.registerEvents(new ItemListener(this), this);
		PM.registerEvents(new PlayerListener(this), this);
		PM.registerEvents(new RestListener(this), this);
	}

	/**
	 * 
	 */
	private void loadMatches() {
		ConfigurationSection c = getConfig().getConfigurationSection("matches");
		for (String s : c.getKeys(false)){
			PlayList list = PlayList.getNew(c.getString(s + "playlist"));
			if (list == null) {
				log("Playlist " + c.getString(s + ".playlist") + " cannot be found!");
				continue;
			}
			int i = c.getInt(s + ".maxplayers", -1);
			if (i < 0){
				log("A startup match does not have maxplayers defined!");
				getConfig().set("matches." + s + ".maxplayers", 16);
				i = 16;
			}
			log("Created match: " + new Match(i, list));
		}
	}
	
	/*------------------------------[Normal messages]--------------------------------------*/
	/**
	 * Sends a white message to the defined player
	 * @param sender The player which we send the message to
	 * @param message The message we are going to send
	 */
	public static void msg(CommandSender sender, String message) {
		if (sender != null){
			String[] messages = message.split("/n");
			for (String m : messages){
				sender.sendMessage(NamePrefix + m);
			}
		}
	}
	
	public static void Msg(Player sender, String message) {
		if (sender != null){
			String[] messages = message.split("/n");
			for (String m : messages){
				sender.sendMessage(NamePrefix + m);
			}
		}
	}
	/*--------------------------------[Bad messages]----------------------------*/

	/**
	 * Sends a red message to the defined player
	 * @param sender The player which we send the message to
	 * @param message The message we are going to send
	 */
	public static void badMsg(CommandSender sender, String message) {
		if (sender != null){
			String[] messages = message.split("/n");
			for (String m : messages){
				sender.sendMessage(NamePrefix + ChatColor.DARK_RED + m);
			}
		}
	}
	
	public static void badMsg(Player sender, String message) {
		if (sender != null){
			String[] messages = message.split("/n");
			for (String m : messages){
				sender.sendMessage(NamePrefix + ChatColor.DARK_RED + m);
			}
		}
	}
	/*-------------------------------[Good messages]-----------------------------*/
	
	/**
	 * Sends a green message to the defined player
	 * @param sender The player which we send the message to
	 * @param message The message we are going to send
	 */
	public static void goodMsg(CommandSender sender, String message) {
		if (sender != null){
			String[] messages = message.split("/n");
			for (String m : messages){
				sender.sendMessage(NamePrefix + ChatColor.DARK_GREEN + m);
			}
		}
	}
	
	public static void goodMsg(Player sender, String message) {
		if (sender != null){
			String[] messages = message.split("/n");
			for (String m : messages){
				sender.sendMessage(NamePrefix + ChatColor.DARK_GREEN + m);
			}
		}
	}
	/*-------------------------------------------------------------------------*/

	public static void log(String message){
		log(message, Level.INFO);
	}
	
	/**
	 * Log to the server with a defined level
	 * @param message The message to log
	 * @param level The level to log the message as
	 */
	public static void log(String message, Level level) {
		String[] messages = message.split("/n");
		if (Bukkit.getServer().getConsoleSender() != null){
			for (String m : messages){
				Bukkit.getServer().getConsoleSender().sendMessage(NamePrefix + m);
			}
		} else {
			for (String m : messages){
				Bukkit.getServer().getLogger().log(level, NamePrefix + m);
			}
		}
	}

	/**
	 * Use FPSCaste.getFPSPlayer(name).isAdmin() instead!<br/>
	 * check to define if a player is an admin
	 * @param name player to check
	 */
	@Deprecated 
	public static boolean isAdmin(String name) {
		return getFPSPlayer(name).isAdmin();
	}
	
	/**
	 * Returns the plugin instance
	 * @return The plugin instance
	 */
	public static FPSCaste getInstance(){
		return plugin;
	}
	
	/**
	 * Sends the protocol manager reference
	 * @return ProtocolManager
	 */
	public static ProtocolManager getProtocolManager(){
		return protocolManager;
	}
	
	/**
	 * Grabs the FPSPlayer instance given by name
	 * @param player the player to get the ID from
	 * @return The FPSPlayer from the name supplied, or null if it doesnt exist
	 */
	public static FPSPlayer getFPSPlayer(String player){
		if(FPSPlayer.customPlayers.containsKey(player) ){
			return FPSPlayer.customPlayers.get(player);
		} else {
			return null;
		}
	}

	/**
	 * Grabs the match instance given by ID
	 * @param match the matchID to get
	 * @return the match, or null if it doesnt exist
	 */
	public static Match getMatch(int match) {
		if(Match.currentMatches.containsKey(match) ){
			return Match.currentMatches.get(match);
		} else {
			return null;
		}
	}
	
	/**
	 * Grabs the map instance given by ID
	 * @param mapID The mapID to get
	 * @return The map, or null if it doesnt exist
	 */
	public static Map getMap(int mapID){
		if(Map.maps.containsKey(mapID)){
			return Map.maps.get(mapID);
		} else {
			return null;
		}
	}
	
	/**
	 * Sends a random map ID to the requester
	 * @return the mapID or -1 if none are available
	 */
	public static int randomMap(){
		if (Map.mapAvailable.size() > 0){
			int r =  new Random().nextInt(Map.mapAvailable.size());
			return Map.mapAvailable.get(r);
		} else {
			return -1;
		}
		
	}
	
	/**
	 * Sends a random match ID to the requester
	 * @return the matchID or -1 if none are available
	 */
	public static int randomMatch(){
		if (Match.openMatches.size() > 0){
			int r =  new Random().nextInt(Match.openMatches.size());
			int matchID = Match.openMatches.get(r);
			return matchID;
		} else {
			return -1;
		}
	}

	public static boolean hasTabAPI() {
		return Bukkit.getServer().getPluginManager().getPlugin("TabAPI") != null;
	}

	public static boolean useTag() {
		return Bukkit.getServer().getPluginManager().getPlugin("TagAPI") != null;
	}
}
