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
            startColor = parseColor(parts[1]);
            endColor = parseColor(parts[2]);
        } catch (IllegalArgumentException e) {
            // Invalid colours are ignored.
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
