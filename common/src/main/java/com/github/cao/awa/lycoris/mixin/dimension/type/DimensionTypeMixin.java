package com.github.cao.awa.lycoris.mixin.dimension.type;

import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.dimension.DimensionTypes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(DimensionType.class)
public class DimensionTypeMixin {
    @Inject(
            method = "ultrawarm",
            at = @At("HEAD"),
            cancellable = true
    )
    public void ultrawarm(CallbackInfoReturnable<Boolean> cir) {
        // Let all dimension can places water.
        cir.setReturnValue(false);
    }
}
