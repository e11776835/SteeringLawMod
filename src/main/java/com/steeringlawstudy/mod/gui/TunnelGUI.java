package com.steeringlawstudy.mod.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.steeringlawstudy.mod.SteeringLawStudy;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.gui.GuiUtils;

public class TunnelGUI {
    public static final TunnelGUI INSTANCE = new TunnelGUI();

    public static Integer currentTunnel = 0;
    public static Integer currentAngle = 1;
    public static Integer currentNumAngles = 1;
    public static Float progress = 0f;

    // private final ResourceLocation BAR = new ResourceLocation(SteeringLawStudy.MOD_ID, "bar.png");
    // private final ResourceLocation BAR_BG = new ResourceLocation(SteeringLawStudy.MOD_ID, "bar_bg.png");
    private final int w_tex = 0, h_tex = 0, w_gui = 100, h_gui = 10;

    @SubscribeEvent
    public void renderGUI(RenderGameOverlayEvent e) {
        if (e.isCancelable() || e.getType() != RenderGameOverlayEvent.ElementType.TEXT) {
            return;
        }

        FontRenderer fontRenderer = Minecraft.getInstance().fontRenderer;
        MatrixStack matrixStack = e.getMatrixStack();

        int x = 180;
        int y = 10;

        // Minecraft.getInstance().textureManager.bindTexture(BAR);
        GuiUtils.drawTexturedModalRect(x + 8, y + 215, w_tex, h_tex, w_gui, h_gui, 0.2f);

        if (currentTunnel == 0) {
            fontRenderer.drawStringWithShadow(matrixStack, "Welcome! Use [W] and [S] to change location", x - 50, y, 0xffffffff);
            fontRenderer.drawStringWithShadow(matrixStack, "If available, change camera angle with [A] and [D]", x - 50, y + 10, 0xffffffff);

        } else {
            fontRenderer.drawStringWithShadow(matrixStack, "Tunnel", x, y, 0xffffffff);
            fontRenderer.drawStringWithShadow(matrixStack, currentTunnel.toString(), x + 35, y, 0xffffffff);

            fontRenderer.drawStringWithShadow(matrixStack, currentAngle.toString(), x + 85, y, 0xffffffff);
            fontRenderer.drawStringWithShadow(matrixStack, "/" + currentNumAngles.toString(), x + 90, y, 0xffffffff);

            fontRenderer.drawStringWithShadow(matrixStack, "Progress:", x, y + 10, 0xffffffff);
            fontRenderer.drawStringWithShadow(matrixStack, progress.toString(), x + 58, y + 10, 0xffffffff);
            fontRenderer.drawStringWithShadow(matrixStack, "%", x + 74, y + 10, 0xffffffff);
        }


    }
}
