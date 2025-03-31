package com.github.cao.awa.lycoris.mixin.entity.tnt;

import com.github.cao.awa.lycoris.Lycoris;
import net.minecraft.entity.TntEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TntEntity.class)
public abstract class TntEntityMixin {
    @Unique
    private boolean isImmediately = false;

    @Shadow public abstract void setFuse(int fuse);

    @Inject(
            method = "tick",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/TntEntity;setFuse(I)V",
                    shift = At.Shift.AFTER
            )
    )
    public void tickImmediately(CallbackInfo ci) {
        // Randomly explosion immediately in 100 of 1.
        if (Lycoris.RANDOM.nextInt(100) == 0) {
            this.isImmediately = true;
            setFuse(0);
        }
    }

    @Inject(
            method = "explode",
            at = @At(
                    value = "HEAD"
            ),
            cancellable = true
    )
    public void onExplode(CallbackInfo ci) {
        // Randomly failure in 1000 of 1
        if (!this.isImmediately && Lycoris.RANDOM.nextInt(1000) == 0) {
            ci.cancel();
        }
    }
}
