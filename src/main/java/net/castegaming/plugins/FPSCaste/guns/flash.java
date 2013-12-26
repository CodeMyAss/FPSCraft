package net.castegaming.plugins.FPSCaste.guns;

import java.util.List;

import net.castegaming.plugins.FPSCaste.FPSCaste;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Sound;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class flash extends AbstractGun{

	@SuppressWarnings("static-access")
	public flash(String gunholder) {
		super(gunholder);
		super.amountOfGun = 5;
		super.clipSize = 1;
		super.gunItemID = gunName.FLASH.gunID;
		super.gunName = gunName.FLASH;
		super.maxAmmo = 1;
		super.slotSpot = 7;
		super.delay = 160L;
	}
	
	@Override
	public void shootGun(){
		final List<String> enemies = user.getMatch().getEnemies(player.getName());
		if (!enemies.contains(player.getName())){
			enemies.add(player.getName());
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
                    if(p.getLocation().distance(stun.getLocation())<5){
                        p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 100, 1));
                    }
                }
                stun.remove();
			}
		}, 70L);
   }
}