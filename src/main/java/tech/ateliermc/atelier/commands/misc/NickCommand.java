package tech.ateliermc.atelier.commands.misc;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import me.lucko.fabric.api.permissions.v0.Permissions;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerPlayer;
import tech.ateliermc.atelier.Atelier;
import tech.ateliermc.atelier.AtelierPermissions;
import tech.ateliermc.atelier.common.AtelierChat;

public class NickCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("nick").requires(Permissions.require(AtelierPermissions.NICK))
                .then(Commands.argument("nickname", StringArgumentType.word())
                .executes(ctx -> {
                    ServerPlayer player;

                    try {
                        player = ctx.getSource().getPlayerOrException();
                    } catch (Exception e) {
                        ctx.getSource().sendFailure(new TextComponent("You must be a player to run this command"));
                        return 0;
                    }

                    String nick = StringArgumentType.getString(ctx, "nickname");

                    if (nick.length() > 24) {
                        ctx.getSource().sendFailure(new TextComponent("Nickname is too long"));
                    }

                    nick = nick.replace("&", "§");

                    CompoundTag tag = new CompoundTag();
                    tag.putString("nick", nick);

                    player.setCustomName(Atelier.asVanilla(AtelierChat.LEGACY_SECTION_UXRC.deserialize(nick)));
                    player.addAdditionalSaveData(tag);

                    return 1;
                })));
    }
}
