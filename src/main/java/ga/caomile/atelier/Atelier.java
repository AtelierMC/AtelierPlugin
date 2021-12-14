package ga.caomile.atelier;

import ga.caomile.atelier.listeners.player.PlayerJoinQuitListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Team;

public class Atelier extends JavaPlugin {
    private AtelierManager manager;

    private boolean emergencyStop = false;

    @Override
    public void onLoad() {
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") == null || Bukkit.getPluginManager().getPlugin("spark") == null) {
            getLogger().severe("Could not find PlaceholderAPI/spark! This plugin(s) are required.");
            emergencyStop = true;
        }
    }

    @Override
    public void onEnable() {
        if (emergencyStop) {
            setEnabled(false);
            return;
        }

        this.manager = new AtelierManager(this);

        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new PlayerJoinQuitListener(this), this);
    }

    @Override
    public void onDisable() {
        // Cleanup
        for (Team team : Bukkit.getScoreboardManager().getMainScoreboard().getTeams()) {
            if (team.getName().startsWith("roleDisplay-")) {
                team.unregister();
            }
        }
    }

    public AtelierManager getManager() {
        return this.manager;
    }
}
