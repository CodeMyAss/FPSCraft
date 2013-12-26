package net.castegaming.plugins.FPSCaste.guns;

import java.util.List;

import net.castegaming.plugins.FPSCaste.FPSCaste;
import net.castegaming.plugins.FPSCaste.util.Explosion;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Sound;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class stun extends AbstractGun{

	@SuppressWarnings("static-access")
	public stun(String gunholder) {
		super(gunholder);
		super.amountOfGun = 2;
		super.clipSize = 1;
		super.gunItemID = gunName.STUN.gunID;
		super.gunName = gunName.STUN;
		super.maxAmmo = 1;
		super.slotSpot = 7;
		super.delay = 400L;
	}
	
	@Override
	public void shootGun(){
		final List<String> enemies = user.getMatch().getEnemies(user.getName());
		if (!enemies.contains(user.getName())){
			enemies.add(user.getName());
		}
		
        final Item stun = player.getWorld().dropItem(player.getEyeLocation(), new ItemStack(gunItemID));
        stun.setVelocity(player.getEyeLocation().getDirection());
        
        playSound(Sound.CHICKEN_EGG_POP, 2f, 1f);
        
        
        if (player.getItemInHand().getAmount() > 1){
        	int amount = player.getItemInHand().getAmount() - 1;
        	ItemStack newStun = new ItemStack(gunItemID, amount);
        	newStun = setSpecialsName(newStun);
        	
        	player.getInventory().setItem(slotSpot, newStun);
        } else {
        	player.getInventory().clear(slotSpot);
        }
        //player.updateInventory();
        
        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(FPSCaste.getInstance(), new Runnable() {
			@Override
			public void run() {
				stun.getWorld().playEffect(stun.getLocation(), Effect.SMOKE, 5);
                stun.getWorld().playSound(stun.getLocation(), Sound.EXPLODE, 1f, 2.5f);
                for(String player : enemies){
                	Player p = Bukkit.getServer().getPlayer(player);
                    if(p != null && p.getLocation().distance(stun.getLocation())<5){
                        p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 140, 2));
                        p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 60, 1));
                    }
                }
                new Explosion(stun, 5, gunName.STUN, user.getName(), 1, false);
                stun.remove();
			}
		}, 70L);
   }
}