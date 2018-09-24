package de.superklug.mygames.supertavarialobby.utils.runnables;

import de.superklug.mygames.supertavarialobby.Lobby;
import java.io.File;
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.scheduler.BukkitRunnable;

public class LocationPartikelRunnable extends BukkitRunnable {
    
    private final Lobby module;

    public LocationPartikelRunnable(final Lobby module) {
        this.module = module;
    }
    
    @Override
    public void run() {
        Bukkit.getOnlinePlayers().forEach((players) -> {
            
            if(module.getSuperAPI().location(new File(module.getDataFolder(), "locations.yml")).doesLocationExists("Spawn")) {
                
                players.spigot().playEffect(module.getSuperAPI().location(new File(module.getDataFolder(), "locations.yml")).getLocation("Spawn"),
                        Effect.INSTANT_SPELL, 1, 1, 0, 0, 0, (float) 0.1, 128, 16);
                
                //module.getSuperAPI().playHelixParticles(module.getSuperAPI().location(new File(module.getDataFolder(), "locations.yml")).getLocation("Spawn"),
                //        2, 10, Effect.FIREWORKS_SPARK, players);
                
                module.getSuperAPI().playLineParticles(module.getSuperAPI().location(new File(module.getDataFolder(), "locations.yml")).getLocation("Spawn"),
                        46, Effect.FIREWORKS_SPARK, players);
                
                players.spigot().playEffect(module.getSuperAPI().location(new File(module.getDataFolder(), "locations.yml")).getLocation("Spawn"),
                        Effect.FLYING_GLYPH, 1, 1, 0, 0, 0, (float) 0.1, 128, 16);
                
            }
            
        });
    }

}
