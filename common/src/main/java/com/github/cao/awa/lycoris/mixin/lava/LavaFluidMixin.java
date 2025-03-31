package com.github.cao.awa.lycoris.mixin.lava;

import com.github.cao.awa.lycoris.config.LycorisConfig;
import com.github.cao.awa.lycoris.fluid.LycorisFluidReplacements;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.block.BlockState;
import net.minecraft.fluid.LavaFluid;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(LavaFluid.class)
public class LavaFluidMixin {
    @WrapOperation(
            method = "flow",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/WorldAccess;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;I)Z"
            )
    )
    public boolean flow(WorldAccess worldAccess, BlockPos blockPos, BlockState blockState, int i, Operation<Boolean> original) {
        if (LycorisConfig.lavaAndWaterGenerateMoreBlocks) {
            // Let lava and water generate more blocks.
            return worldAccess.setBlockState(blockPos, LycorisFluidReplacements.getReplacement(), i);
        } else {
            // Do not change block when disabled feature.
            return original.call(worldAccess, blockPos, blockState, i);
        }
    }
}
