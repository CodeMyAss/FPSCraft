package net.castegaming.plugins.FPSCaste;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;

import net.castegaming.plugins.FPSCaste.config.Config;
import net.castegaming.plugins.FPSCaste.config.PlayerConfiguration;
import net.castegaming.plugins.FPSCaste.enums.ChatChannel;
import net.castegaming.plugins.FPSCaste.enums.Points;
import net.castegaming.plugins.FPSCaste.enums.Rank;
import net.castegaming.plugins.FPSCaste.enums.gameState;
import net.castegaming.plugins.FPSCaste.enums.gunName;
import net.castegaming.plugins.FPSCaste.enums.teamName;
import net.castegaming.plugins.FPSCaste.map.MapPreset;
import net.castegaming.plugins.FPSCaste.playerclass.PlayerClass;
import net.castegaming.plugins.FPSCaste.playerclass.weapons.Weapon;
import net.castegaming.plugins.FPSCaste.playerclass.weapons.WeaponContainer;
import net.castegaming.plugins.FPSCaste.storage.PlayerStorage;
import net.castegaming.plugins.FPSCaste.util.StatusBarAPI;
import net.castegaming.plugins.FPSCaste.util.Util;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.mcsg.double0negative.tabapi.TabAPI;

public class FPSPlayer {
	/**
	 * HashMap containing every FPSPlayer
	 * @param String the name of the player
	 * @param FPSPlayer the new class of himself
	 */
	public static HashMap<String, FPSPlayer> customPlayers = new HashMap<String, FPSPlayer>();
	
	/**
	 * All the options to create empty string additions
	 */
	private static String[] options = {
		" " , 
		"  " , ChatColor.RESET + "" , "" + ChatColor.RESET , ChatColor.RED + "" , "" + ChatColor.RED , ChatColor.BLUE + "" , "" + ChatColor.BLUE , 
		"   " , ChatColor.RESET + " " , " " + ChatColor.RESET , ChatColor.RED + " " , " " + ChatColor.RED , ChatColor.BLUE + " " , " " + ChatColor.BLUE , 
		"    " , ChatColor.RESET + "  " , "  " + ChatColor.RESET , ChatColor.RED + "  " , "  " + ChatColor.RED , ChatColor.BLUE + "  " , "  " + ChatColor.BLUE , 
		"     " , ChatColor.RESET + "   " , "   " + ChatColor.RESET , ChatColor.RED + "   " , "   " + ChatColor.RED , ChatColor.BLUE + "   " , "   " + ChatColor.BLUE , 
		"      " , ChatColor.RESET + "    " , "    " + ChatColor.RESET , ChatColor.RED + "    " , "    " + ChatColor.RED , ChatColor.BLUE + "    " , "    " + ChatColor.BLUE , 
		"       " , ChatColor.RESET + "     " , "     " + ChatColor.RESET , ChatColor.RED + "     " , "     " + ChatColor.RED , ChatColor.BLUE + "     " , "     " + ChatColor.BLUE , 
		"        " , ChatColor.RESET + "      " , "      " + ChatColor.RESET , ChatColor.RED + "      " , "      " + ChatColor.RED , ChatColor.BLUE + "      " , "      " + ChatColor.BLUE , 
		"         " , ChatColor.RESET + "       " , "       " + ChatColor.RESET , ChatColor.RED + "       " , "       " + ChatColor.RED , ChatColor.BLUE + "       " , "       " + ChatColor.BLUE , 
		"          " , ChatColor.RESET + "        " , "        " + ChatColor.RESET , ChatColor.RED + "        " , "        " + ChatColor.RED , ChatColor.BLUE + "         " , "         " + ChatColor.BLUE , 
		"           " , ChatColor.RESET + "         " , "         " + ChatColor.RESET , ChatColor.RED + "         " , "         " + ChatColor.RED , ChatColor.BLUE + "         " , "         " + ChatColor.BLUE , 
		}; //51

	
	/*========================= Class variables ===================*/
	/*
	 * player starts -> house
	 * 
	 * 
	 */
	
	/**
	 * The name of this player
	 */
	private String player;
	
	private boolean isAdmin = false;
	private boolean isIngame = false;
	private boolean isFrozen = false;
	
	/**
	 * The players he is currently assisting
	 */
	private HashMap<String, Integer> assistingplayers;
	
	/**
	 * The plaeyrs which currently 'assist' him, aka damaged him
	 */
	private HashMap<String, Integer> assistedplayers;
	
	private PlayerClass currentClass;
	
	/**
	 * Current kills from this match
	 */
	private int kills = 0;
	
	/**
	 * Current deaths from this match
	 */
	private int deaths = 0;
	
	/**
	 * Current assists from this match
	 */
	private int assists = 0;
	
	/**
	 * Highest killstreak ever gotten
	 */
	private int maxKillstreak = 0;
	
	/**
	 * match ID from last or current
	 */
	private int match = 0;
	
	/**
	 * Current match points
	 */
	private int points = 0;
	
	/**
	 * The total XP, without prestige
	 */
	private int xp = 0;
	
	/**
	 * Defines the time in seconds this player played in a match. no spectator.
	 */
	private int matchPlayTime = 0;
	
	private PlayerConfiguration config;
	
	/**
	 * The task ID for the reloading<br/>
	 * If its -1 its not reloading
	 */
	//private int reloading = -1;
	
	private boolean buildingMode = false;

	private MapPreset selectedMap;
	
	private PlayerStorage storage;
	
	/**
	 * Holds the channel this player is currently talking in
	 */
	private ChatChannel channel = ChatChannel.MATCH;
	
	/**
	 * The current level of zoom for this player.
	 */
	private int zoomlevel;

	private long shootingTime;
	
	
	/**
	 * Creates a new player by the given name<br/>
	 * Loads his config, and does loadPlayerData()
	 * @param player the name of  the new FPSPlayer
	 */
	public FPSPlayer(String player){
		this.player = player;
		config = Config.getPlayerConfig(player);
		assistedplayers = new HashMap<String, Integer>();
		assistingplayers = new HashMap<String, Integer>();
		
		loadPlayerData();
		customPlayers.put(player, this);
		
		if (FPSCaste.hasTabAPI()){
			TabAPI.setPriority(FPSCaste.getInstance(), getPlayer(), 2);
		}
	}
	
	/**
	 * Loads all the required player data from his config
	 */
	public void loadPlayerData(){
		xp = Integer.parseInt(config.getConfig().getString("xp"));
		maxKillstreak = config.getConfig().getInt("killstreak");
		currentClass = new PlayerClass(player);
		
		if (config.getConfig().contains("oldInv")){
			//load old things, he left without his items returned.
			
			Double stats[] = config.getConfig().getDoubleList("oldInv.stats").toArray(new Double[6]);
			
			@SuppressWarnings("unchecked")
			List<ItemStack> contentList = (List<ItemStack>)config.getConfig().get("oldInv.content");
			ItemStack[] content = contentList.toArray(new ItemStack[0]);
			
			@SuppressWarnings("unchecked")
			List<ItemStack> armorList = (List<ItemStack>)config.getConfig().get("oldInv.armor");
			ItemStack[] armor = armorList.toArray(new ItemStack[0]); 
			
			Location teleportPlace = Util.StringToLoc(config.getConfig().getString("oldInv.location"));
			config.set("oldInv", null);
			saveConfig();
			
			storage = new PlayerStorage(stats, content, armor, teleportPlace);
			returnOldThings();
		}
	}

	/**
	 * Saves this player his config
	 */
	private void saveConfig() {
		config.save();
	}
	
	public PlayerConfiguration getConfig(){
		return config;
	}

	/**
	 * Checks if the player is admin
	 * @return
	 */
	public boolean isAdmin() {
		if (!getPlayer().hasPermission("FPSCaste.admin")){
			if (!getPlayer().isOp()){
				return isAdmin;
			} else {
				return true;
			}
		} else {
			return true;
		}
	}
	
	/**
	 * Sets the player's admin status to the one defined
	 * @param yesno
	 */
	public void setAdmin(boolean yesno) {
		isAdmin = yesno;
		config.setAdmin(yesno);
		config.save();
	}  
	
	/**
	 * Returns whether the player is Ingame or not
	 * @return true or false
	 */
	public boolean isIngame() {
		return isIngame;
	}
	
	/**
	 * Function to get the player class (For .getLocation() etc)
	 * @return The FPSPlayer his player class
	 */
	public Player getPlayer(){
		return Bukkit.getServer().getPlayerExact(player);
	}

	/**
	 * saves all the playerdata to a config
	 */
	public void savePlayerData() {
		config.set("kills", config.getConfig().getInt("kills", 0) + kills);
		config.set("deaths", config.getConfig().getInt("deaths", 0) + deaths);
		config.set("assists", config.getConfig().getInt("assists", 0) + assists);
		config.set("xp", xp);
		config.set("killstreak", maxKillstreak);
		
		saveConfig();
	}
	
	/**
	 * Removes the player instance
	 */
	public void remove(){
		leave();
		customPlayers.remove(player);
	}
	
	/**
	 * Makes a player leave a match. <br/>
	 * Fails silently if hes not ingame
	 */
	public void leave() {
		if (isIngame){
			FPSCaste.getMatch(match).removePlayer(player);
			isIngame = false;
			returnOldThings();
			savePlayerData();
			Player player = getPlayer();
			if (player != null){
				player.removePotionEffect(PotionEffectType.INVISIBILITY);
				player.setScoreboard(Bukkit.getServer().getScoreboardManager().getMainScoreboard());
				
				try {
					if (FPSCaste.hasTabAPI()) TabAPI.clearTab(player);
				} catch (NullPointerException e){
					//?? tabapi bug
				}
			}
		}
	}
	
	/**
	 * Returns the player his old things<br/>
	 * IF the player is not online, it will try to store it
	 */
	public void returnOldThings(){
		Player player = getPlayer();
		if (player != null){
			resetPlayerInfo();
			player.teleport(storage.getTeleportPlace());
			
			player.getInventory().clear();
			player.getInventory().setContents(storage.getTeleportInventory());
			player.getInventory().setArmorContents(storage.getTeleportArmory());
			
			player.setHealth(storage.getHealth());
			player.setFoodLevel(storage.getFood());
			player.setSaturation(storage.getSaturation());
			player.setLevel(storage.getLevel());
			player.setExp(storage.getExp());
			player.setGameMode(storage.getGameMode());
		} else {
			System.out.println("null, wrote to config");
			config.set("oldInv.content", storage.getTeleportInventory());
			config.set("oldInv.armor", storage.getTeleportArmory());
			config.set("oldInv.location", Util.toLocationString(storage.getTeleportPlace()));
			config.set("oldInv.stats", storage.getTeleportStats());
			saveConfig();
		}
	}
	
	/**
	 * Grabs the match which the player is currently in
	 * @return The player his current match, or null if he is not playing
	 */
	public Match getMatch(){
		if (isIngame){
			return FPSCaste.getMatch(match);
		} else {
			return null;
		}
	}

	/**
	 * Grabs the name of this player
	 * @return the name (String)
	 */
	public String getName() {
		return player;
	}

	/**
	 * Lets the player join the match given by int
	 * @param matchID The match to join
	 */
	public void joinMatch(int matchID) {
		match = matchID;
		isIngame = true;
		
		try {
			storage = new PlayerStorage(getPlayer());
			
			if (FPSCaste.hasTabAPI()){
				TabAPI.setPriority(FPSCaste.getInstance(), getPlayer(), 2);
			}
			
			goodMsg("You have joined the match! (Map: " + getMatch().getMap().getMapName() + ")");
			resetPlayerInfo();
			getPlayer().setGameMode(GameMode.SURVIVAL); 
			
			FPSCaste.getMatch(matchID).addPlayer(player);
			freeze();
			Invis();
			
			spawn();
		} catch (NullPointerException e){
			e.printStackTrace();
			badMsg("Tried to place you in a match, but the teleporting went wrong. Try again later");
			//keep matchID for some reasons i dont know yet
			leave();
		}
	}

	/**
	 * Freezes the player
	 */
	public void freeze() {
		isFrozen = true;
	}
	
	/**
	 * Unfreezes the player
	 */
	public void unFreeze() {
		isFrozen = false;
	}

	/**
	 * Check whether the player is frozen or not
	 * @return true or false
	 */
	public boolean isFrozen() {
		return isFrozen;
	}
	
	/**
	 * Resets various things:<br/>
	 * setLevel(0)<br/>
	 * setFoodLevel(20)<br/>
	 * setHealth(20)<br/>
	 * setSaturation(10)<br/>
	 * setExhaustion(0.0f)<br/>
	 * setExp(0)<br/>
	 * Unfreeze, uninvis
	 */
	public void resetPlayerInfo(){
		if (getPlayer() != null){
			getPlayer().setLevel(0);
			getPlayer().setFoodLevel(20);
			getPlayer().setHealth(20.0);
			getPlayer().setSaturation(10);
			getPlayer().setExp(0);
			getPlayer().setExhaustion(0.0f);
			
			getPlayer().getInventory().clear();
			clearArmor();
		}
	}
	
	/**
	 * Runs resetPlayerInfo and resetStats
	 */
	public void resetPlayerInfoAndStats(){
		resetPlayerInfo();
		resetStats();
	}
	
	/**
	 * 
	 */
	private void resetStats() {
		kills = 0;
		deaths = 0;
		assists = 0;
		points = 0;
		matchPlayTime = 0;	
	}

	/**
	 * Lets a player join the team specified!
	 * @param name the {@link teamName} the fpsplayer should join
	 */
	public void join(teamName name) {
		goodMsg("You have joined the " + name.toString() + "!");
		getMatch().add(name, player);
		resetPlayerInfo();
		spawn();
		unInvis();
		
		if (getMatch().getState().equals(gameState.PREGAME)){
			freeze();
			goodMsg("You will be unfrozen when the match starts!");
		} else {
			unFreeze();
		}
	}
	
	/**
	 * Gets the tema the player is in
	 * @return the teamEnum or null
	 */
	public teamName getTeam(){
		if (isIngame){
			return getMatch().getPlayerTeam(player);
		} else {
			return null;
		}
	}
	
	/**
	 * Sends a green message to the player
	 * @param message The message to send
	 */
	public void goodMsg(String message){
		FPSCaste.goodMsg(getPlayer(), message);
	}
	
	/**
	 * Sends a red message to the player
	 * @param message The message to send
	 */
	public void badMsg(String message){
		FPSCaste.badMsg(getPlayer(), message);
	}
	
	/**
	 * Sends a white message to the player
	 * @param message The message to send
	 */
	public void Msg(String message){
		FPSCaste.Msg(getPlayer(), message);
	}
	
	/**
	 * Makes the player visible
	 */
	public void unInvis(){
		getPlayer().removePotionEffect(PotionEffectType.INVISIBILITY);
	}
	
	/**
	 * Makes the player invisible
	 */
	public void Invis(){
		getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 999999999, 1));
	}

	/**
	 * Spawns the player and gives him his stuff<br/>
	 * Only when he is not death, due to the way teleport works :/<br/>
	 * <br/>
	 * - giveClass() only when he has a valid team
	 * @return 
	 */
	public void spawn() {
		if (!isIngame) return;
		new BukkitRunnable(){
			@Override
			public void run() {
				getPlayer().teleport(getSpawn());
				if (!getTeam().equals(teamName.SPECTATOR)){
					resetPlayerInfo();
					giveClass();
		    		updateXPbar();
		    	}
			}
		}.runTaskLater(FPSCaste.getInstance(), 5);
	}
	
	private Location getSpawn(){
		if (getMatch().getState().equals(gameState.PREGAME) || getTeam().equals(teamName.SPECTATOR)){
			return getMatch().spawn(getTeam());
		} else {
			return getMatch().respawn(getTeam());
		}
	}

	/**
	 * Gives the player his class
	 */
	public void giveClass(){
		checkPlayerClass();
		currentClass.reset();
		for (Weapon w : currentClass.getWeapons()){
			if (w != null){
				w.setOwner(player);
				w.giveNew();
			}
		}
	}
	
	/**
	 * Checks if the current class matches the class from config<br/>
	 * If not, it updates the {@link PlayerClass}
	 */
	public void checkPlayerClass(){
		if (!config.getSelectedClass().equals(currentClass.getName())){
			currentClass = new PlayerClass(player);
		}
	}

	/**
	 * gives the player his gun<br/>
	 * Links to giveWeapon(gunName)
	 * @param weaponID the weapon to give (integer)
	 * @return 
	 */
	public Weapon giveWeapon(int weaponID) {
		return giveWeapon(WeaponContainer.getWeapon(weaponID));
	}
	
	public Weapon giveWeapon(String weaponName) {
		return giveWeapon(WeaponContainer.getWeapon(weaponName));
	}
	
	/**
	 * gives the player his gun
	 * @param gunID the gun to give (integer)
	 * @return 
	 */
	public Weapon giveWeapon(WeaponContainer weapon) {
		Weapon w = new Weapon(weapon);
		currentClass.setWeapon(w, w.getSlot());
		w.giveNew();
		return w;
	}
	
	/**
	 * left uses the weapon the player is currently holding
	 */
	public void useLeft(){
		if (getWeapon() != null){
			if (getWeapon().canZoom()){
				zoom();
			}
		}
	}
	
	/**
	 * right uses the weapon the player is currently holding
	 */
	public void useRight(){
		useRight(true);
	}
	
	public void useRight(boolean uses) {
		//TODO rewrite
		if (getWeapon() != null){
			if (uses){
				if (getWeapon().isGun() && canShoot()){
					stopReloading();
					if (getWeapon().getBullets() < 2){
						if (getWeapon().isEmpty()){
							 if (getWeapon().getMagezines() > 0){
								 reload();
							 } else {
								 //empty and no magezines
							 }
						} else {
							// not empty, shoot once more and set empty
							useRightShoot();
							getWeapon().setEmpty();
						}
					}  else {
						useRightShoot();
					}
				} else {
					getWeapon().useRight();
					if (getWeapon().getAmount() > 1){
						getWeapon().setAmount(getWeapon().getAmount() - 1);
					} else {
						getclass().removeWeapon(getPlayer().getInventory().getHeldItemSlot());
						getPlayer().setItemInHand(new ItemStack(Material.AIR));
					}
				}
			} else {
				getWeapon().useRight();
			}
		}
	}
	
	private void useRightShoot(){
		getWeapon().useRight();
		shootingTime = System.currentTimeMillis();
		getWeapon().setBullets(getWeapon().getBullets()-1);
	}
	
	/**
	 * Returns the weapon from the current slot
	 * @return The weapon
	 */
	public Weapon getWeapon(){
		return getWeapon(getPlayer().getInventory().getHeldItemSlot());
	}
	
	/**
	 * Returns the weapon from the slot
	 * @param slot the slot to look in
	 * @return The weapon
	 */
	public Weapon getWeapon(int slot){
		return currentClass.getWeapon(slot);
	}

	/**
	 * Retuns if the player is reloading or not
	 * @return true or false
	 */
	public boolean isReloading() {
		for (Weapon w: getclass().getWeapons()){
			if (w != null && w.isReloading()) return true;
		}
		
		return false;
	}

	/**
	 * Toggles the player in a building mode
	 * @param canHe true/false
	 */
	public void setBuilding(boolean canHe) {
		buildingMode = canHe;
	}
	
	/**
	 * returns whether the player is alowed to build or not
	 * @return true or false
	 */
	public boolean canBuild() {
		return buildingMode;
	}
	
	/**
	 * Returns the kills made by this player
	 * @return
	 */
	public int getKills(){
		return kills;
	}
	
	/**
	 * Returns the deaths made by this player
	 * @return
	 */
	public int getDeaths(){
		return deaths;
	}
	
	/**
	 * Returns the assists made by this player
	 * @return
	 */
	public int getAssists(){
		return assists;
	}
	
	/**
	 * Adds a kill to the player
	 */
	public void addKill(String death) {
		kills++;
		addKillstreak(1);
		assistingplayers.remove(death);
		
		givePoints(getMatch().handleKill(player, death));
		
		if (maxKillstreak < getKillstreak()){
			maxKillstreak = getKillstreak();
		}
		
		goodMsg("Youve got a kill! total: " + kills + " Killstreak: " + getKillstreak() );
	}

	/**
	 * Adds a death to the player
	 * @param killer the name of the killer
	 */
	public void addDeath(String killer) {
		deaths++;
		assistingplayers.clear();
		Set<String> list = assistedplayers.keySet();
		for (String name : list){
			if (!name.equals(killer)){
				FPSPlayer p = FPSCaste.getFPSPlayer(name);
				if (p != null){
					p.addAssist(player);
					p.removeAssistingPlayer(player);
				}
			}
		}
		assistedplayers.clear();
		goodMsg("Youre death! deaths: " + deaths);
	}

	private void addAssist(String name) {
		assists++;
		givePoints(Points.ASSIST);
		goodMsg("You have asisted! assists: " + name + " total: " + assists);
	}

	/**
	 * Grabs all the players this player is assisting (aka attacked)
	 * @return Set<String> Containing all the assisintg players
	 */
	public Set<String> getAssistingplayers() {
		return assistingplayers.keySet();
	}

	/**
	 * Adds a player to this player his assists
	 * @param assisting Whether this is him damaging someone(true), or he getting damaged(false)
	 * @param playername The player to add
	 */
	public void addAssist(Boolean assisting, final String playername) {
		if (playername.equals(player)) return;
		
		HashMap<String, Integer> assistList;
		if(assisting){
			assistList = assistingplayers;
		} else {
			assistList = assistedplayers;
		}
		
		if (assistList.containsKey(playername)){
			Bukkit.getServer().getScheduler().cancelTask(assistList.get(playername));
		}
		
		//goodMsg("Successfully assisted " + playername);
		assistList.put(playername, Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(FPSCaste.getInstance(), new Runnable() {
			@Override
			public void run() {
				if (assistingplayers.containsKey(playername))
					removeAssistingPlayer(playername);
				//goodMsg("Removed assist!");
			}
		}, 100));
		
	}
	
	/**
	 * removes a player to this player his assists
	 * @param playername The player to remove
	 */
	public void removeAssistingPlayer(String playername) {
		FPSCaste.getFPSPlayer(playername).assistedplayers.remove(player);
		assistingplayers.remove(playername);
	}

	/**
	 * Boolean to check if the player can shoot
	 * @param delay the delay from the gun he wants to shoot
	 * @return True or false 
	 */
	public boolean canShoot() {
		if ((shootingTime + getWeapon().getDelay()) < System.currentTimeMillis()){
			return true;
		} else {
			return false;
		}
	}
	
	public int getKillstreak(){
		return getPlayer().getLevel();
	}
	
	public void addKillstreak(int kills){
		getPlayer().setLevel(getKillstreak() + kills);
	}

	/**
	 * This should only be called if the player is about to die in a special occasion<br/>
	 * Old way! We now use meta values
	 * @param gunName
	 * @param name
	 */
	@Deprecated
	public void addSpecialItem(gunName gunName, String name) {
		ItemStack specialDead = new ItemStack(1,1);
		ItemMeta meta = specialDead.getItemMeta();
		meta.setDisplayName(gunName + "");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add(name);
		meta.setLore(lore);
		specialDead.setItemMeta(meta);
		
		getPlayer().getInventory().addItem(specialDead);
	}

	/**
	 * Gives {@link Points} to this player <br/>
	 * Use Points.xxxx!
	 * @param {@link Points} the Points.value
	 */
	public void givePoints(int points) {
		goodMsg("+" + points + " points!");
		this.points += points;
		
		while (points >= Rank.getXPLeft(xp)){
			points -= Rank.getXPLeft(xp);
			xp += Rank.getXPLeft(xp);
			if (isIngame){
				getMatch().broadcast(player + " has been ranked up to " + getRankName() + "(" + getRank() + ")");
			} else {
				goodMsg("You have been ranked up to " + getRankName() + "(" + getRank() + ")");
			}
		}
		xp += points;
		updateXPbar();
	}

	public int getTotalAssists() {
		return config.getConfig().getInt("assists", 0);
	}

	public int getTotalKills() {
		return config.getConfig().getInt("kills", 0);
	}

	public int getTotalDeaths() {
		return config.getConfig().getInt("deaths", 0);
	}

	public int getmaxKillstreak() {
		return maxKillstreak;
	}

	/**
	 * Get the current match points
	 * @return int matchpoints
	 */
	public int getPoints() {
		return points;
	}

	/**
	 * Get the total XP, without prestige
	 * @return int total XP
	 */
	public int getXPTotal() {
		return xp;
	}

	public PlayerClass getclass() {
		return currentClass;
	}

	public void setClass(String className) {
		getConfig().setSelectedClass(className);
	}
	
	public void setClass(PlayerClass playerClass) {
		currentClass = playerClass;
	}

	/**
	 * Sets the crrent selected map to the one provided
	 * @param preset the map to set to 
	 */
	public void setSelectedMap(MapPreset preset) {
		selectedMap = preset;
	}
	
	/**
	 * Sends you the current set {@link MapPreset}, or null
	 * @return The mapPreset currently fot this player
	 */
	public MapPreset getSelectedMap() {
		return selectedMap;
	}

	/**
	 * Handles the rightclick of this player<br/>
	 * Gets handled at the match side.
	 */
	public void handleRightClick() {
		getMatch().handleRightClickNear(player);
		useRight();
	}
	
	/**
	 * Handles the movement of this player<br/>
	 * Gets handled at the match side.
	 */
	public void handleMove() {
		getMatch().handleMovement(player);
	}

	/**
	 * Returns if this player is an enemy to the other player
	 * @param name the other player
	 * @return true if he is, otherwise false
	 */
	public boolean isEnemyTo(String name) {
		if (FPSCaste.getFPSPlayer(name) != null && FPSCaste.getFPSPlayer(name).isIngame && isIngame){
			if (FPSCaste.getFPSPlayer(name).getTeam()!= null && getTeam() != null && FPSCaste.getFPSPlayer(name).getTeam().equals(getTeam())){
				return getTeam().equals(teamName.ALONE);
			} else {
				return true;
			}
		}
		return false;
	}
	
	public void updateTab(){ //update the tab for a player
		if (FPSCaste.hasTabAPI() && isIngame && getPlayer() != null){
			TabAPI.clearTab(getPlayer());
			TabAPI.setTabString(FPSCaste.getInstance(), getPlayer(), 0, 0, ChatColor.GREEN + "Player");
			TabAPI.setTabString(FPSCaste.getInstance(), getPlayer(), 0, 1, ChatColor.GREEN + "Kills / Deaths");
			TabAPI.setTabString(FPSCaste.getInstance(), getPlayer(),0, 2,  ChatColor.GREEN + "Assist / Score");
	
			TabAPI.setTabString(FPSCaste.getInstance(), getPlayer(), 1, 0, ChatColor.GRAY + " -------------");
			TabAPI.setTabString(FPSCaste.getInstance(), getPlayer(), 1, 1, "        " + ChatColor.GOLD + getMatch().getTimeLeft().replace(":", "|"));
			TabAPI.setTabString(FPSCaste.getInstance(), getPlayer(),1, 2,  ChatColor.GRAY + " ------------ ");	
			
			/////////** Names **////////////
			List<String> names = new LinkedList<String>();
			
			if (getMatch().getMode().isFFA() || getTeam().equals(teamName.SPECTATOR)){
				names.addAll(sort(getMatch().getPlayers().keySet()));
			} else {
				names.addAll(sort(getMatch().getTeam(getTeam())));
				List<String> leftOvers = new LinkedList<String>(getMatch().getPlayers().keySet());
				leftOvers.removeAll(getMatch().getTeam(getTeam()));
				leftOvers.removeAll(getMatch().getTeam(teamName.SPECTATOR));
				names.addAll(sort(leftOvers));
				names.addAll(sort(getMatch().getTeam(teamName.SPECTATOR)));
			}
			
			/////////** Scores **////////////
			List<String> scores = new LinkedList<String>();
			for (String name : names){
				FPSPlayer p = FPSCaste.getFPSPlayer(name); 
				scores.add(check(p.getKills() + " / " + p.getDeaths(), scores));
				scores.add(check(p.getAssists() + " / " + p.getPoints(), scores)); 
			}
			
			int pos = 2;
			for (String name : names){
				try {
					if (!(getMatch().getMode().isFFA() || getTeam().equals(teamName.SPECTATOR))){
						if (pos == getMatch().getTeam(getTeam()).size()+2){
							TabAPI.setTabString(FPSCaste.getInstance(), getPlayer(), getMatch().getTeam(getTeam()).size()+2, 0, ChatColor.GREEN + " -------------");
							TabAPI.setTabString(FPSCaste.getInstance(), getPlayer(), getMatch().getTeam(getTeam()).size()+2, 1, ChatColor.GREEN + "--------------");
							TabAPI.setTabString(FPSCaste.getInstance(), getPlayer(), getMatch().getTeam(getTeam()).size()+2, 2,  ChatColor.GREEN + "------------- ");	
							pos++;
						} else if (pos == getMatch().getPlayers().size()+2){
							TabAPI.setTabString(FPSCaste.getInstance(), getPlayer(), getMatch().getPlayers().size()-getMatch().getTeam(teamName.SPECTATOR).size()+2, 0, ChatColor.DARK_RED + " -------------");
							TabAPI.setTabString(FPSCaste.getInstance(), getPlayer(), getMatch().getPlayers().size()-getMatch().getTeam(teamName.SPECTATOR).size()+2, 1, ChatColor.DARK_RED + "--------------");
							TabAPI.setTabString(FPSCaste.getInstance(), getPlayer(), getMatch().getPlayers().size()-getMatch().getTeam(teamName.SPECTATOR).size()+2, 2,  ChatColor.DARK_RED + "------------- ");	
							pos++;
						}
					}
					TabAPI.setTabString(FPSCaste.getInstance(), getPlayer(), pos,0, name);
					TabAPI.setTabString(FPSCaste.getInstance(), getPlayer(), pos,1, scores.get(names.indexOf(name)*2));
					TabAPI.setTabString(FPSCaste.getInstance(), getPlayer(), pos,2, scores.get(names.indexOf(name)*2 + 1));
					pos++;
				} catch (Exception e) {
					//passed the tab limit
					break;
				}
				
			}
			TabAPI.updatePlayer(getPlayer());
		}
	}
	
	/**
	 * Sorts the list of names on points<br/>
	 * most points is first
	 * @param set The list of names to sort
	 * @return A list<String> with the names
	 */
	private List<String> sort(Set<String> players){
		return sort(new LinkedList<String>(players));
	}
	
	/**
	 * Sorts the list of names on points<br/>
	 * most points is first
	 * @param set The list of names to sort
	 * @return A list<String> with the names
	 */
	private List<String> sort(List<String> players){
		List<String> sortedPlayers = new LinkedList<String>();
		
		if (players.size() > 1){
			for(String name : players){
				if (FPSCaste.getFPSPlayer(name) != null){
					int points = FPSCaste.getFPSPlayer(name).getPoints();
					int i = 0;
					for (String name2 : sortedPlayers){
						if (FPSCaste.getFPSPlayer(name2).getPoints() < points){
							break;
						} else if (FPSCaste.getFPSPlayer(name2).getPoints() == points){
							if (FPSCaste.getFPSPlayer(name2).getKills() >= getKills()){
								break;
							} else{
								i++;
								break;
							}
						}
						i++;
					}
					sortedPlayers.add(i, name);
				}
			}
			return sortedPlayers;
		} else {
			return players;
		}
		
	}
	
	/**
	 * Creates an invisible addition to the string
	 * @param s The string to fix
	 * @param scores the list of strings to check against
	 * @return The fixed string
	 */
	private String check(String s, List<String> scores){
		String newString = s;
		int i=0;
		while(scores.contains(newString)){
			newString = s + options[i];
			if (i++ <= options.length && (s + options[i+1]).length() <= 16){
				i++;
			} else {
				FPSCaste.log("Could not make an invisible different string! i=" + i, Level.WARNING);
				break;
			}
		}
		return newString;
	}
	
	/**
	 * Appends the character to the string until the defined string length is reached
	 * @param string The string to fix
	 * @param infront If this needs to be infront(true), behind(false) or evened out (null)
	 * @param Character the char to append
	 * @param length The max lenght of this string
	 * @return The fixed string, could be the same
	 */
	@SuppressWarnings("unused")
	private String fix(String string, Boolean infront, Character c, int lenght){
		if (string.length() < lenght){
			for (int i=0; i<lenght-string.length(); i++){
				if (infront == null){
					if (i/2 == 0){
						string = c + string;
					} else {
						string += c;
					}
				}else if (infront){
					string = c + string;
				}else {
					string += c;
				}
			}
			return string;
		} else {
			return string;
		}
	}

	/**
	 * returns if this player can damage the player passed through<br/>
	 * Different then isEnemy since this takes Hardcore into account
	 * @param name The Player to compare with
	 * @return If he can damage (true) or not (false)
	 */
	public boolean canDamage(String name) {
		return getMatch().canDamage(getName(), name);
	}
	
	/**
	 * Method used later on, incase of shooting and UAV
	 * @param name The Player to compare with
	 * @return If he can see him (true) or not (false)
	 */
	public boolean canSee(String name) {
		return false;
	}
	
	/**
	 * Returns the rank number
	 * @return int ranknumber
	 */
	public int getRank(){
		return Rank.getclosestLevel(xp, false);
	}
	
	/**
	 * the name of this player its current rank
	 * @return
	 */
	public String getRankName(){
		return Rank.getLevelName(getRank());
	}
	
	/**
	 * Returns the XP left to the next level
	 * @return the XP, an integer
	 */
	public int getXPLeft(){
		return Rank.getXPLeft(xp);
	}
	
	/**
	 * 
	 */
	public void updateXPbar(){
		if (isOnline() && isIngame){
			float xpbetween = Rank.XPBetween(getRank(), Rank.getclosestLevel(xp, true));
			float percent = getXPLeft() / xpbetween;
			getPlayer().setExp(1 - percent);
		}
	}
	/**
	 * Defines if the player is online
	 * @return true if he is, otherwise false
	 */
	public boolean isOnline(){
		return Bukkit.getServer().getPlayer(player) != null;
	}
	
	/**
	 * Returns the total time played a match as a non-spectator
	 * @return the time in seconds
	 */
	public int getPlaytime(){
		return matchPlayTime;
	}
	
	/**
	 * This will up the seconds played with 20 ticks, supposedly a second.
	 */
	public void addSecondPlaytime(){
		matchPlayTime++;
	}
	
	/**
	 * clears this player his armour
	 */
	public void clearArmor(){
		getPlayer().getInventory().setHelmet(null);
		getPlayer().getInventory().setChestplate(null);
		getPlayer().getInventory().setLeggings(null);
		getPlayer().getInventory().setBoots(null);
	}

	/**
	 * Changes the {@link ChatChannel} to the one given.<br/>
	 * If this channel is allready selected, a message  saying that will appear
	 * @param c The {@link ChatChannel} to change too
	 */
	public void setChatChannel(ChatChannel c) {
		if (!channel.equals(c)){
			channel = c;
			goodMsg("Your chat channel has changed to: " + ChatColor.GOLD + c.toString().toLowerCase());
		} else {
			badMsg("You are allready in \"" + ChatColor.GOLD + c.toString().toLowerCase() + "\"");
		}
	}
	
	/**
	 * Returns this player his current chat channel
	 * @return His current {@link ChatChannel}
	 */
	public ChatChannel getChatChannel(){
		return channel;
	}
	
	/**
	 * Zooms out this player.
	 * sets zoom level to 0, and removes the zoom stats
	 */
	public void zoomOut(){
		setZoomlevel(0);
	}

	/**
	 * @return the zoomlevel
	 */
	public int getZoomlevel() {
		return zoomlevel;
	}

	/**
	 * @param zoomlevel the zoomlevel to set
	 */
	public void setZoomlevel(int zoomlevel) {
		this.zoomlevel = zoomlevel;
		//set potions accordingly
		if (zoomlevel > 0){
			//set potions
			getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 24000, zoomlevel*2), true);
		} else {
			//remove potions
			getPlayer().removePotionEffect(PotionEffectType.SLOW);
		}
	}
	
	/**
	 * Zooms to the next level, if possible<br/>
	 * if its the last level, it wil zoom out.
	 */
	public void zoom() {
		if (getWeapon().canZoom()){
			if (getWeapon().getMaxZoomLevel() > zoomlevel){
				setZoomlevel(getZoomlevel()+1);
			} else {
				zoomOut();
			}
		}
	}
	
	/**
	 * checks if the player is zooming
	 * @return true if he is, else false
	 */
	public boolean isZooming() {
		return zoomlevel > 0;
	}

	/**
	 * cancels the reload.<br/>
	 * 
	 */
	public void stopReloading() {
		if (isReloading()){
			for (Weapon w : getclass().getWeapons()){
				if (w != null){
					w.stopReload();
				}
			}
		}
	}

	/**
	 * Reloads the gun if possible.<br/>
	 * this will also try to zoom out
	 */
	public void reload() {
		if (getWeapon() != null && getWeapon().getMagezines() > 0){
			
			//reloading = getPlayer().getInventory().getHeldItemSlot();
			if (getWeapon().canReload() && getWeapon().needsReload()){
				stopReloading();
				getWeapon().reload();
				goodMsg("Reloading...");
				zoomOut();
			}
		}
	}
	
	/**
	 * sends all messages to the player, without formatting.
	 * @param messages A list of messages
	 */
	public void sendClearMessage(String[] messages) {
		for (String string : messages){
			getPlayer().sendMessage(string);
		}
	}

	/**
	 * Sends a clear message to the player
	 * @param message the message to send
	 */
	public void sendClearMessage(String message) {
		getPlayer().sendMessage(message);
	}

	/**
	 * Checks if the player has custom classes.
	 * @return true if he has, otherwise false.
	 */
	public boolean hasCustomClasses() {
		return config.hasCustomClasses();
	}

	/**
	 * places all the custom classes in 1 string.
	 * @return a string of classes
	 */
	public String getCustomclassnames() {
		return config.getCustomclasses();
	}
	
	@Override
	public String toString() {
		return player;
	}
	
	/**
	 * Creates a timer utilising the boss health bar
	 * @param time time in seconds
	 * @param text
	 * @param done
	 */
	public void createTimer(int time, String text, String done){
		StatusBarAPI.displayLoadingBar(text, done, getPlayer(), time, true);
	}

	/**
	 * creates a boss health bar with the text above. Full health
	 * @param text The text to set
	 */
	public void createTextBar(String text) {
		createTextBar(text, 1);
	}
	
	/**
	 * creates a boss health bar with the text above. Using the percentage defined
	 * @param text The text to set
	 * @param healthPercentage the percentage to use
	 */
	public void createTextBar(String text, float healthPercentage) {
		StatusBarAPI.setStatusBar(getPlayer(), text, healthPercentage);
	}
}
