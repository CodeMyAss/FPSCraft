package net.castegaming.plugins.FPSCaste.gamemodes;

import java.util.HashMap;
import java.util.Set;

import net.castegaming.plugins.FPSCaste.FPSCaste;
import net.castegaming.plugins.FPSCaste.FPSPlayer;
import net.castegaming.plugins.FPSCaste.Match;
import net.castegaming.plugins.FPSCaste.enums.Points;
import net.castegaming.plugins.FPSCaste.enums.gameType;
import net.castegaming.plugins.FPSCaste.map.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

public class FreeForAll extends GameMode {

	HashMap<String, Scoreboard> pBoards = new HashMap<String, Scoreboard>();
	private int topScore;
	
	public FreeForAll() {
		super(gameType.FFA, 10, 30);
	}
	
	//TODO make scoreboard for nr1 kills and your kills
	
	@Override
	protected HashMap<Block, Material> checkGenerate(Map map) {
		return new HashMap<Block, Material>();
	}

	@Override
	public boolean isFFA() {
		return true;
	}

	@Override
	public void handleNear(String name) {

	}

	@Override
	public void handleNotNear(String name) {

	}

	@Override
	public void handleRightClick(String name) {
		
	}

	@Override
	public boolean isHardcore() {
		return false;
	}

	@Override
	public void init(Map map) {
		
	}

	@Override
	public int handleKill(String attacker, String defender) {
		if (FPSCaste.getFPSPlayer(attacker).getKills() > topScore){
			topScore++;
			for (String name : pBoards.keySet()){
				pBoards.get(name).getObjective(DisplaySlot.SIDEBAR).getScore(Bukkit.getServer().getOfflinePlayer(ChatColor.DARK_RED + "#1:")).setScore(topScore);
			}
		}
		pBoards.get(attacker).getObjective(DisplaySlot.SIDEBAR).getScore(Bukkit.getServer().getOfflinePlayer(ChatColor.GREEN + "You:")).setScore(FPSCaste.getFPSPlayer(attacker).getKills());
		
		if (topScore == GetMaxPoints()){
			ended = true;
			endedReason = "The game has ended! " + attacker + " won! Last death was " + defender + "!";
		}
		
		return Points.KILL_TDM;
	}

	@Override
	public void handleAssist(String attacker, String defender) {
		
	}

	@Override
	public void second(Match match) {
		
	}
	
	@Override
	public Scoreboard getBoard(String name) {
		return pBoards.get(name) != null ? pBoards.get(name) : clean;
	}
	
	@Override
	public int getScore(String name) {
		return FPSCaste.getFPSPlayer(name).getKills();
	}
	
	@Override
	public int getTopScore() {
		int mostKills = 0;
		
		for (String name : pBoards.keySet()){
			FPSPlayer p = FPSCaste.getFPSPlayer(name);
			if (p.getKills() > mostKills){
				mostKills = p.getKills();
			}
		}
		return mostKills;
	}
	
	@Override
	public boolean isTie() {
		int mostKills = 0;
		boolean tie = false;
		
		for (String name : pBoards.keySet()){
			FPSPlayer p = FPSCaste.getFPSPlayer(name);
			if (p.getKills() > mostKills){
				mostKills = p.getKills();
			} else if (p.getKills() == mostKills){
				tie = true;
			}
		}
		return tie;
	}
	
	@Override
	public void playerJoin(String pname) {
		if (!pBoards.containsKey(pname)){
			ScoreboardManager manager = Bukkit.getScoreboardManager();
			Scoreboard board = manager.getNewScoreboard();
			Objective object = board.registerNewObjective("points", "dummy");
			object.setDisplaySlot(DisplaySlot.SIDEBAR);
			object.setDisplayName(ChatColor.GOLD + "Kills");
			object.getScore(Bukkit.getServer().getOfflinePlayer(ChatColor.AQUA + "Limit:")).setScore(GetMaxPoints());
			object.getScore(Bukkit.getServer().getOfflinePlayer(ChatColor.GREEN + "You:")).setScore(0);
			object.getScore(Bukkit.getServer().getOfflinePlayer(ChatColor.DARK_RED + "#1:")).setScore(topScore);
			pBoards.put(pname, board);
		}
	}
	
	@Override
	public void playerLeave(String player) {
		pBoards.remove(player);
		
		if (FPSCaste.getFPSPlayer(player).getKills() == topScore){
			topScore = 0;
			if (FPSCaste.getFPSPlayer(player).getMatch() != null){
				Set<String> players = FPSCaste.getFPSPlayer(player).getMatch().getPlayers().keySet();
				for (String name : players){
					if (name != player){
						if (FPSCaste.getFPSPlayer(name).getKills() > topScore) topScore = FPSCaste.getFPSPlayer(name).getKills();
					}
				}
			}
		}
	}
}
