package tech.ateliermc.atelier.common;

import me.lucko.spark.api.statistic.StatisticWindow;
import me.lucko.spark.api.statistic.types.DoubleStatistic;
import net.luckperms.api.model.user.User;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoPacket;
import net.minecraft.network.protocol.game.ClientboundTabListPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.ServerScoreboard;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.scores.Objective;
import net.minecraft.world.scores.Scoreboard;
import net.minecraft.world.scores.criteria.ObjectiveCriteria;
import tech.ateliermc.atelier.Atelier;
import tech.ateliermc.atelier.bridge.server.ServerScoreboardBridge;
import tech.ateliermc.atelier.bridge.server.level.ServerPlayerBridge;
import tech.ateliermc.atelier.bridge.world.entity.player.PlayerBridge;
import tech.ateliermc.atelier.util.FormatUtil;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class AtelierScoreboard {
    static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yy hh:mm aa");

    public static void init(MinecraftServer server) {
        Atelier.executorService.scheduleAtFixedRate(() -> {
            for (ServerPlayer player : server.getPlayerList().getPlayers()) {
                User user = Atelier.luckPerms.getUserManager().getUser(player.getUUID());

                ((ServerPlayerBridge) player).setPlayerListName(new TextComponent(" ")
                        .append(Atelier.adventure().toNative(AtelierChat.LEGACY_SECTION_UXRC.deserialize(user.getCachedData().getMetaData().getPrefix())))
                        .append(new TextComponent(" " + player.getDisplayName().getContents()).withStyle(ChatFormatting.WHITE))
                );

                for (ServerPlayer anotherPlayersLoopThatIDontWant : server.getPlayerList().getPlayers()) {
                    anotherPlayersLoopThatIDontWant.connection.send(new ClientboundPlayerInfoPacket(ClientboundPlayerInfoPacket.Action.UPDATE_DISPLAY_NAME, player));
                }

                final ClientboundTabListPacket packet = new ClientboundTabListPacket(
                        // Header
                        new TextComponent("")
                                .append(AtelierChat.ATELIER_LOGO)
                                .append(" ??7??? ??b1.17 ??7??? ??c1.18.1??7 ???")
                                .append("\n")
                                .append("??8??m                         ??8"),
                        // Footer
                        new TextComponent("??8??m                         ??8")
                                .append(new TextComponent("\n??d????????? ").withStyle(Style.EMPTY.withColor(TextColor.parseColor("#FBBBE1"))))
                                .append("??7welcomes you!")
                                .append("\n??7You can join our ??bDiscord ??7 through:")
                                .append("\n")
                                .append(AtelierChat.ATELIER_DISCORD_LINK)
                                .append("\n\n")
                                .append("??7" + player.getServer().getPlayerCount() + "??8/??b" + player.getServer().getMaxPlayers() + " ??? ??7??? ??a" + dateFormat.format(new Date(System.currentTimeMillis())) + " ???")
                );

                player.connection.send(packet);

                if (((ServerPlayerBridge) player).enableScoreboard()) {
                    ((ServerScoreboardBridge) player.getScoreboard()).bridge$removePlayer(player, true);
                    ((PlayerBridge) player).setScoreboard(makeScoreboard(player));
                    ((ServerScoreboardBridge) player.getScoreboard()).bridge$addPlayer(player, true);
                }
            }
        }, 0, 1, TimeUnit.SECONDS);
    }

    public static Scoreboard makeScoreboard(ServerPlayer player) {
        ServerScoreboard scoreboard = new ServerScoreboard(player.server);
        User user = Atelier.luckPerms.getUserManager().getUser(player.getUUID());

        double tpsValue = Atelier.spark.tps().poll(StatisticWindow.TicksPerSecond.SECONDS_10);
        double cpuValue = Atelier.spark.cpuProcess().poll(StatisticWindow.CpuUsage.SECONDS_10);

        Objective objective = scoreboard.addObjective(player.getScoreboardName() + "obj-dummy",
                ObjectiveCriteria.DUMMY,
                new TextComponent("")
                        .append(AtelierChat.ATELIER_LOGO)
                        .append("??7 ??? ")
                        .append(new TextComponent(FormatUtil.formatPing(player.latency) + "ms"))
                        .append("??7  ???")
                ,
                ObjectiveCriteria.RenderType.INTEGER);
        scoreboard.setDisplayObjective(Scoreboard.DISPLAY_SLOT_SIDEBAR, objective);

        scoreboard.getOrCreatePlayerScore("??8??m                        ??4", objective).setScore(12);
        scoreboard.getOrCreatePlayerScore("??f", objective).setScore(11);
        scoreboard.getOrCreatePlayerScore("??b?????7 Player:", objective).setScore(10);
        scoreboard.getOrCreatePlayerScore(" ??8??? ??6User??7: ??f" + player.getName().getContents(), objective).setScore(9);
        scoreboard.getOrCreatePlayerScore(" ??8??? ??6Rank??7: ??f" + FormatUtil.upcaseFirstLetter(user.getCachedData().getMetaData().getPrimaryGroup()), objective).setScore(8);
        scoreboard.getOrCreatePlayerScore("??d", objective).setScore(7);
        scoreboard.getOrCreatePlayerScore("??b?????7 Server:", objective).setScore(6);
        scoreboard.getOrCreatePlayerScore(" ??8??? ??6TPS??7: ??f" + FormatUtil.formatTps(tpsValue), objective).setScore(5);
        scoreboard.getOrCreatePlayerScore(" ??8??? ??6CPU??7: ??f" + FormatUtil.formatCpuUsage(cpuValue), objective).setScore(4);
        scoreboard.getOrCreatePlayerScore(" ??8??? ??6RAM??7: ??f" + ((Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1024 / 1024) + "??8/??f" + (Runtime.getRuntime().totalMemory() / 1024 / 1024), objective).setScore(3);
        scoreboard.getOrCreatePlayerScore("??r", objective).setScore(2);
        scoreboard.getOrCreatePlayerScore("??b??i ateliermc.tech", objective).setScore(1);
        scoreboard.getOrCreatePlayerScore("??8??m                         ??8", objective).setScore(0);

        return scoreboard;
    }
}
