package tech.ateliermc.atelier;

import me.lucko.spark.api.Spark;
import me.lucko.spark.api.SparkProvider;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.kyori.adventure.platform.fabric.FabricServerAudiences;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.minecraft.world.entity.Entity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import tech.ateliermc.atelier.commands.admin.HoloCommand;
import tech.ateliermc.atelier.commands.misc.ClearNickCommand;
import tech.ateliermc.atelier.commands.misc.NickCommand;
import tech.ateliermc.atelier.commands.misc.SitCommand;
import tech.ateliermc.atelier.commands.misc.ToggleScoreboardCommand;
import tech.ateliermc.atelier.commands.tp.HomeCommand;
import tech.ateliermc.atelier.commands.tp.SpawnCommand;
import tech.ateliermc.atelier.common.AtelierScoreboard;
import tech.ateliermc.atelier.common.AtelierSit;
import tech.ateliermc.atelier.common.hologram.AtelierHologram;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Atelier implements DedicatedServerModInitializer {
    public static final String MOD_ID = "atelier";

    public static Logger LOGGER = LogManager.getLogger("Atelier");

    public static final Map<UUID, Long> commandCooldown = new HashMap<>();
    public static final int cooldown = 900;

    public static ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

    public static LuckPerms luckPerms;
    public static Spark spark;

    private static FabricServerAudiences adventure;
    public static FabricServerAudiences adventure() {
        if (Atelier.adventure == null) {
            throw new IllegalStateException("Tried to access Adventure without a running server!");
        }
        return adventure;
    }

    @Override
    public void onInitializeServer() {
        AtelierHologram.load();

        CommandRegistrationCallback.EVENT.register((dispatcher, dedzicated) -> {
            SpawnCommand.register(dispatcher);
            HomeCommand.register(dispatcher);

            NickCommand.register(dispatcher);
            ClearNickCommand.register(dispatcher);
            SitCommand.register(dispatcher);

            ToggleScoreboardCommand.register(dispatcher);

            new HoloCommand().register(dispatcher, "holo");
        });

        ServerLifecycleEvents.SERVER_STARTING.register(server -> {
            Atelier.adventure = FabricServerAudiences.of(server);
        });
        ServerLifecycleEvents.SERVER_STARTED.register(server -> {
            Atelier.luckPerms = LuckPermsProvider.get();
            Atelier.spark = SparkProvider.get();
            AtelierScoreboard.init(server);
        });

        ServerLifecycleEvents.SERVER_STOPPING.register(server -> {
            try {
                executorService.awaitTermination(5, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        ServerLifecycleEvents.SERVER_STOPPED.register(server -> {
            AtelierHologram.save();
            Atelier.adventure = null;

            for (Entity entity : AtelierSit._chairs) {
                if (entity.isAlive())
                    entity.kill();
            }
        });
    }
}
