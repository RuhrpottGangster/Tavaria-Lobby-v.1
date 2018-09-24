package de.superklug.mygames.supertavarialobby.utils.managers;

import com.google.common.collect.Maps;
import de.superklug.mygames.supertavarialobby.Lobby;
import de.superklug.mygames.supertavarialobby.utils.enums.InventoryType;
import java.util.Map;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class InventoryManager {
    
    private final Lobby module;
    
    private final Map<InventoryType, Inventory> inventories = Maps.newConcurrentMap();
    
    public final @Getter ItemStack fill;

    public InventoryManager(final Lobby module) {
        this.module = module;
        
        this.fill = module.getSuperAPI().item(Material.STAINED_GLASS_PANE, 1, (short) 7).setNoName().build();
        
        init();
    }
    
    private void init() {
        
        {
            final Inventory inventory = Bukkit.createInventory(null, 9);
            
                inventory.setItem(0, module.getSuperAPI().item(Material.COMPASS).setDisplayname("§8•§7● Navigator").build());
                inventory.setItem(1, module.getSuperAPI().item(Material.BLAZE_ROD).setDisplayname("§8•§7● Spieler §8▍ §aAktiviert").build());
                
                inventory.setItem(7, module.getSuperAPI().item(Material.CHEST).setDisplayname("§8•§7● Extras").build());
                inventory.setItem(8, module.getSuperAPI().item(Material.NETHER_STAR).setDisplayname("§8•§7● Lobby wecheln").build());
            
            this.inventories.put(InventoryType.HOTBAR, inventory);
        }
        
        {
            final Inventory inventory = Bukkit.createInventory(null, 9*6, "§8•§7● Navigator");
            
                for(int i = 0; i < inventory.getSize(); i++) {
                    inventory.setItem(i, fill);
                }
                
                

            this.inventories.put(InventoryType.NAVIGATOR, inventory);
        }
        
        {
            final Inventory inventory = Bukkit.createInventory(null, 9*5, "§8•§7● Nick");
            
                for(int i = 0; i < inventory.getSize(); i++) {
                    inventory.setItem(i, fill);
                }
                
                inventory.setItem(9+7, module.getSuperAPI().item(Material.INK_SACK, 1, (short) 8)
                        .setDisplayname("§8•§7● Spieler").setLore("§8► §7Du wirst als §8Spieler §7angezeigt§8!").build());
                
                inventory.setItem(9+5, module.getSuperAPI().item(Material.INK_SACK, 1, (short) 14)
                        .setDisplayname("§6•§e● Gold").setLore("§8► §7Du wirst als §6Gold §7angezeigt§8!").build());
                
                inventory.setItem(9+3, module.getSuperAPI().item(Material.INK_SACK, 1, (short) 10)
                        .setDisplayname("§2•§a● Emerald").setLore("§8► §7Du wirst als §aEmerald §7angezeigt§8!").build());
                
                inventory.setItem(9+1, module.getSuperAPI().item(Material.INK_SACK, 1, (short) 6)
                        .setDisplayname("§b•§3● Diamond").setLore("§8► §7Du wirst als §3Diamond §7angezeigt§8!").build());
                
                inventory.setItem(9+9+9+4, module.getSuperAPI().item(Material.INK_SACK, 1, (short) 1)
                    .setDisplayname("§4•§c● Deaktivieren").setLore("§8► §7Du wirst mit deinem originalen Rang angezeigt§8!").build());
                
            this.inventories.put(InventoryType.NICK, inventory);
        }
        
        {
            final Inventory inventory = Bukkit.createInventory(null, 9*5, "§8•§7● Extras");
            
                for(int i = 0; i < inventory.getSize(); i++) {
                    inventory.setItem(i, fill);
                }
                
                //inventory.setItem(10, module.getSuperAPI().item(Material.BONE)
                //        .setDisplayname("§8•§7● Haustiere").setLore(" ", "§8► §7Besitze niedliche §bHaustiere§8, §7die dich", " §7in der Lobby überall hin begleiten§8.").build());
                
                inventory.setItem(9+1, module.getSuperAPI().item(Material.BANNER)
                    .setDisplayname("§8•§7● Banner").setBannerBaseDyeColor(DyeColor.WHITE).setLore(" ", "§8► §7Ziehe verschiedene §bKöpfe §7in der Lobby", " §7an um dich zu verkleiden§8.").build());
                
                inventory.setItem(9+3, module.getSuperAPI().item(Material.CHAINMAIL_HELMET)
                    .setDisplayname("§8•§7● Hüte").setLore(" ", "§8► §7Ziehe einzigartige §bHüte §7in der Lobby", " §7an und zeige den anderen Spielern§8, §7wer der", " §7Boss auf dem Server ist§8.").build());
                
                inventory.setItem(9+5, module.getSuperAPI().item(Material.SKULL_ITEM, 1, (short) 3)
                    .setDisplayname("§8•§7● Köpfe").setLore(" ", "§8► §7Ziehe verschiedene §bKöpfe §7in der Lobby", " §7an um dich zu verkleiden§8.").build());
                
                inventory.setItem(9+7, module.getSuperAPI().item(Material.BLAZE_POWDER)
                    .setDisplayname("§8•§7● Partikel").setLore(" ", "§8► §7Verursache coole §bPartikel §7in der Lobby", " §7und bekomme die Aufmerksammkeit aller Spieler§8.").build());
                
                inventory.setItem(9+9+9+2, module.getSuperAPI().item(Material.ARROW)
                    .setDisplayname("§8•§7● Schuss§8-§7Effekte").setLore(" ", "§8► §7Hinterlasse atemberaubende §bSchuss§8-§bEffekte", " §7beim schießen mit dem Bogen in jedem Spielmodus§8.").build());
                
                inventory.setItem(9+9+9+6, module.getSuperAPI().item(Material.GOLD_INGOT)
                    .setDisplayname("§6•§e● Spezial").setLore(" ", "§8► §7Kaufe dir §6Gold§8/§aEmerald§8/§3Diamond §7und erhalte Zugriff", " §7auf alle Features in dieser Kategorie§8.").build());

            this.inventories.put(InventoryType.EXTRAS, inventory);
        }
        
        {
            final Inventory inventory = Bukkit.createInventory(null, 9*5, "§8•§7● Lobby wechseln");
            
                for(int i = 0; i < inventory.getSize(); i++) {
                    inventory.setItem(i, fill);
                }
                
                inventory.setItem(9+4, module.getSuperAPI().item(Material.GLOWSTONE_DUST)
                        .setDisplayname("§6•§e● Premium §8▍ §e01").setLore("§8► §7TODO: Sockets§8!").build());
                
                inventory.setItem(9+9+9+3, module.getSuperAPI().item(Material.SULPHUR)
                        .setDisplayname("§8•§7● Spieler §8▍ §701").setLore("§8► §7TODO: Sockets§8!").build());
                
                inventory.setItem(9+9+9+5, module.getSuperAPI().item(Material.SULPHUR)
                        .setDisplayname("§8•§7● Spieler §8▍ §702").setLore("§8► §7TODO: Sockets§8!").build());

            this.inventories.put(InventoryType.LOBBYSWITCHER, inventory);
        }
        
    }
    
    public Inventory getInventory(final InventoryType type) {
        final Inventory inventory = this.inventories.get(type);
        
        assert inventory != null;
        
        return inventory;
    }

}
