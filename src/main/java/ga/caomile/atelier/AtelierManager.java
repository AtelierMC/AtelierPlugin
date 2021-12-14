package ga.caomile.atelier;

import ga.caomile.atelier.api.AtelierPlayer;
import ga.caomile.atelier.common.AtelierScoreboard;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class AtelierManager {
    private final Map<Player, AtelierPlayer> ATELIER_PLAYERS = new ConcurrentHashMap<>();

    private final Atelier plugin;

    private final AtelierScoreboard atelierScoreboard;

    public AtelierManager(final Atelier plugin) {
        this.plugin = plugin;
        this.atelierScoreboard = new AtelierScoreboard(plugin);
    }

    public void addNewAtelierPlayer(Player player) {
        ATELIER_PLAYERS.put(player, new AtelierPlayer(player));
    }

    public void removeNewAtelierPlayer(Player player) {
        ATELIER_PLAYERS.remove(player, new AtelierPlayer(player));
    }

    public AtelierPlayer getAtelierPlayer(Player player) {
        return ATELIER_PLAYERS.get(player);
    }

    public AtelierScoreboard getAtelierScoreboard() {
        return atelierScoreboard;
    }
}
