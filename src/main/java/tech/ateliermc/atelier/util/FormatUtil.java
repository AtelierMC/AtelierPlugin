package tech.ateliermc.atelier.util;

import net.minecraft.ChatFormatting;

public class FormatUtil {
    public static String percent(double value, double max) {
        double percent = (value * 100d) / max;
        return (int) percent + "%";
    }

    public static String formatTps(double tps) {
        String color;

        if (tps > 18.0) {
            color = "§a";
        } else if (tps > 14.0) {
            color = "§e";
        } else {
            color = "§c";
        }

        return color + (tps > 20.0 ? "*" : "") + Math.min(Math.round(tps * 100.0) / 100.0, 20.0);
    }


    public static String formatCpuUsage(double usage) {
        String color;
        if (usage > 0.9) {
            color = "§c";
        } else if (usage > 0.65) {
            color = "§e";
        } else {
            color = "§a";
        }

        return color + FormatUtil.percent(usage, 1d) + " ";
    }

    public static String formatPing(double ping) {
        String color;
        if (ping >= 200) {
            color = "§c";
        } else if (ping >= 100) {
            color = "§e";
        } else {
            color = "§a";
        }

        return color + (int) Math.ceil(ping);
    }

    public static String upcaseFirstLetter(String string) {
        return string.substring(0,1).toUpperCase() + string.substring(1).toLowerCase();
    }
}
