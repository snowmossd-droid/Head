package vennlmao.code.utils;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import vennlmao.code.KhanhNgu;

public class CommandHelper {

    public static void giveHead(KhanhNgu plugin, CommandSender sender, Player target) {
        ItemStack head = plugin.getHeadManager().createHead();
        target.getInventory().addItem(head);

        String msg = plugin.getConfig().getString("messages.give-success", "&aĐã cho {player} đầu đặc biệt!");
        sender.sendMessage(ColorUtils.colorize(msg.replace("{player}", target.getName())));
    }

    public static void takeHead(KhanhNgu plugin, CommandSender sender, Player target) {
        boolean found = false;
        ItemStack[] contents = target.getInventory().getContents();

        for (int i = 0; i < contents.length; i++) {
            ItemStack item = contents[i];
            if (plugin.getHeadManager().isSpecialHead(item)) {
                target.getInventory().setItem(i, null);
                found = true;
                break;
            }
        }

        if (found) {
            plugin.getEffectManager().removeEffects(target);
            String msg = plugin.getConfig().getString("messages.take-success", "&aĐã lấy đầu từ {player}!");
            sender.sendMessage(ColorUtils.colorize(msg.replace("{player}", target.getName())));
        } else {
            String msg = plugin.getConfig().getString("messages.take-fail", "&e{player} &ckhông có đầu đặc biệt!");
            sender.sendMessage(ColorUtils.colorize(msg.replace("{player}", target.getName())));
        }
    }
}
