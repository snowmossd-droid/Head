package vennlmao.code;

import org.bukkit.plugin.java.JavaPlugin;
import vennlmao.code.commands.HeadCommand;
import vennlmao.code.commands.KhanhNguCommand;
import vennlmao.code.listeners.HeadListener;
import vennlmao.code.managers.HeadManager;
import vennlmao.code.managers.EffectManager;
import vennlmao.code.tasks.HintTask;

public class KhanhNgu extends JavaPlugin {

    private static KhanhNgu instance;
    private HeadManager headManager;
    private EffectManager effectManager;
    private HintTask hintTask;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();

        headManager = new HeadManager(this);
        effectManager = new EffectManager(this);

        getServer().getPluginManager().registerEvents(new HeadListener(this), this);

        getCommand("head").setExecutor(new HeadCommand(this));
        getCommand("khanhngu").setExecutor(new KhanhNguCommand(this));

        startHintTask();

        getLogger().info("KhanhNgu by VennLMAO đã khởi động!");
    }

    @Override
    public void onDisable() {
        if (effectManager != null) {
            effectManager.removeAllEffects();
        }
        getLogger().info("KhanhNgu đã tắt!");
    }

    public void reload() {
        reloadConfig();
        headManager = new HeadManager(this);
        effectManager.reload();
        if (hintTask != null) {
            hintTask.cancel();
        }
        startHintTask();
    }

    private void startHintTask() {
        if (getConfig().getBoolean("hints.enabled", true)) {
            int interval = getConfig().getInt("hints.interval", 300);
            hintTask = new HintTask(this);
            hintTask.runTaskTimer(this, interval * 20L, interval * 20L);
        }
    }

    public static KhanhNgu getInstance() {
        return instance;
    }

    public HeadManager getHeadManager() {
        return headManager;
    }

    public EffectManager getEffectManager() {
        return effectManager;
    }
}
