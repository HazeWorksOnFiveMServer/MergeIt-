
package com.haze47.mergeit;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;

public class MergeListener implements Listener {

    private static final String MERGE_ITEM_NAME = "mergeit!";

    // Map of raw materials to their block variants
    private static final Map<Material, Material> MERGEABLES = new HashMap<>();

    static {
        MERGEABLES.put(Material.RAW_IRON, Material.RAW_IRON_BLOCK);
        MERGEABLES.put(Material.RAW_GOLD, Material.RAW_GOLD_BLOCK);
        MERGEABLES.put(Material.RAW_COPPER, Material.RAW_COPPER_BLOCK);
        MERGEABLES.put(Material.REDSTONE, Material.REDSTONE_BLOCK);
        MERGEABLES.put(Material.LAPIS_LAZULI, Material.LAPIS_BLOCK);
        MERGEABLES.put(Material.COAL, Material.COAL_BLOCK);
        MERGEABLES.put(Material.DIAMOND, Material.DIAMOND_BLOCK);
        MERGEABLES.put(Material.EMERALD, Material.EMERALD_BLOCK);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        // Schedule a repeating task to check inventory periodically
        new BukkitRunnable() {
            @Override
            public void run() {
                if (!player.isOnline()) {
                    cancel();
                    return;
                }
                mergeItemsInInventory(player.getInventory());
            }
        }.runTaskTimer(MergeItPlugin.getPlugin(MergeItPlugin.class), 0, 20);
    }

    private void mergeItemsInInventory(Inventory inventory) {
        boolean hasMergeItem = inventory.contains(Material.PAPER) && inventory.all(Material.PAPER).values().stream()
                .anyMatch(stack -> {
                    ItemMeta meta = stack.getItemMeta();
                    return meta != null && meta.hasDisplayName() && meta.getDisplayName().equals(MERGE_ITEM_NAME);
                });

        if (!hasMergeItem) return;

        for (Map.Entry<Material, Material> entry : MERGEABLES.entrySet()) {
            Material input = entry.getKey();
            Material output = entry.getValue();

            if (inventory.contains(input, 9)) {
                inventory.removeItem(new ItemStack(input, 9));
                inventory.addItem(new ItemStack(output, 1));
            }
        }
    }
}
