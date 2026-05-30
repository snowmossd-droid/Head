package vennlmao.code.managers;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import vennlmao.code.KhanhNgu;
import vennlmao.code.utils.ColorUtils;

import java.util.*;

public class EffectManager {

    private final KhanhNgu plugin;
    private final Set<UUID> activePlayers = new HashSet<>();

    private static final int DURATION = Integer.MAX_VALUE;
    private static final boolean AMBIENT = true;
    private static final boolean PARTICLES = false;
    private static final boolean ICON = true;

    private record EffectEntry(PotionEffectType type, String configKey) {}

    private static final List<EffectEntry> EFFECT_ENTRIES = List.of(
            new EffectEntry(PotionEffectType.INVISIBILITY, "invisibility"),
            new EffectEntry(PotionEffectType.SPEED, "speed"),
            new EffectEntry(PotionEffectType.STRENGTH, "strength"),
            new EffectEntry(PotionEffectType.REGENERATION, "regeneration"),
            new EffectEntry(PotionEffectType.FIRE_RESISTANCE, "fire-resistance")
    );

    public EffectManager(KhanhNgu plugin) {
        this.plugin = plugin;
    }

    public void applyEffects(Player player) {
        if (activePlayers.contains(player.getUniqueId())) return;

        for (EffectEntry entry : EFFECT_ENTRIES) {
            String path = "effects." + entry.configKey();
            if (plugin.getConfig().getBoolean(path + ".enabled", true)) {
                int amplifier = plugin.getConfig().getInt(path + ".amplifier", 0);
                player.addPotionEffect(new PotionEffect(
                        entry.type(), DURATION, amplifier, AMBIENT, PARTICLES, ICON
                ));
            }
        }

        activePlayers.add(player.getUniqueId());

        String msg = plugin.getConfig().getString("messages.effects-applied", "&aĐã kích hoạt các hiệu ứng đặc biệt!");
        player.sendMessage(ColorUtils.colorize(msg));
    }

    public void removeEffects(Player player) {
        if (!activePlayers.contains(player.getUniqueId())) return;

        for (EffectEntry entry : EFFECT_ENTRIES) {
            player.removePotionEffect(entry.type());
        }

        activePlayers.remove(player.getUniqueId());

        String msg = plugin.getConfig().getString("messages.effects-removed", "&cHiệu ứng đặc biệt đã bị xóa!");
        player.sendMessage(ColorUtils.colorize(msg));
    }

    public boolean hasEffects(Player player) {
        return activePlayers.contains(player.getUniqueId());
    }

    public void removeAllEffects() {
        for (UUID uuid : new HashSet<>(activePlayers)) {
            Player player = plugin.getServer().getPlayer(uuid);
            if (player != null && player.isOnline()) {
                for (EffectEntry entry : EFFECT_ENTRIES) {
                    player.removePotionEffect(entry.type());
                }
            }
        }
        activePlayers.clear();
    }

    public void reload() {
        for (UUID uuid : new HashSet<>(activePlayers)) {
            Player player = plugin.getServer().getPlayer(uuid);
            if (player != null && player.isOnline()) {
                for (EffectEntry entry : EFFECT_ENTRIES) {
                    player.removePotionEffect(entry.type());
                }
                applyEffects(player);
            }
        }
    }
}
