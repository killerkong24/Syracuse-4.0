package org.mapleir.dot4j.systems.module.impl.player;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import org.mapleir.dot4j.event.EventTarget;
import org.mapleir.dot4j.gui.setting.BooleanSetting;
import org.mapleir.dot4j.systems.module.core.Category;
import org.mapleir.dot4j.systems.module.core.Module;

@Module.Info(name = "NoSlow", description = "Removes slowing down", category = Category.PLAYER)

public class NoSlow extends Module {

    public BooleanSetting items = new BooleanSetting("Items", true);
    public BooleanSetting sneaking = new BooleanSetting("Sneaking", false);
    public BooleanSetting webs = new BooleanSetting("Webs", false);
    private int ticks = 0;

    public NoSlow() {
        addSettings(items, sneaking, webs);
    }

    public static boolean doesBoxTouchBlock(Box box, Block block) {
        for (int x = (int) Math.floor(box.minX); x < Math.ceil(box.maxX); x++) {
            for (int y = (int) Math.floor(box.minY); y < Math.ceil(box.maxY); y++) {
                for (int z = (int) Math.floor(box.minZ); z < Math.ceil(box.maxZ); z++) {
                    if (mc.world.getBlockState(new BlockPos(x, y, z)).getBlock() == block) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public void onDisable() {
        ticks = 0;
        super.onDisable();
    }

    // items no slow handled in ClientPlayerMixin

    @Override
    public void onEnable() {
        ticks = 0;
        super.onEnable();
    }

    @EventTarget
    public void onTick() {
        if (webs.isEnabled() && doesBoxTouchBlock(mc.player.getBoundingBox(), Blocks.COBWEB)) {
            mc.player.slowMovement(mc.world.getBlockState(mc.player.getBlockPos()), new Vec3d(2.35, 1.75, 2.35));
        }
    }


}