package tech.ateliermc.atelier;

import me.lucko.fabric.api.permissions.v0.PermissionCheckEvent;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.util.TriState;
import net.minecraft.server.MinecraftServer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import tech.ateliermc.atelier.commands.tpa.TpaAcceptCommand;
import tech.ateliermc.atelier.commands.tpa.TpaCommand;
import tech.ateliermc.atelier.commands.tpa.TpaDenyCommand;
import tech.ateliermc.atelier.common.AtelierScoreboard;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class Atelier implements DedicatedServerModInitializer {
    public static final String MOD_ID = "atelier";

    public static Logger LOGGER = LogManager.getLogger("Atelier");

    public static ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

    @Override
    public void onInitializeServer() {
        CommandRegistrationCallback.EVENT.register((dispatcher, dedzicated) -> {
            TpaCommand.register(dispatcher);
            TpaDenyCommand.register(dispatcher);
            TpaAcceptCommand.register(dispatcher);
        });

        PermissionCheckEvent.EVENT.register((source, permission) -> TriState.of(source.hasPermission(3)));
    }
}
