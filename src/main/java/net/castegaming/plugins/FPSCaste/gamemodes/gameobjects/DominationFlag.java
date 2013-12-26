package net.castegaming.plugins.FPSCaste.gamemodes.gameobjects;

import net.castegaming.plugins.FPSCaste.FPSCaste;
import net.castegaming.plugins.FPSCaste.enums.Points;
import net.castegaming.plugins.FPSCaste.enums.teamName;

import org.bukkit.Location;

public class DominationFlag extends Flag{
	
	public DominationFlag(Location position, String name) {
		super(position, name);
	}

	/**
	 * Whether this flag is captured or in its begin state.
	 * @return If it is captured(true) or not (false)
	 */
	public boolean isCaptured(){
		return !getHolder().equals(teamName.SPECTATOR);
	}

	@Override
	public void onCapture(String startingName, int matchID, teamName team) {
		for (String name : getNearPlayers(matchID)){
			if (!name.equals(startingName)){
				FPSCaste.getFPSPlayer(name).givePoints(Points.CAPTURE_ASSIST);
			} else {
				if (isCaptured()) FPSCaste.getFPSPlayer(startingName).givePoints(Points.CAPTURE_ENEMY);
				else FPSCaste.getFPSPlayer(startingName).givePoints(Points.CAPTURE_NEUTRAL);
			}
		}
		FPSCaste.getMatch(matchID).broadcastTeamGood(startingName + " has captured flag " + getName(), team);
		setHolders(team);
		setCapturing(false);
	}
}
