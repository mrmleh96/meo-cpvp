package com.meo.dupe;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.item.Items;
import net.minecraft.screen.slot.SlotActionType;
import org.lwjgl.glfw.GLFW;

public final class MeoDupeClient implements ClientModInitializer {
    private static KeyBinding openMenuKey;
    private static boolean dupeEnabled;
    private static boolean autoTotemEnabled;
    private static int pendingTotemSlot = -1;

    @Override
    public void onInitializeClient() {
        openMenuKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.meocpvp.open_menu",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_I,
                KeyBinding.Category.MISC
        ));

        ClientTickEvents.END_CLIENT_TICK.register(MeoDupeClient::onClientTick);
    }

    private static void onClientTick(MinecraftClient client) {
        while (openMenuKey.wasPressed()) {
            if (client.currentScreen == null && client.player != null) {
                client.setScreen(new DupeScreen());
            }
        }

        if (!autoTotemEnabled || client.player == null || client.interactionManager == null) {
            pendingTotemSlot = -1;
            return;
        }

        // Do not replace another open GUI (including the MEO menu) with the inventory.
        if (client.currentScreen != null && !(client.currentScreen instanceof InventoryScreen)) {
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

        int inventorySlotId = pendingTotemSlot < 9 ? 36 + pendingTotemSlot : pendingTotemSlot;
        client.interactionManager.clickSlot(
                client.player.currentScreenHandler.syncId,
                inventorySlotId,
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
