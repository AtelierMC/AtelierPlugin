package tech.ateliermc.atelier.commands.nick;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import me.lucko.fabric.api.permissions.v0.Permissions;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerPlayer;
import tech.ateliermc.atelier.AtelierPermissions;

public class NickCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("nick").requires(Permissions.require(AtelierPermissions.NICK))
                .then(Commands.argument("nickname", StringArgumentType.greedyString())
                .executes(ctx -> {
                    ServerPlayer player;

                    try {
                        player = ctx.getSource().getPlayerOrException();
                    } catch (Exception e) {
                        ctx.getSource().sendFailure(new TextComponent("You must be a player to run this command"));
                        return 0;
                    }

                    String nick = StringArgumentType.getString(ctx, "nickname");

                    if (nick.length() > 32) {
                        ctx.getSource().sendFailure(new TextComponent("Nickname is too long"));
                    }

                    nick = nick.replace("&", "§");
                    player.setCustomName(new TextComponent(nick));

                    return 1;
                }))
        );
    }
}
