package com.github.cao.awa.lycoris.mixin.lava;

import com.github.cao.awa.lycoris.fluid.LycorisFluidReplacements;
import net.minecraft.block.BlockState;
import net.minecraft.fluid.LavaFluid;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(LavaFluid.class)
public class LavaFluidMixin {
    @Redirect(
            method = "flow",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/WorldAccess;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;I)Z")
    )
    public boolean flow(WorldAccess worldAccess, BlockPos blockPos, BlockState blockState, int i) {
        return worldAccess.setBlockState(blockPos, LycorisFluidReplacements.getReplacement(), i);
    }
}
