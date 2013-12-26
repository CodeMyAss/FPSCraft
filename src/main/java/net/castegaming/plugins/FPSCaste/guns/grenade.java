package net.castegaming.plugins.FPSCaste.guns;

import net.castegaming.plugins.FPSCaste.FPSCaste;
import net.castegaming.plugins.FPSCaste.util.Explosion;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;

public class grenade extends AbstractGun{

	@SuppressWarnings("static-access")
	public grenade(String gunholder) {
		super(gunholder);
		super.amountOfGun = 2;
		super.clipSize = 1;
		super.gunItemID = gunName.GRENADE.gunID;
		super.gunName = gunName.GRENADE;
		super.maxAmmo = 2;
		super.slotSpot = 8;
		super.delay = 400;
	}
	
	@Override
	public void shootGun(){
        final Item grenade = player.getWorld().dropItem(player.getEyeLocation(), new ItemStack(gunItemID));
        grenade.setVelocity(player.getEyeLocation().getDirection().multiply(1.2));
        
        final String name = player.getName();
        playSound(Sound.FUSE, 1f, 0.75f);
        
        ///////////////////////////////////////////////////////////
        //store thrown grenade with Item ID in FPSPlayer location//
        ///////////////////////////////////////////////////////////
        
        if (player.getItemInHand().getAmount() > 1){
        	int amount = player.getItemInHand().getAmount() - 1;
        	ItemStack newGrenade = new ItemStack(gunItemID, amount);
        	newGrenade = setSpecialsName(newGrenade);
        	
        	player.getInventory().setItem(slotSpot, newGrenade);
        } else {
        	player.getInventory().clear(slotSpot);
        }
        
        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(FPSCaste.getInstance(), new Runnable() {
            @Override
            public void run() {
                new Explosion(grenade, 6, gunName, name, 22, true);
                //grenade.getWorld().playSound(grenade.getLocation(), Sound.EXPLODE, 2f, 1f);
                //grenade.playEffect(EntityEffect.WOLF_SMOKE);
                
                grenade.remove();
           }
       }, 70L);
   }
}
