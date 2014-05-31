package net.castegaming.plugins.FPSCaste.playerclass.weapons;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Sound;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import net.castegaming.plugins.FPSCaste.FPSCaste;
import net.castegaming.plugins.FPSCaste.FPSPlayer;
import net.castegaming.plugins.FPSCaste.enums.GunClass;
import net.castegaming.plugins.FPSCaste.util.Explosion;
import net.castegaming.plugins.FPSCaste.util.Parse;

public class Special extends WeaponContainer {

	private List<PotionEffect> effects = new LinkedList<PotionEffect>();
	private Sound trowSound;
	private Sound explodeSound;
	private Effect explodeEffect;
	private int range;
	
	@SuppressWarnings("unchecked")
	public Special(int id, String name, GunClass type, String group, int delay, double damage, int level, int amount, Map<String, Object> args) {
		super(id, name, type, group, delay, damage, level, amount);
		trowSound = Sound.valueOf(Parse.parseString(args.get("trowsound"), "CHICKEN_EGG_POP"));
		explodeSound = Sound.valueOf(Parse.parseString(args.get("explodesound"), "EXPLODE"));
		explodeEffect = Effect.valueOf(Parse.parseString(args.get("explodeeffect"), "SMOKE"));
		range = Parse.parseInt(args.get("range"), 5);
		
		if (args.get("effects") != null){
			effects = (List<PotionEffect>) args.get("effects");
		}
	}
	
	@Override
	public ItemStack constructItem() {
		ItemStack item = super.constructItem();
		item.setAmount(getAmount());
		return item;
	}

	/**
	 * 
	 * @param effects
	 */
	public void setEffects(List<PotionEffect> effects) {
		this.effects = effects;
	}
	
	/**
	 * @return
	 */
	public List<PotionEffect> getEffecs() {
		return effects;
	}

	@Override
	public void useRight(FPSPlayer p) {
		final String name = p.getName();
		final WeaponContainer w = this;
		
        final Item stun = p.getPlayer().getWorld().dropItem(p.getPlayer().getEyeLocation(), new ItemStack(getItemID()));
        stun.setVelocity(p.getPlayer().getEyeLocation().getDirection());
        
        p.getPlayer().getWorld().playSound(p.getPlayer().getLocation(), trowSound, 2f, 1f);
        
        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(FPSCaste.getInstance(), new Runnable() {
			@Override
			public void run() {
				stun.getWorld().playEffect(stun.getLocation(), explodeEffect, range);
                stun.getWorld().playSound(stun.getLocation(), explodeSound, 1f, 2.5f);
                new Explosion(stun.getLocation(), range, w, name, getDamage(), false, effects);
                stun.remove();
			}
		}, getDelay());
	}

	@Override
	public void useLeft(FPSPlayer p) {
		useRight(p);
	}

	/**
	 * @return the range
	 */
	public int getRange() {
		return range;
	}
}
