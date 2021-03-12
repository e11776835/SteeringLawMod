package com.steeringlawstudy.mod.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.steeringlawstudy.mod.SteeringLawStudy;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class TunnelGUI {
    public static final TunnelGUI INSTANCE = new TunnelGUI();

    public static Integer currentTunnel = 0;
    public static Integer currentAngle = 1;
    public static boolean currentAngleDone = false;
    public static Integer currentNumAngles = 1;
    public static Float progress = 0f;

    @SubscribeEvent
    public void renderGUI(RenderGameOverlayEvent e) {
        if (SteeringLawStudy.DEV_MODE || e.isCancelable() || e.getType() != RenderGameOverlayEvent.ElementType.TEXT) {
            return;
        }

        // TODO ===== PROGRESS BAR
        // private final ResourceLocation BAR = new ResourceLocation(SteeringLawStudy.MOD_ID, "bar.png");
        // private static final int w_tex = 0, h_tex = 0, w_gui = 100, h_gui = 10;
        // Minecraft.getInstance().textureManager.bindTexture(BAR);
        // GuiUtils.drawTexturedModalRect(x + 8, y + 215, w_tex, h_tex, w_gui, h_gui, 0.2f);

        FontRenderer fontRenderer = Minecraft.getInstance().fontRenderer;
        MatrixStack matrixStack = e.getMatrixStack();

        int x = 175;
        int y = 80;

        if (currentTunnel == 0) {
            // START AREA
            fontRenderer.drawStringWithShadow(matrixStack, "PLEASE READ THIS BEFORE STARTING",
                    x - 20, y, 0xff0000);
            fontRenderer.drawStringWithShadow(matrixStack, "Use [W] and [S] to change location",
                    x - 21, y + 30, 0xffffff);
            fontRenderer.drawStringWithShadow(matrixStack, "If available, change camera angle with [A] and [D]",
                    x - 52, y + 40, 0xffffff);

            fontRenderer.drawStringWithShadow(matrixStack, "Please help the pandas by tracing the white paths!",
                    x - 52, y + 60, 0xffffff);
            fontRenderer.drawStringWithShadow(matrixStack, "To complete your quest, trace the paths " +
                            SteeringLawStudy.COMPLETIONS + " times from every angle.",
                    x - 87, y + 70, 0xffffff);

        } else if (currentTunnel == SteeringLawStudy.NUM_TUNNELS) {
            // END AREA
            fontRenderer.drawStringWithShadow(matrixStack, "CONGRATULATIONS! THE PANDAS ARE HAPPY!",
                    x - 20, y, 0x00ff00);

        } else {
            // REGULAR TUNNELS
            x += 10;
            y = 10;

            if (progress >= 100) {
                // LEVEL COMPLETE
                fontRenderer.drawStringWithShadow(matrixStack, "LEVEL COMPLETE",
                        x + 10, y, 0x00ff00);

            } else {
                // LEVEL PROGRESS
                String progressString = "";
                if (progress <= 0) {
                    progressString = " 0";
                } else {
                    progressString = progress.toString().substring(0, 2);
                }

                fontRenderer.drawStringWithShadow(matrixStack, "Progress:",
                        x + 10, y + 10, 0xffffff);
                fontRenderer.drawStringWithShadow(matrixStack, progressString,
                        x + 68, y + 10, 0xffffff);
                fontRenderer.drawStringWithShadow(matrixStack, "%",
                        x + 84, y + 10, 0xffffff);

                // LEVEL
                fontRenderer.drawStringWithShadow(matrixStack, "Level", x, y, 0xffffff);
                fontRenderer.drawStringWithShadow(matrixStack, currentTunnel.toString(), x + 35, y, 0xffffff);

                // ANGLE
                if (currentAngleDone) {
                    fontRenderer.drawStringWithShadow(matrixStack, "ANGLE COMPLETE", x + 52, y, 0x00ff00);

                } else {
                    fontRenderer.drawStringWithShadow(matrixStack, "Angle", x + 52, y, 0xffffff);
                    fontRenderer.drawStringWithShadow(matrixStack, currentAngle.toString(), x + 85, y, 0xffffff);
                    fontRenderer.drawStringWithShadow(matrixStack, "/" + currentNumAngles.toString(),
                            x + 90, y, 0xffffff);
                }

            }
        }

    }
}
