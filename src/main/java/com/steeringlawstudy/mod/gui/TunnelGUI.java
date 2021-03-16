package com.steeringlawstudy.mod.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.steeringlawstudy.mod.SteeringLawStudy;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.Direction;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class TunnelGUI {
    public static final TunnelGUI INSTANCE = new TunnelGUI();

    public static Integer currentTunnel = 0;
    public static Integer currentAngle = 1;
    public static boolean currentAngleDone = false;
    public static Integer currentNumAngles = 2;
    public static Float progress = 0f;
    public static Direction dir = Direction.SOUTH;

    @SubscribeEvent
    public void renderGUI(RenderGameOverlayEvent e) {
        // Only enter if not in Dev Mode, only for text rendering
        if (SteeringLawStudy.BUILD_MODE || e.isCancelable() || e.getType() != RenderGameOverlayEvent.ElementType.TEXT) {
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
        int y = 30;

        if (currentTunnel == 0) {
            manageTutorialScreen(fontRenderer, matrixStack, x, y);

        } else if (currentTunnel == SteeringLawStudy.NUM_TUNNELS) {
            // END AREA
            fontRenderer.drawStringWithShadow(matrixStack, "CONGRATULATIONS!",
                    x + 25, y, 0x00ff00);
            fontRenderer.drawStringWithShadow(matrixStack, "================",
                    x + 23, y + 10, 0x00ff00);

            fontRenderer.drawStringWithShadow(matrixStack, "You broke all seals on panda island!",
                    x - 28, y + 30, 0x00ff00);
            fontRenderer.drawStringWithShadow(matrixStack, "Finally, the evil curse is lifted and all pandas are safe!",
                    x - 73, y + 40, 0x00ff00);
            fontRenderer.drawStringWithShadow(matrixStack, "All the pandas are cheering you on for what you did!",
                    x - 68, y + 50, 0x00ff00);

            fontRenderer.drawStringWithShadow(matrixStack, "THANK YOU SO MUCH FOR YOUR HELP!",
                    x - 25, y + 70, 0x00ff00);
            fontRenderer.drawStringWithShadow(matrixStack, "To restart, press [W]",
                    x + 23, y + 80, 0x00ff00);

        } else {
            // REGULAR TUNNELS
            x += 15;
            y = 10;

            if (progress >= 100) {
                // LEVEL COMPLETE
                fontRenderer.drawStringWithShadow(matrixStack, "LEVEL COMPLETE",
                        x + 10, y + 100, 0x00ff00);
                fontRenderer.drawStringWithShadow(matrixStack, "==============",
                        x + 10, y + 110, 0x00ff00);

                fontRenderer.drawStringWithShadow(matrixStack, "Continue by using [W]",
                        x, y + 130, 0x00ff00);

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
                fontRenderer.drawStringWithShadow(matrixStack, "Level", x + 30, y, 0xffffff);
                fontRenderer.drawStringWithShadow(matrixStack, currentTunnel.toString(), x + 65, y, 0xffffff);
            }
        }

    }

    private void manageTutorialScreen(FontRenderer fontRenderer, MatrixStack matrixStack, int x, int y) {
        // START AREA
        if (dir == Direction.WEST) {
            fontRenderer.drawStringWithShadow(matrixStack, "< [1/4] >",
                    x + 42, y + 170, 0xffffff);
            fontRenderer.drawStringWithShadow(matrixStack, " ------> ",
                    x + 42, y + 120, 0xffffff);

            fontRenderer.drawStringWithShadow(matrixStack, "WELCOME TO PANDA ISLAND!",
                    x + 2, y, 0xff0000);
            fontRenderer.drawStringWithShadow(matrixStack, "========================",
                    x - 2, y + 10, 0xff0000);

            fontRenderer.drawStringWithShadow(matrixStack, "Please turn around, reading everything you need to know",
                    x - 76, y + 50, 0xffffff);

        } else if (dir == Direction.NORTH) {
            fontRenderer.drawStringWithShadow(matrixStack, "< [2/4] >",
                    x + 42, y + 170, 0xffffff);

            fontRenderer.drawStringWithShadow(matrixStack, "STORY",
                    x + 49, y, 0xffff00);
            fontRenderer.drawStringWithShadow(matrixStack, "=====",
                    x + 49, y + 10, 0xffff00);

            fontRenderer.drawStringWithShadow(matrixStack, "\"Please help us!\", the sad panda cries. \"An evil force",
                    x - 69, y + 50, 0xffffff);
            fontRenderer.drawStringWithShadow(matrixStack, "has cursed panda island, and all pandas are gone!",
                    x - 65, y + 60, 0xffffff);
            fontRenderer.drawStringWithShadow(matrixStack, "To remove the curse, all evil seals have to be broken.",
                    x - 72, y + 70, 0xffffff);
            fontRenderer.drawStringWithShadow(matrixStack, "Use our magic bamboo to save us!\"",
                    x - 26, y + 80, 0xffffff);

        } else if (dir == Direction.EAST) {
            fontRenderer.drawStringWithShadow(matrixStack, "< [3/4] >",
                    x + 42, y + 170, 0xffffff);

            fontRenderer.drawStringWithShadow(matrixStack, "OBJECTIVE",
                    x + 38, y, 0xffff00);
            fontRenderer.drawStringWithShadow(matrixStack, "=========",
                    x + 37, y + 10, 0xffff00);

            fontRenderer.drawStringWithShadow(matrixStack, "Please help the pandas by tracing the evil seals!",
                    x - 59, y + 50, 0xffffff);
            fontRenderer.drawStringWithShadow(matrixStack, "To break the seals, trace them " +
                            SteeringLawStudy.COMPLETIONS * currentNumAngles + " times from green to red.",
                    x - 78, y + 60, 0xffffff);

        } else if (dir == Direction.SOUTH) {
            fontRenderer.drawStringWithShadow(matrixStack, "< [4/4] >",
                    x + 42, y + 170, 0xffffff);

            fontRenderer.drawStringWithShadow(matrixStack, "CONTROLS",
                    x + 41, y, 0xffff00);
            fontRenderer.drawStringWithShadow(matrixStack, "========",
                    x + 41, y + 10, 0xffff00);

            fontRenderer.drawStringWithShadow(matrixStack, "Use [W] and [S] to change location",
                    x - 19, y + 50, 0xffffff);
            fontRenderer.drawStringWithShadow(matrixStack, "When ready, start your journey with [W]",
                    x - 34, y + 70, 0x00ff00);
        }

        // ALL DIRECTIONS
        fontRenderer.drawStringWithShadow(matrixStack, "Look around to continue reading!",
                x - 17,y + 160, 0xffffff);

    }
}
