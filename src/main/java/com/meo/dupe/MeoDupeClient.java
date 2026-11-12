package com.meo.dupe;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.item.Items;
import net.minecraft.screen.slot.SlotActionType;
import org.lwjgl.glfw.GLFW;

public final class MeoDupeClient implements ClientModInitializer {
    private static boolean wasNDown;
    private static boolean dupeEnabled;
    private static boolean autoTotemEnabled;
    private static int pendingTotemSlot = -1;

    @Override
    public void onInitializeClient() {
        ClientTickEvents.END_CLIENT_TICK.register(MeoDupeClient::onClientTick);
    }

    private static void onClientTick(MinecraftClient client) {
        if (client.getWindow() != null) {
            boolean nDown = GLFW.glfwGetKey(
                    client.getWindow().getHandle(),
                    GLFW.GLFW_KEY_N
            ) == GLFW.GLFW_PRESS;

            if (nDown && !wasNDown && client.currentScreen == null) {
                client.setScreen(new DupeScreen());
            }
            wasNDown = nDown;
        }

        if (!autoTotemEnabled || client.player == null || client.interactionManager == null) {
            pendingTotemSlot = -1;
            return;
        }

        if (client.player.getOffHandStack().isOf(Items.TOTEM_OF_UNDYING)) {
            pendingTotemSlot = -1;
            return;
        }

        if (pendingTotemSlot < 0) {
            for (int slot = 0; slot < 36; slot++) {
                if (client.player.getInventory().getStack(slot).isOf(Items.TOTEM_OF_UNDYING)) {
                    pendingTotemSlot = slot;
                    break;
                }
            }
        }

        if (pendingTotemSlot < 0) {
            return;
        }

        if (!(client.currentScreen instanceof InventoryScreen)) {
            client.setScreen(new InventoryScreen(client.player));
            return;
        }

        client.interactionManager.clickSlot(
                client.player.currentScreenHandler.syncId,
                pendingTotemSlot < 9 ? 36 + pendingTotemSlot : pendingTotemSlot,
                40,
                SlotActionType.SWAP,
                client.player
        );
        pendingTotemSlot = -1;
    }

    static boolean isDupeEnabled() {
        return dupeEnabled;
    }

    static void toggleDupe() {
        dupeEnabled = !dupeEnabled;
    }

    static boolean isAutoTotemEnabled() {
        return autoTotemEnabled;
    }

    static void toggleAutoTotem() {
        autoTotemEnabled = !autoTotemEnabled;
        pendingTotemSlot = -1;
    }
}
