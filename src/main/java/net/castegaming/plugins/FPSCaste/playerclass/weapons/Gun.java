package net.castegaming.plugins.FPSCaste.playerclass.weapons;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Map;
import java.util.Random;

import net.castegaming.plugins.FPSCaste.FPSCaste;
import net.castegaming.plugins.FPSCaste.FPSPlayer;
import net.castegaming.plugins.FPSCaste.enums.GunClass;
import net.castegaming.plugins.FPSCaste.util.Parse;

import org.bukkit.ChatColor;
import org.bukkit.entity.Arrow;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.util.Vector;

public class Gun extends WeaponContainer{
	
	private static Random r = new Random();
	
	private boolean reloadable = true;
	
	private int magezine;
	
	private double distance;

	private double spread;

	private int reloadTime;
	
	/**
	 * Creates a new gun
	 * @param id item ID
	 * @param name name of the gun
	 * @param type type of the gun
	 * @param group group of the gun
	 * @param delay the delay between a shot
	 * @param damage damage done by this gun
	 * @param level the level required for this weapon
	 * @param reloadable defines if this gun is reloadable
	 * @param args Map[String, String] Used for additional args.
	 */
	public Gun(int id, String name, GunClass type, String group, int delay, double damage, int level, int amount, Map<String, Object> args) { 
		super(id, name, type, group, delay, damage, level, amount);
		if(args != null){
			this.magezine = Parse.parseInt(args.get("magezine"), 4);
			this.distance = Parse.parseInt(args.get("distance"), 10);
			this.spread = Parse.parseDouble(args.get("spread"), 0.1);
			this.reloadTime = Parse.parseInt(args.get("reloadtime"), 25);
		}
	}
	
	/**
	 * Reloadable is true by default.<br/>
	 * level is set to 1, always
	 * @param id item ID
	 * @param name name of the gun
	 * @param type type of the gun
	 * @param group group of the gun
	 * @param delay the delay between a shot
	 * @param damage damage done by this gun
	 * @param args other shenanigans
	 */
	public Gun(int id, String name, GunClass type, String group, int delay, double damage, int amount, Map<String, Object> args) { 
		this(id, name, type, group, delay, damage, 1, amount, args);
	}
	
	/**
	 * Reloadable is true by default.<br/>
	 * level is set to 1, always<br/>
	 * amount is set to 1
	 * @param id item ID
	 * @param name name of the gun
	 * @param type type of the gun
	 * @param group group of the gun
	 * @param delay the delay between a shot
	 * @param damage damage done by this gun
	 * @param args other shenanigans
	 */
	public Gun(int id, String name, GunClass type, String group, int delay, double damage, Map<String, Object> args) { 
		this(id, name, type, group, delay, damage, 1, 1, args);
	}
	
	/**
	 * @return the reloadable
	 */
	public boolean isReloadable() {
		return reloadable;
	}
	
	/**
	 * @return the spread
	 */
	public double getSpread() {
		return spread;
	}

	public int getReloadTime() {
		return reloadTime;
	}
	
	@Override
	public ItemStack constructItem() {
		ItemStack item = super.constructItem();
		item.setAmount(getAmount());
		ItemMeta m = item.getItemMeta();
		m.setLore(new LinkedList<String>(Arrays.asList(ChatColor.DARK_PURPLE + "Has " + magezine + " magezines left")));
		item.setItemMeta(m);
		return item;
	}

	@Override
	public void useRight(FPSPlayer p) {
		Vector velocity = p.getPlayer().getEyeLocation().getDirection().multiply(distance/10); 
		double spread = this.spread;
		
        if (!p.isZooming())spread /= 3;
        
        changeDirection(velocity, spread/3);
        
        Arrow arrow = p.getPlayer().launchProjectile(Arrow.class); 
        arrow.setMetadata("FPSweapon", new FixedMetadataValue(FPSCaste.getInstance(), getName()));
        arrow.setVelocity(velocity); 
	}

	@Override
	public void useLeft(FPSPlayer p) {
		p.zoom();
	}
	
	private Vector changeDirection(Vector v, double Spread){
		double modifyX = r.nextDouble() * Spread;
		double modifyY = r.nextDouble() * Spread ;
		if (modifyX == 1){
			
		}
		return v;
	}
}
