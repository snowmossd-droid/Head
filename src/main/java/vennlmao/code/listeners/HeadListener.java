package vennlmao.code.listeners;

import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.entity.Player;
import vennlmao.code.KhanhNgu;
import vennlmao.code.utils.ColorUtils;

public class HeadListener implements Listener {

    private final KhanhNgu plugin;

    public HeadListener(KhanhNgu plugin) {
        this.plugin = plugin;
    }

    private boolean isHoldingSpecialHead(Player player) {
        ItemStack main = player.getInventory().getItemInMainHand();
        ItemStack off = player.getInventory().getItemInOffHand();
        return plugin.getHeadManager().isSpecialHead(main)
                || plugin.getHeadManager().isSpecialHead(off);
    }

    private void checkAndUpdateEffects(Player player) {
        if (isHoldingSpecialHead(player)) {
            plugin.getEffectManager().applyEffects(player);
        } else {
            plugin.getEffectManager().removeEffects(player);
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onBlockPlace(BlockPlaceEvent event) {
        if (plugin.getHeadManager().isSpecialHead(event.getItemInHand())) {
            event.setCancelled(true);
            String msg = plugin.getConfig().getString("messages.cannot-place", "&cBạn không thể đặt đầu này xuống đất!");
            event.getPlayer().sendMessage(ColorUtils.colorize(msg));
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onItemHeld(PlayerItemHeldEvent event) {
        plugin.getServer().getScheduler().runTask(plugin, () -> checkAndUpdateEffects(event.getPlayer()));
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onSwapHands(PlayerSwapHandItemsEvent event) {
        plugin.getServer().getScheduler().runTask(plugin, () -> checkAndUpdateEffects(event.getPlayer()));
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;
        plugin.getServer().getScheduler().runTask(plugin, () -> checkAndUpdateEffects(player));
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onDropItem(PlayerDropItemEvent event) {
        plugin.getServer().getScheduler().runTask(plugin, () -> checkAndUpdateEffects(event.getPlayer()));
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        plugin.getEffectManager().removeEffects(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK && event.getAction() != Action.RIGHT_CLICK_AIR) return;
        ItemStack item = event.getItem();
        if (plugin.getHeadManager().isSpecialHead(item)) {
            event.setCancelled(true);
        }
    }
}
