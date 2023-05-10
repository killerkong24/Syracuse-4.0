package net.fabricmc.fabric.helper.utils;


import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.RespawnAnchorBlock;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;

import java.util.ArrayList;
import java.util.stream.Stream;

import static net.fabricmc.fabric.helper.utils.SyraMC.mc;


public enum BlockUtils2 {
    ;
    public static boolean isAnchorCharged(BlockPos anchor)
    {
        if (!isBlock(Blocks.RESPAWN_ANCHOR, anchor))
            return false;
        try
        {
            return BlockUtils2.getBlockState(anchor).get(RespawnAnchorBlock.CHARGES) != 0;
        }
        catch (IllegalArgumentException e)
        {
            return false;
        }
    }

    private static void addToArrayIfHasBlock(ArrayList<BlockPos> array, BlockPos pos) {
        if (hasBlock(pos) && !isBlockReplaceable(pos))
            array.add(pos);
    }

    public static ArrayList<BlockPos> getClickableNeighbors(BlockPos pos) {
        ArrayList<BlockPos> blocks = new ArrayList<>();
        addToArrayIfHasBlock(blocks, pos.add(1, 0, 0));
        addToArrayIfHasBlock(blocks, pos.add(0, 1, 0));
        addToArrayIfHasBlock(blocks, pos.add(0, 0, 1));
        addToArrayIfHasBlock(blocks, pos.add(-1, 0, 0));
        addToArrayIfHasBlock(blocks, pos.add(0, -1, 0));
        addToArrayIfHasBlock(blocks, pos.add(0, 0, -1));
        return blocks;
    }

    public static boolean isBlockReachable(BlockPos pos, double reach)
    {
        BlockState state = BlockUtils2.getBlockState(pos);
        VoxelShape shape = state.getOutlineShape(mc.world, pos);
        if(shape.isEmpty())
            shape = VoxelShapes.fullCube();

        Vec3d eyesPos = RotationUtils.getEyesPos();
        Vec3d relCenter = shape.getBoundingBox().getCenter();
        Vec3d center = Vec3d.of(pos).add(relCenter);

        boolean reachable = false;
        for (Direction direction : Direction.values())
        {
            Vec3i dirVec = direction.getVector();
            Vec3d relHitVec = new Vec3d(relCenter.x * dirVec.getX(),
                    relCenter.y * dirVec.getY(), relCenter.z * dirVec.getZ());
            Vec3d hitVec = center.add(relHitVec);
            if (eyesPos.squaredDistanceTo(hitVec) <= reach * reach)
                reachable = true;
        }
        return reachable;
    }

    public static boolean placeBlock(BlockPos pos) {
        return placeBlock(pos, getDefaultBlockState());
    }

    public static boolean placeBlock(BlockPos pos, BlockState state) {
        // if block is replaceable
//		if (hasBlock(pos) && BlockUtils.isBlockReplaceable(pos))
//		{
//			BlockState blockToReplace = BlockUtils.getBlockState(pos);
//			Vec3d center = blockToReplace.getOutlineShape(mc.world, pos)
//					.getBoundingBox().getCenter();
//
//
//
//			// fake rotation
//			CWHACK.getRotationFaker().setServerLookPos(center);
//
//			// get raycast result
//			BlockHitResult hitResult = BlockUtils.serverRaycastBlock(pos);
//			ActionResult result = mc.interactionManager.interactBlock(mc.player, mc.world, Hand.MAIN_HAND, hitResult);
//
//			// swing hand
//			boolean succeed = result == ActionResult.SUCCESS;
//			if (succeed)
//				mc.player.swingHand(Hand.MAIN_HAND);
//
//			// return
//			return succeed;
//		}

        // if there has already been a block
        if (hasBlock(pos))
            return false;

        if (!BlockUtils2.canPlace(state, pos))
            return false;

        // if there is no clickable neighbors
        ArrayList<BlockPos> neighbors = BlockUtils2.getClickableNeighbors(pos);
        if (neighbors.isEmpty())
            return false;

        // find the correct neighbor to click on
        BlockPos neighborToClick = null;
        Direction directionToClick = null;
        Vec3d faceCenterToClick = null;
        for (BlockPos neighbor : neighbors) {
            BlockState block = BlockUtils2.getBlockState(neighbor);
            Direction correctFace = null;

            // iterate through 6 faces to find the correct face
            for (Direction face : Direction.values()) {
                if (pos.equals(neighbor.add(face.getVector()))) {
                    correctFace = face;
                    break;
                }
            }

            Vec3d faceCenter = Vec3d.ofCenter(neighbor).add(Vec3d.of(correctFace.getVector()).multiply(0.5));

            BlockHitResult hit = mc.world.raycastBlock(RotationUtils.getEyesPos(), faceCenter, neighbor, BlockUtils2.getBlockState(neighbor).getOutlineShape(mc.world, neighbor), BlockUtils2.getBlockState(neighbor));
            if (hit == null) {
                neighborToClick = neighbor;
                directionToClick = correctFace;
                faceCenterToClick = faceCenter;
                break;
            }
        }

        // if no viable neighbor found
        if (neighborToClick == null)
            return false;

        //CWHACK.getRotationFaker().setServerLookPos(faceCenterToClick);

        ActionResult result = mc.interactionManager.interactBlock(mc.player, Hand.MAIN_HAND, new BlockHitResult(faceCenterToClick, directionToClick, neighborToClick, false));

        if (result == ActionResult.SUCCESS) {
            mc.player.swingHand(Hand.MAIN_HAND);
            return true;
        }

        return false;
    }

    public static boolean canPlace(BlockState state, BlockPos pos) {
        return mc.world.canPlace(state, pos, null);
    }

    public static boolean hasBlock(BlockPos pos) {
        return !mc.world.getBlockState(pos).isAir();
    }

    public static boolean isBlock(Block block, BlockPos pos) {
        return getBlockState(pos).getBlock() == block;
    }

    public static Block getBlock(BlockPos pos) {
        return mc.world.getBlockState(pos).getBlock();
    }

    public static BlockState getBlockState(BlockPos pos) {
        return mc.world.getBlockState(pos);
    }

    public static BlockState getDefaultBlockState() {
        return Blocks.STONE.getDefaultState();
    }

    public static boolean isBlockReplaceable(BlockPos pos) {
        return getBlockState(pos).getMaterial().isReplaceable();
    }

    public static Stream<BlockPos> getAllInBoxStream(BlockPos from, BlockPos to) {
        BlockPos min = new BlockPos(Math.min(from.getX(), to.getX()),
                Math.min(from.getY(), to.getY()), Math.min(from.getZ(), to.getZ()));
        BlockPos max = new BlockPos(Math.max(from.getX(), to.getX()),
                Math.max(from.getY(), to.getY()), Math.max(from.getZ(), to.getZ()));

        Stream<BlockPos> stream = Stream.iterate(min, pos -> {

            int x = pos.getX();
            int y = pos.getY();
            int z = pos.getZ();

            x++;

            if (x > max.getX()) {
                x = min.getX();
                y++;
            }

            if (y > max.getY()) {
                y = min.getY();
                z++;
            }

            if (z > max.getZ())
                throw new IllegalStateException("Stream limit didn't work.");

            return new BlockPos(x, y, z);
        });

        int limit = (max.getX() - min.getX() + 1)
                * (max.getY() - min.getY() + 1) * (max.getZ() - min.getZ() + 1);

        return stream.limit(limit);
    }

    public static boolean rightClickBlock(BlockPos pos) {
        Direction side = null;
        Direction[] sides = Direction.values();

        BlockState state = BlockUtils2.getBlockState(pos);
        VoxelShape shape = state.getOutlineShape(mc.world, pos);
        if (shape.isEmpty())
            return false;

        Vec3d eyesPos = RotationUtils.getEyesPos();
        Vec3d relCenter = shape.getBoundingBox().getCenter();
        Vec3d center = Vec3d.of(pos).add(relCenter);

        Vec3d[] hitVecs = new Vec3d[sides.length];
        for (int i = 0; i < sides.length; i++) {
            Vec3i dirVec = sides[i].getVector();
            Vec3d relHitVec = new Vec3d(relCenter.x * dirVec.getX(),
                    relCenter.y * dirVec.getY(), relCenter.z * dirVec.getZ());
            hitVecs[i] = center.add(relHitVec);
        }

        double distanceSqToCenter = eyesPos.squaredDistanceTo(center);
        for (int i = 0; i < sides.length; i++) {
            // check if side is facing towards player
            if (eyesPos.squaredDistanceTo(hitVecs[i]) >= distanceSqToCenter)
                continue;
            side = sides[i];
            break;
        }

        // player is inside of block, side doesn't matter
        if (side == null)
            side = sides[0];

        ActionResult result1 = null;
        //result1 = mc.interactionManager.interactItem(mc.player, mc.world, Hand.MAIN_HAND);
        ActionResult result2 = mc.interactionManager.interactBlock(mc.player, Hand.MAIN_HAND, new BlockHitResult(hitVecs[side.ordinal()], side, pos, false));
        boolean bl = result1 == ActionResult.SUCCESS || result2 == ActionResult.SUCCESS;
        if (bl)
            mc.player.swingHand(Hand.MAIN_HAND);
        return bl;
    }
}
