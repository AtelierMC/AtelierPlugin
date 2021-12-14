package ga.caomile.atelier.api;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;

import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public enum AtelierRole {
    MANGO("<gradient:#FFFF00:#FF9900> Mango </gradient>"),
    MEMBER("<color:green> Member </color:green>")
    ;

    private final Component display;

    AtelierRole(String display) {
        this.display = MiniMessage.get().parse(display);
    }

    public Component getDisplay() {
        return this.display;
    }

    public String getId() {
        return this.toString();
    }

    @Override
    public String toString() {
        return this.name().toLowerCase(Locale.ROOT);
    }
}
