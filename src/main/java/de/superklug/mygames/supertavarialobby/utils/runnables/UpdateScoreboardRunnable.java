package de.superklug.mygames.supertavarialobby.utils.runnables;

import de.superklug.mygames.supertavarialobby.Lobby;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

public class UpdateScoreboardRunnable extends BukkitRunnable {
    
    private final Lobby module;

    public UpdateScoreboardRunnable(final Lobby module) {
        this.module = module;
    }

    @Override
    public void run() {
        try {
            
            Bukkit.getOnlinePlayers().forEach((players) -> {
                if(players.getScoreboard() != null) {
                    if(module.getUserData(players) != null) {
                        int seconds = module.getUserData(players).getPlaytime();

                        int hours = 0;
                        int minutes = 0;

                        while(seconds >= 60) {
                            minutes++;
                            seconds = seconds - 60;
                        }

                        while(minutes >= 60) {
                            hours++;
                            minutes = minutes - 60;
                        }

                        module.getSuperAPI().scoreboard(players).update("§8➥ §b" + hours + "§7h", " §b" + minutes + "§7m §b" + seconds + "§7s", 9);

                        module.getSuperAPI().scoreboard(players).update("§8➥ ", "§b" + module.getUserData(players).getTokens(), 6);
                        module.getSuperAPI().scoreboard(players).update("§8➥ ", "§b" + module.getUserData(players).getCoins(), 3);
                        module.getSuperAPI().scoreboard(players).update("§8➥ ", "§cLädt...", 0);

                        module.getUserData(players).setPlaytime(module.getUserData(players).getPlaytime() + 1);
                    }
                }
            });
            
        } catch(Exception exception) {}
    }

}
