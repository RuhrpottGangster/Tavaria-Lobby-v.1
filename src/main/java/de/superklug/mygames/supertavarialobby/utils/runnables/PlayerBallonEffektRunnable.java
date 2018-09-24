package de.superklug.mygames.supertavarialobby.utils.runnables;

import de.superklug.mygames.supertavarialobby.Lobby;
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.scheduler.BukkitRunnable;

public class PlayerBallonEffektRunnable extends BukkitRunnable {
    
    private final Lobby module;

    public PlayerBallonEffektRunnable(final Lobby module) {
        this.module = module;
    }
    
    @Override
    public void run() {
        try {
            
            module.getPlayerBallonBlock().values().forEach((fallingBlocks) -> {
                Bukkit.getOnlinePlayers().forEach((players) -> {
                    players.spigot().playEffect(fallingBlocks.getLocation(), Effect.SNOW_SHOVEL, 1, 1, 0, 0, 0, (float) 0.1, 16, 16);
                });
            });
            
        } catch(Exception exception) {}
    }

}
