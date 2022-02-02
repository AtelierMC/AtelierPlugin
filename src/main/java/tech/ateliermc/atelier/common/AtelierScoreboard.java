package tech.ateliermc.atelier.common;

import me.lucko.spark.api.Spark;
import me.lucko.spark.api.SparkProvider;
import me.lucko.spark.api.statistic.StatisticWindow;
import me.lucko.spark.api.statistic.types.DoubleStatistic;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextColor;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.protocol.game.ClientboundTabListPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.ServerScoreboard;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.scores.Objective;
import net.minecraft.world.scores.PlayerTeam;
import net.minecraft.world.scores.Score;
import net.minecraft.world.scores.Scoreboard;
import net.minecraft.world.scores.criteria.ObjectiveCriteria;
import org.apache.logging.log4j.core.config.builder.api.ComponentBuilder;
import tech.ateliermc.atelier.Atelier;
import tech.ateliermc.atelier.bridge.world.entity.player.PlayerBridge;

import java.awt.*;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class AtelierScoreboard {

    static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yy hh:mm aa");
    static final LuckPerms luckPerms = LuckPermsProvider.get();
    static final Spark spark = SparkProvider.get();

    protected static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("###,##0.0");

    public static void init(MinecraftServer server) {
        Atelier.executorService.scheduleAtFixedRate(() -> {
            for (ServerPlayer player : server.getPlayerList().getPlayers()) {
                /*
                User user = luckPerms.getUserManager().getUser(player.getUUID());
                DoubleStatistic<StatisticWindow.TicksPerSecond> tps = spark.tps();

                ServerScoreboard scoreboard = new ServerScoreboard(player.getServer());

                Objective objective = scoreboard.addObjective(player.getScoreboardName() + "obj-dummy",
                        ObjectiveCriteria.DUMMY,
                        new TextComponent("§f§lAtelierMC"),
                        ObjectiveCriteria.RenderType.INTEGER);
                scoreboard.setDisplayObjective(Scoreboard.DISPLAY_SLOT_SIDEBAR, objective);

                PlayerTeam tpsTeam = new PlayerTeam(scoreboard, "tps");
                tpsTeam.setPlayerPrefix(new TextComponent(" §8● §6TPS§7: §f"));
                tpsTeam.setPlayerSuffix(new TextComponent(DECIMAL_FORMAT.format(tps.poll(StatisticWindow.TicksPerSecond.MINUTES_1))));
                scoreboard.addPlayerToTeam("tps", tpsTeam);

                scoreboard.getOrCreatePlayerScore("§8§m                        §4", objective).setScore(13);
                scoreboard.getOrCreatePlayerScore(ChatFormatting.GRAY + dateFormat.format(new Date(System.currentTimeMillis())), objective).setScore(12);
                scoreboard.getOrCreatePlayerScore("§f", objective).setScore(11);
                scoreboard.getOrCreatePlayerScore("§b◆§7 Player:", objective).setScore(10);
                scoreboard.getOrCreatePlayerScore(" §8● §6User§7: §f" + player.getDisplayName().getContents(), objective).setScore(9);
                scoreboard.getOrCreatePlayerScore(" §8● §6Rank§7: §f" + user.getCachedData().getMetaData().getPrefix(), objective).setScore(8);
                scoreboard.getOrCreatePlayerScore(" §8● §6Ping§7: §f" + player.latency + "§7ms", objective).setScore(7);
                scoreboard.getOrCreatePlayerScore("§d", objective).setScore(6);
                scoreboard.getOrCreatePlayerScore("§b◆§7 Server:", objective).setScore(5);
                scoreboard.getOrCreatePlayerScore(" §8● §6TPS§7: §f", objective).setScore(4);
                scoreboard.getOrCreatePlayerScore(" §8● §6Memory§7: §f" + ((Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1024 / 1024) + "§8/§f" + (Runtime.getRuntime().totalMemory() / 1024 / 1024), objective).setScore(3);
                scoreboard.getOrCreatePlayerScore("§r", objective).setScore(2);
                scoreboard.getOrCreatePlayerScore("§e§i ateliermc.tech", objective).setScore(1);
                scoreboard.getOrCreatePlayerScore("§8§m                         §8", objective).setScore(0);

                ((PlayerBridge) player).setScoreboard(scoreboard);

                */
                final ClientboundTabListPacket packet = new ClientboundTabListPacket(
                        // Header
                        new TextComponent("AtelierMC"),
                        // Footer
                        new TextComponent("\n" +
                                "§7Welcome player, §fAtelier SMP Season 3" + "\n" +
                                "§7finally's here, deep breath through deep sorrow" + "\n" +
                                "§8Our fate intertwined once again." + "\n" + "\n" +
                                "§7" + player.getServer().getPlayerCount() + "§8/§b" + player.getServer().getMaxPlayers() + " ☃ §7┃ " + dateFormat.format(new Date(System.currentTimeMillis())) + " ⌚")
                );

                player.connection.send(packet);
            }
        }, 0, 5, TimeUnit.SECONDS);
    }
}
