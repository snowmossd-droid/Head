package vennlmao.code.managers;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.NamespacedKey;
import vennlmao.code.KhanhNgu;
import vennlmao.code.utils.ColorUtils;

import java.net.URL;
import java.util.Base64;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class HeadManager {

    private final KhanhNgu plugin;
    private static final String HEAD_KEY = "khanhngu_special_head";
    private ItemStack cachedHead;

    public HeadManager(KhanhNgu plugin) {
        this.plugin = plugin;
        this.cachedHead = null;
    }

    public ItemStack createHead() {
        if (cachedHead != null) return cachedHead.clone();

        String textureUrl = plugin.getConfig().getString(
                "head.texture-url",
                "https://textures.minecraft.net/texture/cfd27d8b218b5aa972fda9054926d7b1b2c0329a456332148fcc3d6c6d34cf0f"
        );
        String displayName = ColorUtils.colorize(
                plugin.getConfig().getString("head.name", "&6&lKhánh Ngủ")
        );
        List<String> lore = plugin.getConfig().getStringList("head.lore")
                .stream()
                .map(ColorUtils::colorize)
                .collect(Collectors.toList());

        ItemStack head = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) head.getItemMeta();

        applyTextureFromUrl(meta, textureUrl);

        meta.setDisplayName(displayName);
        meta.setLore(lore);

        NamespacedKey key = new NamespacedKey(plugin, HEAD_KEY);
        meta.getPersistentDataContainer().set(key, PersistentDataType.BYTE, (byte) 1);

        head.setItemMeta(meta);
        cachedHead = head;
        return head.clone();
    }

    private void applyTextureFromUrl(SkullMeta meta, String textureUrl) {
        try {
            GameProfile profile = new GameProfile(UUID.randomUUID(), "KhanhNguHead");

            String encodedUrl = Base64.getEncoder().encodeToString(
                    ("{\"textures\":{\"SKIN\":{\"url\":\"" + textureUrl + "\"}}}").getBytes()
            );

            profile.getProperties().put("textures", new Property("textures", encodedUrl));

            java.lang.reflect.Field profileField = meta.getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
            profileField.set(meta, profile);
        } catch (Exception e) {
            plugin.getLogger().warning("Không thể áp dụng texture từ URL: " + textureUrl);
            plugin.getLogger().warning("Lỗi: " + e.getMessage());
        }
    }

    public boolean isSpecialHead(ItemStack item) {
        if (item == null || item.getType() != Material.PLAYER_HEAD) return false;
        if (!item.hasItemMeta()) return false;
        NamespacedKey key = new NamespacedKey(plugin, HEAD_KEY);
        return item.getItemMeta().getPersistentDataContainer().has(key, PersistentDataType.BYTE);
    }

    public void invalidateCache() {
        cachedHead = null;
    }
}
