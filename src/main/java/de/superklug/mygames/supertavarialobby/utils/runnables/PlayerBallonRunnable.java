package de.superklug.mygames.supertavarialobby.utils.runnables;

import de.superklug.mygames.supertavarialobby.Lobby;
import org.bukkit.Bukkit;
import org.bukkit.entity.Bat;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class PlayerBallonRunnable extends BukkitRunnable {
    
    private final Lobby module;

    public PlayerBallonRunnable(final Lobby module) {
        this.module = module;
    }

    @Override
    public void run() {
        try {
            
            Bukkit.getOnlinePlayers().forEach((players) -> {
                if (module.getLobbyData(players) != null && module.getLobbyData(players).getActivePremiumGadgets().contains("ballon")) {

                    if (module.getPlayerBallonBlock().containsKey(players)) {
                        module.getPlayerBallonBlock().get(players).setTicksLived(1);

                        if (module.getPlayerBallonBlock().get(players).isDead()) {

                            module.getPlayerBallonBlock().get(players).remove();
                            module.getPlayerBallonBlock().remove(players);

                            if (module.getPlayerBallonBat().containsKey(players)) {
                                module.getPlayerBallonBat().get(players).remove();
                                module.getPlayerBallonBat().remove(players);
                            }

                            module.spawnBallon(players);

                        }

                    }

                    if (module.getPlayerBallonBat().containsKey(players)) {
                        Bat bat = module.getPlayerBallonBat().get(players);

                        if (bat.getLocation().distance(players.getLocation()) >= 2) {
                            Vector v = new Vector(players.getLocation().getX() - bat.getLocation().getX(), players.getLocation().getY() - bat.getLocation().getY(), players.getLocation().getZ() - bat.getLocation().getZ()).normalize().multiply(0.5);
                            bat.setVelocity(v);
                        } else {
                            bat.setVelocity(new Vector(0, 0, 0));
                        }
                        if (players.getEyeLocation().getY() - bat.getLocation().getY() >= 0.25) {
                            bat.setVelocity(new Vector(0, 0.5, 0));
                        }
                        if (bat.getLocation().distance(players.getLocation()) >= 8) {
                            Vector v = new Vector(players.getLocation().getX() - bat.getLocation().getX(), players.getLocation().getY() - bat.getLocation().getY(), players.getLocation().getZ() - bat.getLocation().getZ()).normalize().multiply(1.25);
                            bat.setVelocity(v);
                        }

                    }

                }
            });
            
        } catch(Exception exception) {}
    }

}
