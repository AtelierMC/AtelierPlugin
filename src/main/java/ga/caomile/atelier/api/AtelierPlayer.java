package ga.caomile.atelier.api;

import org.bukkit.entity.Player;

public class AtelierPlayer {
    private final Player player;

    private AtelierRole role = AtelierRole.MANGO;

    public AtelierPlayer(Player player) {
        this.player = player;
    }

    public AtelierRole getRole() {
        return this.role;
    }
}
