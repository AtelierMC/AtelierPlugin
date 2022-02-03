package tech.ateliermc.atelier;

import me.lucko.fabric.api.permissions.v0.PermissionCheckEvent;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.util.TriState;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import tech.ateliermc.atelier.commands.spawn.SpawnCommand;
import tech.ateliermc.atelier.commands.tpa.TpaAcceptCommand;
import tech.ateliermc.atelier.commands.tpa.TpaCommand;
import tech.ateliermc.atelier.commands.tpa.TpaDenyCommand;
import tech.ateliermc.atelier.common.AtelierScoreboard;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Atelier implements DedicatedServerModInitializer {
    public static final String MOD_ID = "atelier";

    public static Logger LOGGER = LogManager.getLogger("Atelier");

    public static ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

    public static final Map<UUID, Long> commandCooldown = new HashMap<>();
    public static final int cooldown = 900;

    @Override
    public void onInitializeServer() {
        CommandRegistrationCallback.EVENT.register((dispatcher, dedzicated) -> {
            SpawnCommand.register(dispatcher);

            TpaCommand.register(dispatcher);
            TpaDenyCommand.register(dispatcher);
            TpaAcceptCommand.register(dispatcher);
        });

        ServerLifecycleEvents.SERVER_STARTED.register((AtelierScoreboard::init));

        ServerLifecycleEvents.SERVER_STOPPING.register(server -> {
            try {
                executorService.awaitTermination(5, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        //PermissionCheckEvent.EVENT.register((source, permission) -> TriState.of(source.hasPermission(3)));
    }
}
