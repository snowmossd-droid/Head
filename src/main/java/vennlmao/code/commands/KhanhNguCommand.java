package vennlmao.code.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import vennlmao.code.KhanhNgu;
import vennlmao.code.utils.ColorUtils;
import vennlmao.code.utils.CommandHelper;

import java.util.List;

public class KhanhNguCommand implements CommandExecutor, TabCompleter {

    private final KhanhNgu plugin;

    public KhanhNguCommand(KhanhNgu plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("khanhngu.admin")) {
            sender.sendMessage(ColorUtils.colorize(plugin.getConfig().getString("messages.no-permission")));
            return true;
        }

        if (args.length == 0) {
            sender.sendMessage(ColorUtils.colorize(plugin.getConfig().getString("messages.usage-khanhngu",
                    "&eUsage: &f/khanhngu <give|take> <player> | /khanhngu reload")));
            return true;
        }

        String sub = args[0].toLowerCase();

        if (sub.equals("reload")) {
            plugin.reload();
            sender.sendMessage(ColorUtils.colorize(plugin.getConfig().getString("messages.reload-success", "&aPlugin đã được reload!")));
            return true;
        }

        if (args.length < 2) {
            sender.sendMessage(ColorUtils.colorize(plugin.getConfig().getString("messages.usage-khanhngu")));
            return true;
        }

        Player target = plugin.getServer().getPlayer(args[1]);
        if (target == null) {
            String msg = plugin.getConfig().getString("messages.player-not-found", "&cKhông tìm thấy: {player}");
            sender.sendMessage(ColorUtils.colorize(msg.replace("{player}", args[1])));
            return true;
        }

        switch (sub) {
            case "give" -> CommandHelper.giveHead(plugin, sender, target);
            case "take" -> CommandHelper.takeHead(plugin, sender, target);
            default -> sender.sendMessage(ColorUtils.colorize(plugin.getConfig().getString("messages.usage-khanhngu")));
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) return List.of("give", "take", "reload");
        if (args.length == 2 && !args[0].equalsIgnoreCase("reload")) {
            return plugin.getServer().getOnlinePlayers()
                    .stream().map(Player::getName)
                    .filter(n -> n.toLowerCase().startsWith(args[1].toLowerCase()))
                    .toList();
        }
        return List.of();
    }
}
