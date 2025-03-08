package com.github.cao.awa.lycoris.mixin.lava;

import com.github.cao.awa.lycoris.fluid.LycorisFluidReplacements;
import net.minecraft.block.BlockState;
import net.minecraft.block.FluidBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(FluidBlock.class)
public class FluidBlockMixin {
    @Redirect(
            method = "receiveNeighborFluids",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/World;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;)Z"
            )
    )
    private boolean receiveNeighborFluids(World world, BlockPos blockPos, BlockState state) {
        return world.setBlockState(blockPos, LycorisFluidReplacements.getReplacement());
    }
}
