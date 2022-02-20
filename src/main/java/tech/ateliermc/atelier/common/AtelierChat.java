package tech.ateliermc.atelier.common;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.platform.fabric.FabricAudiences;
import net.kyori.adventure.text.flattener.ComponentFlattener;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.translation.GlobalTranslator;
import net.kyori.adventure.translation.TranslationRegistry;
import net.kyori.adventure.translation.Translator;
import net.luckperms.api.model.user.User;
import net.minecraft.ChatFormatting;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tech.ateliermc.atelier.Atelier;

import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AtelierChat {
    private static final Pattern LOCALIZATION_PATTERN = Pattern.compile("%(?:(\\d+)\\$)?s");
    public static final ComponentFlattener FLATTENER = ComponentFlattener.basic().toBuilder()
            .complexMapper(net.kyori.adventure.text.TranslatableComponent.class, (translatable, consumer) -> {
                if (!Language.getInstance().has(translatable.key())) {
                    for (final Translator source : GlobalTranslator.get().sources()) {
                        if (source instanceof TranslationRegistry registry && registry.contains(translatable.key())) {
                            consumer.accept(GlobalTranslator.render(translatable, Locale.US));
                            return;
                        }
                    }
                }
                final @NotNull String translated = Language.getInstance().getOrDefault(translatable.key());

                final Matcher matcher = LOCALIZATION_PATTERN.matcher(translated);
                final List<net.kyori.adventure.text.Component> args = translatable.args();
                int argPosition = 0;
                int lastIdx = 0;
                while (matcher.find()) {
                    // append prior
                    if (lastIdx < matcher.start()) {
                        consumer.accept(net.kyori.adventure.text.Component.text(translated.substring(lastIdx, matcher.start())));
                    }
                    lastIdx = matcher.end();

                    final @Nullable String argIdx = matcher.group(1);
                    // calculate argument position
                    if (argIdx != null) {
                        try {
                            final int idx = Integer.parseInt(argIdx) - 1;
                            if (idx < args.size()) {
                                consumer.accept(args.get(idx));
                            }
                        } catch (final NumberFormatException ex) {
                            // ignore, drop the format placeholder
                        }
                    } else {
                        final int idx = argPosition++;
                        if (idx < args.size()) {
                            consumer.accept(args.get(idx));
                        }
                    }
                }

                // append tail
                if (lastIdx < translated.length()) {
                    consumer.accept(net.kyori.adventure.text.Component.text(translated.substring(lastIdx)));
                }
            })
            .build();
    public static final LegacyComponentSerializer LEGACY_SECTION_UXRC = LegacyComponentSerializer.builder().flattener(FLATTENER).hexColors().useUnusualXRepeatedCharacterHexFormat().build();

    public static final Component ATELIER_LOGO = Atelier.adventure().toNative(AtelierChat.LEGACY_SECTION_UXRC.deserialize("§x§e§9§f§2§8§dA§x§d§a§e§a§9§bt§x§c§a§e§2§a§9e§x§b§b§d§9§b§7l§x§a§b§d§1§c§5i§x§9§c§c§9§d§3e§x§8§c§c§0§e§1r§x§7§d§b§8§e§fM§x§6§d§a§f§f§dC"));
    public static final Component ATELIER_DISCORD_LINK = Atelier.adventure().toNative(AtelierChat.LEGACY_SECTION_UXRC.deserialize("§x§1§7§d§1§f§ba§x§2§0§d§2§f§bt§x§2§a§d§4§f§be§x§3§3§d§5§f§bl§x§3§d§d§7§f§bi§x§4§6§d§8§f§be§x§4§f§d§a§f§br§x§5§9§d§b§f§bm§x§6§2§d§d§f§bc§x§6§c§d§e§f§b.§x§7§5§e§0§f§ct§x§7§e§e§1§f§ce§x§8§8§e§2§f§cc§x§9§1§e§4§f§ch§x§9§b§e§5§f§c/§x§a§4§e§7§f§cd§x§a§d§e§8§f§ci§x§b§7§e§a§f§cs§x§c§0§e§b§f§cc§x§c§a§e§d§f§co§x§d§3§e§e§f§cr§x§d§c§e§f§f§dd"));

    public static void handleChat(PlayerList playerList, ServerPlayer player, Component rawComponent, ChatType chatType, UUID uuid) {
        final TranslatableComponent component = (TranslatableComponent) rawComponent;
        final User user = Atelier.luckPerms.getUserManager().getUser(player.getUUID());
        final String rawMessage = (String) component.getArgs()[1];
        final Component prefix = Atelier.adventure().toNative(LEGACY_SECTION_UXRC.deserialize(user.getCachedData().getMetaData().getPrefix()));

        Audience audience = Atelier.adventure().audience(playerList.getPlayers());

        audience.sendMessage(net.kyori.adventure.text.Component.text()
                .append(LEGACY_SECTION_UXRC.deserialize(user.getCachedData().getMetaData().getPrefix() + " "),
                        Atelier.adventure().toAdventure(player.getDisplayName()),
                        net.kyori.adventure.text.Component.text(" ▶ ", NamedTextColor.GRAY),
                        net.kyori.adventure.text.Component.text(rawMessage, NamedTextColor.WHITE)
                )
        );

        playerList.getServer().sendMessage(component, uuid);
    }
}
