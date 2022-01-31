package tech.ateliermc.atelier.utils;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.TextColor;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class HexUtils {

    private static final int CHARS_UNTIL_LOOP = 30;
    private static final Pattern RAINBOW_PATTERN = Pattern.compile("<(?<type>rainbow|r)(#(?<speed>\\d+))?(:(?<saturation>\\d*\\.?\\d+))?(:(?<brightness>\\d*\\.?\\d+))?(:(?<loop>l|L|loop))?>");
    private static final Pattern GRADIENT_PATTERN = Pattern.compile("<(?<type>gradient|g)(#(?<speed>\\d+))?(?<hex>(:#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})){2,})(:(?<loop>l|L|loop))?>");
    private static final List<Pattern> HEX_PATTERNS = Arrays.asList(
            Pattern.compile("<#([A-Fa-f0-9]){6}>"),   // <#FFFFFF>
            Pattern.compile("\\{#([A-Fa-f0-9]){6}}"), // {#FFFFFF}
            Pattern.compile("&#([A-Fa-f0-9]){6}"),    // &#FFFFFF
            Pattern.compile("#([A-Fa-f0-9]){6}")      // #FFFFFF
    );

    private static final Pattern STOP = Pattern.compile(
            "<(rainbow|r)(#(\\d+))?(:(\\d*\\.?\\d+))?(:(\\d*\\.?\\d+))?(:(l|L|loop))?>|" +
                    "<(gradient|g)(#(\\d+))?((:#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})){2,})(:(l|L|loop))?>|" +
                    "(&[a-f0-9r])|" +
                    "<#([A-Fa-f0-9]){6}>|" +
                    "\\{#([A-Fa-f0-9]){6}}|" +
                    "&#([A-Fa-f0-9]){6}|" +
                    "#([A-Fa-f0-9]){6}|" +
                    ChatFormatting.PREFIX_CODE
    );

    private HexUtils() {

    }



}
