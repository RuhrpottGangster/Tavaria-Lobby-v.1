package de.superklug.mygames.supertavarialobby.listeners;

import de.superklug.mygames.superapi.events.EventListener;
import de.superklug.mygames.supertavarialobby.Lobby;
import de.superklug.mygames.supertavarialobby.utils.enums.InventoryType;
import de.superklug.mygames.supertavariamongoapi.entities.Nick;
import de.superklug.mygames.supertavariamongoapi.entities.User;
import de.superklug.mygames.supertavariamongoapi.enums.NickType;
import java.io.File;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Effect;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.WeatherType;
import org.bukkit.block.Sign;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockFadeEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.hanging.HangingPlaceEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.event.player.PlayerChatTabCompleteEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.event.player.PlayerUnleashEntityEvent;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.help.HelpTopic;
import org.bukkit.inventory.Inventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.NameTagVisibility;
import org.bukkit.util.Vector;

public class PlayerListeners implements Listener {
    
    private final Lobby module;

    public PlayerListeners(final Lobby module) {
        this.module = module;
        
        init();
    }
    
    // Inv Click
    // Move (Jumppads)
    // Toggle Flight
    // Toggle Sneak
    private void init() {
        
        //<editor-fold defaultstate="collapsed" desc="AsyncPlayerChatEvent">
        module.getSuperAPI().registerEvent(AsyncPlayerChatEvent.class, (EventListener<AsyncPlayerChatEvent>) (AsyncPlayerChatEvent event) -> {
            final Player player = event.getPlayer();
            String message = event.getMessage().replaceAll("%", "%%");
            
            if(module.getUserData(player).getRankId() >= 6) {
                message = ChatColor.translateAlternateColorCodes('&', message);
            }
            
            switch(module.getChatType()) {
                
                case NONE:
                    if(module.getUserData(player).getRankId() < 7) {

                        event.setCancelled(true);
                        player.sendMessage(module.getSuperAPI().getPrefix() + "Der Chat ist derzeit deaktiviert§8!");
                        player.playSound(player.getLocation(), Sound.ANVIL_LAND, 0.25F, 10F);

                    }
                    break;
                    
                case VIP:
                    if(module.getUserData(player).getRankId() == 0) {

                        event.setCancelled(true);
                        player.sendMessage(module.getSuperAPI().getPrefix() + "Du benötigst den §6Gold§8/§aEmerald§8/§3Diamond §7Rang um schreiben zu können§8!");
                        player.playSound(player.getLocation(), Sound.ANVIL_LAND, 0.25F, 10F);

                    }
                    break;
                    
                default:
                    break;
                
            }
            
            event.setFormat(player.getDisplayName() + " §8➠ §r" + message.trim());
            player.playSound(player.getLocation(), Sound.ITEM_PICKUP, 0.25F, 10F);
            
        }, EventPriority.NORMAL);
        //</editor-fold>
        
        //<editor-fold defaultstate="collapsed" desc="BlockBreakEvent">
        module.getSuperAPI().registerEvent(BlockBreakEvent.class, (EventListener<BlockBreakEvent>) (BlockBreakEvent event) -> {
            final Player player = event.getPlayer();
            
            if(!module.getEditors().contains(player))
                event.setCancelled(true);
            
        }, EventPriority.NORMAL);
        //</editor-fold>
        
        //<editor-fold defaultstate="collapsed" desc="BlockPlaceEvent">
        module.getSuperAPI().registerEvent(BlockPlaceEvent.class, (EventListener<BlockPlaceEvent>) (BlockPlaceEvent event) -> {
            final Player player = event.getPlayer();

            if (!module.getEditors().contains(player)) {
                event.setCancelled(true);
            }

        }, EventPriority.NORMAL);
        //</editor-fold>
        
        //<editor-fold defaultstate="collapsed" desc="BlockFadeEvent">
        module.getSuperAPI().registerEvent(BlockFadeEvent.class, (EventListener<BlockFadeEvent>) (BlockFadeEvent event) -> {
            event.setCancelled(true);
        }, EventPriority.NORMAL);
        //</editor-fold>
        
        //<editor-fold defaultstate="collapsed" desc="EntityChangeBlockEvent">
        module.getSuperAPI().registerEvent(EntityChangeBlockEvent.class, (EventListener<EntityChangeBlockEvent>) (EntityChangeBlockEvent event) -> {
            event.setCancelled(true);
        }, EventPriority.NORMAL);
        //</editor-fold>
        
        //<editor-fold defaultstate="collapsed" desc="EntityDamageEvent">
        module.getSuperAPI().registerEvent(EntityDamageEvent.class, (EventListener<EntityDamageEvent>) (EntityDamageEvent event) -> {
            event.setCancelled(true);
        }, EventPriority.NORMAL);
        //</editor-fold>
        
        //<editor-fold defaultstate="collapsed" desc="EntityDamageByEntityEvent">
        module.getSuperAPI().registerEvent(EntityDamageByEntityEvent.class, (EventListener<EntityDamageByEntityEvent>) (EntityDamageByEntityEvent event) -> {
            event.setCancelled(true);
        }, EventPriority.NORMAL);
        //</editor-fold>
        
        //<editor-fold defaultstate="collapsed" desc="FoodLevelChangeEvent">
        module.getSuperAPI().registerEvent(FoodLevelChangeEvent.class, (EventListener<FoodLevelChangeEvent>) (FoodLevelChangeEvent event) -> {
            event.setCancelled(true);
        }, EventPriority.NORMAL);
        //</editor-fold>
        
        //<editor-fold defaultstate="collapsed" desc="HangingPlaceEvent">
        module.getSuperAPI().registerEvent(HangingPlaceEvent.class, (EventListener<HangingPlaceEvent>) (HangingPlaceEvent event) -> {
            final Player player = event.getPlayer();
            
            if(!module.getEditors().contains(player))
                event.setCancelled(true);
            
        }, EventPriority.NORMAL);
        //</editor-fold>
        
        //<editor-fold defaultstate="collapsed" desc="HangingBreakByEntityEvent">
        module.getSuperAPI().registerEvent(HangingBreakByEntityEvent.class, (EventListener<HangingBreakByEntityEvent>) (HangingBreakByEntityEvent event) -> {
            
            if(event.getRemover() instanceof Player) {
                final Player player = (Player) event.getRemover();
                
                if(!module.getEditors().contains(player))
                    event.setCancelled(true);
                
            }
            
        }, EventPriority.NORMAL);
        //</editor-fold>
        
        //<editor-fold defaultstate="collapsed" desc="InventoryClickEvent">
        module.getSuperAPI().registerEvent(InventoryClickEvent.class, (EventListener<InventoryClickEvent>) (InventoryClickEvent event) -> {
            
            if(event.getWhoClicked() instanceof Player) {
                try {
                    
                    final Player player = (Player) event.getWhoClicked();

                    if(event.getCurrentItem() == null) return;
                    if(event.getCurrentItem().getType() == null) return;
                    if(event.getCurrentItem().getItemMeta() == null) return;
                    if(event.getCurrentItem().getItemMeta().getDisplayName() == null) return;

                    if(!module.getEditors().contains(player)) event.setCancelled(true);

                    switch (event.getInventory().getTitle()) {

                        //<editor-fold defaultstate="collapsed" desc="navigator">
                        case "§8•§7● Navigator":

                            break;
                        //</editor-fold>

                        //<editor-fold defaultstate="collapsed" desc="nick">
                        case "§8•§7● Nick":
                            
                            switch (event.getCurrentItem().getItemMeta().getDisplayName()) {
                                
                                case "§8•§7● Spieler":
                                    
                                    if(module.getMongoAPI().getBackendManager().doesNickExists(player.getUniqueId().toString())) {
                                        
                                        module.getMongoAPI().getBackendManager().getNick(player.getUniqueId().toString(), (Nick t) -> {
                                            t.setType(NickType.SPIELER);
                                            
                                            module.getMongoAPI().getBackendManager().updateNick(t);
                                            
                                        });
                                        
                                    } else {
                                        
                                        module.getMongoAPI().getBackendManager().createNick(player.getUniqueId().toString(), "", "", NickType.SPIELER);
                                        
                                    }
                                    
                                    System.out.println(">> NickType Spieler");
                                    break;
                                
                                case "§6•§e● Gold":
                                    break;
                                    
                                case "§2•§a● Emerald":
                                    break;
                                    
                                case "§b•§3● Diamond":
                                    break;
                                    
                                case "§4•§c● Deaktivieren":
                                    
                                    if (module.getMongoAPI().getBackendManager().doesNickExists(player.getUniqueId().toString())) {
                                        module.getMongoAPI().getBackendManager().deleteNick(player.getUniqueId().toString());
                                    }
                                    
                                    System.out.println(">> NickType NONE");
                                    break;
                                    
                                default:
                                    break;
                                    
                            }
                            
                            break;
                        //</editor-fold>

                        //<editor-fold defaultstate="collapsed" desc="extras- banner">
                        case "§8•§7● Extras §8▍ §7Banner":
                            switch (event.getCurrentItem().getItemMeta().getDisplayName()) {

                                case "§8•§7● Weiß":
                                    if (module.getLobbyData(player).getGadgetsBought().contains("banner-white") 
                                            | module.getLobbyData(player).getGadgetsBought().contains("banner-*")
                                            | module.getLobbyData(player).getGadgetsBought().contains("*")) {

                                        module.getLobbyData(player).getActiveGadgets().forEach((gadgets) -> {
                                            if (gadgets.startsWith("banner-") | gadgets.startsWith("hat-") | gadgets.startsWith("head-")) {
                                                module.getLobbyData(player).getActiveGadgets().remove(gadgets);
                                            }
                                        });

                                        player.getInventory().setHelmet(module.getGadgets().get("banner-white"));
                                        player.playSound(player.getLocation(), Sound.WOOD_CLICK, 0.5F, 0.74F);
                                        player.sendMessage(module.getSuperAPI().getPrefix() + "Du hast den Banner §8» " + event.getCurrentItem().getItemMeta().getDisplayName() + " §7ausgewählt§8.");
                                        module.getLobbyData(player).getActiveGadgets().add("banner-white");
                                        player.closeInventory();

                                    } else {
                                        player.sendMessage(module.getSuperAPI().getPrefix() + "Du musst diesen §bBanner §7erst im §bCoinshop §7kaufen§8!");
                                    }
                                    break;

                                case "§8•§7● Orange":
                                    if (module.getLobbyData(player).getGadgetsBought().contains("banner-orange") 
                                            | module.getLobbyData(player).getGadgetsBought().contains("banner-*")
                                            | module.getLobbyData(player).getGadgetsBought().contains("*")) {

                                        module.getLobbyData(player).getActiveGadgets().forEach((gadgets) -> {
                                            if (gadgets.startsWith("banner-") | gadgets.startsWith("hat-") | gadgets.startsWith("head-")) {
                                                module.getLobbyData(player).getActiveGadgets().remove(gadgets);
                                            }
                                        });

                                        player.getInventory().setHelmet(module.getGadgets().get("banner-orange"));
                                        player.playSound(player.getLocation(), Sound.WOOD_CLICK, 0.5F, 0.74F);
                                        player.sendMessage(module.getSuperAPI().getPrefix() + "Du hast den Banner §8» " + event.getCurrentItem().getItemMeta().getDisplayName() + " §7ausgewählt§8.");
                                        module.getLobbyData(player).getActiveGadgets().add("banner-orange");
                                        player.closeInventory();

                                    } else {
                                        player.sendMessage(module.getSuperAPI().getPrefix() + "Du musst diesen §bBanner §7erst im §bCoinshop §7kaufen§8!");
                                    }
                                    break;

                                case "§8•§7● Pirat":
                                    if (module.getLobbyData(player).getGadgetsBought().contains("banner-pirate") 
                                            | module.getLobbyData(player).getGadgetsBought().contains("banner-*")
                                            | module.getLobbyData(player).getGadgetsBought().contains("*")) {

                                        module.getLobbyData(player).getActiveGadgets().forEach((gadgets) -> {
                                            if (gadgets.startsWith("banner-") | gadgets.startsWith("hat-") | gadgets.startsWith("head-")) {
                                                module.getLobbyData(player).getActiveGadgets().remove(gadgets);
                                            }
                                        });

                                        player.getInventory().setHelmet(module.getGadgets().get("banner-pirate"));
                                        player.playSound(player.getLocation(), Sound.WOOD_CLICK, 0.5F, 0.74F);
                                        player.sendMessage(module.getSuperAPI().getPrefix() + "Du hast den Banner §8» " + event.getCurrentItem().getItemMeta().getDisplayName() + " §7ausgewählt§8.");
                                        module.getLobbyData(player).getActiveGadgets().add("banner-pirate");
                                        player.closeInventory();

                                    } else {
                                        player.sendMessage(module.getSuperAPI().getPrefix() + "Du musst diesen §bBanner §7erst im §bCoinshop §7kaufen§8!");
                                    }
                                    break;

                                case "§8•§7● Deutschland":
                                    if (module.getLobbyData(player).getGadgetsBought().contains("banner-germany") 
                                            | module.getLobbyData(player).getGadgetsBought().contains("banner-*")
                                            | module.getLobbyData(player).getGadgetsBought().contains("*")) {

                                        module.getLobbyData(player).getActiveGadgets().forEach((gadgets) -> {
                                            if (gadgets.startsWith("banner-") | gadgets.startsWith("hat-") | gadgets.startsWith("head-")) {
                                                module.getLobbyData(player).getActiveGadgets().remove(gadgets);
                                            }
                                        });

                                        player.getInventory().setHelmet(module.getGadgets().get("banner-germany"));
                                        player.playSound(player.getLocation(), Sound.WOOD_CLICK, 0.5F, 0.74F);
                                        player.sendMessage(module.getSuperAPI().getPrefix() + "Du hast den Banner §8» " + event.getCurrentItem().getItemMeta().getDisplayName() + " §7ausgewählt§8.");
                                        module.getLobbyData(player).getActiveGadgets().add("banner-germany");
                                        player.closeInventory();

                                    } else {
                                        player.sendMessage(module.getSuperAPI().getPrefix() + "Du musst diesen §bBanner §7erst im §bCoinshop §7kaufen§8!");
                                    }
                                    break;

                                case "§8•§7● Geist":
                                    if (module.getLobbyData(player).getGadgetsBought().contains("banner-ghost") 
                                            | module.getLobbyData(player).getGadgetsBought().contains("banner-*")
                                            | module.getLobbyData(player).getGadgetsBought().contains("*")) {

                                        module.getLobbyData(player).getActiveGadgets().forEach((gadgets) -> {
                                            if (gadgets.startsWith("banner-") | gadgets.startsWith("hat-") | gadgets.startsWith("head-")) {
                                                module.getLobbyData(player).getActiveGadgets().remove(gadgets);
                                            }
                                        });

                                        player.getInventory().setHelmet(module.getGadgets().get("banner-ghost"));
                                        player.playSound(player.getLocation(), Sound.WOOD_CLICK, 0.5F, 0.74F);
                                        player.sendMessage(module.getSuperAPI().getPrefix() + "Du hast den Banner §8» " + event.getCurrentItem().getItemMeta().getDisplayName() + " §7ausgewählt§8.");
                                        module.getLobbyData(player).getActiveGadgets().add("banner-ghost");
                                        player.closeInventory();

                                    } else {
                                        player.sendMessage(module.getSuperAPI().getPrefix() + "Du musst diesen §bBanner §7erst im §bCoinshop §7kaufen§8!");
                                    }
                                    break;

                                case "§8•§7● Mojang":
                                    if (module.getLobbyData(player).getGadgetsBought().contains("banner-mojang") 
                                            | module.getLobbyData(player).getGadgetsBought().contains("banner-*")
                                            | module.getLobbyData(player).getGadgetsBought().contains("*")) {

                                        module.getLobbyData(player).getActiveGadgets().forEach((gadgets) -> {
                                            if (gadgets.startsWith("banner-") | gadgets.startsWith("hat-") | gadgets.startsWith("head-")) {
                                                module.getLobbyData(player).getActiveGadgets().remove(gadgets);
                                            }
                                        });

                                        player.getInventory().setHelmet(module.getGadgets().get("banner-mojang"));
                                        player.playSound(player.getLocation(), Sound.WOOD_CLICK, 0.5F, 0.74F);
                                        player.sendMessage(module.getSuperAPI().getPrefix() + "Du hast den Banner §8» " + event.getCurrentItem().getItemMeta().getDisplayName() + " §7ausgewählt§8.");
                                        module.getLobbyData(player).getActiveGadgets().add("banner-mojang");
                                        player.closeInventory();

                                    } else {
                                        player.sendMessage(module.getSuperAPI().getPrefix() + "Du musst diesen §bBanner §7erst im §bCoinshop §7kaufen§8!");
                                    }
                                    break;

                                case "§8•§7● Tintenfisch":
                                    if (module.getLobbyData(player).getGadgetsBought().contains("banner-octopos") 
                                            | module.getLobbyData(player).getGadgetsBought().contains("banner-*")
                                            | module.getLobbyData(player).getGadgetsBought().contains("*")) {

                                        module.getLobbyData(player).getActiveGadgets().forEach((gadgets) -> {
                                            if (gadgets.startsWith("banner-") | gadgets.startsWith("hat-") | gadgets.startsWith("head-")) {
                                                module.getLobbyData(player).getActiveGadgets().remove(gadgets);
                                            }
                                        });

                                        player.getInventory().setHelmet(module.getGadgets().get("banner-octopos"));
                                        player.playSound(player.getLocation(), Sound.WOOD_CLICK, 0.5F, 0.74F);
                                        player.sendMessage(module.getSuperAPI().getPrefix() + "Du hast den Banner §8» " + event.getCurrentItem().getItemMeta().getDisplayName() + " §7ausgewählt§8.");
                                        module.getLobbyData(player).getActiveGadgets().add("banner-octopos");
                                        player.closeInventory();

                                    } else {
                                        player.sendMessage(module.getSuperAPI().getPrefix() + "Du musst diesen §bBanner §7erst im §bCoinshop §7kaufen§8!");
                                    }
                                    break;

                                case "§8•§7● Creeper":
                                    if (module.getLobbyData(player).getGadgetsBought().contains("banner-creeper") 
                                            | module.getLobbyData(player).getGadgetsBought().contains("banner-*")
                                            | module.getLobbyData(player).getGadgetsBought().contains("*")) {

                                        module.getLobbyData(player).getActiveGadgets().forEach((gadgets) -> {
                                            if (gadgets.startsWith("banner-") | gadgets.startsWith("hat-") | gadgets.startsWith("head-")) {
                                                module.getLobbyData(player).getActiveGadgets().remove(gadgets);
                                            }
                                        });

                                        player.getInventory().setHelmet(module.getGadgets().get("banner-creeper"));
                                        player.playSound(player.getLocation(), Sound.WOOD_CLICK, 0.5F, 0.74F);
                                        player.sendMessage(module.getSuperAPI().getPrefix() + "Du hast den Banner §8» " + event.getCurrentItem().getItemMeta().getDisplayName() + " §7ausgewählt§8.");
                                        module.getLobbyData(player).getActiveGadgets().add("banner-creeper");
                                        player.closeInventory();

                                    } else {
                                        player.sendMessage(module.getSuperAPI().getPrefix() + "Du musst diesen §bBanner §7erst im §bCoinshop §7kaufen§8!");
                                    }
                                    break;

                                case "§4•§c● Banner ausziehen":
                                    if (player.getInventory().getHelmet() != null && player.getInventory().getHelmet().getType() == Material.BANNER) {
                                        player.getInventory().setHelmet(null);

                                        module.getLobbyData(player).getActiveGadgets().forEach((gadgets) -> {
                                            if (gadgets.startsWith("banner-") | gadgets.startsWith("hat-") | gadgets.startsWith("head-")) {
                                                module.getLobbyData(player).getActiveGadgets().remove(gadgets);
                                            }
                                        });

                                        player.playSound(player.getLocation(), Sound.WOOD_CLICK, 0.5F, 0.74F);
                                        player.sendMessage(module.getSuperAPI().getPrefix() + "Du hast deinen §bBanner §7ausgezogen§8!");
                                        player.closeInventory();
                                    } else {
                                        player.sendMessage(module.getSuperAPI().getPrefix() + "Du trägst keinen §bBanner §7auf deinem Kopf§8!");
                                    }
                                    break;

                                default:
                                    break;

                            }
                            break;
                        //</editor-fold>
                            
                        //<editor-fold defaultstate="collapsed" desc="extras- hat">
                        case "§8•§7● Extras §8▍ §7Hüte":
                            switch (event.getCurrentItem().getItemMeta().getDisplayName()) {

                                case "§8•§7● Taucher":
                                    if (module.getLobbyData(player).getGadgetsBought().contains("hat-driver") 
                                            | module.getLobbyData(player).getGadgetsBought().contains("hat-*")
                                            | module.getLobbyData(player).getGadgetsBought().contains("*")) {

                                        module.getLobbyData(player).getActiveGadgets().forEach((gadgets) -> {
                                            if (gadgets.startsWith("banner-") | gadgets.startsWith("hat-") | gadgets.startsWith("head-")) {
                                                module.getLobbyData(player).getActiveGadgets().remove(gadgets);
                                            }
                                        });

                                        player.getInventory().setHelmet(module.getGadgets().get("hat-driver"));
                                        player.playSound(player.getLocation(), Sound.WOOD_CLICK, 0.5F, 0.74F);
                                        player.sendMessage(module.getSuperAPI().getPrefix() + "Du hast den Hut §8» " + event.getCurrentItem().getItemMeta().getDisplayName() + " §7ausgewählt§8.");
                                        module.getLobbyData(player).getActiveGadgets().add("hat-driver");
                                        player.closeInventory();

                                    } else {
                                        player.sendMessage(module.getSuperAPI().getPrefix() + "Du musst diesen §bHut §7erst im §bCoinshop §7kaufen§8!");
                                    }
                                    break;
                                    
                                case "§8•§7● Astronaut":
                                    if (module.getLobbyData(player).getGadgetsBought().contains("hat-astronaut") 
                                            | module.getLobbyData(player).getGadgetsBought().contains("hat-*")
                                            | module.getLobbyData(player).getGadgetsBought().contains("*")) {

                                        module.getLobbyData(player).getActiveGadgets().forEach((gadgets) -> {
                                            if (gadgets.startsWith("banner-") | gadgets.startsWith("hat-") | gadgets.startsWith("head-")) {
                                                module.getLobbyData(player).getActiveGadgets().remove(gadgets);
                                            }
                                        });

                                        player.getInventory().setHelmet(module.getGadgets().get("hat-astronaut"));
                                        player.playSound(player.getLocation(), Sound.WOOD_CLICK, 0.5F, 0.74F);
                                        player.sendMessage(module.getSuperAPI().getPrefix() + "Du hast den Hut §8» " + event.getCurrentItem().getItemMeta().getDisplayName() + " §7ausgewählt§8.");
                                        module.getLobbyData(player).getActiveGadgets().add("hat-astronaut");
                                        player.closeInventory();

                                    } else {
                                        player.sendMessage(module.getSuperAPI().getPrefix() + "Du musst diesen §bHut §7erst im §bCoinshop §7kaufen§8!");
                                    }
                                    break;

                                case "§8•§7● TNT":
                                    if (module.getLobbyData(player).getGadgetsBought().contains("hat-tnt") 
                                            | module.getLobbyData(player).getGadgetsBought().contains("hat-*")
                                            | module.getLobbyData(player).getGadgetsBought().contains("*")) {

                                        module.getLobbyData(player).getActiveGadgets().forEach((gadgets) -> {
                                            if (gadgets.startsWith("banner-") | gadgets.startsWith("hat-") | gadgets.startsWith("head-")) {
                                                module.getLobbyData(player).getActiveGadgets().remove(gadgets);
                                            }
                                        });

                                        player.getInventory().setHelmet(module.getGadgets().get("hat-tnt"));
                                        player.playSound(player.getLocation(), Sound.WOOD_CLICK, 0.5F, 0.74F);
                                        player.sendMessage(module.getSuperAPI().getPrefix() + "Du hast den Hut §8» " + event.getCurrentItem().getItemMeta().getDisplayName() + " §7ausgewählt§8.");
                                        module.getLobbyData(player).getActiveGadgets().add("hat-tnt");
                                        player.closeInventory();

                                    } else {
                                        player.sendMessage(module.getSuperAPI().getPrefix() + "Du musst diesen §bHut §7erst im §bCoinshop §7kaufen§8!");
                                    }
                                    break;

                                case "§8•§7● Smile":
                                    if (module.getLobbyData(player).getGadgetsBought().contains("hat-smile") 
                                            | module.getLobbyData(player).getGadgetsBought().contains("hat-*")
                                            | module.getLobbyData(player).getGadgetsBought().contains("*")) {

                                        module.getLobbyData(player).getActiveGadgets().forEach((gadgets) -> {
                                            if (gadgets.startsWith("banner-") | gadgets.startsWith("hat-") | gadgets.startsWith("head-")) {
                                                module.getLobbyData(player).getActiveGadgets().remove(gadgets);
                                            }
                                        });

                                        player.getInventory().setHelmet(module.getGadgets().get("hat-smile"));
                                        player.playSound(player.getLocation(), Sound.WOOD_CLICK, 0.5F, 0.74F);
                                        player.sendMessage(module.getSuperAPI().getPrefix() + "Du hast den Hut §8» " + event.getCurrentItem().getItemMeta().getDisplayName() + " §7ausgewählt§8.");
                                        module.getLobbyData(player).getActiveGadgets().add("hat-smile");
                                        player.closeInventory();

                                    } else {
                                        player.sendMessage(module.getSuperAPI().getPrefix() + "Du musst diesen §bHut §7erst im §bCoinshop §7kaufen§8!");
                                    }
                                    break;

                                case "§8•§7● Melone":
                                    if (module.getLobbyData(player).getGadgetsBought().contains("hat-melon") 
                                            | module.getLobbyData(player).getGadgetsBought().contains("hat-*")
                                            | module.getLobbyData(player).getGadgetsBought().contains("*")) {

                                        module.getLobbyData(player).getActiveGadgets().forEach((gadgets) -> {
                                            if (gadgets.startsWith("banner-") | gadgets.startsWith("hat-") | gadgets.startsWith("head-")) {
                                                module.getLobbyData(player).getActiveGadgets().remove(gadgets);
                                            }
                                        });

                                        player.getInventory().setHelmet(module.getGadgets().get("hat-melon"));
                                        player.playSound(player.getLocation(), Sound.WOOD_CLICK, 0.5F, 0.74F);
                                        player.sendMessage(module.getSuperAPI().getPrefix() + "Du hast den Hut §8» " + event.getCurrentItem().getItemMeta().getDisplayName() + " §7ausgewählt§8.");
                                        module.getLobbyData(player).getActiveGadgets().add("hat-melon");
                                        player.closeInventory();

                                    } else {
                                        player.sendMessage(module.getSuperAPI().getPrefix() + "Du musst diesen §bHut §7erst im §bCoinshop §7kaufen§8!");
                                    }
                                    break;

                                case "§8•§7● Kürbis":
                                    if (module.getLobbyData(player).getGadgetsBought().contains("hat-pumpkin") 
                                            | module.getLobbyData(player).getGadgetsBought().contains("hat-*")
                                            | module.getLobbyData(player).getGadgetsBought().contains("*")) {

                                        module.getLobbyData(player).getActiveGadgets().forEach((gadgets) -> {
                                            if (gadgets.startsWith("banner-") | gadgets.startsWith("hat-") | gadgets.startsWith("head-")) {
                                                module.getLobbyData(player).getActiveGadgets().remove(gadgets);
                                            }
                                        });

                                        player.getInventory().setHelmet(module.getGadgets().get("hat-pumpkin"));
                                        player.playSound(player.getLocation(), Sound.WOOD_CLICK, 0.5F, 0.74F);
                                        player.sendMessage(module.getSuperAPI().getPrefix() + "Du hast den Hut §8» " + event.getCurrentItem().getItemMeta().getDisplayName() + " §7ausgewählt§8.");
                                        module.getLobbyData(player).getActiveGadgets().add("hat-pumpkin");
                                        player.closeInventory();

                                    } else {
                                        player.sendMessage(module.getSuperAPI().getPrefix() + "Du musst diesen §bHut §7erst im §bCoinshop §7kaufen§8!");
                                    }
                                    break;

                                case "§8•§7● Spongebob":
                                    if (module.getLobbyData(player).getGadgetsBought().contains("hat-spongebob") 
                                            | module.getLobbyData(player).getGadgetsBought().contains("hat-*")
                                            | module.getLobbyData(player).getGadgetsBought().contains("*")) {

                                        module.getLobbyData(player).getActiveGadgets().forEach((gadgets) -> {
                                            if (gadgets.startsWith("banner-") | gadgets.startsWith("hat-") | gadgets.startsWith("head-")) {
                                                module.getLobbyData(player).getActiveGadgets().remove(gadgets);
                                            }
                                        });

                                        player.getInventory().setHelmet(module.getGadgets().get("hat-spongebob"));
                                        player.playSound(player.getLocation(), Sound.WOOD_CLICK, 0.5F, 0.74F);
                                        player.sendMessage(module.getSuperAPI().getPrefix() + "Du hast den Hut §8» " + event.getCurrentItem().getItemMeta().getDisplayName() + " §7ausgewählt§8.");
                                        module.getLobbyData(player).getActiveGadgets().add("hat-spongebob");
                                        player.closeInventory();

                                    } else {
                                        player.sendMessage(module.getSuperAPI().getPrefix() + "Du musst diesen §bHut §7erst im §bCoinshop §7kaufen§8!");
                                    }
                                    break;

                                case "§8•§7● Maske":
                                    if (module.getLobbyData(player).getGadgetsBought().contains("hat-mask") 
                                            | module.getLobbyData(player).getGadgetsBought().contains("hat-*")
                                            | module.getLobbyData(player).getGadgetsBought().contains("*")) {

                                        module.getLobbyData(player).getActiveGadgets().forEach((gadgets) -> {
                                            if (gadgets.startsWith("banner-") | gadgets.startsWith("hat-") | gadgets.startsWith("head-")) {
                                                module.getLobbyData(player).getActiveGadgets().remove(gadgets);
                                            }
                                        });

                                        player.getInventory().setHelmet(module.getGadgets().get("hat-mask"));
                                        player.playSound(player.getLocation(), Sound.WOOD_CLICK, 0.5F, 0.74F);
                                        player.sendMessage(module.getSuperAPI().getPrefix() + "Du hast den Hut §8» " + event.getCurrentItem().getItemMeta().getDisplayName() + " §7ausgewählt§8.");
                                        module.getLobbyData(player).getActiveGadgets().add("hat-mask");
                                        player.closeInventory();

                                    } else {
                                        player.sendMessage(module.getSuperAPI().getPrefix() + "Du musst diesen §bHut §7erst im §bCoinshop §7kaufen§8!");
                                    }
                                    break;

                                case "§4•§c● Hut ausziehen":
                                    if (player.getInventory().getHelmet() != null && player.getInventory().getHelmet().getType() != Material.BANNER
                                            && player.getInventory().getHelmet().getType() != Material.SKULL_ITEM) {
                                        
                                        player.getInventory().setHelmet(null);

                                        module.getLobbyData(player).getActiveGadgets().forEach((gadgets) -> {
                                            if (gadgets.startsWith("banner-") | gadgets.startsWith("hat-") | gadgets.startsWith("head-")) {
                                                module.getLobbyData(player).getActiveGadgets().remove(gadgets);
                                            }
                                        });

                                        player.playSound(player.getLocation(), Sound.WOOD_CLICK, 0.5F, 0.74F);
                                        player.sendMessage(module.getSuperAPI().getPrefix() + "Du hast deinen §bHut §7ausgezogen§8!");
                                        player.closeInventory();
                                    } else {
                                        player.sendMessage(module.getSuperAPI().getPrefix() + "Du trägst keinen §bHut §7auf deinem Kopf§8!");
                                    }
                                    break;

                                default:
                                    break;

                            }
                            break;
                        //</editor-fold>
                            
                        //<editor-fold defaultstate="collapsed" desc="extras- heads">
                        case "§8•§7● Extras §8▍ §7Köpfe":
                            switch (event.getCurrentItem().getItemMeta().getDisplayName()) {

                                case "§8•§7● Vanilloper":
                                    if (module.getLobbyData(player).getGadgetsBought().contains("head-vanilloper")
                                            | module.getLobbyData(player).getGadgetsBought().contains("head-*")
                                            | module.getLobbyData(player).getGadgetsBought().contains("*")) {

                                        module.getLobbyData(player).getActiveGadgets().forEach((gadgets) -> {
                                            if (gadgets.startsWith("banner-") | gadgets.startsWith("hat-") | gadgets.startsWith("head-")) {
                                                module.getLobbyData(player).getActiveGadgets().remove(gadgets);
                                            }
                                        });

                                        player.getInventory().setHelmet(module.getGadgets().get("head-vanilloper"));
                                        player.playSound(player.getLocation(), Sound.WOOD_CLICK, 0.5F, 0.74F);
                                        player.sendMessage(module.getSuperAPI().getPrefix() + "Du hast den Kopf §8» " + event.getCurrentItem().getItemMeta().getDisplayName() + " §7ausgewählt§8.");
                                        module.getLobbyData(player).getActiveGadgets().add("head-vanilloper");
                                        player.closeInventory();

                                    } else {
                                        player.sendMessage(module.getSuperAPI().getPrefix() + "Du musst diesen §bKopf §7erst im §bCoinshop §7kaufen§8!");
                                    }
                                    break;

                                case "§8•§7● CyroDev":
                                    if (module.getLobbyData(player).getGadgetsBought().contains("head-cyrodev")
                                            | module.getLobbyData(player).getGadgetsBought().contains("head-*")
                                            | module.getLobbyData(player).getGadgetsBought().contains("*")) {

                                        module.getLobbyData(player).getActiveGadgets().forEach((gadgets) -> {
                                            if (gadgets.startsWith("banner-") | gadgets.startsWith("hat-") | gadgets.startsWith("head-")) {
                                                module.getLobbyData(player).getActiveGadgets().remove(gadgets);
                                            }
                                        });

                                        player.getInventory().setHelmet(module.getGadgets().get("head-cyrodev"));
                                        player.playSound(player.getLocation(), Sound.WOOD_CLICK, 0.5F, 0.74F);
                                        player.sendMessage(module.getSuperAPI().getPrefix() + "Du hast den Kopf §8» " + event.getCurrentItem().getItemMeta().getDisplayName() + " §7ausgewählt§8.");
                                        module.getLobbyData(player).getActiveGadgets().add("head-cyrodev");
                                        player.closeInventory();

                                    } else {
                                        player.sendMessage(module.getSuperAPI().getPrefix() + "Du musst diesen §bKopf §7erst im §bCoinshop §7kaufen§8!");
                                    }
                                    break;

                                case "§8•§7● SuperKlug":
                                    if (module.getLobbyData(player).getGadgetsBought().contains("head-superklug")
                                            | module.getLobbyData(player).getGadgetsBought().contains("head-*")
                                            | module.getLobbyData(player).getGadgetsBought().contains("*")) {

                                        module.getLobbyData(player).getActiveGadgets().forEach((gadgets) -> {
                                            if (gadgets.startsWith("banner-") | gadgets.startsWith("hat-") | gadgets.startsWith("head-")) {
                                                module.getLobbyData(player).getActiveGadgets().remove(gadgets);
                                            }
                                        });

                                        player.getInventory().setHelmet(module.getGadgets().get("head-superklug"));
                                        player.playSound(player.getLocation(), Sound.WOOD_CLICK, 0.5F, 0.74F);
                                        player.sendMessage(module.getSuperAPI().getPrefix() + "Du hast den Kopf §8» " + event.getCurrentItem().getItemMeta().getDisplayName() + " §7ausgewählt§8.");
                                        module.getLobbyData(player).getActiveGadgets().add("head-superklug");
                                        player.closeInventory();

                                    } else {
                                        player.sendMessage(module.getSuperAPI().getPrefix() + "Du musst diesen §bKopf §7erst im §bCoinshop §7kaufen§8!");
                                    }
                                    break;

                                case "§8•§7● ungespielt":
                                    if (module.getLobbyData(player).getGadgetsBought().contains("head-ungespielt")
                                            | module.getLobbyData(player).getGadgetsBought().contains("head-*")
                                            | module.getLobbyData(player).getGadgetsBought().contains("*")) {

                                        module.getLobbyData(player).getActiveGadgets().forEach((gadgets) -> {
                                            if (gadgets.startsWith("banner-") | gadgets.startsWith("hat-") | gadgets.startsWith("head-")) {
                                                module.getLobbyData(player).getActiveGadgets().remove(gadgets);
                                            }
                                        });

                                        player.getInventory().setHelmet(module.getGadgets().get("head-ungespielt"));
                                        player.playSound(player.getLocation(), Sound.WOOD_CLICK, 0.5F, 0.74F);
                                        player.sendMessage(module.getSuperAPI().getPrefix() + "Du hast den Kopf §8» " + event.getCurrentItem().getItemMeta().getDisplayName() + " §7ausgewählt§8.");
                                        module.getLobbyData(player).getActiveGadgets().add("head-ungespielt");
                                        player.closeInventory();

                                    } else {
                                        player.sendMessage(module.getSuperAPI().getPrefix() + "Du musst diesen §bKopf §7erst im §bCoinshop §7kaufen§8!");
                                    }
                                    break;

                                case "§8•§7● rewinside":
                                    if (module.getLobbyData(player).getGadgetsBought().contains("head-rewinside")
                                            | module.getLobbyData(player).getGadgetsBought().contains("head-*")
                                            | module.getLobbyData(player).getGadgetsBought().contains("*")) {

                                        module.getLobbyData(player).getActiveGadgets().forEach((gadgets) -> {
                                            if (gadgets.startsWith("banner-") | gadgets.startsWith("hat-") | gadgets.startsWith("head-")) {
                                                module.getLobbyData(player).getActiveGadgets().remove(gadgets);
                                            }
                                        });

                                        player.getInventory().setHelmet(module.getGadgets().get("head-rewinside"));
                                        player.playSound(player.getLocation(), Sound.WOOD_CLICK, 0.5F, 0.74F);
                                        player.sendMessage(module.getSuperAPI().getPrefix() + "Du hast den Kopf §8» " + event.getCurrentItem().getItemMeta().getDisplayName() + " §7ausgewählt§8.");
                                        module.getLobbyData(player).getActiveGadgets().add("head-rewinside");
                                        player.closeInventory();

                                    } else {
                                        player.sendMessage(module.getSuperAPI().getPrefix() + "Du musst diesen §bKopf §7erst im §bCoinshop §7kaufen§8!");
                                    }
                                    break;

                                case "§8•§7● GommeHD":
                                    if (module.getLobbyData(player).getGadgetsBought().contains("head-gommehd")
                                            | module.getLobbyData(player).getGadgetsBought().contains("head-*")
                                            | module.getLobbyData(player).getGadgetsBought().contains("*")) {

                                        module.getLobbyData(player).getActiveGadgets().forEach((gadgets) -> {
                                            if (gadgets.startsWith("banner-") | gadgets.startsWith("hat-") | gadgets.startsWith("head-")) {
                                                module.getLobbyData(player).getActiveGadgets().remove(gadgets);
                                            }
                                        });

                                        player.getInventory().setHelmet(module.getGadgets().get("head-gommehd"));
                                        player.playSound(player.getLocation(), Sound.WOOD_CLICK, 0.5F, 0.74F);
                                        player.sendMessage(module.getSuperAPI().getPrefix() + "Du hast den Kopf §8» " + event.getCurrentItem().getItemMeta().getDisplayName() + " §7ausgewählt§8.");
                                        module.getLobbyData(player).getActiveGadgets().add("head-gommehd");
                                        player.closeInventory();

                                    } else {
                                        player.sendMessage(module.getSuperAPI().getPrefix() + "Du musst diesen §bKopf §7erst im §bCoinshop §7kaufen§8!");
                                    }
                                    break;

                                case "§8•§7● Vareide":
                                    if (module.getLobbyData(player).getGadgetsBought().contains("head-vareide")
                                            | module.getLobbyData(player).getGadgetsBought().contains("head-*")
                                            | module.getLobbyData(player).getGadgetsBought().contains("*")) {

                                        module.getLobbyData(player).getActiveGadgets().forEach((gadgets) -> {
                                            if (gadgets.startsWith("banner-") | gadgets.startsWith("hat-") | gadgets.startsWith("head-")) {
                                                module.getLobbyData(player).getActiveGadgets().remove(gadgets);
                                            }
                                        });

                                        player.getInventory().setHelmet(module.getGadgets().get("head-vareide"));
                                        player.playSound(player.getLocation(), Sound.WOOD_CLICK, 0.5F, 0.74F);
                                        player.sendMessage(module.getSuperAPI().getPrefix() + "Du hast den Kopf §8» " + event.getCurrentItem().getItemMeta().getDisplayName() + " §7ausgewählt§8.");
                                        module.getLobbyData(player).getActiveGadgets().add("head-vareide");
                                        player.closeInventory();

                                    } else {
                                        player.sendMessage(module.getSuperAPI().getPrefix() + "Du musst diesen §bKopf §7erst im §bCoinshop §7kaufen§8!");
                                    }
                                    break;

                                case "§8•§7● Gronkh":
                                    if (module.getLobbyData(player).getGadgetsBought().contains("head-gronkh")
                                            | module.getLobbyData(player).getGadgetsBought().contains("head-*")
                                            | module.getLobbyData(player).getGadgetsBought().contains("*")) {

                                        module.getLobbyData(player).getActiveGadgets().forEach((gadgets) -> {
                                            if (gadgets.startsWith("banner-") | gadgets.startsWith("hat-") | gadgets.startsWith("head-")) {
                                                module.getLobbyData(player).getActiveGadgets().remove(gadgets);
                                            }
                                        });

                                        player.getInventory().setHelmet(module.getGadgets().get("head-gronkh"));
                                        player.playSound(player.getLocation(), Sound.WOOD_CLICK, 0.5F, 0.74F);
                                        player.sendMessage(module.getSuperAPI().getPrefix() + "Du hast den Kopf §8» " + event.getCurrentItem().getItemMeta().getDisplayName() + " §7ausgewählt§8.");
                                        module.getLobbyData(player).getActiveGadgets().add("head-gronkh");
                                        player.closeInventory();

                                    } else {
                                        player.sendMessage(module.getSuperAPI().getPrefix() + "Du musst diesen §bKopf §7erst im §bCoinshop §7kaufen§8!");
                                    }
                                    break;

                                case "§4•§c● Kopf ausziehen":
                                    if (player.getInventory().getHelmet() != null && player.getInventory().getHelmet().getType() == Material.SKULL_ITEM) {
                                        player.getInventory().setHelmet(null);

                                        module.getLobbyData(player).getActiveGadgets().forEach((gadgets) -> {
                                            if (gadgets.startsWith("banner-") | gadgets.startsWith("hat-") | gadgets.startsWith("head-")) {
                                                module.getLobbyData(player).getActiveGadgets().remove(gadgets);
                                            }
                                        });

                                        player.playSound(player.getLocation(), Sound.WOOD_CLICK, 0.5F, 0.74F);
                                        player.sendMessage(module.getSuperAPI().getPrefix() + "Du hast deinen §bKopf §7ausgezogen§8!");
                                        player.closeInventory();
                                    } else {
                                        player.sendMessage(module.getSuperAPI().getPrefix() + "Du trägst keinen §bKopf§8!");
                                    }
                                    break;

                                default:
                                    break;

                            }
                            break;
                        //</editor-fold>
                            
                        //<editor-fold defaultstate="collapsed" desc="extras- particle">
                        case "§8•§7● Extras §8▍ §7Partikel":
                            switch (event.getCurrentItem().getItemMeta().getDisplayName()) {

                                case "§8•§7● Grüne§8-§7Funken":
                                    if (module.getLobbyData(player).getGadgetsBought().contains("particle-greensparks")
                                            | module.getLobbyData(player).getGadgetsBought().contains("particle-*")
                                            | module.getLobbyData(player).getGadgetsBought().contains("*")) {

                                        module.getLobbyData(player).getActiveGadgets().forEach((gadgets) -> {
                                            if(gadgets.startsWith("particle-")) {
                                                module.getLobbyData(player).getActiveGadgets().remove(gadgets);
                                            }
                                        });

                                        player.playSound(player.getLocation(), Sound.WOOD_CLICK, 0.5F, 0.74F);
                                        player.sendMessage(module.getSuperAPI().getPrefix() + "Du hast die Partikel §8» " + event.getCurrentItem().getItemMeta().getDisplayName() + " §7ausgewählt§8.");
                                        module.getLobbyData(player).getActiveGadgets().add("particle-greensparks");
                                        player.closeInventory();

                                    } else {
                                        player.sendMessage(module.getSuperAPI().getPrefix() + "Du musst diese §bPartikel §7erst im §bCoinshop §7kaufen§8!");
                                    }
                                    break;

                                case "§8•§7● Weiße§8-§7Funken":
                                    if (module.getLobbyData(player).getGadgetsBought().contains("particle-whitesparks")
                                            | module.getLobbyData(player).getGadgetsBought().contains("particle-*")
                                            | module.getLobbyData(player).getGadgetsBought().contains("*")) {

                                        module.getLobbyData(player).getActiveGadgets().forEach((gadgets) -> {
                                            if (gadgets.startsWith("particle-")) {
                                                module.getLobbyData(player).getActiveGadgets().remove(gadgets);
                                            }
                                        });

                                        player.playSound(player.getLocation(), Sound.WOOD_CLICK, 0.5F, 0.74F);
                                        player.sendMessage(module.getSuperAPI().getPrefix() + "Du hast die Partikel §8» " + event.getCurrentItem().getItemMeta().getDisplayName() + " §7ausgewählt§8.");
                                        module.getLobbyData(player).getActiveGadgets().add("particle-whitesparks");
                                        player.closeInventory();

                                    } else {
                                        player.sendMessage(module.getSuperAPI().getPrefix() + "Du musst diese §bPartikel §7erst im §bCoinshop §7kaufen§8!");
                                    }
                                    break;

                                case "§8•§7● Lila§8-§7Funken":
                                    if (module.getLobbyData(player).getGadgetsBought().contains("particle-purplesparks")
                                            | module.getLobbyData(player).getGadgetsBought().contains("particle-*")
                                            | module.getLobbyData(player).getGadgetsBought().contains("*")) {

                                        module.getLobbyData(player).getActiveGadgets().forEach((gadgets) -> {
                                            if (gadgets.startsWith("particle-")) {
                                                module.getLobbyData(player).getActiveGadgets().remove(gadgets);
                                            }
                                        });

                                        player.playSound(player.getLocation(), Sound.WOOD_CLICK, 0.5F, 0.74F);
                                        player.sendMessage(module.getSuperAPI().getPrefix() + "Du hast die Partikel §8» " + event.getCurrentItem().getItemMeta().getDisplayName() + " §7ausgewählt§8.");
                                        module.getLobbyData(player).getActiveGadgets().add("particle-purplesparks");
                                        player.closeInventory();

                                    } else {
                                        player.sendMessage(module.getSuperAPI().getPrefix() + "Du musst diese §bPartikel §7erst im §bCoinshop §7kaufen§8!");
                                    }
                                    break;

                                case "§8•§7● Flammen":
                                    if (module.getLobbyData(player).getGadgetsBought().contains("particle-flames")
                                            | module.getLobbyData(player).getGadgetsBought().contains("particle-*")
                                            | module.getLobbyData(player).getGadgetsBought().contains("*")) {

                                        module.getLobbyData(player).getActiveGadgets().forEach((gadgets) -> {
                                            if (gadgets.startsWith("particle-")) {
                                                module.getLobbyData(player).getActiveGadgets().remove(gadgets);
                                            }
                                        });

                                        player.playSound(player.getLocation(), Sound.WOOD_CLICK, 0.5F, 0.74F);
                                        player.sendMessage(module.getSuperAPI().getPrefix() + "Du hast die Partikel §8» " + event.getCurrentItem().getItemMeta().getDisplayName() + " §7ausgewählt§8.");
                                        module.getLobbyData(player).getActiveGadgets().add("particle-flames");
                                        player.closeInventory();

                                    } else {
                                        player.sendMessage(module.getSuperAPI().getPrefix() + "Du musst diese §bPartikel §7erst im §bCoinshop §7kaufen§8!");
                                    }
                                    break;

                                case "§8•§7● Portal":
                                    if (module.getLobbyData(player).getGadgetsBought().contains("particle-portal")
                                            | module.getLobbyData(player).getGadgetsBought().contains("particle-*")
                                            | module.getLobbyData(player).getGadgetsBought().contains("*")) {

                                        module.getLobbyData(player).getActiveGadgets().forEach((gadgets) -> {
                                            if (gadgets.startsWith("particle-")) {
                                                module.getLobbyData(player).getActiveGadgets().remove(gadgets);
                                            }
                                        });

                                        player.playSound(player.getLocation(), Sound.WOOD_CLICK, 0.5F, 0.74F);
                                        player.sendMessage(module.getSuperAPI().getPrefix() + "Du hast die Partikel §8» " + event.getCurrentItem().getItemMeta().getDisplayName() + " §7ausgewählt§8.");
                                        module.getLobbyData(player).getActiveGadgets().add("particle-portal");
                                        player.closeInventory();

                                    } else {
                                        player.sendMessage(module.getSuperAPI().getPrefix() + "Du musst diese §bPartikel §7erst im §bCoinshop §7kaufen§8!");
                                    }
                                    break;

                                case "§8•§7● Buchstaben":
                                    if (module.getLobbyData(player).getGadgetsBought().contains("particle-letters")
                                            | module.getLobbyData(player).getGadgetsBought().contains("particle-*")
                                            | module.getLobbyData(player).getGadgetsBought().contains("*")) {

                                        module.getLobbyData(player).getActiveGadgets().forEach((gadgets) -> {
                                            if (gadgets.startsWith("particle-")) {
                                                module.getLobbyData(player).getActiveGadgets().remove(gadgets);
                                            }
                                        });

                                        player.playSound(player.getLocation(), Sound.WOOD_CLICK, 0.5F, 0.74F);
                                        player.sendMessage(module.getSuperAPI().getPrefix() + "Du hast die Partikel §8» " + event.getCurrentItem().getItemMeta().getDisplayName() + " §7ausgewählt§8.");
                                        module.getLobbyData(player).getActiveGadgets().add("particle-letters");
                                        player.closeInventory();

                                    } else {
                                        player.sendMessage(module.getSuperAPI().getPrefix() + "Du musst diese §bPartikel §7erst im §bCoinshop §7kaufen§8!");
                                    }
                                    break;

                                case "§8•§7● Lava§8-§7Funken":
                                    if (module.getLobbyData(player).getGadgetsBought().contains("particle-lavasparks")
                                            | module.getLobbyData(player).getGadgetsBought().contains("particle-*")
                                            | module.getLobbyData(player).getGadgetsBought().contains("*")) {

                                        module.getLobbyData(player).getActiveGadgets().forEach((gadgets) -> {
                                            if (gadgets.startsWith("particle-")) {
                                                module.getLobbyData(player).getActiveGadgets().remove(gadgets);
                                            }
                                        });

                                        player.playSound(player.getLocation(), Sound.WOOD_CLICK, 0.5F, 0.74F);
                                        player.sendMessage(module.getSuperAPI().getPrefix() + "Du hast die Partikel §8» " + event.getCurrentItem().getItemMeta().getDisplayName() + " §7ausgewählt§8.");
                                        module.getLobbyData(player).getActiveGadgets().add("particle-lavasparks");
                                        player.closeInventory();

                                    } else {
                                        player.sendMessage(module.getSuperAPI().getPrefix() + "Du musst diese §bPartikel §7erst im §bCoinshop §7kaufen§8!");
                                    }
                                    break;

                                case "§8•§7● Regenbogen":
                                    if (module.getLobbyData(player).getGadgetsBought().contains("particle-rainbow")
                                            | module.getLobbyData(player).getGadgetsBought().contains("particle-*")
                                            | module.getLobbyData(player).getGadgetsBought().contains("*")) {

                                        module.getLobbyData(player).getActiveGadgets().forEach((gadgets) -> {
                                            if (gadgets.startsWith("particle-")) {
                                                module.getLobbyData(player).getActiveGadgets().remove(gadgets);
                                            }
                                        });

                                        player.playSound(player.getLocation(), Sound.WOOD_CLICK, 0.5F, 0.74F);
                                        player.sendMessage(module.getSuperAPI().getPrefix() + "Du hast die Partikel §8» " + event.getCurrentItem().getItemMeta().getDisplayName() + " §7ausgewählt§8.");
                                        module.getLobbyData(player).getActiveGadgets().add("particle-rainbow");
                                        player.closeInventory();

                                    } else {
                                        player.sendMessage(module.getSuperAPI().getPrefix() + "Du musst diese §bPartikel §7erst im §bCoinshop §7kaufen§8!");
                                    }
                                    break;

                                case "§4•§c● Partikel deaktivieren":
                                    
                                    module.getLobbyData(player).getActiveGadgets().forEach((gadgets) -> {
                                        if(gadgets.startsWith("particle-")) {
                                            module.getLobbyData(player).getActiveGadgets().remove(gadgets);
                                        } else {
                                            player.sendMessage(module.getSuperAPI().getPrefix() + "Du hast keine §bPartikel §7aktiviert§8!");
                                            return;
                                        }
                                    });
                                    
                                    player.playSound(player.getLocation(), Sound.WOOD_CLICK, 0.5F, 0.74F);
                                    player.sendMessage(module.getSuperAPI().getPrefix() + "Du hast deine §bPartikel §7deaktiviert§8!");
                                    player.closeInventory();
                                    break;

                                default:
                                    break;

                            }
                            break;
                        //</editor-fold>
                            
                        //<editor-fold defaultstate="collapsed" desc="extras- spezial">
                        case "§8•§7● Extras §8▍ §eSpezial":
                            switch (event.getCurrentItem().getItemMeta().getDisplayName()) {

                                case "§6•§e● Doppel§8-§eSprung §8▍ §aAktiviert": case "§6•§e● Doppel§8-§eSprung §8▍ §cDeaktiviert":
                                    if (module.getUserData(player).getRankId() >= 1) {

                                        if (!module.getLobbyData(player).getActivePremiumGadgets().contains("doublejump")) {

                                            module.getLobbyData(player).getActivePremiumGadgets().add("doublejump");

                                            player.getOpenInventory().setItem(10, module.getSuperAPI().item(Material.FEATHER)
                                                    .setDisplayname("§6•§e● Doppel§8-§eSprung §8▍ §aAktiviert")
                                                    .setLore(" ", "§8► §7Nur für Spieler mit dem Rang §6Gold§8/§aEmerald§8/§3Diamond §7und höher§8!").build());
                                            player.updateInventory();

                                        } else {

                                            module.getLobbyData(player).getActivePremiumGadgets().remove("doublejump");

                                            player.getOpenInventory().setItem(10, module.getSuperAPI().item(Material.FEATHER)
                                                    .setDisplayname("§6•§e● Doppel§8-§eSprung §8▍ §cDeaktiviert")
                                                    .setLore(" ", "§8► §7Nur für Spieler mit dem Rang §6Gold§8/§aEmerald§8/§3Diamond §7und höher§8!").build());
                                            player.updateInventory();

                                        }

                                    } else {
                                        player.sendMessage(module.getSuperAPI().getPrefix() + "Nur für Spieler mit dem Rang §6Gold§8/§aEmerald§8/§3Diamond §7und höher§8!");
                                    }
                                    break;
                                    
                                case "§6•§e● Jetpack §8▍ §aAktiviert": case "§6•§e● Jetpack §8▍ §cDeaktiviert":
                                    if (module.getUserData(player).getRankId() >= 1) {

                                        if(!module.getLobbyData(player).getActivePremiumGadgets().contains("jetpack")) {
                                            
                                            module.getLobbyData(player).getActivePremiumGadgets().add("jetpack");

                                            player.getOpenInventory().setItem(12, module.getSuperAPI().item(Material.FIREWORK)
                                                    .setDisplayname("§6•§e● Jetpack §8▍ §aAktiviert")
                                                    .setLore(" ", "§8► §7Nur für Spieler mit dem Rang §6Gold§8/§aEmerald§8/§3Diamond §7und höher§8!").build());
                                            player.updateInventory();

                                        } else {

                                            module.getLobbyData(player).getActivePremiumGadgets().remove("jetpack");
                                            
                                            player.getOpenInventory().setItem(12, module.getSuperAPI().item(Material.FIREWORK)
                                                    .setDisplayname("§6•§e● Jetpack §8▍ §cDeaktiviert")
                                                    .setLore(" ", "§8► §7Nur für Spieler mit dem Rang §6Gold§8/§aEmerald§8/§3Diamond §7und höher§8!").build());
                                            player.updateInventory();

                                        }

                                    } else {
                                        player.sendMessage(module.getSuperAPI().getPrefix() + "Nur für Spieler mit dem Rang §6Gold§8/§aEmerald§8/§3Diamond §7und höher§8!");
                                    }
                                    break;
                                    
                                case "§6•§e● Disco§8-§eSchuhe §8▍ §aAktiviert": case "§6•§e● Disco§8-§eSchuhe §8▍ §cDeaktiviert":
                                    if (module.getUserData(player).getRankId() >= 1) {

                                        if (!module.getLobbyData(player).getActivePremiumGadgets().contains("discoboots")) {

                                            module.getLobbyData(player).getActivePremiumGadgets().add("discoboots");

                                            player.getOpenInventory().setItem(14, module.getSuperAPI().item(Material.LEATHER_BOOTS)
                                                    .setDisplayname("§6•§e● Disco§8-§eSchuhe §8▍ §aAktiviert")
                                                    .setLore(" ", "§8► §7Nur für Spieler mit dem Rang §6Gold§8/§aEmerald§8/§3Diamond §7und höher§8!").build());
                                            player.updateInventory();

                                        } else {

                                            module.getLobbyData(player).getActivePremiumGadgets().remove("discoboots");
                                            player.getInventory().setBoots(null);

                                            player.getOpenInventory().setItem(14, module.getSuperAPI().item(Material.LEATHER_BOOTS)
                                                    .setDisplayname("§6•§e● Disco§8-§eSchuhe §8▍ §cDeaktiviert")
                                                    .setLore(" ", "§8► §7Nur für Spieler mit dem Rang §6Gold§8/§aEmerald§8/§3Diamond §7und höher§8!").build());
                                            player.updateInventory();

                                        }

                                    } else {
                                        player.sendMessage(module.getSuperAPI().getPrefix() + "Nur für Spieler mit dem Rang §6Gold§8/§aEmerald§8/§3Diamond §7und höher§8!");
                                    }
                                    break;
                                    
                                case "§6•§e● Disco§8-§eHut §8▍ §aAktiviert": case "§6•§e● Disco§8-§eHut §8▍ §cDeaktiviert":
                                    if (module.getUserData(player).getRankId() >= 1) {

                                        if (!module.getLobbyData(player).getActivePremiumGadgets().contains("discohat")) {

                                            module.getLobbyData(player).getActivePremiumGadgets().add("discohat");

                                            player.getOpenInventory().setItem(16, module.getSuperAPI().item(Material.STAINED_GLASS)
                                                    .setDisplayname("§6•§e● Disco§8-§eHut §8▍ §aAktiviert")
                                                    .setLore(" ", "§8► §7Nur für Spieler mit dem Rang §6Gold§8/§aEmerald§8/§3Diamond §7und höher§8!").build());
                                            player.updateInventory();

                                        } else {

                                            module.getLobbyData(player).getActivePremiumGadgets().remove("discohat");
                                            player.getInventory().setHelmet(null);
                                            
                                            if (!module.getLobbyData(player).getActiveGadgets().isEmpty()) {
                                                module.getLobbyData(player).getActiveGadgets().forEach((gadgets) -> {

                                                    if (gadgets.startsWith("banner-")) {
                                                        if (module.getLobbyData(player).getGadgetsBought().contains(gadgets)
                                                                | module.getLobbyData(player).getGadgetsBought().contains("banner-*")
                                                                | module.getLobbyData(player).getGadgetsBought().contains("*")) {

                                                            player.getInventory().setHelmet(module.getGadgets().get(gadgets));

                                                        }
                                                    }

                                                    if (gadgets.startsWith("hat-")) {
                                                        if (module.getLobbyData(player).getGadgetsBought().contains(gadgets)
                                                                | module.getLobbyData(player).getGadgetsBought().contains("hat-*")
                                                                | module.getLobbyData(player).getGadgetsBought().contains("*")) {

                                                            player.getInventory().setHelmet(module.getGadgets().get(gadgets));

                                                        }
                                                    }

                                                    if (gadgets.startsWith("head-")) {
                                                        if (module.getLobbyData(player).getGadgetsBought().contains(gadgets)
                                                                | module.getLobbyData(player).getGadgetsBought().contains("head-*")
                                                                | module.getLobbyData(player).getGadgetsBought().contains("*")) {

                                                            player.getInventory().setHelmet(module.getGadgets().get(gadgets));

                                                        }
                                                    }

                                                });
                                            }

                                            player.getOpenInventory().setItem(16, module.getSuperAPI().item(Material.STAINED_GLASS)
                                                    .setDisplayname("§6•§e● Disco§8-§eHut §8▍ §cDeaktiviert")
                                                    .setLore(" ", "§8► §7Nur für Spieler mit dem Rang §6Gold§8/§aEmerald§8/§3Diamond §7und höher§8!").build());
                                            player.updateInventory();

                                        }

                                    } else {
                                        player.sendMessage(module.getSuperAPI().getPrefix() + "Nur für Spieler mit dem Rang §6Gold§8/§aEmerald§8/§3Diamond §7und höher§8!");
                                    }
                                    break;
                                    
                                case "§6•§e● Ballon §8▍ §aAktiviert": case "§6•§e● Ballon §8▍ §cDeaktiviert":
                                    if (module.getUserData(player).getRankId() >= 1) {

                                        if (!module.getLobbyData(player).getActivePremiumGadgets().contains("ballon")) {

                                            module.getLobbyData(player).getActivePremiumGadgets().add("ballon");

                                            player.getOpenInventory().setItem(28, module.getSuperAPI().item(Material.LEASH)
                                                    .setDisplayname("§6•§e● Ballon §8▍ §aAktiviert")
                                                    .setLore(" ", "§8► §7Nur für Spieler mit dem Rang §6Gold§8/§aEmerald§8/§3Diamond §7und höher§8!").build());
                                            player.updateInventory();

                                            module.spawnBallon(player);
                                        } else {

                                            module.getLobbyData(player).getActivePremiumGadgets().remove("ballon");

                                            player.getOpenInventory().setItem(28, module.getSuperAPI().item(Material.LEASH)
                                                    .setDisplayname("§6•§e● Ballon §8▍ §cDeaktiviert")
                                                    .setLore(" ", "§8► §7Nur für Spieler mit dem Rang §6Gold§8/§aEmerald§8/§3Diamond §7und höher§8!").build());
                                            player.updateInventory();

                                            if (module.getPlayerBallonBlock().containsKey(player)) {
                                                module.getPlayerBallonBlock().get(player).remove();
                                                module.getPlayerBallonBlock().remove(player);
                                            }

                                            if (module.getPlayerBallonBat().containsKey(player)) {
                                                module.getPlayerBallonBat().get(player).remove();
                                                module.getPlayerBallonBat().remove(player);
                                            }

                                        }

                                    } else {
                                        player.sendMessage(module.getSuperAPI().getPrefix() + "Nur für Spieler mit dem Rang §6Gold§8/§aEmerald§8/§3Diamond §7und höher§8!");
                                    }
                                    break;
                                    
                                case "§6•§e● Stacker §8▍ §aAktiviert": case "§6•§e● Stacker §8▍ §cDeaktiviert":
                                    if (module.getUserData(player).getRankId() >= 1) {

                                        if (!module.getLobbyData(player).getActivePremiumGadgets().contains("stacker")) {

                                            module.getLobbyData(player).getActivePremiumGadgets().add("stacker");

                                            player.getOpenInventory().setItem(30, module.getSuperAPI().item(Material.SADDLE)
                                                    .setDisplayname("§6•§e● Stacker §8▍ §aAktiviert")
                                                    .setLore(" ", "§8► §7Nur für Spieler mit dem Rang §6Gold§8/§aEmerald§8/§3Diamond §7und höher§8!").build());
                                            player.updateInventory();

                                        } else {

                                            module.getLobbyData(player).getActivePremiumGadgets().remove("stacker");

                                            player.getOpenInventory().setItem(30, module.getSuperAPI().item(Material.SADDLE)
                                                    .setDisplayname("§6•§e● Stacker §8▍ §cDeaktiviert")
                                                    .setLore(" ", "§8► §7Nur für Spieler mit dem Rang §6Gold§8/§aEmerald§8/§3Diamond §7und höher§8!").build());
                                            player.updateInventory();

                                        }

                                    } else {
                                        player.sendMessage(module.getSuperAPI().getPrefix() + "Nur für Spieler mit dem Rang §6Gold§8/§aEmerald§8/§3Diamond §7und höher§8!");
                                    }
                                    break;
                                    
                                case "§6•§e● Haustiere":
                                    if (module.getUserData(player).getRankId() >= 1) {

                                        final Inventory inventory = Bukkit.createInventory(null, 9 * 5, "§8•§7● Extras §8▍ §eHaustiere");

                                        for (int i = 0; i < inventory.getSize(); i++) {
                                            inventory.setItem(i, module.getInventoryManager().getFill());
                                        }
                                        
                                        player.openInventory(inventory);
                                        
                                    } else {
                                        player.sendMessage(module.getSuperAPI().getPrefix() + "Nur für Spieler mit dem Rang §6Gold§8/§aEmerald§8/§3Diamond §7und höher§8!");
                                    }
                                    break;
                                    
                                case "§6•§e● Umkleide":
                                    if (module.getUserData(player).getRankId() >= 1) {
                                        
                                        final Inventory inventory = Bukkit.createInventory(null, 9 * 5, "§8•§7● Extras §8▍ §eUmkleide");

                                        for (int i = 0; i < inventory.getSize(); i++) {
                                            inventory.setItem(i, module.getInventoryManager().getFill());
                                        }

                                        player.openInventory(inventory);
                                        
                                    } else {
                                        player.sendMessage(module.getSuperAPI().getPrefix() + "Nur für Spieler mit dem Rang §6Gold§8/§aEmerald§8/§3Diamond §7und höher§8!");
                                    }
                                    break;

                                default:
                                    break;

                            }
                            break;
                        //</editor-fold>

                        //<editor-fold defaultstate="collapsed" desc="extras">
                        case "§8•§7● Extras":
                            if (event.getCurrentItem().getType() != Material.STAINED_GLASS_PANE) {
                                player.closeInventory();
                                player.playSound(player.getLocation(), Sound.CLICK, 0.5F, 1F);
                            }

                            switch (event.getCurrentItem().getItemMeta().getDisplayName()) {

                                case "§8•§7● Banner": {
                                    final Inventory inventory = Bukkit.createInventory(null, 9 * 5, "§8•§7● Extras §8▍ §7Banner");

                                    for (int i = 0; i < inventory.getSize(); i++) {
                                        inventory.setItem(i, module.getInventoryManager().getFill());
                                    }

                                    inventory.setItem(10, module.getSuperAPI().item(Material.BANNER)
                                            .setDisplayname("§8•§7● Weiß").setLore(module.getBoughtLore(player, "banner-white")).setBannerBaseDyeColor(DyeColor.WHITE).build());

                                    inventory.setItem(12, module.getSuperAPI().item(Material.BANNER)
                                            .setDisplayname("§8•§7● Orange").setLore(module.getBoughtLore(player, "banner-orange")).setBannerBaseDyeColor(DyeColor.ORANGE).build());

                                    inventory.setItem(14, module.getSuperAPI().item(Material.BANNER)
                                            .setDisplayname("§8•§7● Pirat").setLore(module.getBoughtLore(player, "banner-pirate")).setBannerBaseDyeColor(DyeColor.BLACK)
                                            .addBannerPattern(new Pattern(DyeColor.BLACK, PatternType.STRIPE_SMALL))
                                            .addBannerPattern(new Pattern(DyeColor.WHITE, PatternType.SKULL))
                                            .addBannerPattern(new Pattern(DyeColor.BLACK, PatternType.GRADIENT_UP)).build());

                                    inventory.setItem(16, module.getSuperAPI().item(Material.BANNER)
                                            .setDisplayname("§8•§7● Deutschland").setLore(module.getBoughtLore(player, "banner-germany")).setBannerBaseDyeColor(DyeColor.RED)
                                            .addBannerPattern(new Pattern(DyeColor.BLACK, PatternType.STRIPE_LEFT))
                                            .addBannerPattern(new Pattern(DyeColor.YELLOW, PatternType.STRIPE_RIGHT))
                                            .addBannerPattern(new Pattern(DyeColor.RED, PatternType.STRIPE_CENTER)).build());

                                    inventory.setItem(28, module.getSuperAPI().item(Material.BANNER)
                                            .setDisplayname("§8•§7● Geist").setLore(module.getBoughtLore(player, "banner-ghost")).setBannerBaseDyeColor(DyeColor.BLACK)
                                            .addBannerPattern(new Pattern(DyeColor.WHITE, PatternType.SKULL))
                                            .addBannerPattern(new Pattern(DyeColor.WHITE, PatternType.CURLY_BORDER))
                                            .addBannerPattern(new Pattern(DyeColor.WHITE, PatternType.CROSS))
                                            .addBannerPattern(new Pattern(DyeColor.WHITE, PatternType.CREEPER))
                                            .addBannerPattern(new Pattern(DyeColor.WHITE, PatternType.STRIPE_TOP)).build());

                                    inventory.setItem(30, module.getSuperAPI().item(Material.BANNER)
                                            .setDisplayname("§8•§7● Mojang").setLore(module.getBoughtLore(player, "banner-mojang")).setBannerBaseDyeColor(DyeColor.WHITE)
                                            .addBannerPattern(new Pattern(DyeColor.ORANGE, PatternType.GRADIENT))
                                            .addBannerPattern(new Pattern(DyeColor.ORANGE, PatternType.GRADIENT_UP))
                                            .addBannerPattern(new Pattern(DyeColor.BROWN, PatternType.GRADIENT))
                                            .addBannerPattern(new Pattern(DyeColor.BROWN, PatternType.GRADIENT_UP))
                                            .addBannerPattern(new Pattern(DyeColor.BLACK, PatternType.MOJANG))
                                            .addBannerPattern(new Pattern(DyeColor.YELLOW, PatternType.MOJANG)).build());

                                    inventory.setItem(32, module.getSuperAPI().item(Material.BANNER)
                                            .setDisplayname("§8•§7● Tintenfisch").setLore(module.getBoughtLore(player, "banner-octopos")).setBannerBaseDyeColor(DyeColor.WHITE)
                                            .addBannerPattern(new Pattern(DyeColor.BLACK, PatternType.CURLY_BORDER))
                                            .addBannerPattern(new Pattern(DyeColor.CYAN, PatternType.HALF_HORIZONTAL_MIRROR))
                                            .addBannerPattern(new Pattern(DyeColor.CYAN, PatternType.STRIPE_TOP))
                                            .addBannerPattern(new Pattern(DyeColor.CYAN, PatternType.STRIPE_CENTER))
                                            .addBannerPattern(new Pattern(DyeColor.CYAN, PatternType.BORDER))
                                            .addBannerPattern(new Pattern(DyeColor.BLACK, PatternType.GRADIENT_UP)).build());

                                    inventory.setItem(34, module.getSuperAPI().item(Material.BANNER)
                                            .setDisplayname("§8•§7● Creeper").setLore(module.getBoughtLore(player, "banner-creeper")).setBannerBaseDyeColor(DyeColor.WHITE)
                                            .addBannerPattern(new Pattern(DyeColor.LIME, PatternType.GRADIENT_UP))
                                            .addBannerPattern(new Pattern(DyeColor.GREEN, PatternType.FLOWER))
                                            .addBannerPattern(new Pattern(DyeColor.GRAY, PatternType.TRIANGLES_BOTTOM))
                                            .addBannerPattern(new Pattern(DyeColor.LIME, PatternType.SQUARE_BOTTOM_LEFT))
                                            .addBannerPattern(new Pattern(DyeColor.GRAY, PatternType.TRIANGLES_TOP))
                                            .addBannerPattern(new Pattern(DyeColor.GREEN, PatternType.CURLY_BORDER))
                                            .addBannerPattern(new Pattern(DyeColor.LIME, PatternType.BRICKS))
                                            .addBannerPattern(new Pattern(DyeColor.BLACK, PatternType.CREEPER)).build());

                                    inventory.setItem(44, module.getSuperAPI().item(Material.BARRIER).setDisplayname("§4•§c● Banner ausziehen").build());

                                    player.openInventory(inventory);
                                }

                                break;

                                case "§8•§7● Hüte": {
                                    final Inventory inventory = Bukkit.createInventory(null, 9 * 5, "§8•§7● Extras §8▍ §7Hüte");

                                    for (int i = 0; i < inventory.getSize(); i++) {
                                        inventory.setItem(i, module.getInventoryManager().getFill());
                                    }

                                    inventory.setItem(10, module.getSuperAPI().item(Material.GLASS)
                                            .setDisplayname("§8•§7● Taucher").setLore(module.getBoughtLore(player, "hat-driver")).build());

                                    inventory.setItem(12, module.getSuperAPI().item(Material.STAINED_GLASS)
                                            .setDisplayname("§8•§7● Astronaut").setLore(module.getBoughtLore(player, "hat-astronaut")).build());

                                    inventory.setItem(14, module.getSuperAPI().item(Material.TNT)
                                            .setDisplayname("§8•§7● TNT").setLore(module.getBoughtLore(player, "hat-tnt")).build());

                                    inventory.setItem(16, module.getSuperAPI().item(Material.DROPPER)
                                            .setDisplayname("§8•§7● Smile").setLore(module.getBoughtLore(player, "hat-smile")).build());

                                    inventory.setItem(28, module.getSuperAPI().item(Material.MELON_BLOCK)
                                            .setDisplayname("§8•§7● Melone").setLore(module.getBoughtLore(player, "hat-melon")).build());

                                    inventory.setItem(30, module.getSuperAPI().item(Material.PUMPKIN)
                                            .setDisplayname("§8•§7● Kürbis").setLore(module.getBoughtLore(player, "hat-pumpkin")).build());

                                    inventory.setItem(32, module.getSuperAPI().item(Material.SPONGE)
                                            .setDisplayname("§8•§7● Spongebob").setLore(module.getBoughtLore(player, "hat-spongebob")).build());

                                    inventory.setItem(34, module.getSuperAPI().item(Material.DAYLIGHT_DETECTOR)
                                            .setDisplayname("§8•§7● Maske").setLore(module.getBoughtLore(player, "hat-mask")).build());

                                    inventory.setItem(44, module.getSuperAPI().item(Material.BARRIER)
                                            .setDisplayname("§4•§c● Hut ausziehen").build());

                                    player.openInventory(inventory);
                                }

                                break;

                                case "§8•§7● Köpfe": {
                                    final Inventory inventory = Bukkit.createInventory(null, 9 * 5, "§8•§7● Extras §8▍ §7Köpfe");

                                    for (int i = 0; i < inventory.getSize(); i++) {
                                        inventory.setItem(i, module.getInventoryManager().getFill());
                                    }

                                    inventory.setItem(10, module.getSuperAPI().item(Material.SKULL_ITEM, 1, (short) 3).setSkullOwner("Vanilloper")
                                            .setDisplayname("§8•§7● Vanilloper").setLore(module.getBoughtLore(player, "head-vanilloper")).build());

                                    inventory.setItem(12, module.getSuperAPI().item(Material.SKULL_ITEM, 1, (short) 3).setSkullOwner("CyroDev")
                                            .setDisplayname("§8•§7● CyroDev").setLore(module.getBoughtLore(player, "head-cyrodev")).build());

                                    inventory.setItem(14, module.getSuperAPI().item(Material.SKULL_ITEM, 1, (short) 3).setSkullOwner("SuperKlug")
                                            .setDisplayname("§8•§7● SuperKlug").setLore(module.getBoughtLore(player, "head-superklug")).build());

                                    inventory.setItem(16, module.getSuperAPI().item(Material.SKULL_ITEM, 1, (short) 3).setSkullOwner("ungespielt")
                                            .setDisplayname("§8•§7● ungespielt").setLore(module.getBoughtLore(player, "head-ungespielt")).build());

                                    inventory.setItem(28, module.getSuperAPI().item(Material.SKULL_ITEM, 1, (short) 3).setSkullOwner("rewinside")
                                            .setDisplayname("§8•§7● rewinside").setLore(module.getBoughtLore(player, "head-rewinside")).build());

                                    inventory.setItem(30, module.getSuperAPI().item(Material.SKULL_ITEM, 1, (short) 3).setSkullOwner("GommeHD")
                                            .setDisplayname("§8•§7● GommeHD").setLore(module.getBoughtLore(player, "head-gommehd")).build());

                                    inventory.setItem(32, module.getSuperAPI().item(Material.SKULL_ITEM, 1, (short) 3).setSkullOwner("Vareide")
                                            .setDisplayname("§8•§7● Vareide").setLore(module.getBoughtLore(player, "head-vareide")).build());

                                    inventory.setItem(34, module.getSuperAPI().item(Material.SKULL_ITEM, 1, (short) 3).setSkullOwner("Gronkh")
                                            .setDisplayname("§8•§7● Gronkh").setLore(module.getBoughtLore(player, "head-gronkh")).build());

                                    inventory.setItem(44, module.getSuperAPI().item(Material.BARRIER)
                                            .setDisplayname("§4•§c● Kopf ausziehen").build());

                                    player.openInventory(inventory);
                                }

                                break;

                                case "§8•§7● Partikel": {
                                    final Inventory inventory = Bukkit.createInventory(null, 9 * 5, "§8•§7● Extras §8▍ §7Partikel");

                                    for (int i = 0; i < inventory.getSize(); i++) {
                                        inventory.setItem(i, module.getInventoryManager().getFill());
                                    }

                                    inventory.setItem(10, module.getSuperAPI().item(Material.EMERALD)
                                            .setDisplayname("§8•§7● Grüne§8-§7Funken").setLore(module.getBoughtLore(player, "particle-greensparks")).build());

                                    inventory.setItem(12, module.getSuperAPI().item(Material.NETHER_STAR)
                                            .setDisplayname("§8•§7● Weiße§8-§7Funken").setLore(module.getBoughtLore(player, "particle-whitesparks")).build());

                                    inventory.setItem(14, module.getSuperAPI().item(Material.EYE_OF_ENDER)
                                            .setDisplayname("§8•§7● Lila§8-§7Funken").setLore(module.getBoughtLore(player, "particle-purplesparks")).build());

                                    inventory.setItem(16, module.getSuperAPI().item(Material.FIREBALL)
                                            .setDisplayname("§8•§7● Flammen").setLore(module.getBoughtLore(player, "particle-flames")).build());

                                    inventory.setItem(28, module.getSuperAPI().item(Material.OBSIDIAN)
                                            .setDisplayname("§8•§7● Portal").setLore(module.getBoughtLore(player, "particle-portal")).build());

                                    inventory.setItem(30, module.getSuperAPI().item(Material.BOOK)
                                            .setDisplayname("§8•§7● Buchstaben").setLore(module.getBoughtLore(player, "particle-letters")).build());

                                    inventory.setItem(32, module.getSuperAPI().item(Material.LAVA_BUCKET)
                                            .setDisplayname("§8•§7● Lava§8-§7Funken").setLore(module.getBoughtLore(player, "particle-lavasparks")).build());

                                    inventory.setItem(34, module.getSuperAPI().item(Material.GLOWSTONE_DUST)
                                            .setDisplayname("§8•§7● Regenbogen").setLore(module.getBoughtLore(player, "particle-rainbow")).build());

                                    inventory.setItem(44, module.getSuperAPI().item(Material.BARRIER)
                                            .setDisplayname("§4•§c● Partikel deaktivieren").build());

                                    player.openInventory(inventory);
                                }

                                break;

                                case "§8•§7● Schuss§8-§7Effekte": {
                                    final Inventory inventory = Bukkit.createInventory(null, 9 * 5, "§8•§7● Extras §8▍ §7Bogen");

                                    for (int i = 0; i < inventory.getSize(); i++) {
                                        inventory.setItem(i, module.getInventoryManager().getFill());
                                    }

                                    inventory.setItem(9, module.getSuperAPI().item(Material.BLAZE_POWDER)
                                            .setDisplayname("§8•§7● Magie").setLore(module.getBoughtLore(player, "bow-magic")).build());

                                    inventory.setItem(11, module.getSuperAPI().item(Material.INK_SACK, 1, (short) 14)
                                            .setDisplayname("§8•§7● Farbenfroh").setLore(module.getBoughtLore(player, "bow-colorful")).build());

                                    inventory.setItem(13, module.getSuperAPI().item(Material.EMERALD)
                                            .setDisplayname("§8•§7● Freundlicher Dorfbewohner").setLore(module.getBoughtLore(player, "bow-happyvillager")).build());

                                    inventory.setItem(15, module.getSuperAPI().item(Material.FIREBALL)
                                            .setDisplayname("§8•§7● Böser Dorfbewohner").setLore(module.getBoughtLore(player, "bow-angyvillager")).build());

                                    inventory.setItem(17, module.getSuperAPI().item(Material.SNOW_BALL)
                                            .setDisplayname("§8•§7● Wolken").setLore(module.getBoughtLore(player, "bow-clouds")).build());

                                    inventory.setItem(19, module.getSuperAPI().item(Material.ENCHANTMENT_TABLE)
                                            .setDisplayname("§8•§7● Zauberei").setLore(module.getBoughtLore(player, "bow-enchant")).build());

                                    inventory.setItem(21, module.getSuperAPI().item(Material.INK_SACK, 1, (short) 1)
                                            .setDisplayname("§8•§7● Liebe").setLore(module.getBoughtLore(player, "bow-love")).build());

                                    inventory.setItem(23, module.getSuperAPI().item(Material.LAVA_BUCKET)
                                            .setDisplayname("§8•§7● Lava").setLore(module.getBoughtLore(player, "bow-lava")).build());

                                    inventory.setItem(25, module.getSuperAPI().item(Material.NOTE_BLOCK)
                                            .setDisplayname("§8•§7● Musik").setLore(module.getBoughtLore(player, "bow-music")).build());

                                    inventory.setItem(27, module.getSuperAPI().item(Material.MAGMA_CREAM)
                                            .setDisplayname("§8•§7● Feuer").setLore(module.getBoughtLore(player, "bow-fire")).build());

                                    inventory.setItem(29, module.getSuperAPI().item(Material.BREWING_STAND_ITEM)
                                            .setDisplayname("§8•§7● Zaubertrank").setLore(module.getBoughtLore(player, "bow-potion")).build());

                                    inventory.setItem(31, module.getSuperAPI().item(Material.FIREWORK)
                                            .setDisplayname("§8•§7● Feuerwerk").setLore(module.getBoughtLore(player, "bow-firework")).build());

                                    inventory.setItem(33, module.getSuperAPI().item(Material.SULPHUR)
                                            .setDisplayname("§8•§7● Rauch").setLore(module.getBoughtLore(player, "bow-smoke")).build());

                                    inventory.setItem(35, module.getSuperAPI().item(Material.BOOK)
                                            .setDisplayname("§8•§7● Verzaubert").setLore(module.getBoughtLore(player, "bow-enchanted")).build());

                                    inventory.setItem(44, module.getSuperAPI().item(Material.BARRIER)
                                            .setDisplayname("§4•§c● Schuss§8-§cEffekte deaktivieren").build());

                                    player.openInventory(inventory);
                                }

                                break;

                                case "§6•§e● Spezial": {
                                    final Inventory inventory = Bukkit.createInventory(null, 9 * 5, "§8•§7● Extras §8▍ §eSpezial");

                                    for (int i = 0; i < inventory.getSize(); i++) {
                                        inventory.setItem(i, module.getInventoryManager().getFill());
                                    }

                                    if (module.getLobbyData(player).getActivePremiumGadgets().contains("doublejump")) {
                                        inventory.setItem(10, module.getSuperAPI().item(Material.FEATHER)
                                                .setDisplayname("§6•§e● Doppel§8-§eSprung §8▍ §aAktiviert")
                                                .setLore(" ", "§8► §7Nur für Spieler mit dem Rang §6Gold§8/§aEmerald§8/§3Diamond §7und höher§8!").build());
                                    } else {
                                        inventory.setItem(10, module.getSuperAPI().item(Material.FEATHER)
                                                .setDisplayname("§6•§e● Doppel§8-§eSprung §8▍ §cDeaktiviert")
                                                .setLore(" ", "§8► §7Nur für Spieler mit dem Rang §6Gold§8/§aEmerald§8/§3Diamond §7und höher§8!").build());
                                    }

                                    if (module.getLobbyData(player).getActivePremiumGadgets().contains("jetpack")) {
                                        inventory.setItem(12, module.getSuperAPI().item(Material.FIREWORK)
                                                .setDisplayname("§6•§e● Jetpack §8▍ §aAktiviert")
                                                .setLore(" ", "§8► §7Nur für Spieler mit dem Rang §6Gold§8/§aEmerald§8/§3Diamond §7und höher§8!").build());
                                    } else {
                                        inventory.setItem(12, module.getSuperAPI().item(Material.FIREWORK)
                                                .setDisplayname("§6•§e● Jetpack §8▍ §cDeaktiviert")
                                                .setLore(" ", "§8► §7Nur für Spieler mit dem Rang §6Gold§8/§aEmerald§8/§3Diamond §7und höher§8!").build());
                                    }

                                    if (module.getLobbyData(player).getActivePremiumGadgets().contains("discoboots")) {
                                        inventory.setItem(14, module.getSuperAPI().item(Material.LEATHER_BOOTS)
                                                .setDisplayname("§6•§e● Disco§8-§eSchuhe §8▍ §aAktiviert")
                                                .setLore(" ", "§8► §7Nur für Spieler mit dem Rang §6Gold§8/§aEmerald§8/§3Diamond §7und höher§8!").build());
                                    } else {
                                        inventory.setItem(14, module.getSuperAPI().item(Material.LEATHER_BOOTS)
                                                .setDisplayname("§6•§e● Disco§8-§eSchuhe §8▍ §cDeaktiviert")
                                                .setLore(" ", "§8► §7Nur für Spieler mit dem Rang §6Gold§8/§aEmerald§8/§3Diamond §7und höher§8!").build());
                                    }

                                    if (module.getLobbyData(player).getActivePremiumGadgets().contains("discohat")) {
                                        inventory.setItem(16, module.getSuperAPI().item(Material.STAINED_GLASS)
                                                .setDisplayname("§6•§e● Disco§8-§eHut §8▍ §aAktiviert")
                                                .setLore(" ", "§8► §7Nur für Spieler mit dem Rang §6Gold§8/§aEmerald§8/§3Diamond §7und höher§8!").build());
                                    } else {
                                        inventory.setItem(16, module.getSuperAPI().item(Material.STAINED_GLASS)
                                                .setDisplayname("§6•§e● Disco§8-§eHut §8▍ §cDeaktiviert")
                                                .setLore(" ", "§8► §7Nur für Spieler mit dem Rang §6Gold§8/§aEmerald§8/§3Diamond §7und höher§8!").build());
                                    }

                                    if (module.getLobbyData(player).getActivePremiumGadgets().contains("ballon")) {
                                        inventory.setItem(28, module.getSuperAPI().item(Material.LEASH)
                                                .setDisplayname("§6•§e● Ballon §8▍ §aAktiviert")
                                                .setLore(" ", "§8► §7Nur für Spieler mit dem Rang §6Gold§8/§aEmerald§8/§3Diamond §7und höher§8!").build());
                                    } else {
                                        inventory.setItem(28, module.getSuperAPI().item(Material.LEASH)
                                                .setDisplayname("§6•§e● Ballon §8▍ §cDeaktiviert")
                                                .setLore(" ", "§8► §7Nur für Spieler mit dem Rang §6Gold§8/§aEmerald§8/§3Diamond §7und höher§8!").build());
                                    }

                                    if (module.getLobbyData(player).getActivePremiumGadgets().contains("stacker")) {
                                        inventory.setItem(30, module.getSuperAPI().item(Material.SADDLE)
                                                .setDisplayname("§6•§e● Stacker §8▍ §aAktiviert")
                                                .setLore(" ", "§8► §7Nur für Spieler mit dem Rang §6Gold§8/§aEmerald§8/§3Diamond §7und höher§8!").build());
                                    } else {
                                        inventory.setItem(30, module.getSuperAPI().item(Material.SADDLE)
                                                .setDisplayname("§6•§e● Stacker §8▍ §cDeaktiviert")
                                                .setLore(" ", "§8► §7Nur für Spieler mit dem Rang §6Gold§8/§aEmerald§8/§3Diamond §7und höher§8!").build());
                                    }

                                    inventory.setItem(32, module.getSuperAPI().item(Material.BONE)
                                            .setDisplayname("§6•§e● Haustiere")
                                            .setLore(" ", "§8► §7Nur für Spieler mit dem Rang §6Gold§8/§aEmerald§8/§3Diamond §7und höher§8!").build());

                                    inventory.setItem(34, module.getSuperAPI().item(Material.DIAMOND_CHESTPLATE)
                                            .setDisplayname("§6•§e● Umkleide")
                                            .setLore(" ", "§8► §7Nur für Spieler mit dem Rang §6Gold§8/§aEmerald§8/§3Diamond §7und höher§8!").build());

                                    player.openInventory(inventory);
                                }

                                break;

                                default:
                                    break;

                            }

                            break;
                        //</editor-fold>

                        //<editor-fold defaultstate="collapsed" desc="lobby wechseln">
                        case "§8•§7● Lobby wechseln":

                            break;
                        //</editor-fold>

                        default:
                            break;

                    }
                    
                } catch(Exception exception) {}
                
            }
            
        }, EventPriority.NORMAL);
        //</editor-fold>
        
        //<editor-fold defaultstate="collapsed" desc="LeavesDecayEvent">
        module.getSuperAPI().registerEvent(LeavesDecayEvent.class, (EventListener<LeavesDecayEvent>) (LeavesDecayEvent event) -> {
            event.setCancelled(true);
        }, EventPriority.NORMAL);
        //</editor-fold>
        
        //<editor-fold defaultstate="collapsed" desc="PlayerArmorStandManipulateEvent">
        module.getSuperAPI().registerEvent(PlayerArmorStandManipulateEvent.class, (EventListener<PlayerArmorStandManipulateEvent>) (PlayerArmorStandManipulateEvent event) -> {
            final Player player = event.getPlayer();
            
            if(!module.getEditors().contains(player))
                event.setCancelled(true);
            
        }, EventPriority.NORMAL);
        //</editor-fold>
        
        //<editor-fold defaultstate="collapsed" desc="PlayerBucketEmptyEvent">
        module.getSuperAPI().registerEvent(PlayerBucketEmptyEvent.class, (EventListener<PlayerBucketEmptyEvent>) (PlayerBucketEmptyEvent event) -> {
            final Player player = event.getPlayer();
            
            if(!module.getEditors().contains(player))
                event.setCancelled(true);
            
        }, EventPriority.NORMAL);
        //</editor-fold>
        
        //<editor-fold defaultstate="collapsed" desc="PlayerBucketFillEvent">
        module.getSuperAPI().registerEvent(PlayerBucketFillEvent.class, (EventListener<PlayerBucketFillEvent>) (PlayerBucketFillEvent event) -> {
            final Player player = event.getPlayer();
            
            if(!module.getEditors().contains(player))
                event.setCancelled(true);
            
        }, EventPriority.NORMAL);
        //</editor-fold>
        
        //<editor-fold defaultstate="collapsed" desc="PlayerChatTabCompleteEvent">
        module.getSuperAPI().registerEvent(PlayerChatTabCompleteEvent.class, (EventListener<PlayerChatTabCompleteEvent>) (PlayerChatTabCompleteEvent event) -> {
            
            event.getTabCompletions().clear();
            
            if(event.getLastToken() != null) {
                Bukkit.getOnlinePlayers().forEach((players) -> {
                    if(players.getName().toLowerCase().startsWith(event.getLastToken().toLowerCase())) {
                        if(!module.getSpectators().contains(players))
                            event.getTabCompletions().add(players.getName());
                    }
                });
            } else {
                Bukkit.getOnlinePlayers().forEach((players) -> {
                    if(!module.getSpectators().contains(players)) {
                        event.getTabCompletions().add(players.getName());
                    }
                });
            }
            
        }, EventPriority.NORMAL);
        //</editor-fold>
        
        //<editor-fold defaultstate="collapsed" desc="PlayerCommandPreprocessEvent">
        module.getSuperAPI().registerEvent(PlayerCommandPreprocessEvent.class, (EventListener<PlayerCommandPreprocessEvent>) (PlayerCommandPreprocessEvent event) -> {
            final Player player = event.getPlayer();
            final String command = event.getMessage().split(" ")[0];
            final HelpTopic topic = Bukkit.getHelpMap().getHelpTopic(command);
            
            if(module.getUserData(player).getRankId() < 15) {
                switch(command.toLowerCase()) {
                    
                    case "/nocheatplus": case "/ncp": case "/aac":
                        event.setCancelled(true);
                        player.sendMessage(module.getSuperAPI().getPrefix() + module.getSuperAPI().getUnknownCommand());
                        break;
                        
                }
            }
            
            if(!event.isCancelled()) {
                if(topic == null) {
                    event.setCancelled(true);
                    player.sendMessage(module.getSuperAPI().getPrefix() + module.getSuperAPI().getUnknownCommand());
                }
            }
            
        }, EventPriority.NORMAL);
        //</editor-fold>
        
        //<editor-fold defaultstate="collapsed" desc="PlayerDeathEvent">
        module.getSuperAPI().registerEvent(PlayerDeathEvent.class, (EventListener<PlayerDeathEvent>) (PlayerDeathEvent event) -> {
            final Player player = event.getEntity();
            
            module.getPlayerKillLocation().put(player, player.getLocation());
            
            event.setDeathMessage(null);
            event.getDrops().clear();
            
            if (module.getPlayerBallonBlock().containsKey(player)) {
                module.getPlayerBallonBlock().get(player).remove();
                module.getPlayerBallonBlock().remove(player);
            }

            if (module.getPlayerBallonBat().containsKey(player)) {
                module.getPlayerBallonBat().get(player).remove();
                module.getPlayerBallonBat().remove(player);
            }

        }, EventPriority.NORMAL);
        //</editor-fold>
        
        //<editor-fold defaultstate="collapsed" desc="PlayerRespawnEvent">
        module.getSuperAPI().registerEvent(PlayerRespawnEvent.class, (EventListener<PlayerRespawnEvent>) (PlayerRespawnEvent event) -> {
            final Player player = event.getPlayer();

            event.setRespawnLocation(module.getPlayerKillLocation().get(player));
            player.getInventory().setContents(module.getInventoryManager().getInventory(InventoryType.HOTBAR).getContents());
            module.addExtraItems(player);
            
            if (!module.getLobbyData(player).getActiveGadgets().isEmpty()) {
                module.getLobbyData(player).getActiveGadgets().forEach((gadgets) -> {

                    if (gadgets.startsWith("banner-")) {
                        if (module.getLobbyData(player).getGadgetsBought().contains(gadgets)
                                | module.getLobbyData(player).getGadgetsBought().contains("banner-*")
                                | module.getLobbyData(player).getGadgetsBought().contains("*")) {

                            player.getInventory().setHelmet(module.getGadgets().get(gadgets));

                        }
                    }

                    if (gadgets.startsWith("hat-")) {
                        if (module.getLobbyData(player).getGadgetsBought().contains(gadgets)
                                | module.getLobbyData(player).getGadgetsBought().contains("hat-*")
                                | module.getLobbyData(player).getGadgetsBought().contains("*")) {

                            player.getInventory().setHelmet(module.getGadgets().get(gadgets));

                        }
                    }

                    if (gadgets.startsWith("head-")) {
                        if (module.getLobbyData(player).getGadgetsBought().contains(gadgets)
                                | module.getLobbyData(player).getGadgetsBought().contains("head-*")
                                | module.getLobbyData(player).getGadgetsBought().contains("*")) {

                            player.getInventory().setHelmet(module.getGadgets().get(gadgets));

                        }
                    }
                    
                    module.spawnBallon(player);
                    
                    if (module.getPlayerKillLocation().containsKey(player))
                        module.getPlayerKillLocation().remove(player);

                });
            }

        }, EventPriority.NORMAL);
        //</editor-fold>
        
        //<editor-fold defaultstate="collapsed" desc="PlayerDropItemEvent">
        module.getSuperAPI().registerEvent(PlayerDropItemEvent.class, (EventListener<PlayerDropItemEvent>) (PlayerDropItemEvent event) -> {
            final Player player = event.getPlayer();
            
            if(!module.getEditors().contains(player))
                event.setCancelled(true);
            
        }, EventPriority.NORMAL);
        //</editor-fold>
        
        //<editor-fold defaultstate="collapsed" desc="PlayerInteractEntityEvent">
        module.getSuperAPI().registerEvent(PlayerInteractEntityEvent.class, (EventListener<PlayerInteractEntityEvent>) (PlayerInteractEntityEvent event) -> {
            final Player player = event.getPlayer();
            
            if(event.getRightClicked().getType() == EntityType.ITEM_FRAME) {
                if(!module.getEditors().contains(player))
                    event.setCancelled(true);
            }
            
            if(player.getItemInHand().getType() == Material.NAME_TAG | player.getItemInHand().getType() == Material.SADDLE) {
                if(!module.getEditors().contains(player)) {
                    event.setCancelled(true);
                    player.updateInventory();
                }
            }
            
            if(event.getRightClicked().getType() == EntityType.PLAYER) {
                final Player target = (Player) event.getRightClicked();
                
                if(player.getItemInHand().getType() == Material.AIR) {
                    if(module.getLobbyData(player).getActivePremiumGadgets().contains("stacker")) {
                        
                        if(player.getPassenger() == null) {
                            
                            if(module.getUserData(player).getRankId() >= module.getUserData(target).getRankId()) {
                                
                                player.setPassenger(target);
                                
                                //player.sendMessage(module.getSuperAPI().getPrefix() + "Du hast " + target.getDisplayName() + " §7gestackt§8!");
                                //target.sendMessage(module.getSuperAPI().getPrefix() + "Du wurdest von " + player.getDisplayName() + " §7gestackt§8!");
                                
                            } else {
                                player.sendMessage(module.getSuperAPI().getPrefix() + "Du darfst keine Spieler mit höherem Rang stacken§8!"); 
                            }
                            
                        } else {
                            player.sendMessage(module.getSuperAPI().getPrefix() + "Du trägst schon jemanden§8!");
                        }
                        
                    }
                }
                
            }
            
        }, EventPriority.NORMAL);
        //</editor-fold>
        
        //<editor-fold defaultstate="collapsed" desc="PlayerInteractEvent">
        module.getSuperAPI().registerEvent(PlayerInteractEvent.class, (EventListener<PlayerInteractEvent>) (PlayerInteractEvent event) -> {
            final Player player = event.getPlayer();
            
            if(event.getAction() == Action.PHYSICAL | event.getAction() == Action.LEFT_CLICK_BLOCK) {
                if(!module.getEditors().contains(player))
                    event.setCancelled(true);
            }
            
            if(event.getAction() == Action.LEFT_CLICK_BLOCK | event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                if(event.getClickedBlock().getType() == Material.DRAGON_EGG) {
                    if(!module.getEditors().contains(player))
                        event.setCancelled(true);
                }
            }
            
            if(event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                if(event.getClickedBlock().getType() == Material.WOODEN_DOOR | event.getClickedBlock().getType() == Material.ACACIA_DOOR | event.getClickedBlock().getType() == Material.BIRCH_DOOR | event.getClickedBlock().getType() == Material.DARK_OAK_DOOR | event.getClickedBlock().getType() == Material.JUNGLE_DOOR | event.getClickedBlock().getType() == Material.SPRUCE_DOOR | event.getClickedBlock().getType() == Material.TRAP_DOOR | event.getClickedBlock().getType() == Material.FENCE_GATE | event.getClickedBlock().getType() == Material.ACACIA_FENCE_GATE | event.getClickedBlock().getType() == Material.BIRCH_FENCE_GATE | event.getClickedBlock().getType() == Material.DARK_OAK_FENCE_GATE | event.getClickedBlock().getType() == Material.JUNGLE_FENCE_GATE | event.getClickedBlock().getType() == Material.SPRUCE_FENCE_GATE | event.getClickedBlock().getType() == Material.FENCE | event.getClickedBlock().getType() == Material.ACACIA_FENCE | event.getClickedBlock().getType() == Material.BIRCH_FENCE | event.getClickedBlock().getType() == Material.DARK_OAK_FENCE | event.getClickedBlock().getType() == Material.JUNGLE_FENCE | event.getClickedBlock().getType() == Material.NETHER_FENCE | event.getClickedBlock().getType() == Material.SPRUCE_FENCE | event.getClickedBlock().getType() == Material.WOOD_BUTTON | event.getClickedBlock().getType() == Material.STONE_BUTTON | event.getClickedBlock().getType() == Material.LEVER | event.getClickedBlock().getType() == Material.CHEST | event.getClickedBlock().getType() == Material.TRAPPED_CHEST | event.getClickedBlock().getType() == Material.ENDER_CHEST | event.getClickedBlock().getType() == Material.NOTE_BLOCK | event.getClickedBlock().getType() == Material.JUKEBOX | event.getClickedBlock().getType() == Material.BED_BLOCK | event.getClickedBlock().getType() == Material.ANVIL | event.getClickedBlock().getType() == Material.FURNACE | event.getClickedBlock().getType() == Material.BURNING_FURNACE | event.getClickedBlock().getType() == Material.ENCHANTMENT_TABLE | event.getClickedBlock().getType() == Material.WORKBENCH | event.getClickedBlock().getType() == Material.DROPPER | event.getClickedBlock().getType() == Material.DISPENSER | event.getClickedBlock().getType() == Material.BREWING_STAND | event.getClickedBlock().getType() == Material.BEACON | event.getClickedBlock().getType() == Material.HOPPER | event.getClickedBlock().getType() == Material.REDSTONE_COMPARATOR_ON | event.getClickedBlock().getType() == Material.REDSTONE_COMPARATOR_OFF | event.getClickedBlock().getType() == Material.DIODE_BLOCK_ON | event.getClickedBlock().getType() == Material.DIODE_BLOCK_OFF | event.getClickedBlock().getType() == Material.DAYLIGHT_DETECTOR | event.getClickedBlock().getType() == Material.DAYLIGHT_DETECTOR_INVERTED) {
                    if(!module.getEditors().contains(player))
                        event.setCancelled(true);
                }
            }
            
            if(event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                if(player.getItemInHand().getType() == Material.MONSTER_EGG | player.getItemInHand().getType() == Material.MONSTER_EGGS) {
                    event.setCancelled(true);
                }
                
                if(event.getClickedBlock().getType() == Material.SIGN | event.getClickedBlock().getType() == Material.SIGN_POST
                        | event.getClickedBlock().getType() == Material.WALL_SIGN) {
                    
                    final Sign sign = (Sign) event.getClickedBlock().getState();
                    
                    if(sign.getLine(0).equals("[Kostüm]") && sign.getLine(2) != null) {
                        module.getSuperAPI().skin(player, sign.getLine(2), true);
                        player.sendMessage(module.getSuperAPI().getPrefix() + "Andere Spieler sehen dich nun als §b" + sign.getLine(2) + "§8!");
                    }
                    
                }
                
            }
            
            if (event.getAction() == Action.LEFT_CLICK_BLOCK | event.getAction() == Action.LEFT_CLICK_AIR) {
                if(player.getPassenger() != null) {
                    Player target = (Player) player.getPassenger();
                    Vector v = player.getLocation().getDirection().multiply(2).setY(0.25);
                    target.leaveVehicle();
                    target.setVelocity(v);
                    
                    Bukkit.getOnlinePlayers().forEach((players) -> {
                        if(players.canSee(player)) {
                            players.spigot().playEffect(player.getLocation(), Effect.CLOUD, 1, 1, 1, 1, 1, 1, 128, 16);
                            players.playSound(player.getEyeLocation(), Sound.FIREWORK_LAUNCH, 1, 1);
                        }
                    });
                    
                }
            }
            
            if(event.getAction() == Action.RIGHT_CLICK_AIR | event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                
                if(player.getItemInHand().getType() == null) return;
                
                if(module.getPlayerInteractCooldown().contains(player)) {
                    event.setCancelled(true);
                    return;
                }
                
                switch(player.getItemInHand().getType()) {
                    
                    case COMPASS:
                        event.setCancelled(true);
                        
                        player.openInventory(module.getInventoryManager().getInventory(InventoryType.NAVIGATOR));
                        player.playSound(player.getLocation(), Sound.DOOR_OPEN, 0.5F, 8.7F);
                        break;
                        
                    case BLAZE_ROD: case STICK:
                        startCooldown(player);
                        event.setCancelled(true);
                        
                        if(player.getItemInHand().getItemMeta().getDisplayName().equals("§8•§7● Spieler §8▍ §aAktiviert")) {
                            player.setItemInHand(module.getSuperAPI().item(Material.STICK).setDisplayname("§8•§7● Spieler §8▍ §cDeaktiviert").build());
                            
                            module.getPlayerHider().add(player);
                            player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 20, 0));
                            player.playSound(player.getLocation(), Sound.FIREWORK_TWINKLE2, 0.5F, 8.6F);
                            
                            Bukkit.getOnlinePlayers().forEach((players) -> {
                                if(player != players) {
                                    player.spigot().playEffect(players.getLocation(), Effect.LAVA_POP, 1, 1, 1, 1, 1, 1, 16, 16);
                                    player.spigot().playEffect(players.getLocation(), Effect.CLOUD, 1, 1, 1, 1, 1, 1, 16, 16);
                                    player.hidePlayer(players);
                                }
                            });
                            
                        } else {
                            player.setItemInHand(module.getSuperAPI().item(Material.BLAZE_ROD).setDisplayname("§8•§7● Spieler §8▍ §aAktiviert").build());

                            module.getPlayerHider().remove(player);
                            player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 20, 0));
                            player.playSound(player.getLocation(), Sound.FIREWORK_TWINKLE, 0.5F, 5.6F);
                            
                            Bukkit.getOnlinePlayers().forEach((players) -> {
                                if(player != players) {
                                    player.showPlayer(players);
                                    player.spigot().playEffect(players.getLocation(), Effect.FLYING_GLYPH, 1, 1, 1, 1, 1, 1, 64, 16);
                                }
                            });
                            
                        }
                        
                        break;
                        
                    case TNT:
                        event.setCancelled(true);
                        
                        if(Bukkit.getServer().spigot().getConfig().getBoolean("settings.bungeecord")) {
                            
                            if(Bukkit.getServer().getServerName().equalsIgnoreCase("Silentlobby")) {
                                
                                final int random = (int) ((Math.random()) * 2 + 1);
                                
                                module.getSuperAPI().switchServer(player, "Lobby-0" + random);
                                player.sendMessage(module.getSuperAPI().getPrefix() + "Du wirst mit " + module.getSuperAPI().getHighlightColor() + "Lobby-0" + random + module.getSuperAPI().getMessageColor() + " verbunden§8!");
                                
                            } else {
                                
                                module.getSuperAPI().switchServer(player, "Silentlobby");
                                player.sendMessage(module.getSuperAPI().getPrefix() + "Du wirst mit der " + module.getSuperAPI().getHighlightColor() + "Silentlobby" + module.getSuperAPI().getMessageColor() + " verbunden§8!");
                                
                            }
                            
                        } else {
                            startCooldown(player);
                            player.sendMessage(module.getSuperAPI().getPrefix() + "Dieser Server muss Bungeecord fähig sein§8!");
                        }
                        
                        break;
                        
                    case NAME_TAG:
                        event.setCancelled(true);
                        
                        player.openInventory(module.getInventoryManager().getInventory(InventoryType.NICK));
                        player.playSound(player.getLocation(), Sound.ENDERMAN_TELEPORT, 0.5F, 5.7F);
                        
                        break;
                        
                    case EYE_OF_ENDER:
                        event.setCancelled(true);
                        startCooldown(player);
                        
                        if(Bukkit.getServer().getName().equalsIgnoreCase("Silentlobby")) {
                            player.sendMessage(module.getSuperAPI().getPrefix() + "Du darfst dein " + module.getSuperAPI().getHighlightColor() + "Schutzschild" + module.getSuperAPI().getMessageColor() + " in der Silentlobby nicht aktivieren§8!");
                            return;
                        }
                        
                        if(module.getPlayerShield().containsKey(player)) {
                            player.playSound(player.getLocation(), Sound.ENDERDRAGON_WINGS, 0.5F, 0.5F);
                            
                            module.getPlayerShield().get(player).cancel();
                            module.getPlayerShield().remove(player);
                            
                            player.getInventory().setItem(5, module.getSuperAPI().item(Material.EYE_OF_ENDER).setDisplayname("§8•§7● Schutzschild §8▍ §cDeaktiviert").build());
                        } else {
                            
                            module.getPlayerShield().put(player, new BukkitRunnable() {
                                
                                @Override
                                public void run() {
                                    Bukkit.getOnlinePlayers().forEach((players) -> {
                                        if(players.canSee(player)) {
                                            players.getWorld().playEffect(player.getLocation(), Effect.ENDER_SIGNAL, 1);
                                        }
                                    });
                                }
                                
                            });
                            
                            module.getPlayerShield().get(player).runTaskTimer(module, 20, 20);
                            
                            player.playSound(player.getLocation(), Sound.ENDERDRAGON_WINGS, 0.5F, 0.5F);
                            
                            player.getInventory().setItem(5, module.getSuperAPI().item(Material.EYE_OF_ENDER).setDisplayname("§8•§7● Schutzschild §8▍ §aAktiviert").build());
                        }
                        
                        break;
                        
                    case CHEST:
                        event.setCancelled(true);
                        
                        player.openInventory(module.getInventoryManager().getInventory(InventoryType.EXTRAS));
                        player.playSound(player.getLocation(), Sound.CHEST_OPEN, 0.5F, 5.7F);
                        
                        break;
                        
                    case NETHER_STAR:
                        event.setCancelled(true);
                        
                        player.openInventory(module.getInventoryManager().getInventory(InventoryType.LOBBYSWITCHER));
                        player.playSound(player.getLocation(), Sound.ZOMBIE_WOODBREAK, 0.5F, 9.7F);
                        
                        break;
                        
                    default:
                        break;
                    
                }
                
            }
            
        }, EventPriority.NORMAL);
        //</editor-fold>
        
        //<editor-fold defaultstate="collapsed" desc="PlayerLoginEvent">
        module.getSuperAPI().registerEvent(PlayerLoginEvent.class, (EventListener<PlayerLoginEvent>) (PlayerLoginEvent event) -> {
            final Player player = event.getPlayer();
            
            if(player.isOp()) {
                player.setOp(false);
            }
            
            module.getMongoAPI().getBackendManager().getLobby(player.getUniqueId().toString(), (de.superklug.mygames.supertavariamongoapi.entities.Lobby t) -> {
                module.getSuperAPI().setMetadata(player, "lobbyData", t);
            });
            
        }, EventPriority.NORMAL);
        //</editor-fold>
        
        //<editor-fold defaultstate="collapsed" desc="PlayerJoinEvent">
        module.getSuperAPI().registerEvent(PlayerJoinEvent.class, (EventListener<PlayerJoinEvent>) (PlayerJoinEvent event) -> {
            final Player player = event.getPlayer();
            
            player.getInventory().clear();
            player.getInventory().setArmorContents(null);
            player.getInventory().setContents(module.getInventoryManager().getInventory(InventoryType.HOTBAR).getContents());
            //module.addExtraItems(player);

            player.spigot().setCollidesWithEntities(false);
            player.setGameMode(GameMode.ADVENTURE);
            player.setFlying(false);
            player.setAllowFlight(false);
            player.setLevel(0);
            player.setExp(0);
            player.setFoodLevel(20);
            player.setMaxHealth(20);
            player.setHealth(20);

            player.setPlayerWeather(WeatherType.CLEAR);

            player.getActivePotionEffects().forEach((effects) -> {
                player.removePotionEffect(effects.getType());
            });
            
            module.getSuperAPI().scoreboard(player)
                    .setScoreboardTitle("§3•§b● Tavaria.de §8▍ §7Lobby")
                    .addScoreboardLine("§f ", 14)
                    .addScoreboardLine("§8•§7● Profil", 13)
                    .addScoreboardLine("§8➥ §b" + player.getName(), 12)
                    .addScoreboardLine("§f  ", 11)
                    .addScoreboardLine("§8•§7● Spielzeit", 10)
                    .addUpdateableScoreboardLine("§8➥ ", "§cLädt...", "§1", 9)
                    .addScoreboardLine("§f   ", 8)
                    .addScoreboardLine("§8•§7● Tokens", 7)
                    .addUpdateableScoreboardLine("§8➥ ", "§cLädt...", "§2", 6)
                    .addScoreboardLine("§f    ", 5)
                    .addScoreboardLine("§8•§7● Coins", 4)
                    .addUpdateableScoreboardLine("§8➥ ", "§cLädt...", "§3", 3)
                    .addScoreboardLine("§f     ", 2)
                    .addScoreboardLine("§8•§7● Spieler", 1)
                    .addUpdateableScoreboardLine("§8➥ ", "§cLädt...", "§4", 0)
                    //  RankId's
                    //
                    //  Spieler: 0
                    //  Gold: 1
                    //  Emerald: 2
                    //  Diamond: 3
                    //  Diamond+: 4
                    //  Youtuber: 5
                    //  Freund: 6
                    //  Builder: 7
                    //  HeadBuilder: 8
                    //  Supporter: 9
                    //  HeadSupporter: 10
                    //  Moderator: 11
                    //  HeadModerator: 12
                    //  Developer: 13
                    //  HeadDeveloper: 14
                    //  Administrator: 15
                    //  Manager: 16
                    //  Owner: 17

                    .createTeam(10, "Owner", "§4Owner §8┃ §4", "", false, NameTagVisibility.ALWAYS)
                    .createTeam(11, "Manager", "§4Manager §8┃ §4", "", false, NameTagVisibility.ALWAYS)
                    .createTeam(12, "Administrator", "§4Admin §8┃ §4", "", false, NameTagVisibility.ALWAYS)
                    .createTeam(13, "HeadDeveloper", "§bH-Dev §8┃ §b", "", false, NameTagVisibility.ALWAYS)
                    .createTeam(14, "Developer", "§bDev §8┃ §b", "", false, NameTagVisibility.ALWAYS)
                    .createTeam(15, "HeadModerator", "§cH-Mod §8┃ §c", "", false, NameTagVisibility.ALWAYS)
                    .createTeam(16, "Moderator", "§cMod §8┃ §c", "", false, NameTagVisibility.ALWAYS)
                    .createTeam(17, "HeadSupporter", "§9H-Sup §8┃ §9", "", false, NameTagVisibility.ALWAYS)
                    .createTeam(18, "Supporter", "§9Sup §8┃ §9", "", false, NameTagVisibility.ALWAYS)
                    .createTeam(19, "HeadBuilder", "§aH-Build §8┃ §a", "", false, NameTagVisibility.ALWAYS)
                    .createTeam(20, "Builder", "§aBuild §8┃ §a", "", false, NameTagVisibility.ALWAYS)
                    .createTeam(21, "Freund", "§eFreund §8┃ §e", "", false, NameTagVisibility.ALWAYS)
                    .createTeam(22, "Youtuber", "§5YT §8┃ §5", "", false, NameTagVisibility.ALWAYS)
                    .createTeam(23, "Diamond+", "§3", "", false, NameTagVisibility.ALWAYS)
                    .createTeam(24, "Diamond", "§3", "", false, NameTagVisibility.ALWAYS)
                    .createTeam(25, "Emerald", "§a", "", false, NameTagVisibility.ALWAYS)
                    .createTeam(26, "Gold", "§6", "", false, NameTagVisibility.ALWAYS)
                    .createTeam(27, "Spieler", "§8", "", false, NameTagVisibility.ALWAYS)
                    .createTeam(28, "Spectator", "", "", true, NameTagVisibility.HIDE_FOR_OTHER_TEAMS)
                    .build();

            module.getSuperAPI().scoreboard(player).animate(new String[]{
                "§3•§b● Tavaria.de §8▍ §7Lobby",
                "§b•§3● Tavaria.de §8▍ §7Lobby",
                "§3•§b● Tavaria.de §8▍ §7Lobby"
            });
            
            module.getMongoAPI().getBackendManager().getUser(player.getUniqueId().toString(), (User t) -> {
                module.getSuperAPI().setMetadata(player, "userData", t);

                if(t.getRankId() == 17) {
                    if(!player.isOp()) {
                        player.setOp(true);
                    }
                }
                
                module.addExtraItems(player);
                
                if(module.getSpectators().contains(player)) {
                    module.getSuperAPI().addToScoreboardTeam(player, "28-Spectator");

                } else if(module.getUserData(player).getRankId() == 17) {
                    module.getSuperAPI().addToScoreboardTeam(player, "10-Owner");

                } else if(module.getUserData(player).getRankId() == 16) {
                    module.getSuperAPI().addToScoreboardTeam(player, "11-Manager");

                } else if(module.getUserData(player).getRankId() == 15) {
                    module.getSuperAPI().addToScoreboardTeam(player, "12-Administrator");

                } else if(module.getUserData(player).getRankId() == 14) {
                    module.getSuperAPI().addToScoreboardTeam(player, "13-HeadDeveloper");

                } else if(module.getUserData(player).getRankId() == 13) {
                    module.getSuperAPI().addToScoreboardTeam(player, "14-Developer");

                } else if(module.getUserData(player).getRankId() == 12) {
                    module.getSuperAPI().addToScoreboardTeam(player, "15-HeadModerator");

                } else if(module.getUserData(player).getRankId() == 11) {
                    module.getSuperAPI().addToScoreboardTeam(player, "16-Moderator");

                } else if(module.getUserData(player).getRankId() == 10) {
                    module.getSuperAPI().addToScoreboardTeam(player, "17-HeadSupporter");

                } else if(module.getUserData(player).getRankId() == 9) {
                    module.getSuperAPI().addToScoreboardTeam(player, "18-Supporter");

                } else if(module.getUserData(player).getRankId() == 8) {
                    module.getSuperAPI().addToScoreboardTeam(player, "19-HeadBuilder");

                } else if(module.getUserData(player).getRankId() == 7) {
                    module.getSuperAPI().addToScoreboardTeam(player, "20-Builder");

                } else if(module.getUserData(player).getRankId() == 6) {
                    module.getSuperAPI().addToScoreboardTeam(player, "21-Freund");

                } else if(module.getUserData(player).getRankId() == 5) {
                    module.getSuperAPI().addToScoreboardTeam(player, "22-Youtuber");

                } else if(module.getUserData(player).getRankId() == 4) {
                    module.getSuperAPI().addToScoreboardTeam(player, "23-Diamond+");

                } else if(module.getUserData(player).getRankId() == 3) {
                    module.getSuperAPI().addToScoreboardTeam(player, "24-Diamond");

                } else if(module.getUserData(player).getRankId() == 2) {
                    module.getSuperAPI().addToScoreboardTeam(player, "25-Emerald");

                } else if(module.getUserData(player).getRankId() == 1) {
                    module.getSuperAPI().addToScoreboardTeam(player, "26-Gold");

                } else {
                    module.getSuperAPI().addToScoreboardTeam(player, "27-Spieler");
                }

                if(module.getSuperAPI().location(new File(module.getDataFolder(), "locations.yml")).doesLocationExists("Spawn-Hologram")) {
                    module.getSuperAPI().hologram(module.getSuperAPI().location(new File(module.getDataFolder(), "locations.yml")).getLocation("Spawn-Hologram"),
                            "§8§m------------------------------",
                            "§8•§7● §bTavaria.de §8▍ §bNetzwerk §7●§8•",
                            " ",
                            "§7Herzlich Willkommen§8,",
                            player.getDisplayName(),
                            " ",
                            "§8► §7Alle wichtigen Befehle sieht du",
                            " §7mit §8/§bhilfe",
                            " ",
                            "§8► §aViel Spaß auf dem Netzwerk§8!",
                            "§8§m------------------------------"
                    ).showHologramForPlayer(player);
                }
                
                try {
                    
                    module.getSuperAPI().runTaskLater(() -> {
                        if (t.getRankId() == 0) {
                            if (!module.getLobbyData(player).getActivePremiumGadgets().isEmpty()) {
                                module.getLobbyData(player).getActivePremiumGadgets().clear();
                                
                                module.getSuperAPI().runTaskLater(() -> {
                                    if (player.getInventory().getHelmet().getType() == Material.STAINED_GLASS) {
                                        player.getInventory().setHelmet(null);
                                    }

                                    if (player.getInventory().getBoots().getType() == Material.LEATHER_BOOTS) {
                                        player.getInventory().setBoots(null);
                                    }
                                }, 20);
                                
                            }
                        } else if (!module.getLobbyData(player).getActivePremiumGadgets().isEmpty()) {

                            if (module.getLobbyData(player).getActivePremiumGadgets().contains("ballon")) {
                                module.spawnBallon(player);
                            }

                        }
                    }, 20);

                    if (!module.getLobbyData(player).getActiveGadgets().isEmpty()) {
                        module.getLobbyData(player).getActiveGadgets().forEach((gadgets) -> {

                            if (gadgets.startsWith("banner-")) {
                                if (module.getLobbyData(player).getGadgetsBought().contains(gadgets)
                                        | module.getLobbyData(player).getGadgetsBought().contains("banner-*")
                                        | module.getLobbyData(player).getGadgetsBought().contains("*")) {

                                    player.getInventory().setHelmet(module.getGadgets().get(gadgets));

                                }
                            }

                            if (gadgets.startsWith("hat-")) {
                                if (module.getLobbyData(player).getGadgetsBought().contains(gadgets)
                                        | module.getLobbyData(player).getGadgetsBought().contains("hat-*")
                                        | module.getLobbyData(player).getGadgetsBought().contains("*")) {

                                    player.getInventory().setHelmet(module.getGadgets().get(gadgets));

                                }
                            }

                            if (gadgets.startsWith("head-")) {
                                if (module.getLobbyData(player).getGadgetsBought().contains(gadgets)
                                        | module.getLobbyData(player).getGadgetsBought().contains("head-*")
                                        | module.getLobbyData(player).getGadgetsBought().contains("*")) {

                                    player.getInventory().setHelmet(module.getGadgets().get(gadgets));

                                }
                            }

                        });
                    }
                    
                } catch(Exception exception) {}

            });

            if(!player.hasPlayedBefore()) {
                if(module.getSuperAPI().location(new File(module.getDataFolder(), "locations.yml")).doesLocationExists("Spawn")) {
                    player.teleport(module.getSuperAPI().location(new File(module.getDataFolder(), "locations.yml")).getLocation("Spawn"));
                }
            }

            module.getPlayerHider().forEach((hiders) -> {
                hiders.hidePlayer(player);
            });
            
            player.playSound(player.getLocation(), Sound.LEVEL_UP, 1F, 10F);
            event.setJoinMessage(null);
        }, EventPriority.NORMAL);
        //</editor-fold>
        
        //<editor-fold defaultstate="collapsed" desc="PlayerMoveEvent">
        module.getSuperAPI().registerEvent(PlayerMoveEvent.class, (EventListener<PlayerMoveEvent>) (PlayerMoveEvent event) -> {
            final Player player = event.getPlayer();
            
            //<editor-fold defaultstate="collapsed" desc="shield">
            module.getPlayerShield().keySet().forEach((shield) -> {
                Bukkit.getOnlinePlayers().forEach((players) -> {
                    
                    players.playEffect(shield.getLocation(), Effect.ENDER_SIGNAL, 1);
                    
                    if(players != shield) {
                            
                        if(shield.getLocation().distance(players.getLocation()) <= 3.0D) {

                            double x = shield.getLocation().getX() + players.getLocation().getX();
                            double y = shield.getLocation().getY() + players.getLocation().getY();
                            double z = shield.getLocation().getZ() + players.getLocation().getZ();

                            if(module.getUserData(shield).getRankId() > module.getUserData(players).getRankId()) {
                                
                                if(players.getPassenger() != null)
                                    players.getPassenger().leaveVehicle();
                                
                                players.playSound(shield.getLocation(), Sound.ENDERDRAGON_WINGS, 0.5F, 7.8F);
                                shield.playSound(shield.getLocation(), Sound.ENDERDRAGON_WINGS, 0.5F, 7.8F);

                                players.setVelocity(new Vector(x, y, z).normalize().multiply(1.0D).setY(1.0D));
                                
                            }

                        }
                            
                    }
                    
                });
            });
            //</editor-fold>
            
            //<editor-fold defaultstate="collapsed" desc="particles">
            if(!module.getLobbyData(player).getActiveGadgets().isEmpty()) {
                module.getLobbyData(player).getActiveGadgets().forEach((gadgets) -> {
                    if(gadgets.startsWith("particle-")) {
                        Bukkit.getOnlinePlayers().forEach((players) -> {
                            
                            if(players.canSee(player)) {
                                if (gadgets.equals("particle-greensparks")) {
                                    players.spigot().playEffect(player.getLocation(), Effect.HAPPY_VILLAGER, 1, 1, (float) 0.5, (float) 0.5, (float) 0.5, 1, 4, 16);
                                }
                                
                                if (gadgets.equals("particle-whitesparks")) {
                                    players.spigot().playEffect(player.getLocation(), Effect.FIREWORKS_SPARK, 1, 1, 0, 0, 0, (float) 0.1, 4, 16);
                                }
                                
                                if (gadgets.equals("particle-purplesparks")) {
                                    players.spigot().playEffect(player.getLocation(), Effect.WITCH_MAGIC, 1, 1, (float) 0.5, (float) 0.5, (float) 0.5, 1, 8, 16);
                                }
                                
                                if (gadgets.equals("particle-flames")) {
                                    players.spigot().playEffect(player.getLocation(), Effect.FLAME, 1, 1, 0, 0, 0, (float) 0.1, 4, 16);
                                }
                                
                                if (gadgets.equals("particle-portal")) {
                                    players.spigot().playEffect(player.getLocation(), Effect.PORTAL, 1, 1, (float) 0.5, (float) 0.5, (float) 0.5, 1, 32, 16);
                                    players.spigot().playEffect(player.getLocation(), Effect.PORTAL, 1, 1, 0, 0, 0, (float) 0.1, 16, 16);
                                }
                                
                                if (gadgets.equals("particle-letters")) {
                                    players.spigot().playEffect(player.getLocation(), Effect.FLYING_GLYPH, 1, 1, (float) 0.5, (float) 0.5, (float) 0.5, 1, 32, 16);
                                    players.spigot().playEffect(player.getLocation(), Effect.FLYING_GLYPH, 1, 1, 0, 0, 0, (float) 0.1, 16, 16);
                                }
                                
                                if (gadgets.equals("particle-lavasparks")) {
                                    players.spigot().playEffect(player.getLocation(), Effect.LAVA_POP, 1, 1, (float) 0.5, (float) 0.5, (float) 0.5, 1, 2, 16);
                                }
                                
                                if (gadgets.equals("particle-rainbow")) {
                                    players.spigot().playEffect(player.getLocation(), Effect.COLOURED_DUST, 1, 1, (float) 0.5, (float) 0.5, (float) 0.5, 1, 16, 16);
                                }
                            }
                            
                        });
                    }
                });
            }
            //</editor-fold>
            
            //<editor-fold defaultstate="collapsed" desc="jetpack">
            if(player.isSneaking()) {
                if(module.getLobbyData(player).getActivePremiumGadgets().contains("jetpack") && !module.getEditors().contains(player)) {
                    
                    Bukkit.getOnlinePlayers().forEach((players) -> {
                        if(players.canSee(player)) {
                            players.spigot().playEffect(player.getLocation(), Effect.PARTICLE_SMOKE, 1, 1, 0, 0, 0, (float) 0.1, 16, 16);
                            players.spigot().playEffect(player.getLocation(), Effect.LAVA_POP, 1, 1, 0, 0, 0, (float) 0.1, 16, 16);
                        }
                    });
                    
                    if (player.getLocation().getY() <= 64) {
                        Vector v = player.getLocation().getDirection().multiply(1.5).setY(0.50);
                        player.setVelocity(v);
                    } else if (player.getLocation().getY() <= 96) {
                        Vector v = player.getLocation().getDirection().multiply(1.5).setY(0.45);
                        player.setVelocity(v);
                    } else if (player.getLocation().getY() <= 112) {
                        Vector v = player.getLocation().getDirection().multiply(1.5).setY(0.40);
                        player.setVelocity(v);
                    } else if (player.getLocation().getY() <= 120) {
                        Vector v = player.getLocation().getDirection().multiply(1.5).setY(0.35);
                        player.setVelocity(v);
                    } else if (player.getLocation().getY() <= 128) {
                        Vector v = player.getLocation().getDirection().multiply(1.5).setY(0.30);
                        player.setVelocity(v);
                    } else {
                        Vector v = player.getLocation().getDirection().multiply(1.5).setY(0.25);
                        player.setVelocity(v);
                    }
                    
                }
            }
            //</editor-fold>
            
        }, EventPriority.NORMAL);
        //</editor-fold>
        
        //<editor-fold defaultstate="collapsed" desc="PlayerQuitEvent">
        module.getSuperAPI().registerEvent(PlayerQuitEvent.class, (EventListener<PlayerQuitEvent>) (PlayerQuitEvent event) -> {
            final Player player = event.getPlayer();
            
            if(module.getSpectators().contains(player))
                module.getSpectators().remove(player);
            
            if(module.getFlys().contains(player))
                module.getFlys().remove(player);
            
            if(module.getPlayerHider().contains(player))
                module.getPlayerHider().remove(player);
            
            if(module.getPlayerShield().containsKey(player)) {
                module.getPlayerShield().get(player).cancel();
                module.getPlayerShield().remove(player);
            }
            
            if(module.getPlayerBallonBlock().containsKey(player)) {
                module.getPlayerBallonBlock().get(player).remove();
                module.getPlayerBallonBlock().remove(player);
            }
            
            if(module.getPlayerBallonBat().containsKey(player)) {
                module.getPlayerBallonBat().get(player).remove();
                module.getPlayerBallonBat().remove(player);
            }
            
            if(module.getPlayerKillLocation().containsKey(player))
                module.getPlayerKillLocation().remove(player);
            
            module.getMongoAPI().getBackendManager().updateUser(module.getUserData(player));
            module.getMongoAPI().getBackendManager().updateLobby(module.getLobbyData(player));
            module.getSuperAPI().removeMetadata(player, "userData");
            module.getSuperAPI().removeMetadata(player, "lobbyData");
            
            Bukkit.getOnlinePlayers().forEach((players) -> {
                players.getScoreboard().getTeams().forEach((teams) -> {
                    
                    teams.removePlayer(player);
                    
                });
            });
            
            event.setQuitMessage(null);
        }, EventPriority.NORMAL);
        //</editor-fold>
        
        //<editor-fold defaultstate="collapsed" desc="PlayerKickEvent">
        module.getSuperAPI().registerEvent(PlayerKickEvent.class, (EventListener<PlayerKickEvent>) (PlayerKickEvent event) -> {
            final Player player = event.getPlayer();
            
            if(module.getSpectators().contains(player))
                module.getSpectators().remove(player);
            
            if(module.getFlys().contains(player))
                module.getFlys().remove(player);
            
            if(module.getPlayerHider().contains(player))
                module.getPlayerHider().remove(player);
            
            if(module.getPlayerShield().containsKey(player)) {
                module.getPlayerShield().get(player).cancel();
                module.getPlayerShield().remove(player);
            }
            
            if(module.getPlayerBallonBlock().containsKey(player)) {
                module.getPlayerBallonBlock().get(player).remove();
                module.getPlayerBallonBlock().remove(player);
            }

            if(module.getPlayerBallonBat().containsKey(player)) {
                module.getPlayerBallonBat().get(player).remove();
                module.getPlayerBallonBat().remove(player);
            }
            
            if (module.getPlayerKillLocation().containsKey(player))
                module.getPlayerKillLocation().remove(player);
            
            module.getMongoAPI().getBackendManager().updateUser(module.getUserData(player));
            module.getMongoAPI().getBackendManager().updateLobby(module.getLobbyData(player));
            module.getSuperAPI().removeMetadata(player, "userData");
            module.getSuperAPI().removeMetadata(player, "lobbyData");

            Bukkit.getOnlinePlayers().forEach((players) -> {
                players.getScoreboard().getTeams().forEach((teams) -> {

                    teams.removePlayer(player);

                });
            });
            
        }, EventPriority.NORMAL);
        //</editor-fold>
        
        //<editor-fold defaultstate="collapsed" desc="PlayerToggleFlightEvent">
        module.getSuperAPI().registerEvent(PlayerToggleFlightEvent.class, (EventListener<PlayerToggleFlightEvent>) (PlayerToggleFlightEvent event) -> {
            final Player player = event.getPlayer();
            
            
            
        }, EventPriority.NORMAL);
        //</editor-fold>
        
        //<editor-fold defaultstate="collapsed" desc="PlayerToggleSneakEvent">
        module.getSuperAPI().registerEvent(PlayerToggleSneakEvent.class, (EventListener<PlayerToggleSneakEvent>) (PlayerToggleSneakEvent event) -> {
            final Player player = event.getPlayer();
            
            
            
        }, EventPriority.NORMAL);
        //</editor-fold>
        
        //<editor-fold defaultstate="collapsed" desc="PlayerUnleashEntityEvent">
        module.getSuperAPI().registerEvent(PlayerUnleashEntityEvent.class, (EventListener<PlayerUnleashEntityEvent>) (PlayerUnleashEntityEvent event) -> {
            event.setCancelled(true);
        }, EventPriority.NORMAL);
        //</editor-fold>
        
        //<editor-fold defaultstate="collapsed" desc="ProjectileLaunchEvent">
        module.getSuperAPI().registerEvent(ProjectileLaunchEvent.class, (EventListener<ProjectileLaunchEvent>) (ProjectileLaunchEvent event) -> {
            event.setCancelled(true);
        }, EventPriority.NORMAL);
        //</editor-fold> 
        
        //<editor-fold defaultstate="collapsed" desc="WeatherChangeEvent">
        module.getSuperAPI().registerEvent(WeatherChangeEvent.class, (EventListener<WeatherChangeEvent>) (WeatherChangeEvent event) -> {
            event.setCancelled(true);
        }, EventPriority.NORMAL);
        //</editor-fold> 
        
    }
    
    //<editor-fold defaultstate="collapsed" desc="startCooldown">
    private void startCooldown(final Player player) {
        module.getPlayerInteractCooldown().add(player);
        
        module.getSuperAPI().runTaskLater(() -> {
            module.getPlayerInteractCooldown().remove(player);
        }, 5);
    }
    //</editor-fold>

}
