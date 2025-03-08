package com.github.cao.awa.lycoris.mixin.bed;

import net.minecraft.block.BedBlock;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BedBlock.class)
public class BedBlockMixin {
    @Inject(
            method = "isBedWorking",
            at = @At("HEAD"),
            cancellable = true
    )
    private static void bedWorks(World world, CallbackInfoReturnable<Boolean> ci) {
        ci.setReturnValue(world.getRegistryKey() != World.OVERWORLD);
    }
}
