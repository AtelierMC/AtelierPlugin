package tech.ateliermc.atelier.commands.tpa;

import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.CommandDispatcher;
import me.lucko.fabric.api.permissions.v0.Permissions;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.GameProfileArgument;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerPlayer;
import tech.ateliermc.atelier.AtelierPermissions;

import java.util.HashMap;
import java.util.Map;

public final class TpaCommand {
    public static Map<ServerPlayer, ServerPlayer> tpaMap = new HashMap<>();

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("tpa").requires(Permissions.require(AtelierPermissions.TPA))
                .then(Commands.argument("target", GameProfileArgument.gameProfile())
                        .suggests((ctx, suggestionsBuilder) -> SharedSuggestionProvider.suggest(ctx.getSource().getServer().getPlayerList()
                                .getPlayers().stream().map((serverPlayer) ->
                                        serverPlayer.getGameProfile().getName()), suggestionsBuilder
                                )
                        )
                .executes(ctx -> {
                    GameProfile targetProfile = GameProfileArgument.getGameProfiles(ctx, "target").stream().findFirst().orElse(null);

                    ServerPlayer source = ctx.getSource().getPlayerOrException();
                    ServerPlayer target = targetProfile != null ? ctx.getSource().getServer().getPlayerList().getPlayer(targetProfile.getId()) : null;

                    if (target == null) {
                        ctx.getSource().sendFailure(new TextComponent("Cannot find that player!").withStyle(ChatFormatting.DARK_RED));
                        return 1;
                    }

                    tpaMap.put(target, source);
                    source.sendMessage(new TextComponent("Request sent to " + targetProfile.getName()).withStyle(ChatFormatting.GREEN), Util.NIL_UUID);
                    source.sendMessage(new TextComponent("Request will timeout after two minutes").withStyle(ChatFormatting.GREEN), Util.NIL_UUID);
                    target.sendMessage(new TextComponent(source.getDisplayName().getContents() + " is requesting to teleport to you").withStyle(ChatFormatting.GREEN), Util.NIL_UUID);
                    target.sendMessage(new TextComponent("Type /tpaccept to accept the request or").withStyle(ChatFormatting.GREEN), Util.NIL_UUID);
                    target.sendMessage(new TextComponent("Type /tpadeny to deny the request").withStyle(ChatFormatting.GREEN), Util.NIL_UUID);
                    return 1;
                })
        ));
    }
}
