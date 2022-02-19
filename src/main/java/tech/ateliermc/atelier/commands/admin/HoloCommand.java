package tech.ateliermc.atelier.commands.admin;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import me.lucko.fabric.api.permissions.v0.Permissions;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerPlayer;
import tech.ateliermc.atelier.AtelierPermissions;
import tech.ateliermc.atelier.common.hologram.AtelierHologram;
import tech.ateliermc.atelier.common.hologram.AtelierHologramEntity;

import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;

public class HoloCommand implements com.mojang.brigadier.Command<CommandSourceStack>, Predicate<CommandSourceStack>, SuggestionProvider<CommandSourceStack> {
    public LiteralCommandNode<CommandSourceStack> register(CommandDispatcher<CommandSourceStack> dispatcher, String label) {
        return dispatcher.register(LiteralArgumentBuilder.<CommandSourceStack>literal(label).requires(this).executes(this)
                .then(RequiredArgumentBuilder.<CommandSourceStack, String>argument("args", StringArgumentType.greedyString()).suggests(this).executes(this))
        );
    }

    @Override
    public CompletableFuture<Suggestions> getSuggestions(CommandContext<CommandSourceStack> context, SuggestionsBuilder builder) {
        return builder.createOffset(builder.getInput().lastIndexOf(' ') + 1).buildFuture();
    }

    @Override
    public boolean test(CommandSourceStack t) {
        return Permissions.check(t, AtelierPermissions.HOLOGRAM, 3);
    }

    @Override
    public int run(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ServerPlayer player = context.getSource().getPlayerOrException();
        try {
            String[] args = context.getInput().split(" ");

            if (args.length == 1) {
                player.sendMessage(new TextComponent("Uncompleted command!"), Util.NIL_UUID);
                return 1;
            }

            if (args[1].equalsIgnoreCase("create")) {
                String text = context.getInput().replace(args[0] + " " + args[1] + " " + args[2], "").trim();

                AtelierHologramEntity e = new AtelierHologramEntity(args[2], player.getLevel(), player.getX(), player.getY() + 1, player.getZ());
                e.setCustomNameVisible(true);
                if (text.isEmpty())
                    e.setLines(new String[] {"Default Hologram Text", "Edit me with /holo setText <name> <text>"});
                else e.setLines(new String[] { text });
                AtelierHologram.save();
                AtelierHologram.entityMap.put(e.getHologramData(), e);
                player.getLevel().addFreshEntity(e);
            }

            if (args[1].equalsIgnoreCase("delete")) {
                String name = args[2];
                if (!AtelierHologram.hologramMap.containsKey(name)) {
                    player.sendMessage(new TextComponent("Hologram \"" + name + "\" does not exist!"), Util.NIL_UUID);
                    return 0;
                }
                AtelierHologram.Data data = AtelierHologram.hologramMap.get(name);
                AtelierHologramEntity entity = AtelierHologram.entityMap.get(data);
                entity.killSuper();
                AtelierHologram.entityMap.remove(data);
                AtelierHologram.hologramMap.remove(name);
                AtelierHologram.save();
                player.sendMessage(new TextComponent("Hologram \"" + name + "\" has been deleted!"), Util.NIL_UUID);
            }

            if (args[1].equalsIgnoreCase("list")) {
                for (String name : AtelierHologram.hologramMap.keySet()) {
                    player.sendMessage(new TextComponent(name + " = " + AtelierHologram.hologramMap.get(name).toString()), Util.NIL_UUID);
                }
            }

            if (args[1].equalsIgnoreCase("teleport")) {
                String name = args[2];
                if (!AtelierHologram.hologramMap.containsKey(name)) {
                    player.sendMessage(new TextComponent("Hologram \"" + name + "\" does not exist!"), Util.NIL_UUID);
                    return 0;
                }
                AtelierHologram.Data data = AtelierHologram.hologramMap.get(name);
                player.teleportTo(data.x, data.y, data.z);
            }

            if (args[1].equalsIgnoreCase("movehere")) {
                String name = args[2];
                if (!AtelierHologram.hologramMap.containsKey(name)) {
                    player.sendMessage(new TextComponent("Hologram \"" + name + "\" does not exist!"), Util.NIL_UUID);
                    return 0;
                }
                AtelierHologram.Data data = AtelierHologram.hologramMap.get(name);
                AtelierHologramEntity entity = AtelierHologram.entityMap.get(data);
                entity.setLocation(player.getX(), player.getY(), player.getZ());
            }

            if (args[1].equalsIgnoreCase("removeline")) {
                String name = args[2];
                if (!AtelierHologram.hologramMap.containsKey(name)) {
                    player.sendMessage(new TextComponent("Hologram \"" + name + "\" does not exist!"), Util.NIL_UUID);
                    return 0;
                }
                AtelierHologram.Data data = AtelierHologram.hologramMap.get(name);
                AtelierHologramEntity entity = AtelierHologram.entityMap.get(data);
                String[] oldLines = entity.getLines();
                String[] newLines = new String[oldLines.length-1];
                int a = 0;
                for (int i = 0; i < oldLines.length; i++) {
                    if (Integer.valueOf(args[3]) == i)
                        continue;
                    newLines[a] = oldLines[i];
                    a++;
                }
                entity.setLines(entity.getLines());
            }

            if (args[1].equalsIgnoreCase("setline")) {
                String name = args[2];
                if (!AtelierHologram.hologramMap.containsKey(name)) {
                    player.sendMessage(new TextComponent("Hologram \"" + name + "\" does not exist!"), Util.NIL_UUID);
                    return 0;
                }
                AtelierHologram.Data data = AtelierHologram.hologramMap.get(name);
                AtelierHologramEntity entity = AtelierHologram.entityMap.get(data);
                String[] oldLines = entity.getLines();
                String[] newLines = new String[oldLines.length];
                int a = 0;
                for (int i = 0; i < oldLines.length; i++) {
                    if (Integer.valueOf(args[3]) == i)
                        newLines[a] = args[4];
                    newLines[a] = oldLines[i];
                    a++;
                }
                entity.setLines(entity.getLines());
            }

        } catch (Exception e) {
            player.sendMessage(new TextComponent("Error: " + e.toString()).setStyle(Style.EMPTY.withColor(ChatFormatting.RED)), Util.NIL_UUID);
            e.printStackTrace();
        }
        return 0;
    }
}
