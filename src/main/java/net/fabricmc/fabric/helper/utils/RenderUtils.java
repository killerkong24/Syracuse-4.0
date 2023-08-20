package net.fabricmc.fabric.helper.utils;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.fabric.helper.Iutils.IMinecraftInstance;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.net.URL;

import static net.fabricmc.fabric.helper.utils.SyraMC.mc;

// This is a Helper class for drawing specific things to the screen
public class RenderUtils implements IMinecraftInstance {

    public static Vec3d getCameraPos() {
        return mc.getBlockEntityRenderDispatcher().camera.getPos();
    }

    // Stolen from DrawableHelper
    public static void fill(@NotNull DrawContext context, double x1, double y1, double x2, double y2, int color) {
        MatrixStack matrices = context.getMatrices();
        Matrix4f matrix = matrices.peek().getPositionMatrix();
        double i;
        if (x1 < x2) {
            i = x1;
            x1 = x2;
            x2 = i;
        }
        if (y1 < y2) {
            i = y1;
            y1 = y2;
            y2 = i;
        }
        float f = (float) (color >> 24 & 0xFF) / 255.0f;
        float g = (float) (color >> 16 & 0xFF) / 255.0f;
        float h = (float) (color >> 8 & 0xFF) / 255.0f;
        float j = (float) (color & 0xFF) / 255.0f;
        BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShader(GameRenderer::getPositionColorProgram);
        bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
        bufferBuilder.vertex(matrix, (float) x1, (float) y2, 0.0f).color(g, h, j, f).next();
        bufferBuilder.vertex(matrix, (float) x2, (float) y2, 0.0f).color(g, h, j, f).next();
        bufferBuilder.vertex(matrix, (float) x2, (float) y1, 0.0f).color(g, h, j, f).next();
        bufferBuilder.vertex(matrix, (float) x1, (float) y1, 0.0f).color(g, h, j, f).next();
        BufferRenderer.drawWithGlobalProgram(bufferBuilder.end());
        RenderSystem.disableBlend();
    }

    public static void drawHollowRect(DrawContext context, int x, int y, int width, int height, int color, int thickness) {
        fill(context, x, y - thickness, x - thickness, y + height + thickness, color);
        fill(context, x + width, y - thickness, x + width + thickness, y + height + thickness, color);

        fill(context, x, y, x + width, y - thickness, color);
        fill(context, x, y + height, x + width, y + height + thickness, color);
    }

    // Credit to BadGamesInc#7634 for this, actually I just realized it's from Coffee client lol so here's a link: https://github.com/business-goose/Coffee/tree/master
    public static void renderRoundedQuad(@NotNull DrawContext context, double fromX, double fromY, double toX, double toY, double rad, double samples, @NotNull Color c) {
        MatrixStack matrices = context.getMatrices();
        int color = c.getRGB();
        Matrix4f matrix = matrices.peek().getPositionMatrix();
        float f = (float) (color >> 24 & 255) / 255.0F;
        float g = (float) (color >> 16 & 255) / 255.0F;
        float h = (float) (color >> 8 & 255) / 255.0F;
        float k = (float) (color & 255) / 255.0F;
        RenderSystem.enableBlend();
        RenderSystem.setShader(GameRenderer::getPositionColorProgram);

        renderRoundedQuadInternal(matrix, g, h, k, f, fromX, fromY, toX, toY, rad, samples);

        RenderSystem.disableBlend();
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
    }

    public static void renderRoundedQuadInternal(Matrix4f matrix, float cr, float cg, float cb, float ca, double fromX, double fromY, double toX, double toY, double rad, double samples) {
        BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();
        bufferBuilder.begin(VertexFormat.DrawMode.TRIANGLE_FAN, VertexFormats.POSITION_COLOR);

        double toX1 = toX - rad;
        double toY1 = toY - rad;
        double fromX1 = fromX + rad;
        double fromY1 = fromY + rad;
        double[][] map = new double[][]{new double[]{toX1, toY1}, new double[]{toX1, fromY1}, new double[]{fromX1, fromY1}, new double[]{fromX1, toY1}};
        for (int i = 0; i < 4; i++) {
            double[] current = map[i];
            for (double r = i * 90d; r < (360 / 4d + i * 90d); r += (90 / samples)) {
                float rad1 = (float) Math.toRadians(r);
                float sin = (float) (Math.sin(rad1) * rad);
                float cos = (float) (Math.cos(rad1) * rad);
                bufferBuilder.vertex(matrix, (float) current[0] + sin, (float) current[1] + cos, 0.0F).color(cr, cg, cb, ca).next();
            }
        }
        BufferRenderer.drawWithGlobalProgram(bufferBuilder.end());
    }

    /**
     * Wrapper method
     */
    public static void drawCircle(DrawContext context, double centerX, double centerY, double radius, double samples, Color color) {
        drawCircle(context, centerX, centerY, radius, samples, color.getRGB());
    }

    /**
     * Draws a cirlce
     * @param context ...
     * @param centerX CenterX of the Circle
     * @param centerY CenterY of the circle
     * @param radius "Bigness" of the circle
     * @param samples How many pixels should be used
     * @param color Color
     */
    public static void drawCircle(DrawContext context, double centerX, double centerY, double radius, double samples, int color) {
        MatrixStack matrices = context.getMatrices();
        float alpha = (float) (color >> 24 & 255) / 255.0F;
        float red = (float) (color >> 16 & 255) / 255.0F;
        float green = (float) (color >> 8 & 255) / 255.0F;
        float blue = (float) (color & 255) / 255.0F;
        RenderSystem.enableBlend();

        RenderSystem.setShader(GameRenderer::getPositionColorProgram);
        Matrix4f matrix = matrices.peek().getPositionMatrix();
        BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();
        bufferBuilder.begin(VertexFormat.DrawMode.TRIANGLE_FAN, VertexFormats.POSITION_COLOR);

        for (double r = 0; r < 360; r += (360 / samples)) {
            float rad1 = (float) Math.toRadians(r);
            float sin = (float) (Math.sin(rad1) * radius);
            float cos = (float) (Math.cos(rad1) * radius);
            bufferBuilder.vertex(matrix, (float) centerX + sin, (float) centerY + cos, 0.0F).color(red, green, blue, alpha).next();
        }

        BufferRenderer.drawWithGlobalProgram(bufferBuilder.end());

        RenderSystem.disableBlend();
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
    }

    // This is never used and I forgor I had it
    public static void betterScissor(double x, double y, double x2, double y2) {
        int xPercent = (int) (x / mc.getWindow().getScaledWidth());
        int yPercent = (int) (y / mc.getWindow().getHeight());
        int widthPercent = (int) (x2 / mc.getWindow().getWidth());
        int heightPercent = (int) (y2 / mc.getWindow().getHeight());
        RenderSystem.enableScissor(xPercent, yPercent, widthPercent, heightPercent);
    }

    /**
     * Very good scissor method for on screen coordinates by @falsel
     * @param x1 Starting x
     * @param y1 Starting y
     * @param x2 ending x
     * @param y2 ending y
     * @author falsel
     */
    public static void enableScissor(int x1, int y1, int x2, int y2) {

        int scaleFactor = (int) mc.getWindow().getScaleFactor();

        RenderSystem.enableScissor(x1 * scaleFactor,
                (mc.getWindow().getScaledHeight() - y2) * scaleFactor,
                (x2-x1) * scaleFactor,
                (y2-y1) * scaleFactor);
    }

    /**
     * Very good scissor method for on screen coordinates by @falsel
     * @param x Starting x
     * @param y Starting y
     * @param x2 ending x
     * @param y2 ending y
     * @author falsel
     */
    public static void enableScissor(double x, double y, double x2, double y2) {
        enableScissor((int) x, (int) y, (int) x2, (int) y2);
    }

    public static void disableScissor() {
        RenderSystem.disableScissor();
    }

    /**
     *  Draws an image at specified coords
     * @param context The gl context to draw with
     * @param x X pos
     * @param y y pos
     * @param path Path to the image
     */
    public static void drawTexturedRectangle(DrawContext context, float x, float y, String path) {
        try {
            URL url = RenderUtils.class.getResource("/assets/vape_menu/" + path);

            if(url == null) {
                System.out.println("URL is null: " + path);
                return;
            }

            BufferedImage image = ImageIO.read(url);
            context.drawTexture(new Identifier("vape_menu", path), (int) x, (int) y, 0.0f, 0.0f, image.getWidth(), image.getHeight(), image.getWidth(), image.getHeight());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void drawScaledTexturedRect(DrawContext context, float x, float y, float scale, String path) {
        MatrixStack matrices = context.getMatrices();
        // Scale
        matrices.push();
        matrices.scale(scale, scale, 0);
        // Bind the texture
        try {
            URL url = RenderUtils.class.getResource("/assets/vape_menu/" + path);

            if(url == null) {
                System.out.println("URL is null: " + path);
                return;
            }

            // Get dimensions of image
            BufferedImage image = ImageIO.read(url);
            // Draw the image
            context.drawTexture(new Identifier("vape_menu", path), (int) (x / scale), (int) (y / scale), 0.0f, 0.0f, image.getWidth(), image.getHeight(), image.getWidth(), image.getHeight());
            matrices.pop();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }


}
