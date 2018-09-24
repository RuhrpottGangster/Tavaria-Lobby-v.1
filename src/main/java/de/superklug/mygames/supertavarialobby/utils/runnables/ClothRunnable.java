package de.superklug.mygames.supertavarialobby.utils.runnables;

import de.superklug.mygames.supertavarialobby.Lobby;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.scheduler.BukkitRunnable;

public class ClothRunnable extends BukkitRunnable {
    
    private final Lobby module;
    
    private int color = 0;
    private int red = 255;
    private int green = 0;
    private int blue = 0;
    
    private int subId = 0;

    public ClothRunnable(final Lobby module) {
        this.module = module;
    }

    @Override
    public void run() {
        try {
            
            if (color == 0) {
                if (blue < 255) {
                    blue = blue + 15;
                } else if (red > 0) {
                    red = red - 15;
                } else if (green < 255) {
                    green = green + 15;
                } else {
                    color = 1;
                }
            } else if (blue > 0) {
                blue = blue - 15;
            } else if (red < 255) {
                red = red + 15;
            } else if (green > 0) {
                green = green - 15;
            } else {
                color = 0;
            }

            if (subId < 15) {
                subId++;
            } else {
                subId = 0;
            }

            Bukkit.getOnlinePlayers().forEach((players) -> {

                if (module.getLobbyData(players) != null && module.getLobbyData(players).getActivePremiumGadgets().contains("discoboots")) {

                    players.getInventory().setBoots(module.getSuperAPI().item(Material.LEATHER_BOOTS)
                            .setDisplayname("§6•§e● Disco§8-§eSchuhe")
                            .setLeatherArmorColorRGB(red, green, blue).build());

                }
                
                if (module.getLobbyData(players) != null && module.getLobbyData(players).getActivePremiumGadgets().contains("discohat")) {

                    players.getInventory().setHelmet(module.getSuperAPI().item(Material.STAINED_GLASS, 1, (short) subId)
                            .setDisplayname("§6•§e● Disco§8-§eHut").build());

                }

                if (players.getOpenInventory().getTitle().equals("§8•§7● Extras §8▍ §eSpezial")) {

                    if (module.getLobbyData(players).getActivePremiumGadgets().contains("discoboots")) {

                        players.getOpenInventory().setItem(14, module.getSuperAPI().item(Material.LEATHER_BOOTS)
                                .setDisplayname("§6•§e● Disco§8-§eSchuhe §8▍ §aAktiviert")
                                .setLore(" ", "§8► §7Nur für Spieler mit dem Rang §6Gold§8/§aEmerald§8/§3Diamond §7und höher§8!")
                                .setLeatherArmorColorRGB(red, green, blue).build());
                        players.updateInventory();

                    } else {

                        players.getOpenInventory().setItem(14, module.getSuperAPI().item(Material.LEATHER_BOOTS)
                                .setDisplayname("§6•§e● Disco§8-§eSchuhe §8▍ §cDeaktiviert")
                                .setLore(" ", "§8► §7Nur für Spieler mit dem Rang §6Gold§8/§aEmerald§8/§3Diamond §7und höher§8!")
                                .setLeatherArmorColorRGB(red, green, blue).build());
                        players.updateInventory();

                    }
                    if (module.getLobbyData(players).getActivePremiumGadgets().contains("discohat")) {

                        players.getOpenInventory().setItem(16, module.getSuperAPI().item(Material.STAINED_GLASS, 1, (short) subId)
                                .setDisplayname("§6•§e● Disco§8-§eHut §8▍ §aAktiviert")
                                .setLore(" ", "§8► §7Nur für Spieler mit dem Rang §6Gold§8/§aEmerald§8/§3Diamond §7und höher§8!").build());
                        players.updateInventory();

                    } else {

                        players.getOpenInventory().setItem(16, module.getSuperAPI().item(Material.STAINED_GLASS, 1, (short) subId)
                                .setDisplayname("§6•§e● Disco§8-§eHut §8▍ §cDeaktiviert")
                                .setLore(" ", "§8► §7Nur für Spieler mit dem Rang §6Gold§8/§aEmerald§8/§3Diamond §7und höher§8!").build());
                        players.updateInventory();

                    }

                }

            });



        } catch(Exception exception) {}
    }

}
