package net.castegaming.plugins.FPSCaste.gamemodes;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Set;

import net.castegaming.plugins.FPSCaste.FPSCaste;
import net.castegaming.plugins.FPSCaste.FPSPlayer;
import net.castegaming.plugins.FPSCaste.Match;
import net.castegaming.plugins.FPSCaste.enums.GameModes;
import net.castegaming.plugins.FPSCaste.enums.Points;
import net.castegaming.plugins.FPSCaste.enums.gameType;
import net.castegaming.plugins.FPSCaste.enums.teamName;
import net.castegaming.plugins.FPSCaste.gamemodes.gameobjects.Flag;
import net.castegaming.plugins.FPSCaste.gamemodes.gameobjects.GameObject;
import net.castegaming.plugins.FPSCaste.map.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

public abstract class GameMode {
	
	public static final Class<? extends GameMode> TDM = TeamDeathmatch.class;
	public static final Class<? extends GameMode> CTF = CaputeTheFlag.class;
	public static final Class<? extends GameMode> FFA = FreeForAll.class;
	public static final Class<? extends GameMode> DOM = Domination.class;
	public static final Class<? extends GameMode> DEM = Demolition.class;
	
	public static GameMode random(){
		return GameModes.values()[new Random().nextInt(GameModes.values().length-1)].getMode();
	}
	
	/*==============================================================*/
	
	private gameType type;
	private List<Integer> roundLength;
	private int scoreMax;
	private boolean hardcore = false;
	protected Map map;
	protected String endedReason = "";
	protected boolean ended = false;
	protected Scoreboard clean = Bukkit.getScoreboardManager().getNewScoreboard();
	
	protected HashMap<teamName, Integer> points;
	protected List<GameObject> gameObjects;
	protected HashMap<teamName, Scoreboard> boards;
	
	
	/**
	 * Default constructor for a gamemode
	 * @param type The {@link gameType} for this gamemode
	 * @param minutes The length of a full match in minutes
	 * @param scoreMax The maximum score of this match
	 * @param m Whether this match is hardcore or not
	 */
	protected GameMode(gameType type, int minutes, int scoreMax) {
		this.type = type;
		this.roundLength = new LinkedList<Integer>();
		roundLength.add(minutes);
		this.scoreMax = scoreMax;
		
		points = new HashMap<teamName, Integer>();
		boards = new HashMap<teamName, Scoreboard>();
		gameObjects = new LinkedList<GameObject>();
	}
	
	protected abstract HashMap<Block, Material> checkGenerate(Map map);

	/**
	 * Returns the blocks needed for this gamemode in the map defined
	 * @param map The map to check for
	 * @return a HashMap filled with Block and Material
	 */
	public HashMap<Block, Material> load(Map map) {
		HashMap<Block, Material> block = checkGenerate(map);
		return block;
	}

	/**
	 * Sends the {@link gameType} enum of this gamemode
	 * @return the {@link gameType}
	 */
	public gameType getType() {
		return type;
	}
	
	/**
	 * Sends the time of a full match in minutes.
	 * @return time in minutes
	 */
	public int GetMatchTime() {
		return getRound(); 
	}
	
	/**
	 * Sends the time of a full match in ticks.
	 * @return time in ticks
	 */
	public int getTicks() {
		return getRound()*60*20;
	}
	
	/**
	 * Sends the time of a full current round in milliseconds.
	 * @return tiem in milliseconds
	 */
	public int getMilli() {
		return getRound()*60*1000;
	}
	
	/**
	 * Sends the max score of this gamemode
	 * @return max points
	 */
	public int GetMaxPoints() {
		return scoreMax;
	}
	
	public void setHardcore(boolean hardcore){
		this.hardcore = hardcore;
	}

	@Override
	public String toString() {
		return type + " Round Length: " + getRound() + " minutes and a max score of " + scoreMax;
	}

	public abstract void handleNear(String name);
	
	/**
	 * Handles waht todo when a player is not near 
	 * @param name
	 */
	public abstract void handleNotNear(String name);
	
	/**
	 * Main constructor. Mainly for location and game objects<br/>
	 * By default adds Allies and axis, and creates 3 boards. (allies, axis, spec)<br/>
	 * - @OVERRIDE if you want to have something fancy
	 * @param map The map this GameMode is linked to
	 */
	public void init(Map map){
		this.map = map;
		addTeam(teamName.ALLIES);
		addTeam(teamName.AXIS);
		
		NewBoard(ChatColor.AQUA, ChatColor.GRAY, ChatColor.GRAY, teamName.SPECTATOR);
		NewBoard(ChatColor.AQUA, ChatColor.GREEN, ChatColor.DARK_RED, teamName.ALLIES);
		NewBoard(ChatColor.AQUA, ChatColor.DARK_RED, ChatColor.GREEN, teamName.AXIS);
	}
	
	/**
	 * Defines what todo when a player kills/dies<br/>
	 * OVERRIDE THIS IF YOU WANT CUSTOM THINGS
	 * 
	 * @param attacker
	 * @param defender
	 * @return
	 */
	public  int handleKill(String attacker, String defender){
		return attacker.equals(defender) ? 0 : Points.KILL_TDM;
	}
	
	public abstract void handleAssist(String attacker, String defender);
	
	/**
	 * Handles what todo when a player rightclicks near a MapObject
	 * @param name the name of the player near
	 */
	public abstract void handleRightClick(String name);
	
	public boolean isNearMapObject(String name, Map map){
		if (Bukkit.getServer().getPlayer(name) != null && FPSCaste.getFPSPlayer(name).isIngame()){
			for (GameObject o : gameObjects){
				if (o.isNear(name)){
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * Adds a gameobject to the list.
	 * @param object
	 */
	public void addObject(GameObject object){
		gameObjects.add(object);
	}

	/**
	 * Defines if this gamemode is an FFA mode.
	 * @return
	 */
	public abstract boolean isFFA();

	/**
	 * @return the score of this player (his team), or the highest score in FFA
	 */
	public int getScore(String name) {
		teamName team = FPSCaste.getFPSPlayer(name).getTeam();
		return points.get(team) != null ? points.get(team) : 0;
	}
	
	/**
	 * @return the score of this team
	 */
	public int getScore(teamName team) {
		return points.get(team) != null ? points.get(team) : 0;
	}

	/**
	 * @param score the score to add to the current score
	 */
	public void addScore(int score, teamName team) {
		if (points.get(team) + score >= scoreMax && scoreMax > 0){
			ended = true;
			points.put(team, scoreMax);
		} else {
			points.put(team, score + getScore(team));
		}
		updateBoards(team);
	}
	
	public void addTeam(teamName name){
		points.put(name, 0);
	}
	
	/**
	 * @param Adds 1 to the current score
	 */
	public void plusOneScore(teamName team) {
		addScore(1, team);
	}
	
	/**
	 * Checks if the game has ended
	 * @return If it ended, true, otherwise false
	 */
	public boolean isFinished(){
		return ended;
	}
	
	/**
	 * Returns why this match has ended
	 * @return
	 */
	public String EndedReason(){
		return endedReason;
	}

	/**
	 * Method which gets called every second
	 * @param match The match, for statistics
	 */
	public abstract void second(Match match);
	
	/**
	 * Returns the scoreboard corresponding to the player
	 * @param name The player to get the board of
	 * @return The scoreboard or an empty one
	 */
	public Scoreboard getBoard(String name){
		FPSPlayer p = FPSCaste.getFPSPlayer(name);
		if (p != null && p.isIngame() && boards.get(p.getTeam()) != null){
			return boards.get(p.getTeam());
		} else {
			return clean;
		}
	}
	
	/**
	 * Returns the scoreboard corresponding to the player
	 * @param name The player to get the board of
	 * @return The scoreboard or an empty one
	 */
	public Scoreboard getBoard(teamName team){
		return boards.get(team) != null ? boards.get(team) : clean;
	}
	
	protected void NewBoard(ChatColor c1, ChatColor c2, ChatColor c3, teamName team){
		ScoreboardManager manager = Bukkit.getScoreboardManager();
		Scoreboard board = manager.getNewScoreboard();
		Objective object = board.registerNewObjective(new Random().nextInt() + "", "dummy");
		object.setDisplaySlot(DisplaySlot.SIDEBAR);
		object.setDisplayName(ChatColor.GOLD + "Points");
		
		if (scoreMax > -1){
			object.getScore(Bukkit.getOfflinePlayer(c1 + "Limit:")).setScore(scoreMax);
		}
		
		object.getScore(Bukkit.getOfflinePlayer(c2 + "Allies:")).setScore(points.get(teamName.ALLIES));
		object.getScore(Bukkit.getOfflinePlayer(c3 + "Axis:")).setScore(points.get(teamName.AXIS));
	
		boards.put(team, board);
	}
	
	/**
	 * Updates the default boards, Axis, Allies and Spectator
	 * @param team The team which has received additional points
	 */
	protected void updateBoards(teamName team){
		teamName t1 = teamName.AXIS;
		if (team.equals(teamName.AXIS)) t1 = teamName.ALLIES;
		
		boards.get(team).getObjective(DisplaySlot.SIDEBAR).getScore(Bukkit.getOfflinePlayer(ChatColor.GREEN + team.toString() + ":")).setScore(points.get(team));
		boards.get(t1).getObjective(DisplaySlot.SIDEBAR).getScore(Bukkit.getOfflinePlayer(ChatColor.DARK_RED + team.toString() + ":")).setScore(points.get(team));
		
		boards.get(teamName.SPECTATOR).getObjective(DisplaySlot.SIDEBAR).getScore(Bukkit.getOfflinePlayer(ChatColor.GRAY + team.toString() + ":")).setScore(points.get(team));
	}
	
	/**
	 * Gets called whenever a player leaves
	 * @param name The player who left
	 */
	public abstract void playerLeave(String name);
	
	/**
	 * Gets called whenever a player joins
	 * @param name The player who joined
	 */
	public abstract void playerJoin(String name);
	
	/**
	 * Resets the boards and points
	 */
	public void reset(){
		boards.clear();
		points.clear();
	}

	/**
	 * Metod to cet the current winner.<br/>
	 * If its a tie, will return all the names<br/>
	 * If nobody scored a point, 'nobody' will be returned
	 * @return A string with the winner(winning) team(s)
	 */
	public String getCurrentWinner() {
		int mostPoints = 0;
		teamName currentWinner = null;
		boolean tie = false;
		//boolean scored = false;
		Set<teamName> tieWinnersNames = new HashSet<teamName>();
		
		for (teamName team : points.keySet()){
			if (points.get(team) > mostPoints){
				mostPoints = points.get(team);
				currentWinner = team;
			} else if (points.get(team) == mostPoints){
				tie = true;
				tieWinnersNames.add(team);
				tieWinnersNames.add(currentWinner);
			}
		}
		
		if (tie){
			String winString = "";
			for (teamName team : tieWinnersNames){
				if (getScore(team) > 0){
					winString += ", " + team.name();
				} else {
					winString = "nobody";
				}
			}
			return winString;
		} else {
			return currentWinner != null ? currentWinner.name() : "nobody";
		}
	}

	/**
	 * Returns the current top score
	 * @return The top score, in an int
	 */
	public int getTopScore() {
		int mostPoints = 0;
		
		for (teamName team : points.keySet()){
			if (points.get(team) > mostPoints){
				mostPoints = points.get(team);
			}
		}
		return mostPoints;
	}

	/**
	 * Returns if the status is a tie
	 * @return boolean tie
	 */
	public boolean isTie() {
		int mostPoints = 0;
		boolean tie = false;
		
		for (teamName team : points.keySet()){
			if (points.get(team) > mostPoints){
				mostPoints = points.get(team);
			} else if (points.get(team) == mostPoints){
				tie = true;
			}
		}
		return tie;
	}
	
	/**
	 * Returns the clsoest flag or null.<br/>
	 * Only null if the player is offline, or there are no flags. (this is impossible)<br/>
	 * Previous flags have priority over the next one if the distance is exacly the same.
	 * @param name
	 * @return
	 */
	public Flag getClosest(String name){
		if (Bukkit.getServer().getPlayer(name) != null){
			Location l = Bukkit.getServer().getPlayer(name).getLocation();
			Flag flag = null;
			double distance = 9999;
			
			for (GameObject o : gameObjects){
				if (o instanceof Flag && l.getWorld().equals(o.getLocation().getWorld()) && l.distance(o.getLocation()) <= distance){
					distance = l.distance(o.getLocation());
					flag = (Flag) o;
				}
			}
			return flag;
		}
		return null;
	}
	
	public List<Flag> getFlags(){
		List<Flag> flags = new LinkedList<Flag>();
		for (GameObject o : gameObjects){
			if (o instanceof Flag) {
				
				flags.add((Flag) o);
			}
		}
		return flags;
	}

	public boolean hasNextRound() {
		// TODO Auto-generated method stub
		return false;
	}

	public int getRound() {
		return roundLength.get(0);
	}
	
	public String getRoundTime() {
		return roundLength.get(0) + "";
	}
	
	public void nextRound(){
		roundLength.remove(0);
	}

	public boolean isHardcore() {
		return hardcore;
	}
}
