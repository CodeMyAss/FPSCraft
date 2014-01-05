package net.castegaming.plugins.FPSCaste;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.scheduler.BukkitRunnable;
import org.kitteh.tag.TagAPI;

import net.castegaming.plugins.FPSCaste.enums.Points;
import net.castegaming.plugins.FPSCaste.enums.breakableBlocks;
import net.castegaming.plugins.FPSCaste.enums.gameState;
import net.castegaming.plugins.FPSCaste.enums.gameType;
import net.castegaming.plugins.FPSCaste.enums.gunName;
import net.castegaming.plugins.FPSCaste.enums.teamName;
import net.castegaming.plugins.FPSCaste.gamemodes.GameMode;
import net.castegaming.plugins.FPSCaste.gamemodes.TeamDeathmatch;
import net.castegaming.plugins.FPSCaste.gamemodes.playlist.PlayList;
import net.castegaming.plugins.FPSCaste.map.Map;

public class Match {
	
	/**
	 * HashMap containing every Match
	 * @param Integer the ID of the match
	 * @param Match the Masch which represents the ID
	 */
	public static HashMap<Integer, Match> currentMatches = new HashMap<Integer, Match>();
	
	/**
	 * List containing all the open matches (AKA not full, or taken)
	 * @param Integer the matchID
	 */
	public static LinkedList<Integer> openMatches = new LinkedList<Integer>();
	
	/**
	 * Broadcasting values, as ticks
	 */
	public static List<Integer> broadCastValues;
	
	/**
	 * Defines the time between endgame, and a new game loading.<br/>
	 * Ints are in seconds
	 */
	public static int switchWaidTime;
	
	//*========================== Class variables ========================*/
	
	/**
	 * Holding all the players in THIS match
	 */
	private HashMap<String, teamName> players = new HashMap<String, teamName>();
	
	/**
	 * Holds the start time of every playerb 
	 */
	private HashMap<String, Long> playerStartTime = new HashMap<String, Long>();
	
	/**
	 * Holds the arrows shot in this match<br/>
	 * @param Integer ArrowID
	 * @param gunName the gun name enum
	 */
	//public HashMap<UUID, gunName> arrows = new HashMap<UUID, gunName>();
	
	/**
	 * Holds all the broken blocks
	 */
	private HashMap<Block, Material> brokenBlocks = new HashMap<Block, Material>();
	
	private int matchID;
	private int allies;
	private int axis;
	private gameState state;
	private int mapID = -1;
	private int maxPlayers;
	private int currentPlayers;
	
	/**
	 * time the match started, in milliseconds
	 */
	private long startingtime;
	
	/**
	 * playtime of the full match, in ticks
	 */
	private long playtime;
	
	/**
	 * If the first kill can still be made or not
	 */
	private boolean firstKill = true;
	 
	/**
	 * The {@link GameMode} of this match
	 */
	private GameMode mode;
	
	/**
	 * Broadcasting values, as ticks
	 */
	private List<Integer> CurrentBroadCastValues;

	/**
	 * The schedulerID for the current scheduled broadcast.
	 */
	private int scheduler;

	/**
	 * scheduler for the end time broadcast *as a fix*
	 */
	private int finaltime_scheduler;

	private PlayList list;
	
	private void Tick20() {
		if (mode.isFinished() && getState().equals(gameState.PLAYING)){
			endGame(mode.EndedReason(), true);
		} else {
			for (String name : getPlayers().keySet()){
				if (!FPSCaste.getFPSPlayer(name).getTeam().equals(teamName.SPECTATOR)){
					FPSCaste.getFPSPlayer(name).addSecondPlaytime();
				}
			}
			
			mode.second(this);
			drawScoreBoard();
			updateTabList();
		}
	}
	
	/**
	 * Makes a new match, with the ID as the current highest ID +1
	 */
	public Match(int maxplayers, PlayList list){
		int ID = 0;
		
		//gets the highest value from all the matches
		for (Integer oldID : currentMatches.keySet()){
			if (oldID > ID){
				ID = oldID;
			}
		}
		
		//Highest matchID + 1 = the new match's ID
		matchID = ID+1;
		maxPlayers = maxplayers; 
		currentMatches.put(matchID, this);
		openMatches.add(matchID);
		
		this.list = list;
		newMatch();
	}

	private void newMatch(){
		if (list == null){
			//keep same map?
		} else {
			list.next();
			String mapname = list.getMap();
			int mapID = Map.isMapAvailable(mapname);
			if(mapID >= 0){
				//set new map
				Map.addAvailable(this.mapID);
				Map.removeAvailable(mapID);
				this.mapID = mapID;
				broadcast("Switching to a new map! (" + getMap().getMapName() + ")");
			} else {
				if (this.mapID >= 0){
					//same map
					broadcast("Keeping the same map because we have not found an available map!");
				}
			}
			mode = list.getMode();
			mode.init(getMap());
		}
		
		brokenBlocks.putAll(mode.load(getMap()));
		
		firstKill = true;
		currentPlayers = 0;
		CurrentBroadCastValues = new LinkedList<Integer>(broadCastValues);
		/*================start first scheduler====================*/
		
		startingtime = System.currentTimeMillis();
		setTime(mode.getRound()*60*20);
		state = gameState.PREGAME;
		
		new BukkitRunnable() {
			@Override
			public void run() {
				try {
					Tick20();
				} catch (Exception e){
					cancel();
				}
			}
		}.runTaskTimer(FPSCaste.getInstance(), 0, 20);
	}
	
	/**
	 * Starts a new round.
	 */
	public void newRound(){
		if (mode.hasNextRound()){
			mode.nextRound();
			broadcast("Next round started!");
			setTime(mode.getRound()*60*20);
		} else {
			endGameTime();
		}
	}
	
	/**
	 * Starts the match!
	 */
	public void startGame() {
		 startingtime = System.currentTimeMillis();
		 state = gameState.PLAYING;
		
		CheckTimeScheduler();
		timeScheduler();
		
		broadcast("The match has started! Enjoy");
		
		//endgame scheduler
		finaltime_scheduler = Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(FPSCaste.getInstance(), new Runnable() {
			@Override
			public void run() {
				endGameTime();
			}
		}, playtime);
	
		 //unfreeze all the players
		for (String name : players.keySet()) {
			if (!FPSCaste.getFPSPlayer(name).getTeam().equals(teamName.SPECTATOR)){
				FPSCaste.getFPSPlayer(name).unFreeze();
			}
		}
	}
	
	public void endGameTime(){
		broadcast("The game has ended because of the time! (" + getPlayTime() + ")");
		if (mode.isTie()){
			if (mode.getCurrentWinner().equals("nobody")){
				endGame("Its a tie! Nobody even scored a point... Lazy basterds!!", true);
			} else {
				endGame("Its a tie! The current winners are: " + mode.getCurrentWinner() + " with " + mode.getTopScore() + " points!", true);
			}
		} else {
			endGame("The current winner is: " + mode.getCurrentWinner() + " with " + mode.getTopScore() + " points!", true);
		}
	}
	
	
	/**
	 * Ends this match.
	 * @param message 
	 */
	public void endGame(final String message, final boolean newgame){
		state = gameState.ENDING;
		Bukkit.getServer().getScheduler().cancelTask(scheduler);
		Bukkit.getServer().getScheduler().cancelTask(finaltime_scheduler);
		rollBack();
		
		int matchtime = getMode().GetMatchTime()  <= 20 ? getMode().GetMatchTime() : 20;
		if (Integer.parseInt(getPlayTime().split(":")[0]) != getMode().GetMatchTime()){
			matchtime = Integer.parseInt(getPlayTime().split(":")[0]) + 1;
		}
		
		for (String name : players.keySet()){
			FPSPlayer p = FPSCaste.getFPSPlayer(name);
			
			/**
			 * ======================
			 * MATCHBONUS CALCULATION
			 * ======================
			 */
			p.Freeze();
			p.savePlayerData();
			double winnerScale = 0.5;
			if (!mode.isFFA() && !mode.isTie() && mode.getCurrentWinner().contains(p.getTeam().name())){
				winnerScale = 1;
			} else if (mode.isFFA()){
				if (p.getKills() == mode.GetMaxPoints()){
					if (mode.isTie()){
						winnerScale = 0.75;
					} else {
						winnerScale = 1;
					}
				}
			} else if (mode.isTie()){
				if (mode.getCurrentWinner().contains(p.getTeam().name())){
					winnerScale = 0.75;
				}
			}
			
			double spm = (p.getRank() * 0.61 + 1) / 2 + 3;
			int playtime = p.getPlaytime();
			int playerScore = (int) Math.round((winnerScale * (matchtime * spm))*(playtime / matchtime));
			p.goodMsg("Here is your matchbonus!");
			p.givePoints(playerScore);
		}
		
		broadcast(message);
		
		final List<String> playerList = new LinkedList<String>(players.keySet());
		
		if (newgame){
			broadcast("Switching to a new map in " + switchWaidTime + " seconds!");
			Counter("Switching to a new map in %counter seconds!", "Switching to a new map!", switchWaidTime, new Runnable() {
				@Override
				public void run() {
					newMatch();
					for (String player : playerList){
						if (Bukkit.getServer().getPlayer(player) != null && FPSCaste.getFPSPlayer(player).isIngame()){
							players.put(player, teamName.SPECTATOR);
							FPSCaste.getFPSPlayer(player).spawn();
						}
					}
				}
			});
		} else {
			broadcast("Game ending now!");
			for (String name : playerList){
				FPSCaste.getFPSPlayer(name).leave();
			}
			
			currentMatches.remove(matchID);
			openMatches.remove(openMatches.indexOf(matchID));
		}
	}
	
	/**
	 * End with a kill
	 * @param killer
	 * @param death
	 */
	public void endGame(String killer, String death) {
		endGame("The last kill was from " + killer + ", killing " + death + ". Playtime: " + getPlayTime(), true);
	}
	
	public void setPlayList(PlayList list){
		this.list = list;
	}
	
	/**
	 * Boradcasts a message to everyone in this match
	 * @param string The mesage to send
	 */
	public void broadcast(String message) {
		for( String player : getPlayers().keySet()){
			message(player, ChatColor.GOLD + message);
		}
	}
	
	/**
	 * Boradcasts a message to everyone in this match<br/>
	 * The team passed is the team which will get a red message.
	 * @param string The mesage to send
	 */
	public void broadcastTeamGood(String message, teamName team) {
		for (String name : players.keySet()){
			if (players.get(name) == team){
				//Teammate
				message(name, ChatColor.GREEN + message);
			} else {
				//enemy
				message(name, ChatColor.DARK_RED + message);
			}
		}
	}
	
	/**
	 * Boradcasts a message to everyone in this match<br/>
	 * The team passed is the team which will get a red message.
	 * @param string The mesage to send
	 */
	public void broadcastTeamBad(String message, teamName team) {
		for (String name : players.keySet()){
			if (players.get(name) == team){
				//Teammate
				message(name, ChatColor.DARK_RED + message);
			} else {
				//enemy
				message(name, ChatColor.GREEN + message);
			}
		}
	}
	
	/**
	 * Sends a message to the player defined
	 * @param name The plaeyr to send the message to
	 * @param message The message to send
	 */
	public void message(String name, String message){
		if (Bukkit.getServer().getPlayer(name) != null){
			Bukkit.getServer().getPlayer(name).sendMessage(message);
		}
	}
	
	/**
	 * Gets all the players inside this match
	 * @return LinkedList<String> holding all the playernames inside this match
	 */
	public HashMap<String, teamName> getPlayers() {
		return players;
	}
	
	/**
	 * Adds the given player to the match
	 * @param playername the player to add
	 */
	public void addPlayer(String playername){
		players.put(playername, teamName.SPECTATOR);
		currentPlayers ++;
		if (maxPlayers == currentPlayers){
			//full now
			openMatches.remove(matchID);
		} 
		
		//enderdragon timer
		if (currentPlayers == 1 && state.equals(gameState.PREGAME)){
			Counter("Starting in %counter seconds!", "Starting now!", switchWaidTime, "startGame");
		}
		mode.playerJoin(playername);
	}
	
	/**
	 * removes the given player to the match
	 * @param playername the player to remove
	 */
	public void removePlayer(String playername){
		if (players.get(playername) == teamName.ALLIES){
			allies--;
		} else if (players.get(playername) == teamName.AXIS){
			axis--;
		}
		
		mode.playerLeave(playername);
		players.remove(playername);
		currentPlayers -= currentPlayers;
		broadcast(playername + " has left the game.");
		if (currentPlayers == 1){
			if (currentMatches.size() > 1){
				//removes it from the list
				
				currentMatches.remove(matchID);
				openMatches.remove(matchID);
			} else {
				addToAvailable();
				//do nothing, because we want atleast 1 map running all the time
			}
		} else {
			addToAvailable();
		}
	}
	
	/**
	 * Adds the current match to the openMatches list
	 * Fails silently if it is allready in.
	 */
	private void addToAvailable(){
		if (!openMatches.contains(matchID)){
			openMatches.add(matchID);
		}
	}

	/**
	 * Returns the map isntance this match is currently running on
	 * @return Map instance
	 */
	public Map getMap() {
		return FPSCaste.getMap(mapID);
	}
	
	/**
	 * returns the matchID form this match
	 * @return this match' matchID
	 */
	public int getMatchID(){
		return matchID;
	}
	
	public void add(teamName teamname, String player) {
		players.put(player, teamname);
		playerStartTime.put(player, timeLeft());
		if (teamname.equals(teamName.ALLIES)){
			allies++;
		} else if (teamname.equals(teamName.AXIS)){
			axis++;
		}
		
		for (String name : players.keySet()){
			if (Bukkit.getServer().getPlayer(name) != null){
				if (!name.equals(player) && FPSCaste.useTag()){
					TagAPI.refreshPlayer(Bukkit.getServer().getPlayer(name), Bukkit.getServer().getPlayer(player));
					TagAPI.refreshPlayer(Bukkit.getServer().getPlayer(player), Bukkit.getServer().getPlayer(name));
				}
				FPSCaste.getFPSPlayer(name).updateTab();
			}
		}
	}

	/**
	 * Returns the match's game state (STARTING, PLAYING, ENDING)
	 * @return the gamestate
	 */
	public gameState getState() {
		return state;
	}

	/**
	 * Sets this match's gamestate
	 * @param state (STARTING, PLAYING, ENDING)
	 */
	public void setState(gameState state) {
		this.state = state;
	}

	public teamName getPlayerTeam(String player) {
		return players.get(player);
	}
	
	/**
	 * Gives the amount of players in the allies team 
	 * @return a value beteen 0 and max team players
	 */
	public int getAllies(){
		return allies;
	}
	
	/**
	 * Gives the amount of players in the axis team 
	 * @return a value beteen 0 and max team players
	 */
	public int getAxis(){
		return axis;
	}

	/**
	 * Returns all the enemies of this player
	 * @param team
	 * @return a List<String> filled with the names
	 */
	public List<String> getEnemies(String player) {
		List<String> EnemyPlayers = new LinkedList<String>();
		teamName playerteam = getPlayerTeam(player);
		
		if (mode.isFFA()){
			EnemyPlayers.addAll(players.keySet());
			EnemyPlayers.remove(player);
			return EnemyPlayers;
		}
		
		for(String name : players.keySet()){
			if (!players.get(name).equals(playerteam)){
				EnemyPlayers.add(name);
			}
		}
		return EnemyPlayers;
	}
	
	/**
	 * Returns this team
	 * @param team the team to get
	 * @return a List<String> filled with the names
	 */
	public List<String> getTeam(teamName team) {
		List<String> teammates = new LinkedList<String>();
		for(String name : players.keySet()){
			if (players.get(name).equals(team)){
				teammates.add(name);
			}
		}
		return teammates;
	}
	
	public void breakOneBlock(Block hitBlock) {
		try{
			breakableBlocks.valueOf(hitBlock.getType().toString());
			brokenBlocks.put(hitBlock, hitBlock.getType());
			hitBlock.getDrops().clear();
			hitBlock.breakNaturally();
		} catch (IllegalArgumentException Ex){}
	}

	public void break3Blocks(Block hitBlock) {
		breakOneBlock(hitBlock);
		breakOneBlock(hitBlock.getRelative(BlockFace.DOWN));
		breakOneBlock(hitBlock.getRelative(BlockFace.UP));
	}
	
	/**
	 * Breaks blocks in a 3x3x3
	 * @param hitBlock the center block
	 */
	public void break27Blocks(Block hitBlock) {
		break3Blocks(hitBlock);
		break3Blocks(hitBlock.getRelative(BlockFace.NORTH));
		break3Blocks(hitBlock.getRelative(BlockFace.NORTH).getRelative(BlockFace.EAST));
		
		break3Blocks(hitBlock.getRelative(BlockFace.EAST));
		break3Blocks(hitBlock.getRelative(BlockFace.EAST).getRelative(BlockFace.SOUTH));
		
		break3Blocks(hitBlock.getRelative(BlockFace.SOUTH));
		break3Blocks(hitBlock.getRelative(BlockFace.SOUTH).getRelative(BlockFace.WEST));
		
		break3Blocks(hitBlock.getRelative(BlockFace.WEST));
		break3Blocks(hitBlock.getRelative(BlockFace.WEST).getRelative(BlockFace.NORTH));
	}
	
	
	
	/**
	 * Rollback every broken block in this match
	 */
	public void rollBack(){
		for (Block block : brokenBlocks.keySet()){
			block.setType(brokenBlocks.get(block));
		}
		brokenBlocks.clear();
	}
	
	/**
	 * Returns the playtime
	 * @return String [minutes]:[seconds]
	 */
	public String getPlayTime(){
		
		long time = System.currentTimeMillis() - startingtime;
		
		String seconds = time/1000 % 60 + "";
		String minutes = time/1000/60 + "";
		
			if (seconds.length() < 2){
				seconds = "0" + seconds;
			}
		
			if (minutes.length() < 2){
				minutes = "0" + minutes;
			}
		
		return minutes + ":" + seconds; 
	}
	
	/**
	 * makes the time into a nice to read string
	 * @return String [minutes]:[seconds]
	 */
	public String getTimeLeft(){
		//mili time the game has played
		if (state.equals(gameState.PREGAME)){
			return mode.getRound() + ":00";
		} else {
			long timeplayed = System.currentTimeMillis() - startingtime;
			
			//mili  time the game has left
			long timeleft = endTime() - timeplayed - startingtime;
			
			String minutes = (int) Math.ceil(timeleft/1000/60) + "";
			String seconds = (int) Math.ceil((timeleft/1000) % 60) + "";
			
			if (minutes.length() < 2){
				minutes = "0" + minutes;
			}
			
			if (seconds.length() < 2){
				seconds = "0" + seconds;
			}
			
			return minutes + ":" + seconds;
		}
	}
	
	/**
	 * makes the time into a nice to read string
	 * @return String [minutes]:[seconds]
	 */
	public String getTimeLeftBroadcast(){
		if (CurrentBroadCastValues.isEmpty()){
			return "00:00";
		}
		
		//mili  time the game has left
		long timeleft = CurrentBroadCastValues.get(0)*50;
		
		CurrentBroadCastValues.get(0);
		
		String minutes = (int) Math.ceil(timeleft/1000/60) + "";
		String seconds = (int) Math.ceil((timeleft/1000) % 60) + "";
		
		if (minutes.length() < 2){
			minutes = "0" + minutes;
		}
		
		if (seconds.length() < 2){
			seconds = "0" + seconds;
		}
		
		return minutes + ":" + seconds;
	}
	
	
	
	/**
	 * Returns the time left to play
	 * @return The time in miliseconds
	 */
	private long timeLeft() {
		long time = (startingtime + playtime*50) - (System.currentTimeMillis() - startingtime) - startingtime;
		return time;
	}
	
	/**
	 * Returns the ending time in milliseconds
	 * @return startingtime + playtime*50
	 */
	private long endTime(){
		return startingtime + playtime*50;
	}
	
	/**
	 * Sets the time in ticks.
	 * @param time in ticks
	 */
	public void setTime(int time){
		startingtime = System.currentTimeMillis();
		playtime = time;
		Bukkit.getServer().getScheduler().cancelTask(scheduler);
		CurrentBroadCastValues = new LinkedList<Integer>(broadCastValues);
		CheckTimeScheduler();
		timeScheduler();
	}
	
	/**
	 * Checks the values and removes it when they were in the past
	 */
	private void CheckTimeScheduler() {
		for (int i=CurrentBroadCastValues.size()-1; i>=0 ; i--){
			if (CurrentBroadCastValues.get(i) >= timeLeft()/50){
				System.out.println("removed " + CurrentBroadCastValues.get(i) + " " + timeLeft()/50);
				CurrentBroadCastValues.remove(i);
			} else {
				return;
			}
		}
	}
	
	/**
	 * Creates a new timeScheduler for the next broadcast time.
	 */
	public void timeScheduler(){
		if (CurrentBroadCastValues.isEmpty()){
			return;
		} 
		
		/**
		 * ticks to millis
		 */
		long nextBroadcastTime = CurrentBroadCastValues.get(0)*50;
		long timeLeft = timeLeft();
		long schedulertime = (int) Math.ceil(((timeLeft - nextBroadcastTime))/50);
		System.out.println(schedulertime + " " + timeLeft + " " + nextBroadcastTime);
		scheduler = Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(FPSCaste.getInstance(), new Runnable() {
			@Override
			public void run() {
				broadcast("Game time left: " + getTimeLeftBroadcast());
				if (CurrentBroadCastValues.size() > 0){
					CurrentBroadCastValues.remove(0);
					timeScheduler();
				}
			}
		}, schedulertime);
	}
	
	public boolean isHardcore(){
		return mode.isHardcore();
	}
	
	public boolean canDamage(String player1, String player2){
		FPSPlayer p1 = FPSCaste.getFPSPlayer(player1);
		FPSPlayer p2 = FPSCaste.getFPSPlayer(player2);
		
		if (p1 != null && p2 != null && p1.isIngame() && p2.isIngame() && !p1.getTeam().equals(teamName.SPECTATOR) && !p2.getTeam().equals(teamName.SPECTATOR)){
			if (!isHardcore() && !mode.isFFA() && !player1.equals(player2)){
				return !p1.getTeam().equals(p2.getTeam());
			}
			return true;
		}
		return false;
	}
	
	@Override
	public String toString() {
		return matchID + " Running map: " + getMap() + " on gamemode: " + getMode(); 
	}

	/**
	 * Sends the current {@link GameMode} of this match
	 * @return The {@link GameMode} from this map
	 */
	public GameMode getMode() {
		return mode;
	}

	/**
	 * Sends the gameType of this match its gamemode
	 * @return {@link gameType} of this match
	 */
	public gameType getGameMode() {
		return getMode().getType();
	}
	
	/**
	 * Checks whether a player is near a mapObject
	 * @param name the name of the plaeyr to check
	 * @return if he is(true) or not(false)
	 */
	public boolean isNearLocation(String name){
		return mode.isNearMapObject(name, getMap());
	}
	
	/**
	 * Hanldes the right click of a player
	 * @param name the player to check
	 */
	public void handleRightCLickNear(String name){
		if (isNearLocation(name)){
			mode.handleRightClick(name);
		}
	}
	
	/**
	 * Handles the movement of a player.
	 * @param name the player to check
	 */
	public void handleMovement(String name){
		if (isNearLocation(name)){
			mode.handleNear(name);
		} else {
			mode.handleNotNear(name);
		}
	}
	
	/**
	 * Adds a kill, and returns the points<br/>
	 * @param death the death player
	 * @param killer the attacker
	 * @return the points for this kill. and if this is the first kill -> + those points
	 */
	public int handleKill(String attacker, String defender){
		FPSPlayer p1 = FPSCaste.getFPSPlayer(attacker);
		FPSPlayer p2 = FPSCaste.getFPSPlayer(defender);
		int totalPoints = 0;
		
		if (p1 != null && p2 != null && p1.isIngame() && p2.isIngame()){
			int points = mode.handleKill(attacker, defender);
			if (!p1.getTeam().equals(p2.getTeam())){
				if (firstKill){
					firstKill = false;
					broadcastTeamGood("First kill made by " + attacker, p1.getTeam());
					points += Points.FIRST_BLOOD;
				}
			}
			totalPoints = points;
		} else {
			totalPoints =  mode.isHardcore() ? -50 : 0;
		}
		return totalPoints;
	}
	
	public void updateTabList(){
		for (String name : players.keySet()){
			FPSPlayer player = FPSCaste.getFPSPlayer(name);
			if (player != null && player.isIngame() && player.isOnline()){
				player.updateTab();
			}
		}
	}

	/**
	 * Returns a respawn location for the team
	 * @param team The team to look for
	 * @return The location
	 */
	public Location respawn(teamName team) {
		return getMap().randomSpawn(team, this);
	}

	/**
	 * returns the location where a player should goto on his first spawn
	 * @param team The team to look for
	 * @return The best location
	 */
	public Location spawn(teamName team) {
		return getMap().spawn(team, this);
	}
	
	/**
	 * Draws the scoreboard on the side for everyon online player<br/>
	 * Scoreboard obtained from the current gamemode
	 */
	private void drawScoreBoard() {
		for (String name : players.keySet()){
			if (Bukkit.getServer().getPlayer(name) != null){
				Bukkit.getServer().getPlayer(name).setScoreboard(mode.getBoard(name));
			}
		}
	}
	
	public void runMethod(String name){
		try {
			this.getClass().getMethod(name, new Class<?>[0]).invoke(this, new Object[0]);
		} catch (Exception e) {
			FPSCaste.log("running of method: " + name + " went wrong!", Level.WARNING);
		} 
	}

	/**
	 * Runs a counter for everyone on the team
	 * @param used The string to display. Use %counter for the current count
	 * @param done The string to display when its done
	 * @param time The time this runs for, in seconds
	 * @param methodToRun The method to run when the counting is done
	 */
	public void Counter(final String used, final String done, final int time, final String methodToRun){
		Counter(used, done, time, new Runnable() {
			@Override
			public void run() {
				runMethod(methodToRun);
			}
		});
	}
	
	private void Counter(final String used, final String done, final int time, final Runnable runnable) {
		new BukkitRunnable() {
			private int counter = time;
			@Override
			public void run() {
				String message = used.replaceAll("%counter", counter + "");
				if (counter == 0){
					message = done;
					runnable.run();
				}
				if (counter == -1){
					cancel();
				} else {
					for (String name : getPlayers().keySet()){
						FPSCaste.getFPSPlayer(name).createTextBar(message, Math.round(100/switchWaidTime * counter));
					}
				}
				counter--;
			}
		}.runTaskTimer(FPSCaste.getInstance(), 0, 20);
	}
}
