package de.superklug.mygames.supertavarialobby;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import de.superklug.mygames.superapi.SuperAPI;
import de.superklug.mygames.supertavarialobby.commands.PlayerCommands;
import de.superklug.mygames.supertavarialobby.listeners.PlayerListeners;
import de.superklug.mygames.supertavarialobby.utils.enums.ChatType;
import de.superklug.mygames.supertavarialobby.utils.managers.InventoryManager;
import de.superklug.mygames.supertavarialobby.utils.runnables.ActionbarRunnable;
import de.superklug.mygames.supertavarialobby.utils.runnables.ClothRunnable;
import de.superklug.mygames.supertavarialobby.utils.runnables.LocationPartikelRunnable;
import de.superklug.mygames.supertavarialobby.utils.runnables.PlayerBallonEffektRunnable;
import de.superklug.mygames.supertavarialobby.utils.runnables.PlayerBallonRunnable;
import de.superklug.mygames.supertavarialobby.utils.runnables.UpdateScoreboardRunnable;
import de.superklug.mygames.supertavariamongoapi.MongoAPI;
import de.superklug.mygames.supertavariamongoapi.entities.User;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.entity.Bat;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class Lobby extends JavaPlugin {
    
    private @Getter SuperAPI superAPI;
    private @Getter MongoAPI mongoAPI;
    
    private @Getter InventoryManager inventoryManager;
    
    private @Getter @Setter ChatType chatType = ChatType.ALL;
    
    private final @Getter List<Player> spectators = Lists.newArrayList();
    private final @Getter List<Player> editors = Lists.newArrayList();
    private final @Getter List<Player> flys = Lists.newArrayList();
    
    private final @Getter List<Player> playerHider = Lists.newArrayList();
    private final @Getter List<Player> playerInteractCooldown = Lists.newArrayList();
    
    private final @Getter Map<Player, BukkitRunnable> playerShield = Maps.newConcurrentMap();
    private final @Getter Map<Player, Location> playerKillLocation = Maps.newConcurrentMap();
    
    private final @Getter Map<Player, FallingBlock> playerBallonBlock = Maps.newConcurrentMap();
    private final @Getter Map<Player, Bat> playerBallonBat = Maps.newConcurrentMap();
    
    private final @Getter Map<String, ItemStack> gadgets = Maps.newConcurrentMap();

    @Override
    public void onEnable() {
        init();
    }

    @Override
    public void onDisable() {
        
        Bukkit.getOnlinePlayers().forEach((players) -> {
            players.kickPlayer(superAPI.getPrefix() + "Der Server lädt kurz neu§8!");
        });
        
        mongoAPI.onDisable();
        
    }
    
    private void init() {
        this.superAPI = new SuperAPI(this, System.currentTimeMillis(), "§3•§b● Lobby §8▍ §7", "§7", "§b");
        this.mongoAPI = new MongoAPI();
        
        this.inventoryManager = new InventoryManager(this);
        
        new ActionbarRunnable(this).runTaskTimer(this, 20, 20);
        new UpdateScoreboardRunnable(this).runTaskTimer(this, 20, 20);
        new LocationPartikelRunnable(this).runTaskTimer(this, 20, 20);
        
        new PlayerBallonRunnable(this).runTaskTimer(this, 1, 1);
        new PlayerBallonEffektRunnable(this).runTaskTimer(this, 40, 40);
        
        new ClothRunnable(this).runTaskTimer(this, 1, 1);
        
        fillGadgets();
        
        final PluginManager pluginManager = Bukkit.getPluginManager();
        
            new PlayerCommands(this);
            pluginManager.registerEvents(new PlayerListeners(this), this);
        
        
    }
    
    //<editor-fold defaultstate="collapsed" desc="addExtraItems">
    public void addExtraItems(final Player player) {
        
        if(getUserData(player).getRankId() >= 5 | player.hasPermission("silentlobby.use")) {
            if(Bukkit.getServer().getServerName().equalsIgnoreCase("Silentlobby")) {
                player.getInventory().setItem(3, getSuperAPI().item(Material.TNT).setDisplayname("§8•§7● Silentlobby §8▍ §aAktiviert").build());
            } else {
                player.getInventory().setItem(3, getSuperAPI().item(Material.TNT).setDisplayname("§8•§7● Silentlobby §8▍ §cDeaktiviert").build());
            }
        }
        
        if(getUserData(player).getRankId() >= 4 | player.hasPermission("nick.use")) {
            player.getInventory().setItem(4, getSuperAPI().item(Material.NAME_TAG).setDisplayname("§8•§7● Autonick §8▍ §cDeaktiviert").build());
        }
        
        if(getUserData(player).getRankId() >= 5 | player.hasPermission("shield.use")) {
            if(playerShield.containsKey(player)) {
                player.getInventory().setItem(5, getSuperAPI().item(Material.EYE_OF_ENDER).setDisplayname("§8•§7● Schutzschild §8▍ §aAktiviert").build());
            } else {  
                player.getInventory().setItem(5, getSuperAPI().item(Material.EYE_OF_ENDER).setDisplayname("§8•§7● Schutzschild §8▍ §cDeaktiviert").build());
            }
        }
        
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="getUserData">
    /**
     * 
     * @param player The player
     * @return The cached user object
     */
    public User getUserData(final Player player) {
        return (User) player.getMetadata("userData").get(0).value();
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="getLobbyData">
    /**
     * 
     * @param player The player
     * @return The cached lobby object
     */
    public de.superklug.mygames.supertavariamongoapi.entities.Lobby getLobbyData(final Player player) {
        return (de.superklug.mygames.supertavariamongoapi.entities.Lobby) player.getMetadata("lobbyData").get(0).value();
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="getBoughtLore">
    public List<String> getBoughtLore(final Player player, final String name) {
        
        if(name.startsWith("banner-")) {
            if(getLobbyData(player).getGadgetsBought().contains(name.toLowerCase())
                    | getLobbyData(player).getGadgetsBought().contains("banner-*")
                    | getLobbyData(player).getGadgetsBought().contains("*")) {
                
                if (getLobbyData(player).getActiveGadgets().contains(name.toLowerCase())) {
                    return Arrays.asList(" ", "§8► §aGekauft §8▍ §2Aktiviert");
                }
                return Arrays.asList(" ", "§8► §aGekauft");
            }
        }
        
        if (name.startsWith("hat-")) {
            if (getLobbyData(player).getGadgetsBought().contains(name.toLowerCase())
                    | getLobbyData(player).getGadgetsBought().contains("hat-*")
                    | getLobbyData(player).getGadgetsBought().contains("*")) {
                
                if (getLobbyData(player).getActiveGadgets().contains(name.toLowerCase())) {
                    return Arrays.asList(" ", "§8► §aGekauft §8▍ §2Aktiviert");
                }
                return Arrays.asList(" ", "§8► §aGekauft");
            }
        }
        
        if (name.startsWith("head-")) {
            if (getLobbyData(player).getGadgetsBought().contains(name.toLowerCase())
                    | getLobbyData(player).getGadgetsBought().contains("head-*")
                    | getLobbyData(player).getGadgetsBought().contains("*")) {
                
                if (getLobbyData(player).getActiveGadgets().contains(name.toLowerCase())) {
                    return Arrays.asList(" ", "§8► §aGekauft §8▍ §2Aktiviert");
                }
                return Arrays.asList(" ", "§8► §aGekauft");
            }
        }
        
        if (name.startsWith("particle-")) {
            if (getLobbyData(player).getGadgetsBought().contains(name.toLowerCase())
                    | getLobbyData(player).getGadgetsBought().contains("particle-*")
                    | getLobbyData(player).getGadgetsBought().contains("*")) {
                
                if (getLobbyData(player).getActiveGadgets().contains(name.toLowerCase())) {
                    return Arrays.asList(" ", "§8► §aGekauft §8▍ §2Aktiviert");
                }
                return Arrays.asList(" ", "§8► §aGekauft");
            }
        }
        
        if (name.startsWith("bow-")) {
            if (getLobbyData(player).getGadgetsBought().contains(name.toLowerCase())
                    | getLobbyData(player).getGadgetsBought().contains("bow-*")
                    | getLobbyData(player).getGadgetsBought().contains("*")) {
                
                if (getLobbyData(player).getActiveGadgets().contains(name.toLowerCase())) {
                    return Arrays.asList(" ", "§8► §aGekauft §8▍ §2Aktiviert");
                }
                return Arrays.asList(" ", "§8► §aGekauft");
            }
        }
        
        return Arrays.asList(" ", "§8► §cNicht gekauft");
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="fillGadgets">
    private void fillGadgets() {
        
        gadgets.put("banner-white", getSuperAPI().item(Material.BANNER)
                .setDisplayname("§8•§7● Weiß").setBannerBaseDyeColor(DyeColor.WHITE).build());
        
        gadgets.put("banner-orange", getSuperAPI().item(Material.BANNER)
                .setDisplayname("§8•§7● Orange").setBannerBaseDyeColor(DyeColor.ORANGE).build());
        
        gadgets.put("banner-pirate", getSuperAPI().item(Material.BANNER)
                .setDisplayname("§8•§7● Pirat").setBannerBaseDyeColor(DyeColor.BLACK)
                .addBannerPattern(new Pattern(DyeColor.BLACK, PatternType.STRIPE_SMALL))
                .addBannerPattern(new Pattern(DyeColor.WHITE, PatternType.SKULL))
                .addBannerPattern(new Pattern(DyeColor.BLACK, PatternType.GRADIENT_UP)).build());
        
        gadgets.put("banner-germany", getSuperAPI().item(Material.BANNER)
                .setDisplayname("§8•§7● Deutschland").setBannerBaseDyeColor(DyeColor.RED)
                .addBannerPattern(new Pattern(DyeColor.BLACK, PatternType.STRIPE_LEFT))
                .addBannerPattern(new Pattern(DyeColor.YELLOW, PatternType.STRIPE_RIGHT))
                .addBannerPattern(new Pattern(DyeColor.RED, PatternType.STRIPE_CENTER)).build());
        
        gadgets.put("banner-ghost", getSuperAPI().item(Material.BANNER)
                .setDisplayname("§8•§7● Geist").setBannerBaseDyeColor(DyeColor.BLACK)
                .addBannerPattern(new Pattern(DyeColor.WHITE, PatternType.SKULL))
                .addBannerPattern(new Pattern(DyeColor.WHITE, PatternType.CURLY_BORDER))
                .addBannerPattern(new Pattern(DyeColor.WHITE, PatternType.CROSS))
                .addBannerPattern(new Pattern(DyeColor.WHITE, PatternType.CREEPER))
                .addBannerPattern(new Pattern(DyeColor.WHITE, PatternType.STRIPE_TOP)).build());
        
        gadgets.put("banner-mojang", getSuperAPI().item(Material.BANNER)
                .setDisplayname("§8•§7● Mojang").setBannerBaseDyeColor(DyeColor.WHITE)
                .addBannerPattern(new Pattern(DyeColor.ORANGE, PatternType.GRADIENT))
                .addBannerPattern(new Pattern(DyeColor.ORANGE, PatternType.GRADIENT_UP))
                .addBannerPattern(new Pattern(DyeColor.BROWN, PatternType.GRADIENT))
                .addBannerPattern(new Pattern(DyeColor.BROWN, PatternType.GRADIENT_UP))
                .addBannerPattern(new Pattern(DyeColor.BLACK, PatternType.MOJANG))
                .addBannerPattern(new Pattern(DyeColor.YELLOW, PatternType.MOJANG)).build());
        
        gadgets.put("banner-octopos", getSuperAPI().item(Material.BANNER)
                .setDisplayname("§8•§7● Tintenfisch").setBannerBaseDyeColor(DyeColor.WHITE)
                .addBannerPattern(new Pattern(DyeColor.BLACK, PatternType.CURLY_BORDER))
                .addBannerPattern(new Pattern(DyeColor.CYAN, PatternType.HALF_HORIZONTAL_MIRROR))
                .addBannerPattern(new Pattern(DyeColor.CYAN, PatternType.STRIPE_TOP))
                .addBannerPattern(new Pattern(DyeColor.CYAN, PatternType.STRIPE_CENTER))
                .addBannerPattern(new Pattern(DyeColor.CYAN, PatternType.BORDER))
                .addBannerPattern(new Pattern(DyeColor.BLACK, PatternType.GRADIENT_UP)).build());
        
        gadgets.put("banner-creeper", getSuperAPI().item(Material.BANNER)
                .setDisplayname("§8•§7● Creeper").setBannerBaseDyeColor(DyeColor.WHITE)
                .addBannerPattern(new Pattern(DyeColor.LIME, PatternType.GRADIENT_UP))
                .addBannerPattern(new Pattern(DyeColor.GREEN, PatternType.FLOWER))
                .addBannerPattern(new Pattern(DyeColor.GRAY, PatternType.TRIANGLES_BOTTOM))
                .addBannerPattern(new Pattern(DyeColor.LIME, PatternType.SQUARE_BOTTOM_LEFT))
                .addBannerPattern(new Pattern(DyeColor.GRAY, PatternType.TRIANGLES_TOP))
                .addBannerPattern(new Pattern(DyeColor.GREEN, PatternType.CURLY_BORDER))
                .addBannerPattern(new Pattern(DyeColor.LIME, PatternType.BRICKS))
                .addBannerPattern(new Pattern(DyeColor.BLACK, PatternType.CREEPER)).build());
        
        gadgets.put("hat-driver", getSuperAPI().item(Material.GLASS)
                .setDisplayname("§8•§7● Taucher").build());
        
        gadgets.put("hat-astronaut", getSuperAPI().item(Material.STAINED_GLASS)
                .setDisplayname("§8•§7● Astronaut").build());
        
        gadgets.put("hat-tnt", getSuperAPI().item(Material.TNT)
                .setDisplayname("§8•§7● TNT").build());
        
        gadgets.put("hat-smile", getSuperAPI().item(Material.DROPPER)
                .setDisplayname("§8•§7● Smile").build());
        
        gadgets.put("hat-melon", getSuperAPI().item(Material.MELON_BLOCK)
                .setDisplayname("§8•§7● Melone").build());
        
        gadgets.put("hat-pumpkin", getSuperAPI().item(Material.PUMPKIN)
                .setDisplayname("§8•§7● Kürbis").build());
        
        gadgets.put("hat-spongebob", getSuperAPI().item(Material.SPONGE)
                .setDisplayname("§8•§7● Spongebob").build());
        
        gadgets.put("hat-mask", getSuperAPI().item(Material.DAYLIGHT_DETECTOR)
                .setDisplayname("§8•§7● Maske").build());
        
        gadgets.put("head-vanilloper", getSuperAPI().item(Material.SKULL_ITEM, 1, (short) 3).setSkullOwner("Vanilloper")
                .setDisplayname("§8•§7● Vanilloper").build());
        
        gadgets.put("head-cyrodev", getSuperAPI().item(Material.SKULL_ITEM, 1, (short) 3).setSkullOwner("CyroDev")
                .setDisplayname("§8•§7● CyroDev").build());
        
        gadgets.put("head-superklug", getSuperAPI().item(Material.SKULL_ITEM, 1, (short) 3).setSkullOwner("SuperKlug")
                .setDisplayname("§8•§7● SuperKlug").build());
        
        gadgets.put("head-ungespielt", getSuperAPI().item(Material.SKULL_ITEM, 1, (short) 3).setSkullOwner("ungespielt")
                .setDisplayname("§8•§7● ungespielt").build());
        
        gadgets.put("head-rewinside", getSuperAPI().item(Material.SKULL_ITEM, 1, (short) 3).setSkullOwner("rewinside")
                .setDisplayname("§8•§7● rewinside").build());
        
        gadgets.put("head-gommehd", getSuperAPI().item(Material.SKULL_ITEM, 1, (short) 3).setSkullOwner("GommeHD")
                .setDisplayname("§8•§7● GommeHD").build());
        
        gadgets.put("head-vareide", getSuperAPI().item(Material.SKULL_ITEM, 1, (short) 3).setSkullOwner("Vareide")
                .setDisplayname("§8•§7● Vareide").build());
        
        gadgets.put("head-gronkh", getSuperAPI().item(Material.SKULL_ITEM, 1, (short) 3).setSkullOwner("Gronkh")
                .setDisplayname("§8•§7● Gronkh").build());
        
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="spawnBallon">
    public void spawnBallon(final Player player) {
        if(getLobbyData(player).getActivePremiumGadgets().contains("ballon")) {
            final int rankId = getUserData(player).getRankId();
            
            FallingBlock fallingBlock = null;
            
            if(rankId == 17) {
                fallingBlock = player.getWorld().spawnFallingBlock(player.getLocation().add(0, 2, 0), Material.REDSTONE_BLOCK, (byte) 0);
                fallingBlock.setDropItem(false);
            }
            if (rankId == 16) {
                fallingBlock = player.getWorld().spawnFallingBlock(player.getLocation().add(0, 2, 0), Material.REDSTONE_BLOCK, (byte) 0);
                fallingBlock.setDropItem(false);
            }
            if (rankId == 15) {
                fallingBlock = player.getWorld().spawnFallingBlock(player.getLocation().add(0, 2, 0), Material.REDSTONE_BLOCK, (byte) 0);
                fallingBlock.setDropItem(false);
            }
            if (rankId == 14) {
                fallingBlock = player.getWorld().spawnFallingBlock(player.getLocation().add(0, 2, 0), Material.DIAMOND_BLOCK, (byte) 0);
                fallingBlock.setDropItem(false);
            }
            if (rankId == 13) {
                fallingBlock = player.getWorld().spawnFallingBlock(player.getLocation().add(0, 2, 0), Material.DIAMOND_BLOCK, (byte) 0);
                fallingBlock.setDropItem(false);
            }
            if (rankId == 12) {
                fallingBlock = player.getWorld().spawnFallingBlock(player.getLocation().add(0, 2, 0), Material.STAINED_CLAY, (byte) 6);
                fallingBlock.setDropItem(false);
            }
            if (rankId == 11) {
                fallingBlock = player.getWorld().spawnFallingBlock(player.getLocation().add(0, 2, 0), Material.STAINED_CLAY, (byte) 6);
                fallingBlock.setDropItem(false);
            }
            if (rankId == 10) {
                fallingBlock = player.getWorld().spawnFallingBlock(player.getLocation().add(0, 2, 0), Material.STAINED_CLAY, (byte) 11);
                fallingBlock.setDropItem(false);
            }
            if (rankId == 9) {
                fallingBlock = player.getWorld().spawnFallingBlock(player.getLocation().add(0, 2, 0), Material.STAINED_CLAY, (byte) 11);
                fallingBlock.setDropItem(false);
            }
            if (rankId == 8) {
                fallingBlock = player.getWorld().spawnFallingBlock(player.getLocation().add(0, 2, 0), Material.STAINED_CLAY, (byte) 13);
                fallingBlock.setDropItem(false);
            }
            if (rankId == 7) {
                fallingBlock = player.getWorld().spawnFallingBlock(player.getLocation().add(0, 2, 0), Material.STAINED_CLAY, (byte) 13);
                fallingBlock.setDropItem(false);
            }
            if (rankId == 6) {
                fallingBlock = player.getWorld().spawnFallingBlock(player.getLocation().add(0, 2, 0), Material.GOLD_BLOCK, (byte) 0);
                fallingBlock.setDropItem(false);
            }
            if (rankId == 5) {
                fallingBlock = player.getWorld().spawnFallingBlock(player.getLocation().add(0, 2, 0), Material.DRAGON_EGG, (byte) 0);
                fallingBlock.setDropItem(false);
            }
            if (rankId == 4) {
                fallingBlock = player.getWorld().spawnFallingBlock(player.getLocation().add(0, 2, 0), Material.STAINED_CLAY, (byte) 9);
                fallingBlock.setDropItem(false);
            }
            if (rankId == 3) {
                fallingBlock = player.getWorld().spawnFallingBlock(player.getLocation().add(0, 2, 0), Material.STAINED_CLAY, (byte) 9);
                fallingBlock.setDropItem(false);
            }
            if (rankId == 2) {
                fallingBlock = player.getWorld().spawnFallingBlock(player.getLocation().add(0, 2, 0), Material.STAINED_CLAY, (byte) 5);
                fallingBlock.setDropItem(false);
            }
            if (rankId == 1) {
                fallingBlock = player.getWorld().spawnFallingBlock(player.getLocation().add(0, 2, 0), Material.STAINED_CLAY, (byte) 4);
                fallingBlock.setDropItem(false);
            }
            
            if(fallingBlock != null) {
                Bat bat = (Bat) player.getWorld().spawnEntity(player.getLocation().add(0, 1, 0), EntityType.BAT);
                
                bat.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 255));
                bat.setRemoveWhenFarAway(false);
                bat.setPassenger(fallingBlock);
                bat.setLeashHolder(player);
                
                getPlayerBallonBlock().put(player, fallingBlock);
                getPlayerBallonBat().put(player, bat);
            }
            
        }
    }
    //</editor-fold>

}
