package ga.caomile.atelier.listeners.player;

import ga.caomile.atelier.Atelier;
import ga.caomile.atelier.api.AtelierPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

public class PlayerJoinQuitListener implements Listener {
    private final Atelier plugin;

    private boolean runningScoreboardTask;

    public PlayerJoinQuitListener(Atelier plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(org.bukkit.event.player.PlayerJoinEvent e) {
        Player player = e.getPlayer();

        this.plugin.getManager().addNewAtelierPlayer(player);
        this.plugin.getManager().getAtelierScoreboard().init(player);
        //e.joinMessage();

        if (!runningScoreboardTask) {
            runningScoreboardTask = true;
            new BukkitRunnable() {
                @Override
                public void run() {
                    if (Bukkit.getOnlinePlayers().size() == 0) return;

                    for (Player player : Bukkit.getOnlinePlayers()) {
                        plugin.getManager().getAtelierScoreboard().updateScoreboard(player);
                    }
                }
            }.runTaskTimerAsynchronously(this.plugin, 0L, 20L);
        }
    }

    @EventHandler
    public void onPlayerQuit(org.bukkit.event.player.PlayerQuitEvent e) {
        Player player = e.getPlayer();

        this.plugin.getManager().removeNewAtelierPlayer(player);
        this.plugin.getManager().getAtelierScoreboard().destroy(player);
    }
}
