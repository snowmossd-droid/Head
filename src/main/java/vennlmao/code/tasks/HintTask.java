package vennlmao.code.tasks;

import org.bukkit.scheduler.BukkitRunnable;
import vennlmao.code.KhanhNgu;
import vennlmao.code.utils.ColorUtils;

import java.util.List;

public class HintTask extends BukkitRunnable {

    private final KhanhNgu plugin;
    private int index = 0;

    public HintTask(KhanhNgu plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        List<String> hints = plugin.getConfig().getStringList("hints.messages");
        if (hints.isEmpty()) return;

        String hint = hints.get(index % hints.size());
        plugin.getServer().broadcastMessage(ColorUtils.colorize(hint));
        index++;
    }
}
