package com.meo.dupe;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

public final class DupeScreen extends Screen {
    public DupeScreen() {
        super(Text.literal("MEO CPvP"));
    }

    @Override
    protected void init() {
        int buttonWidth = 190;
        int buttonHeight = 20;
        int x = (this.width - buttonWidth) / 2;
        int y = this.height / 2 - 34;

        // Item duplication cannot be performed by a client-only button.
        addDrawableChild(ButtonWidget.builder(Text.literal("Dupe: Sunucu desteği gerekli"), button -> {
            button.setMessage(Text.literal("Dupe: İstemci tarafından yapılamaz"));
        }).dimensions(x, y, buttonWidth, buttonHeight).build());

        addDrawableChild(ButtonWidget.builder(totemLabel(), button -> {
            MeoDupeClient.toggleAutoTotem();
            button.setMessage(totemLabel());
            if (MeoDupeClient.isAutoTotemEnabled()) {
                close();
            }
        }).dimensions(x, y + 26, buttonWidth, buttonHeight).build());

        addDrawableChild(ButtonWidget.builder(Text.literal("Envanteri Aç"), button -> {
            MinecraftClient client = MinecraftClient.getInstance();
            if (client.player != null) {
                client.setScreen(new InventoryScreen(client.player));
            }
        }).dimensions(x, y + 52, buttonWidth, buttonHeight).build());

        addDrawableChild(ButtonWidget.builder(Text.literal("Kapat"), button -> close())
                .dimensions(x, y + 78, buttonWidth, buttonHeight)
                .build());
    }

    private static Text totemLabel() {
        return Text.literal("Auto Totem: " + (MeoDupeClient.isAutoTotemEnabled() ? "Açık" : "Kapalı"));
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        renderBackground(context, mouseX, mouseY, delta);
        int panelWidth = 280;
        int panelHeight = 174;
        int left = (this.width - panelWidth) / 2;
        int top = (this.height - panelHeight) / 2;

        context.fill(left, top, left + panelWidth, top + panelHeight, 0xE5101712);
        context.fill(left, top, left + panelWidth, top + 2, 0xFF35C46A);
        context.fill(left, top + panelHeight - 2, left + panelWidth, top + panelHeight, 0xFF35C46A);
        context.fill(left, top, left + 2, top + panelHeight, 0xFF35C46A);
        context.fill(left + panelWidth - 2, top, left + panelWidth, top + panelHeight, 0xFF35C46A);

        context.drawCenteredTextWithShadow(textRenderer, title, this.width / 2, top + 10, 0xFFFFFFFF);
        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }
}
