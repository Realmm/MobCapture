package me.realmm.mobcapture;

import me.realmm.mobcapture.utils.Messages;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.projectiles.ProjectileSource;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by James on 04-Jul-17.
 */
public class MobCapture extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent e) {
        if (e.getDamager().getType() != EntityType.EGG) return;
        Projectile egg = (Projectile) e.getDamager();
        ProjectileSource projectileSource = egg.getShooter();
        if (!(projectileSource instanceof Entity)) return;

        if (((Entity) projectileSource).getType() != EntityType.PLAYER) return;

        Player p = (Player) egg.getShooter();
        Entity mob = e.getEntity();

        if (!(p.getLocation().distance(mob.getLocation()) >= 10)) {
            p.sendMessage(Messages.NOT_FAR_ENOUGH);
            return;
        }

        if (!(mob instanceof MushroomCow) &&
                !(mob instanceof Blaze) &&
                !(mob instanceof CaveSpider) &&
                !(mob instanceof Creeper) &&
                !(mob instanceof Endermite) &&
                !(mob instanceof Ghast) &&
                !(mob instanceof MagmaCube) &&
                !(mob instanceof Silverfish) &&
                !(mob instanceof Skeleton) &&
                !(mob instanceof Slime) &&
                !(mob instanceof Spider) &&
                !(mob instanceof Witch)) return;

        evaluate(p, mob);

    }

    private void evaluate(Player p, Entity mob) {
        int random = ThreadLocalRandom.current().nextInt(1, 100);
        if (p.getLevel() >= 35) {
            setExpToZero(p);
            dropEgg(mob);
            p.sendMessage(Messages.SUCCESS);
            return;
        }
        if (p.getLevel() >= 10) {
            setExpToZero(p);
            if (random <= 10) {
                dropEgg(mob);
                p.sendMessage(Messages.SUCCESS);
                return;
            }
            p.sendMessage(Messages.FAILURE);
            return;
        }
        p.sendMessage(Messages.LOW_LEVEL);
    }

    private void setExpToZero(Player p) {
        p.setLevel(0);
        p.setExp(0);
    }

    private void dropEgg(Entity mob) {
        short data = mob.getType().getTypeId();
        mob.getWorld().dropItem(mob.getLocation(), new ItemStack(Material.MONSTER_EGG, 1, data));
        mob.remove();
    }

}
