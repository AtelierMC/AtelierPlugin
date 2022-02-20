package tech.ateliermc.atelier.commands.nick;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import me.lucko.fabric.api.permissions.v0.Permissions;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerPlayer;
import tech.ateliermc.atelier.AtelierPermissions;

import java.util.Locale;

public class WhoisCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("whois").requires(Permissions.require(AtelierPermissions.WHOIS))
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
                            boolean found = false;

                            for (ServerPlayer player1 : player.getServer().getPlayerList().getPlayers()) {
                                if (player1.getDisplayName().getContents().toLowerCase(Locale.ROOT).contains(nick.toLowerCase(Locale.ROOT))) {
                                    ctx.getSource().sendSuccess(new TextComponent("§b" + player1.getName().getContents() + "§f is currently §7" + player1.getDisplayName().getContents()), false);
                                    found = true;
                                    break;
                                }
                            }

                            if (!found) {
                                ctx.getSource().sendFailure(new TextComponent("§7" + nick + "§f is not currently online or not found"));
                            }

                            return 1;
                        }))
        );
    }
}
