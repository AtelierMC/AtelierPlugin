package tech.ateliermc.atelier;

import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.kyori.adventure.platform.fabric.FabricServerAudiences;
import net.kyori.adventure.text.Component;
import net.minecraft.world.entity.Entity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import tech.ateliermc.atelier.commands.misc.SitCommand;
import tech.ateliermc.atelier.commands.tp.HomeCommand;
import tech.ateliermc.atelier.commands.tp.SpawnCommand;
import tech.ateliermc.atelier.common.AtelierChat;
import tech.ateliermc.atelier.common.AtelierScoreboard;
import tech.ateliermc.atelier.common.AtelierSit;

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

    private static FabricServerAudiences adventure;

    public static FabricServerAudiences adventure() {
        if (Atelier.adventure == null) {
            throw new IllegalStateException("Tried to access Adventure without a running server!");
        }
        return adventure;
    }

    @Override
    public void onInitializeServer() {
        CommandRegistrationCallback.EVENT.register((dispatcher, dedzicated) -> {
            SpawnCommand.register(dispatcher);
            HomeCommand.register(dispatcher);

            SitCommand.register(dispatcher);
        });

        ServerLifecycleEvents.SERVER_STARTING.register(server -> Atelier.adventure = FabricServerAudiences.of(server));
        ServerLifecycleEvents.SERVER_STARTED.register((AtelierScoreboard::init));

        ServerLifecycleEvents.SERVER_STOPPING.register(server -> {
            try {
                executorService.awaitTermination(5, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        ServerLifecycleEvents.SERVER_STOPPED.register(server -> {
            Atelier.adventure = null;

            for (Entity entity : AtelierSit._chairs) {
                if (entity.isAlive())
                    entity.kill();
            }
        });

        //PermissionCheckEvent.EVENT.register((source, permission) -> TriState.of(source.hasPermission(3)));
    }

    public static net.minecraft.network.chat.Component asVanilla(final Component component) {
        if (component == null) return null;
        return net.minecraft.network.chat.Component.Serializer.fromJson(AtelierChat.GSON.serializer().toJsonTree(component));
    }
}
