package com.meo.dupe;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

public final class DupeScreen extends Screen {
    public DupeScreen() {
        super(Text.literal("MEO CPvP"));
    }

    @Override
    protected void init() {
        int buttonWidth = 180;
        int buttonHeight = 20;
        int x = (this.width - buttonWidth) / 2;
        int y = this.height / 2 - 18;

        addDrawableChild(ButtonWidget.builder(dupeLabel(), button -> {
            MeoDupeClient.toggleDupe();
            button.setMessage(dupeLabel());
        }).dimensions(x, y, buttonWidth, buttonHeight).build());

        addDrawableChild(ButtonWidget.builder(totemLabel(), button -> {
            MeoDupeClient.toggleAutoTotem();
            button.setMessage(totemLabel());
        }).dimensions(x, y + 28, buttonWidth, buttonHeight).build());

        addDrawableChild(ButtonWidget.builder(Text.literal("Kapat"), button -> close())
                .dimensions(x, y + 56, buttonWidth, buttonHeight)
                .build());
    }

    private static Text dupeLabel() {
        return Text.literal("Dupe: " + (MeoDupeClient.isDupeEnabled() ? "Açık" : "Kapalı"));
    }

    private static Text totemLabel() {
        return Text.literal("Auto Totem: " + (MeoDupeClient.isAutoTotemEnabled() ? "Açık" : "Kapalı"));
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        renderBackground(context, mouseX, mouseY, delta);
        int panelWidth = 260;
        int panelHeight = 150;
        int left = (this.width - panelWidth) / 2;
        int top = (this.height - panelHeight) / 2;

        context.fill(left, top, left + panelWidth, top + panelHeight, 0xE5101712);
        context.fill(left, top, left + panelWidth, top + 2, 0xFF35C46A);
        context.fill(left, top + panelHeight - 2, left + panelWidth, top + panelHeight, 0xFF35C46A);
        context.fill(left, top, left + 2, top + panelHeight, 0xFF35C46A);
        context.fill(left + panelWidth - 2, top, left + panelWidth, top + panelHeight, 0xFF35C46A);

        context.drawCenteredTextWithShadow(textRenderer, title, this.width / 2, top + 12, 0xFFFFFFFF);
        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }
}
