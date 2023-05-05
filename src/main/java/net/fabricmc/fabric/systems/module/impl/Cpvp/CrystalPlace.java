package net.fabricmc.fabric.systems.module.impl.Cpvp;


import net.minecraft.block.Blocks;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.fabricmc.fabric.gui.setting.NumberSetting;
import net.fabricmc.fabric.helper.utils.CrystalUtils;
import net.fabricmc.fabric.systems.module.core.Category;
import net.fabricmc.fabric.systems.module.core.Module;

@Module.Info(name = "Crystal Place", description = "Crystal Placer", category = Category.CPVP)
public class CrystalPlace extends Module {
    public NumberSetting delay = new NumberSetting("Delay", 0, 10, 0, 1);

    private double tickTimer;
    public CrystalPlace() {
        addSettings(delay);
        tickTimer = 0;
    }

    @Override
    public void onTick() {
        if (tickTimer > -1) {
            tickTimer--;
            return;
        }
        Vec3d cameraPos = mc.player.getCameraPosVec(0F);
        Vec3d rotationVec = mc.player.getRotationVec(0F);
        Vec3d targetPos = cameraPos.add(rotationVec.multiply(4.5D));
        BlockHitResult hit = mc.world.raycast(new RaycastContext(cameraPos, targetPos, RaycastContext.ShapeType.OUTLINE, RaycastContext.FluidHandling.NONE, mc.player));
        if (hit.getType() == HitResult.Type.BLOCK && CrystalUtils.canPlaceCrystalServer(hit.getBlockPos()) && (Blocks.OBSIDIAN == mc.world.getBlockState(hit.getBlockPos()).getBlock() || Blocks.BEDROCK == mc.world.getBlockState(hit.getBlockPos()).getBlock()) && (mc.player.getMainHandStack().isOf(Items.END_CRYSTAL))) {
            ActionResult result = mc.interactionManager.interactBlock(mc.player, Hand.MAIN_HAND, hit);
            if (result.isAccepted() && result.shouldSwingHand()) {
                mc.player.swingHand(Hand.MAIN_HAND);
            }
        }
        tickTimer = (int) delay.getValue();
    }
}
