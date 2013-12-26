package net.castegaming.plugins.FPSCaste.gamemodes.gameobjects;

import java.util.HashMap;

import net.castegaming.plugins.FPSCaste.FPSCaste;
import net.castegaming.plugins.FPSCaste.enums.teamName;

import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.scheduler.BukkitRunnable;

public abstract class Flag extends GameObject{

	private teamName holder;
	private boolean capturing = false;
	protected int captureProgress = 0;
	
	public Flag(Location position, String name) {
		super(position, name);
		setHolders(teamName.SPECTATOR);
		build();
	}

	/**
	 * Sends the current holders of this flag.
	 * @return {@link teamName} the holders
	 */
	public teamName getHolder() {
		return holder;
	}

	/**
	 * @param holder the holders to set in their {@link teamName}
	 * @return 
	 */
	public Flag setHolders(teamName holder) {
		this.holder = holder;
		return this;
	}
	
	@Override
	public HashMap<Block, Material> getBlocks() {
		return blocks;
	}
	
	/**
	 * Checks if this flag is currently beeing captured
	 * @return true if it is
	 */
	public boolean isCapturing(){
		return capturing;
	}
	
	public abstract void onCapture(String startingName, int matchID, teamName Team);

	/**
	 * @param capturing the capturing to set
	 */
	public void setCapturing(boolean capturing) {
		this.capturing = capturing;
		if (!capturing) captureProgress = 0;
	}
	
	@Override
	public void build() {
		Block b = getLocation().getWorld().getBlockAt(getLocation());
		blocks.put(b, b.getType());
		blocks.put(b.getRelative(BlockFace.UP), b.getRelative(BlockFace.UP).getType());
		blocks.put(b.getRelative(BlockFace.UP).getRelative(BlockFace.UP), b.getRelative(BlockFace.UP).getRelative(BlockFace.UP).getType());
	
		b.setType(Material.FENCE);
		b.getRelative(BlockFace.UP).setType(Material.FENCE);
		b.getRelative(BlockFace.UP).getRelative(BlockFace.UP).setTypeIdAndData(Material.WOOL.getId(), DyeColor.GRAY.getWoolData(), true);
	}
	
	public void remove() {
		Block b = getLocation().getWorld().getBlockAt(getLocation());
		if (blocks.get(b) != null){
			b.setType(blocks.get(b));
			b.getRelative(BlockFace.UP).setType(blocks.get(b.getRelative(BlockFace.UP)));
			b.getRelative(BlockFace.UP).getRelative(BlockFace.UP).setType(blocks.get(b.getRelative(BlockFace.UP).getRelative(BlockFace.UP)));
			blocks.remove(b);
			blocks.remove(b.getRelative(BlockFace.UP));
			blocks.remove(b.getRelative(BlockFace.UP).getRelative(BlockFace.UP));
		}
	}
	
	/**
	 * Method for the capturing process of a flag
	 * @param time The time this capture takes, in ticks
	 * @param team The team who tries to capture
	 * @param startingName The person who started the capture
	 * @param text the text to use. %time will be replaced with the percentage left left
	 */
	public void takeOver(final float time, final teamName team, final String startingName, final String text){
		if (!getHolder().equals(team) && !isCapturing()){
			setCapturing(true);
			
			final int matchID = FPSCaste.getFPSPlayer(startingName).getMatch().getMatchID();
			new BukkitRunnable() {
				@Override
				public void run() {
					//if (!getHolder().equals(team)){
						boolean containsTeamMates = false;
						boolean containsEnemies = false;
						
						for (String name : getNearPlayers(matchID)){
							if (FPSCaste.getFPSPlayer(name).getTeam().equals(team)){
								containsTeamMates = true;
							} else {
								containsEnemies = true;
							}
							int total = 100 - (Math.round(100/time * captureProgress % 100));
							FPSCaste.getFPSPlayer(name).createTextBar(text.replaceAll("%time", total+""), total);
						}
						
						if (containsTeamMates){
							if(!containsEnemies){
								if (captureProgress+1 == time){
									setCapturing(false);
									onCapture(startingName, matchID, team);
									cancel();
								} else {
									captureProgress++;
								}
							}
						} else {
							setCapturing(false);
							cancel();
						}
					//}
				}
			}.runTaskTimer(FPSCaste.getInstance(), 0, 1);
		}
	}
}
