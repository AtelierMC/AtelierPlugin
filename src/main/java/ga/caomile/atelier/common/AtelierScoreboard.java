package ga.caomile.atelier.common;

import ga.caomile.atelier.Atelier;
import ga.caomile.atelier.AtelierConstants;
import ga.caomile.atelier.api.AtelierPlayer;
import ga.caomile.atelier.utils.HexUtils;
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

        playerScoreboard.getTeam("tps").suffix(Component.text(HexUtils.colorify("&7❏&f  TPS&7 ▶&e ") + (double) Math.round(Bukkit.getTPS()[0] * 10) / 10, NamedTextColor.GREEN));
        playerScoreboard.getTeam("online").suffix(Component.text(HexUtils.colorify("&e {online}/{max}"
                .replace("{online}", String.valueOf(Bukkit.getOnlinePlayers().size()))
                .replace("{max}", String.valueOf(Bukkit.getMaxPlayers())))));

        // TAB
        player.playerListName(Component.text("[", NamedTextColor.DARK_GRAY)
                .append(Component.text(player.getPing() + "ms", NamedTextColor.GREEN))
                .append(Component.text("]", NamedTextColor.DARK_GRAY))
                .append(atelierPlayer.getRole().getDisplay())
                .append(Component.text(player.getName() + " ", NamedTextColor.WHITE))
        );

        ((CraftPlayer) player).setPlayerListHeader(HexUtils.colorify(
                "§8§l>    <g#100:#F2770B:#E85C90:#E0C810>AtelierMC §8(" + this.plugin.getServer().getMinecraftVersion() +  ")§8§l     <" +
                "\n" +
                "§fOnline: §e" + Bukkit.getOnlinePlayers().size() + "§f/§e" + Bukkit.getMaxPlayers() +
                "\n"
        ));
        ((CraftPlayer) player).setPlayerListFooter(HexUtils.colorify(
                "\n" +
                "§fJoin our <#7289da>Discord Server §ffor support!"
        ));
    }

    public void init(Player player) {
        AtelierPlayer atelierPlayer = this.plugin.getManager().getAtelierPlayer(player);

        // Scoreboard initialization
        Scoreboard board = Bukkit.getScoreboardManager().getNewScoreboard();
        Objective objective = board.registerNewObjective("sidebar", "dummy", HexUtils.colorify("<g:#043AFB:#00FDEE>&lAtelierMC"));
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        
        // Init teams
        Team tps = board.registerNewTeam("tps");
        tps.addEntry("  <#18c1ff>●&f TPS&7:");
        tps.suffix(Component.text("&7❏&f  TPS&7 ▶&e " + (double) Math.round(Bukkit.getTPS()[0] * 10) / 10));
        Team online = board.registerNewTeam("online");
        online.addEntry("  <#18c1ff>●&f Online&7:");
        online.suffix(Component.text(HexUtils.colorify("&e {online}&8/&e{max}"
                .replace("{online}", String.valueOf(Bukkit.getOnlinePlayers().size()))
                .replace("{max}", String.valueOf(Bukkit.getMaxPlayers())))));

        objective.getScore(HexUtils.colorify("&7" + dateFormat.format(new Date(System.currentTimeMillis())))).setScore(10);
        objective.getScore(" ").setScore(9);
        objective.getScore(HexUtils.colorify("&8► &b&lPLAYER")).setScore(8);
        objective.getScore(HexUtils.colorify("  <#18c1ff>●&f User&7: " + player.getName())).setScore(7);
        objective.getScore(HexUtils.colorify("  <#18c1ff>●&f Rank&7: " + LegacyComponentSerializer.builder().build().serialize(atelierPlayer.getRole().getDisplay()))).setScore(6);
        objective.getScore("").setScore(5);
        objective.getScore(HexUtils.colorify("&8► &b&lSERVER")).setScore(4);
        objective.getScore(HexUtils.colorify("  <#18c1ff>●&f TPS&7:")).setScore(3);
        objective.getScore(HexUtils.colorify("  <#18c1ff>●&f Online&7:")).setScore(2);
        objective.getScore("").setScore(1);
        objective.getScore(HexUtils.colorify("&6&o " + AtelierConstants.ATELIER_IP)).setScore(0);

        player.setScoreboard(board);
    }

    public void destroy(Player player) {
        player.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
    }
}
