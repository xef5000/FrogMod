package com.xef5000.mixin;

import com.xef5000.FrogMod;
import com.xef5000.features.CropHitboxes;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.block.BlockCrops;
import net.minecraft.block.BlockNetherWart;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.*;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(BlockNetherWart.class)
public abstract class MixinBlockNetherWart extends BlockBush {

    public final AxisAlignedBB[] NETHER_WART_BOX_one = {
            new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.3125D, 1.0D),
            new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.5D, 1.0D),
            new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.6875D, 1.0D),
            new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D)
    };
    public final AxisAlignedBB[] NETHER_WART_BOX = {
            new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.3125D, 1.0D),
            new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.5D, 1.0D),
            new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.6875D, 1.0D),
            new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.875D, 1.0D)
    };

    private final AxisAlignedBB oneByOne = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);

    @Override
    public AxisAlignedBB getSelectedBoundingBox(World worldIn, BlockPos pos) {
        CropHitboxes.updateWartMaxY(worldIn, pos, worldIn.getBlockState(pos).getBlock());
        return super.getSelectedBoundingBox(worldIn, pos);
    }

    @Override
    public MovingObjectPosition collisionRayTrace(World worldIn, BlockPos pos, Vec3 start, Vec3 end) {
        CropHitboxes.updateWartMaxY(worldIn, pos, worldIn.getBlockState(pos).getBlock());
        return super.collisionRayTrace(worldIn, pos, start, end);

    }

    public void updateWartBB(World world, BlockPos pos, Block block) {
        ((BlockAccessor) block).setMaxY(
                FrogMod.INSTANCE.getFrogModConfig().oneByOneCrops
                        ? NETHER_WART_BOX_one[world.getBlockState(pos).getValue(BlockNetherWart.AGE)].maxY
                        : NETHER_WART_BOX[world.getBlockState(pos).getValue(BlockNetherWart.AGE)].maxY
        );
    }

    public void updateCropBB(World world, BlockPos pos, Block block) {
        final IBlockState blockState = world.getBlockState(pos);
        final Integer ageValue = blockState.getValue(BlockCrops.AGE);
        BlockAccessor accessor = (BlockAccessor) block;
        if (FrogMod.INSTANCE.getFrogModConfig().oneByOneCrops) {
            accessor.setMaxY(oneByOne.maxY);
            accessor.setMaxX(oneByOne.maxX);
            accessor.setMaxZ(oneByOne.maxZ);
            return;
        }

        accessor.setMaxY(0.25F);
    }

}
