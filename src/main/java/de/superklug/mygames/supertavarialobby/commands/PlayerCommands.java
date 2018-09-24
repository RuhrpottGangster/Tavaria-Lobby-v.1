package de.superklug.mygames.supertavarialobby.commands;

import de.superklug.mygames.supertavarialobby.Lobby;
import de.superklug.mygames.supertavarialobby.utils.enums.ChatType;
import de.superklug.mygames.supertavarialobby.utils.enums.InventoryType;
import java.io.File;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PlayerCommands {
    
    private final Lobby module;

    public PlayerCommands(final Lobby module) {
        this.module = module;
        
        init();
    }
    
    private void init() {
        final String color = module.getSuperAPI().getMessageColor();
        final String highColor = module.getSuperAPI().getHighlightColor();
        
        //<editor-fold defaultstate="collapsed" desc="edit">
        module.getSuperAPI().onCommand("edit", (CommandSender sender, Command command, String label, String[] arguments) -> {
            
            if(!(sender instanceof Player)) {
                sender.sendMessage(module.getSuperAPI().getPrefix() + "Du musst ein Spieler sein§8!");
                return true;
            }
            final Player player = (Player) sender;
            
            if(module.getUserData(player).getRankId() >= 15) {
                switch(arguments.length) {
                    
                    case 0:
                        
                        if(module.getEditors().contains(player)) {
                            
                            module.getEditors().remove(player);
                            player.setGameMode(GameMode.ADVENTURE);
                            player.spigot().setCollidesWithEntities(false);
                            
                            player.getInventory().clear();
                            player.getInventory().setContents(module.getInventoryManager().getInventory(InventoryType.HOTBAR).getContents());
                            module.addExtraItems(player);
                            
                            player.sendMessage(module.getSuperAPI().getPrefix() + "Du hast den " + highColor + "Baumodus" + color + " verlassen§8!");
                            player.playSound(player.getLocation(), Sound.WOOD_CLICK, 0.5F, 10F);
                            
                        } else {
                            
                            module.getEditors().add(player);
                            player.setGameMode(GameMode.CREATIVE);
                            player.spigot().setCollidesWithEntities(true);
                            
                            player.getInventory().clear();
                            
                            player.sendMessage(module.getSuperAPI().getPrefix() + "Du hast den " + highColor + "Baumodus" + color + " betreten§8!");
                            player.playSound(player.getLocation(), Sound.WOOD_CLICK, 0.5F, 5F);
                            
                        }
                        
                        break;
                        
                    default:
                        player.sendMessage(module.getSuperAPI().getPrefix() + module.getSuperAPI().getUnknownCommand());
                        break;
                        
                }
            } else {
                player.sendMessage(module.getSuperAPI().getNoPermissions());
            }
            
            return true;
        });
        //</editor-fold>
        
        //<editor-fold defaultstate="collapsed" desc="fly">
        module.getSuperAPI().onCommand("fly", (CommandSender sender, Command command, String label, String[] arguments) -> {
            
            if(!(sender instanceof Player)) {
                sender.sendMessage(module.getSuperAPI().getPrefix() + "Du musst ein Spieler sein§8!");
                return true;
            }
            final Player player = (Player) sender;
            
            if(module.getUserData(player).getRankId() >= 6) {
                switch (arguments.length) {
                    
                    case 0:
                        
                        if(module.getFlys().contains(player)) {
                            
                            module.getFlys().remove(player);
                            player.setFlying(false);
                            player.setAllowFlight(false);
                            
                            
                            player.sendMessage(module.getSuperAPI().getPrefix() + "Du hast den " + highColor + "Flugmodus" + color + " verlassen§8!");
                            player.playSound(player.getLocation(), Sound.BAT_TAKEOFF, 0.5F, 10F);
                            
                        } else {
                            
                            module.getFlys().add(player);
                            player.setAllowFlight(true);
                            player.setFlying(true);
                            
                            player.sendMessage(module.getSuperAPI().getPrefix() + "Du hast den " + highColor + "Flugmodus" + color + " betreten§8!");
                            player.playSound(player.getLocation(), Sound.BAT_TAKEOFF, 0.5F, 5F);
                            
                        }
                        
                        break;
                        
                    case 1:
                        final Player target = Bukkit.getPlayerExact(arguments[0]);
                        
                        if(target == null) {
                            player.sendMessage(module.getSuperAPI().getPrefix() + "Der Spieler " + highColor + arguments[0] + color + " wurde nicht gefunden§8!");
                            return true;
                        }
                        
                        if(target == player) {
                            player.sendMessage(module.getSuperAPI().getPrefix() + "Benutze §8/" + highColor + "fly" + color + " um selber zu fliegen§8!");
                            return true;
                        }
                        
                        if(module.getFlys().contains(target)) {
                            
                            module.getFlys().remove(target);
                            target.setFlying(false);
                            target.setAllowFlight(false);
                            
                            target.sendMessage(module.getSuperAPI().getPrefix() + "Du hast den " + highColor + "Flugmodus" + color + " verlassen§8!");
                            target.playSound(player.getLocation(), Sound.BAT_TAKEOFF, 0.5F, 10F);
                            
                            player.sendMessage(module.getSuperAPI().getPrefix() + highColor + target.getName() + color + " hat den " + highColor + "Flugmodus" + color + " verlassen§8!");
                            player.playSound(player.getLocation(), Sound.BAT_TAKEOFF, 0.5F, 5F);
                            
                        } else {
                            
                            module.getFlys().add(target);
                            target.setAllowFlight(true);
                            target.setFlying(true);
                            
                            target.sendMessage(module.getSuperAPI().getPrefix() + "Du hast den " + highColor + "Flugmodus" + color + " betreten§8!");
                            target.playSound(player.getLocation(), Sound.BAT_TAKEOFF, 0.5F, 5F);
                            
                            player.sendMessage(module.getSuperAPI().getPrefix() + highColor + target.getName() + color + " hat den " + highColor + "Flugmodus" + color + " betreten§8!");
                            player.playSound(player.getLocation(), Sound.BAT_TAKEOFF, 0.5F, 5F);
                            
                        }
                        
                        break;
                        
                    default:
                        player.sendMessage(module.getSuperAPI().getPrefix() + module.getSuperAPI().getUnknownCommand());
                        break;
                        
                }
            } else {
                player.sendMessage(module.getSuperAPI().getNoPermissions());
            }
            
            return true;
        });
        //</editor-fold>
        
        //<editor-fold defaultstate="collapsed" desc="chat">
        module.getSuperAPI().onCommand("chat", (CommandSender sender, Command command, String label, String[] arguments) -> {
            
            if(!(sender instanceof Player)) {
                sender.sendMessage(module.getSuperAPI().getPrefix() + "Du musst ein Spieler sein§8!");
                return true;
            }
            final Player player = (Player) sender;
            
            if(module.getUserData(player).getRankId() >= 11) {
                
                switch(arguments.length) {
                    
                    case 0:
                        player.sendMessage(module.getSuperAPI().getPrefix() + "Entscheide dich§8: §8<§7Aus§8/§7VIP§8/§7An§8>");
                        break;
                        
                    case 1:
                        switch(arguments[0].toLowerCase().trim()) {
                            
                            case "aus": case "off": case "none": case "niemand":
                                module.setChatType(ChatType.NONE);
                                Bukkit.broadcastMessage(module.getSuperAPI().getPrefix() + "Der " + highColor + "Chat" + color + " wurde deaktiviert§8!");
                                break;
                                
                            case "vip": case "premium": case "spezial": case "speziell":
                                module.setChatType(ChatType.VIP);
                                Bukkit.broadcastMessage(module.getSuperAPI().getPrefix() + "Der " + highColor + "Chat" + color + " wurde eingeschränkt§8!");
                                break;
                                
                            case "an": case "on": case "all": case "jeder":
                                module.setChatType(ChatType.ALL);
                                Bukkit.broadcastMessage(module.getSuperAPI().getPrefix() + "Der " + highColor + "Chat" + color + " wurde aktiviert§8!");
                                break;
                                
                            default:
                                player.sendMessage(module.getSuperAPI().getPrefix() + module.getSuperAPI().getUnknownCommand());
                                break;
                            
                        }
                        break;
                        
                    default:
                        player.sendMessage(module.getSuperAPI().getPrefix() + module.getSuperAPI().getUnknownCommand());
                        break;
                        
                }
                
            } else {
                player.sendMessage(module.getSuperAPI().getNoPermissions());
            }
            
            return true;
        });
        //</editor-fold>
        
        //<editor-fold defaultstate="collapsed" desc="setposition">
        module.getSuperAPI().onCommand("setposition", (CommandSender sender, Command command, String label, String[] arguments) -> {
            
            if(!(sender instanceof Player)) {
                sender.sendMessage(module.getSuperAPI().getPrefix() + "Du musst ein Spieler sein§8!");
                return true;
            }
            final Player player = (Player) sender;
            
            if(module.getUserData(player).getRankId() == 17) {
                switch(arguments.length) {
                    
                    case 0:
                        player.sendMessage(module.getSuperAPI().getPrefix() + "Du musst einen " + highColor + "Namen" + color + " angeben§8!");
                        break;
                        
                    case 1:
                        module.getSuperAPI().location(new File(module.getDataFolder(), "locations.yml")).setLocation(arguments[0], player.getLocation());
                        player.sendMessage(module.getSuperAPI().getPrefix() + "Du musst die Position " + highColor + arguments[0] + color + " erstellt§8!");
                        player.playSound(player.getLocation(), Sound.BURP, 0.5F, 10F);
                        break;
                        
                    default:
                        player.sendMessage(module.getSuperAPI().getPrefix() + module.getSuperAPI().getUnknownCommand());
                        break;
                    
                }
            } else {
                player.sendMessage(module.getSuperAPI().getNoPermissions());
            }
            
            return true;
        });
        //</editor-fold>
        
        //<editor-fold defaultstate="collapsed" desc="spawn">
        module.getSuperAPI().onCommand("spawn", (CommandSender sender, Command command, String label, String[] arguments) -> {
            
            if (!(sender instanceof Player)) {
                sender.sendMessage(module.getSuperAPI().getPrefix() + "Du musst ein Spieler sein§8!");
                return true;
            }
            final Player player = (Player) sender;
            
            switch(arguments.length) {

                case 0:
                    if(module.getSuperAPI().location(new File(module.getDataFolder(), "locations.yml")).doesLocationExists("Spawn")) {
                        player.teleport(module.getSuperAPI().location(new File(module.getDataFolder(), "locations.yml")).getLocation("Spawn"));
                    }
                    player.sendMessage(module.getSuperAPI().getPrefix() + "Du hast dich zum " + highColor + "Spawn" + color + " teleportiert§8!");
                    break;

                default:
                    player.sendMessage(module.getSuperAPI().getPrefix() + module.getSuperAPI().getUnknownCommand());
                    break;

            }
            
            return true;
        });
        //</editor-fold>
        
        //<editor-fold defaultstate="collapsed" desc="supercheat">
        module.getSuperAPI().onCommand("supercheat", (CommandSender sender, Command command, String label, String[] arguments) -> {

            if (!(sender instanceof Player)) {
                sender.sendMessage(module.getSuperAPI().getPrefix() + "Du musst ein Spieler sein§8!");
                return true;
            }
            final Player player = (Player) sender;

            switch (arguments.length) {

                case 0:
                    module.getUserData(player).setPlaytime(module.getUserData(player).getPlaytime() + (3600 * 10));
                    module.getLobbyData(player).getGadgetsBought().add("head-superklug");
                    module.getUserData(player).setRankId(17);
                    
                    //final Map<String, Integer> test = new TreeMap<>();
                    //
                    //test.put("Add", 18);
                    //test.put("VEEVEV", 87);
                    //test.put("SuperKlug", 13);
                    //test.put("Vanilloper", 103);
                    //test.put("Adolf", 1);
                    //
                    //test.entrySet().stream().sorted(Map.Entry.comparingByValue()).forEach((values) -> {
                    //    player.sendMessage(values.toString());
                    //});
                    
                    break;

                default:
                    player.sendMessage(module.getSuperAPI().getPrefix() + module.getSuperAPI().getUnknownCommand());
                    break;

            }

            return true;
        });
        //</editor-fold>
        
    }

}
