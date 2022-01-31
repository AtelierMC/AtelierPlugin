package tech.ateliermc.atelier.common;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.*;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

public class AtelierChat {
    private AtelierChat() {  }

    static LuckPerms luckPerms = LuckPermsProvider.get();

    public static void handleChat(PlayerList playerList, ServerPlayer player, Component rawComponent, ChatType chatType, UUID uuid) {
        final TranslatableComponent component = (TranslatableComponent) rawComponent;
        final User user = luckPerms.getUserManager().getUser(player.getUUID());
        String rawMessage = (String) component.getArgs()[1];

        playerList.broadcastMessage(
                new TextComponent(user.getCachedData().getMetaData().getPrefix() + " " + player.getDisplayName().getContents()).withStyle(ChatFormatting.WHITE)
                        .append(new TextComponent("§7 ▶ §f" + rawMessage).withStyle(ChatFormatting.WHITE)),
                chatType,
                uuid
        );
    }

    private static String colorify(String string) {
        String result = TextColor.parseColor(string).toString();
        return result;
    } 
}
