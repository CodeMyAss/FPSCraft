package net.castegaming.plugins.FPSCaste.gamemodes.gameobjects;

import net.castegaming.plugins.FPSCaste.FPSCaste;
import net.castegaming.plugins.FPSCaste.enums.teamName;

import org.bukkit.Bukkit;
import org.bukkit.Location;

public class CTFFlag extends Flag{

	private String runner = "";
	
	private Location startingLocation;

	public CTFFlag(Location position, String name) {
		super(position, name);
		startingLocation = position;
	}

	/**
	 * Returns the current player running with this flag, or "" if it is:<br/>
	 * - Laying on the ground<br/>
	 * - Standing on its home
	 * @return the runner
	 */
	public String getRunner() {
		return runner;
	}

	/**
	 * Changes the current runner to the one defined
	 * @param runner the one which wwe are going to change to
	 */
	public void setRunner(String runner) {
		this.runner = runner;
	}
	
	/**
	 * Will drop the flag on the ground.<br/>
	 * This wont do anything if the flag is allready on the ground or at its home
	 */
	public void drop(){
		if (!runner.equals("")){
			FPSCaste.getFPSPlayer(runner).getMatch().broadcast(runner + " has dropped flag " + getName());
			setLocation(Bukkit.getServer().getPlayer(runner).getLocation());
			build();
			runner = "";
		}
	}

	@Override
	public void onCapture(String startingName, int matchID, teamName team) {
		if (getHolder().equals(team)){
			FPSCaste.getFPSPlayer(startingName).givePoints(net.castegaming.plugins.FPSCaste.enums.Points.FLAG_RETURN);
			FPSCaste.getMatch(matchID).broadcastTeamGood(startingName + " returned the flag!", team);
			respawn();
		} else {
			FPSCaste.getFPSPlayer(startingName).givePoints(net.castegaming.plugins.FPSCaste.enums.Points.FLAG_GRAB);
			FPSCaste.getMatch(matchID).broadcastTeamGood(startingName + " took the flag!", team);
			setRunner(startingName);
			remove();
		}
	}

	public void respawn() {
		remove();
		setLocation(startingLocation);
		build();
		runner = "";
	}

	public Location getOriginLocation() {
		return startingLocation;
	}

	public boolean hasRunner() {
		return !runner.equals("");
	}
}
