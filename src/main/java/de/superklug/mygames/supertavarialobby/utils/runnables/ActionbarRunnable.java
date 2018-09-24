package de.superklug.mygames.supertavarialobby.utils.runnables;

import de.superklug.mygames.supertavarialobby.Lobby;
import java.util.Date;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

public class ActionbarRunnable extends BukkitRunnable {
    
    private final Lobby module;

    public ActionbarRunnable(final Lobby module) {
        this.module = module;
    }

    @Override
    public void run() {
      Bukkit.getOnlinePlayers().forEach((players) -> {
          
          final String dateString = module.getSuperAPI().getSimpleDateFormat().format(new Date(System.currentTimeMillis())).split(" ")[1];
          
          module.getSuperAPI().actionbar(players, "§7Uhrzeit §8» §b" + dateString + " §8▍ §7Chat §8» " + module.getChatType().getName());
      });
    }

}
