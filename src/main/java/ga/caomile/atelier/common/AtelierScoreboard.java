package ga.caomile.atelier.common;

import ga.caomile.atelier.Atelier;
import ga.caomile.atelier.AtelierConstants;
import ga.caomile.atelier.api.AtelierPlayer;
import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.minecraft.server.MinecraftServer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_18_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

import java.text.SimpleDateFormat;
import java.util.Date;

public final class AtelierScoreboard {
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    private final Atelier plugin;

    public AtelierScoreboard(Atelier plugin) {
        this.plugin = plugin;
    }
    
    public void updateScoreboard(Player player) {
        AtelierPlayer atelierPlayer = this.plugin.getManager().getAtelierPlayer(player);
        Scoreboard playerScoreboard = player.getScoreboard();

        playerScoreboard.getTeam("memory").suffix(Component.text(((Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1000 / 1000) + "/" + Runtime.getRuntime().totalMemory() / 1000 / 1000 + "MB", NamedTextColor.GREEN));
        playerScoreboard.getTeam("cpu").suffix(Component.text(PlaceholderAPI.setPlaceholders(player, "%spark_cpu_process%"), NamedTextColor.GREEN));
        playerScoreboard.getTeam("tps").suffix(Component.text((double) Math.round(Bukkit.getTPS()[0] * 10) / 10, NamedTextColor.GREEN));

        // TAB
        player.playerListName(Component.text("[", NamedTextColor.DARK_GRAY)
                .append(Component.text(player.getPing() + "ms", NamedTextColor.GREEN))
                .append(Component.text("]", NamedTextColor.DARK_GRAY))
                .append(atelierPlayer.getRole().getDisplay())
                .append(Component.text(player.getName() + " ", NamedTextColor.WHITE))
        );

        ((CraftPlayer) player).setPlayerListHeader(
                "§8§l>    §x§0§3§4§f§f§9§lA§x§0§3§6§5§f§8§lt§x§0§2§7§b§f§6§le§x§0§2§9§0§f§5§ll§x§0§1§a§6§f§3§li§x§0§1§b§c§f§2§le§x§0§0§d§1§f§0§lr§x§0§0§e§7§e§f§lM§x§0§0§f§d§e§e§lC §8(" + this.plugin.getServer().getMinecraftVersion() +  ")§8§l     <" +
                "\n" +
                "§fOnline: §e" + Bukkit.getOnlinePlayers().size() + "§f/§e" + Bukkit.getMaxPlayers() +
                "\n"
        );
        ((CraftPlayer) player).setPlayerListFooter(
                "\n" +
                "§fJoin our §x§7§2§8§9§d§aDiscord Server §ffor support!"
        );
    }

    public void init(Player player) {
        AtelierPlayer atelierPlayer = this.plugin.getManager().getAtelierPlayer(player);

        // Scoreboard initialization
        Scoreboard board = Bukkit.getScoreboardManager().getNewScoreboard();
        Objective objective = board.registerNewObjective("sidebar", "dummy", MiniMessage.get().parse("<gradient:#043AFB:#00FDEE><bold>AtelierMC</bold></gradient>"));
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        
        // Init teams
        Team cpu = board.registerNewTeam("cpu");
        cpu.addEntry("CPU: ");
        cpu.suffix(Component.text(PlaceholderAPI.setPlaceholders(player, "%spark_cpu_process%"), NamedTextColor.GREEN));
        Team memory = board.registerNewTeam("memory");
        memory.addEntry("Memory: ");
        memory.suffix(Component.text(((Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1000 / 1000) + "/" + Runtime.getRuntime().totalMemory() / 1000 / 1000 + "MB", NamedTextColor.GREEN));
        Team tps = board.registerNewTeam("tps");
        tps.addEntry("TPS: ");
        tps.suffix(Component.text((double) Math.round(Bukkit.getTPS()[0] * 10) / 10, NamedTextColor.GREEN));

        objective.getScore(ChatColor.GRAY + AtelierConstants.ATELIER_IP).setScore(0);
        objective.getScore("").setScore(1);
        objective.getScore("Memory: ").setScore(3);
        objective.getScore("CPU: ").setScore(4);
        objective.getScore("TPS: ").setScore(5);
        objective.getScore(" ").setScore(6);
        objective.getScore("Rank:" + LegacyComponentSerializer.builder().build().serialize(atelierPlayer.getRole().getDisplay())).setScore(7);
        objective.getScore("Player: " + ChatColor.GREEN +  player.getName()).setScore(8);
        objective.getScore("  ").setScore(9);
        objective.getScore(ChatColor.GRAY + dateFormat.format(new Date(System.currentTimeMillis()))).setScore(10);

        player.setScoreboard(board);
    }

    public void destroy(Player player) {
        player.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
    }
}
