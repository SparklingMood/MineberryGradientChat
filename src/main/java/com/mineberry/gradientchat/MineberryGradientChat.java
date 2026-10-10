
package com.mineberry.gradientchat;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.message.v1.ClientSendMessageEvents;

public final class MineberryGradientChat implements ClientModInitializer {

    private static final int MAX_LENGTH = 256;

    @Override
    public void onInitializeClient() {
        ClientSendMessageEvents.MODIFY_CHAT.register(
            MineberryGradientChat::formatMessage
        );
    }

    private static String formatMessage(String message) {
        if (message == null || message.isEmpty()) {
            return message;
        }

        // Send a message without a gradient using !plain
        if (message.startsWith("!plain ")) {
            return message.substring(7);
        }

        // Leave commands unchanged
        if (message.startsWith("/")) {
            return message;
        }

        int[] chars = message.codePoints().toArray();
        int count = 0;

        for (int c : chars) {
            if (!Character.isWhitespace(c)) count++;
        }

        if (count == 0) return message;

        StringBuilder out = new StringBuilder();
        int index = 0;

        for (int c : chars) {
            if (Character.isWhitespace(c)) {
                out.appendCodePoint(c);
                continue;
            }

            double t = count <= 1 ? 0 :
                (double) index / (count - 1);

            int r = (int) Math.round(138 + (255 - 138) * t);
            int g = (int) Math.round(43 + (105 - 43) * t);
            int b = (int) Math.round(226 + (180 - 226) * t);

            out.append(String.format(
                "&#%02X%02X%02X", r, g, b
            ));
            out.appendCodePoint(c);
            index++;
        }

        // Avoid sending an overlong formatted message
        if (out.length() > MAX_LENGTH) return message;

        return out.toString();
    }
}
