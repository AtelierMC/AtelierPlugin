package tech.ateliermc.atelier;

import net.fabricmc.api.DedicatedServerModInitializer;
import net.minecraft.server.MinecraftServer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Atelier implements DedicatedServerModInitializer {
    
    public MinecraftServer server;
    public Logger LOGGER = LogManager.getLogger("Atelier");

    @Override
    public void onInitializeServer() {

    }
}
