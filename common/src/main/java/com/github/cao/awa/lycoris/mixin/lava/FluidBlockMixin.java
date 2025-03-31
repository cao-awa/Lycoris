package com.github.cao.awa.lycoris.mixin.lava;

import com.github.cao.awa.lycoris.config.LycorisConfig;
import com.github.cao.awa.lycoris.fluid.LycorisFluidReplacements;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.block.BlockState;
import net.minecraft.block.FluidBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(FluidBlock.class)
public class FluidBlockMixin {
    @WrapOperation(
            method = "receiveNeighborFluids",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/World;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;)Z"
            )
    )
    private boolean receiveNeighborFluids(World world, BlockPos blockPos, BlockState state, Operation<Boolean> original) {
        if (LycorisConfig.lavaAndWaterGenerateMoreBlocks) {
            // Let lava and water generate more blocks.
            return world.setBlockState(blockPos, LycorisFluidReplacements.getReplacement());
        } else {
            // Do not change block when disabled feature.
            return original.call(world, blockPos, state);
        }
    }
}
