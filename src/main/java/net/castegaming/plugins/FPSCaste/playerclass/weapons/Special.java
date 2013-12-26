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
	
	@SuppressWarnings("unchecked")
	public Special(int id, String name, GunClass type, String group, int delay, double damage, int level, int amount, Map<String, Object> args) {
		super(id, name, type, group, delay, damage, level, amount);
		trowSound = Sound.valueOf(Parse.parseString(args.get("trowsound").toString(), "CHICKEN_EGG_POP"));
		explodeSound = Sound.valueOf(Parse.parseString(args.get("explodesound").toString(), "EXPLODE"));
		explodeEffect = Effect.valueOf(Parse.parseString(args.get("explodeeffect").toString(), "SMOKE"));
		
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

	public void setEffects(List<PotionEffect> effects) {
		this.effects = effects;
	}

	@Override
	public void useRight(FPSPlayer p) {
		final String name = p.getName();
		final List<String> enemies = p.getMatch().getEnemies(name);
		if (!enemies.contains(name)){
			enemies.add(name);
		}
		
        final Item stun = p.getPlayer().getWorld().dropItem(p.getPlayer().getEyeLocation(), new ItemStack(getItemID()));
        stun.setVelocity(p.getPlayer().getEyeLocation().getDirection());
        
        p.getPlayer().getWorld().playSound(p.getPlayer().getLocation(), trowSound, 2f, 1f);
        
        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(FPSCaste.getInstance(), new Runnable() {
			@Override
			public void run() {
				stun.getWorld().playEffect(stun.getLocation(), explodeEffect, 5);
                stun.getWorld().playSound(stun.getLocation(), explodeSound, 1f, 2.5f);
                new Explosion(stun, 5, WeaponContainer.getWeapon(getName()), name, getDamage(), false, effects);
//                for(String player : enemies){
//                	Player p = Bukkit.getServer().getPlayer(player);
//                    if(p.getLocation().distance(stun.getLocation())<5){
//                    	for (PotionEffect effect : effects){
//                    		p.addPotionEffect(effect);
//                    	}
//                    }
//                }
                stun.remove();
			}
		}, 70L);
	}
//        if (p.getPlayer().getItemInHand().getAmount() > 1){
//        	int amount = player.getItemInHand().getAmount() - 1;
//        	ItemStack newStun = new ItemStack(gunItemID, amount);
//        	newStun = setSpecialsName(newStun);
//        	
//        	player.getInventory().setItem(slotSpot, newStun);
//        } else {
//        	player.getInventory().clear(slotSpot);
//        }
//        player.updateInventory();

	@Override
	public void useLeft(FPSPlayer p) {
		// TODO Auto-generated method stub
		
	}

}
