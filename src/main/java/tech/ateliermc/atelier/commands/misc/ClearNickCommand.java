package tech.ateliermc.atelier.commands.misc;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import me.lucko.fabric.api.permissions.v0.Permissions;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerPlayer;
import tech.ateliermc.atelier.AtelierPermissions;

public class ClearNickCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("clearnick").requires(Permissions.require(AtelierPermissions.NICK))
                .executes(ctx -> {
                    ServerPlayer player;

                    try {
                        player = ctx.getSource().getPlayerOrException();
                    } catch (Exception e) {
                        ctx.getSource().sendFailure(new TextComponent("You must be a player to run this command"));
                        return 0;
                    }

                    player.setCustomName(null);

                    return 1;
                }));
    }
}
