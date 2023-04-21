package org.mapleir.dot4j.systems.module.impl.Cpvp;

import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.util.math.Vec3d;
import org.mapleir.dot4j.gui.setting.BooleanSetting;
import org.mapleir.dot4j.gui.setting.NumberSetting;
import org.mapleir.dot4j.helper.utils.*;
import org.mapleir.dot4j.systems.module.core.Category;
import org.mapleir.dot4j.systems.module.core.Module;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

@Module.Info(name = "AutoDhand", description = "anti dier", category = Category.CPVP)
public class AutoDhand extends Module {

    private final NumberSetting dHandHealth = new NumberSetting("DHand Health",0, 20, 0, 1 );
    private final NumberSetting distance = new NumberSetting("Distance",1,10,1,0.1);
    private final NumberSetting activatesAbove = new NumberSetting("Activation Hight", 0, 4, 0, 0.1);
    private final BooleanSetting dhandafterpop = new BooleanSetting("DHand after Pop",false);
    private final BooleanSetting dhandAtHealth = new BooleanSetting("DHand at Health", false);
    private final BooleanSetting checkPlayersAround = new BooleanSetting("Check Around Players",false);
    private final BooleanSetting predictCrystals = new BooleanSetting("Predict Crystals",false);
    private final BooleanSetting checkEnemiesAim = new BooleanSetting("Check Aim", false);
    private final BooleanSetting checkHoldingItems = new BooleanSetting("Check Items", false);
    private boolean BelowHearts;
    private boolean noOffhandTotem;

    public AutoDhand() {
        this.BelowHearts = false;
        this.noOffhandTotem = false;
        addSettings(dhandafterpop, dhandAtHealth, dHandHealth, checkPlayersAround, distance, predictCrystals, checkEnemiesAim, checkHoldingItems, activatesAbove);
    }

    private List<EndCrystalEntity> getNearByCrystals() {
        final Vec3d pos = mc.player.getPos();
        return (List<EndCrystalEntity>)mc.world.getEntitiesByClass((Class)EndCrystalEntity.class, new Box(pos.add(-6.0, -6.0, -6.0), pos.add(6.0, 6.0, 6.0)), a -> true);
    }

    @Override
    public void onTick() {
        final double distanceSq = this.distance.getValue() * this.distance.getValue();
        final PlayerInventory inv = AutoDhand.mc.player.getInventory();
        if (((ItemStack)inv.offHand.get(0)).getItem() != Items.TOTEM_OF_UNDYING && this.dhandafterpop.isEnabled() && !this.noOffhandTotem) {
            this.noOffhandTotem = true;
            InventoryUtils.selectItemFromHotbar(Items.TOTEM_OF_UNDYING);
        }
        if (((ItemStack)inv.offHand.get(0)).getItem() == Items.TOTEM_OF_UNDYING) {
            this.noOffhandTotem = false;
        }
        if (mc.player.getHealth() <= this.dHandHealth.getValue() && this.dhandAtHealth.isEnabled() && !this.BelowHearts) {
            this.BelowHearts = true;
            InventoryUtils.selectItemFromHotbar(Items.TOTEM_OF_UNDYING);
        }
        if (mc.player.getHealth() > this.dHandHealth.getValue()) {
            this.BelowHearts = false;
        }
        if (mc.player.getHealth() > 19.0f) {
            return;
        }
        if (this.checkPlayersAround.isEnabled() && mc.world.getPlayers().parallelStream().filter(e -> e != mc.player).noneMatch(player -> mc.player.squaredDistanceTo(player) <= distanceSq)) {
            return;
        }
        final double activatesAboveV = this.activatesAbove.getValue();
        for (int f = (int)Math.floor(activatesAboveV), i = 1; i <= f; ++i) {
            if (BlockUtils.hasBlock(mc.player.getBlockPos().add(0, -i, 0))) {
                return;
            }
        }
        if (BlockUtils.hasBlock(new BlockPos(mc.player.getPos().add(0.0, -activatesAboveV, 0.0)))) {
            return;
        }
        final List<EndCrystalEntity> crystals = this.getNearByCrystals();
        final ArrayList<Vec3d> crystalsPos = new ArrayList<Vec3d>();
        crystals.forEach(e -> crystalsPos.add(e.getPos()));
        if (this.predictCrystals.isEnabled()) {
            Stream<BlockPos> stream = BlockUtils.getAllInBoxStream(mc.player.getBlockPos().add(-6, -8, -6), mc.player.getBlockPos().add(6, 2, 6)).filter(e -> BlockUtils.isBlock(Blocks.OBSIDIAN, e) || BlockUtils.isBlock(Blocks.BEDROCK, e)).filter(CrystalUtils::canPlaceCrystalClient);
            if (this.checkEnemiesAim.isEnabled()) {
                if (this.checkHoldingItems.isEnabled()) {
                    stream = stream.filter(this::arePeopleAimingAtBlockAndHoldingCrystals);
                }
                else {
                    stream = stream.filter(this::arePeopleAimingAtBlock);
                }
            }
            stream.forEachOrdered(e -> crystalsPos.add(Vec3d.ofBottomCenter(e).add(0.0, 1.0, 0.0)));
        }
        for (final Vec3d pos : crystalsPos) {
            final double damage = DamageUtils.crystalDamage((PlayerEntity) mc.player, pos, true, null, false);
            if (damage >= mc.player.getHealth() + mc.player.getAbsorptionAmount()) {
                InventoryUtils.selectItemFromHotbar(Items.TOTEM_OF_UNDYING);
                break;
            }
        }
    }

    private boolean arePeopleAimingAtBlock(final BlockPos block) {
        final Vec3d[] eyesPos = new Vec3d[1];
        final BlockHitResult[] hitResult = new BlockHitResult[1];
        return mc.world.getPlayers().parallelStream().filter(e -> e != mc.player).anyMatch(e -> {
            eyesPos[0] = RotationUtils.getEyesPos(e);
            hitResult[0] = mc.world.raycast(new RaycastContext(eyesPos[0], eyesPos[0].add(RotationUtils.getPlayerLookVec(e).multiply(4.5)), RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.NONE, (Entity) e));
            return hitResult[0] != null && hitResult[0].getBlockPos().equals((Object)block);
        });
    }

    private boolean arePeopleAimingAtBlockAndHoldingCrystals(final BlockPos block) {
        final Vec3d[] eyesPos = new Vec3d[1];
        final BlockHitResult[] hitResult = new BlockHitResult[1];
        return mc.world.getPlayers().parallelStream().filter(e -> e != mc.player).filter(e -> e.isHolding(Items.END_CRYSTAL)).anyMatch(e -> {
            eyesPos[0] = RotationUtils.getEyesPos(e);
            hitResult[0] = mc.world.raycast(new RaycastContext(eyesPos[0], eyesPos[0].add(RotationUtils.getPlayerLookVec(e).multiply(4.5)), RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.NONE, (Entity) e));
            return hitResult[0] != null && hitResult[0].getBlockPos().equals((Object)block);
        });
    }
}
