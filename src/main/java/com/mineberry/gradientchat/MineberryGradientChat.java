
package com.mineberry.gradientchat;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.message.v1.ClientSendMessageEvents;

public final class MineberryGradientChat implements ClientModInitializer {

    private static final int MAX_LENGTH = 256;
    private static final int DEFAULT_GROUP_SIZE = 2;

    private static boolean enabled = true;
    private static int startColor = 0x8A2BE2;
    private static int endColor = 0xFF69B4;

    @Override
    public void onInitializeClient() {
        ClientSendMessageEvents.ALLOW_CHAT.register(message -> {
            if (message.equals("!toggle")) {
                enabled = !enabled;
                return false;
            }

            if (message.equals("!chatcolor")
                    || message.startsWith("!chatcolor ")) {
                changeColors(message);
                return false;
            }

            return true;
        });

        ClientSendMessageEvents.MODIFY_CHAT.register(
            MineberryGradientChat::formatMessage
        );
    }

    private static void changeColors(String message) {
        String[] parts = message.trim().split("\\s+");

        if (parts.length != 3) {
            return;
        }

        try {
            int first = parseColor(parts[1]);
            int second = parseColor(parts[2]);

            startColor = first;
            endColor = second;
        } catch (IllegalArgumentException ignored) {
            // Ignore invalid colours.
        }
    }

    private static int parseColor(String value) {
        String hex = value.startsWith("#")
            ? value.substring(1)
            : value;

        if (!hex.matches("[0-9a-fA-F]{6}")) {
            throw new IllegalArgumentException("Invalid colour");
        }

        return Integer.parseInt(hex, 16);
    }

    private static String formatMessage(String message) {
        if (message == null || message.isEmpty()) {
            return message;
        }

        if (message.startsWith("!plain ")) {
            return message.substring(7);
        }

        if (message.startsWith("/") || !enabled) {
            return message;
        }

        int[] chars = message.codePoints().toArray();
        int count = 0;

        for (int c : chars) {
            if (!Character.isWhitespace(c)) {
                count++;
            }
        }

        if (count == 0) {
            return message;
        }

        for (int groupSize = DEFAULT_GROUP_SIZE;
             groupSize <= count;
             groupSize++) {

            String formatted = buildGradient(
                chars, count, groupSize
            );

            if (formatted.length() <= MAX_LENGTH) {
                return formatted;
            }
        }

        return message;
    }

    private static String buildGradient(
            int[] chars, int count, int groupSize) {

        int groups = (count + groupSize - 1) / groupSize;
        StringBuilder out = new StringBuilder();
        int index = 0;

        for (int c : chars) {
            if (Character.isWhitespace(c)) {
                out.appendCodePoint(c);
                continue;
            }

            if (index % groupSize == 0) {
                int group = index / groupSize;

                double t = groups <= 1
                    ? 0.0
                    : (double) group / (groups - 1);

                int r = interpolate(
                    (startColor >> 16) & 255,
                    (endColor >> 16) & 255, t
                );

                int g = interpolate(
                    (startColor >> 8) & 255,
                    (endColor >> 8) & 255, t
                );

                int b = interpolate(
                    startColor & 255,
                    endColor & 255, t
                );

                out.append(String.format(
                    "&#%02X%02X%02X", r, g, b
                ));
            }

            out.appendCodePoint(c);
            index++;
        }

        return out.toString();
    }

    private static int interpolate(int a, int b, double t) {
        return (int) Math.round(a + (b - a) * t);
    }
}
