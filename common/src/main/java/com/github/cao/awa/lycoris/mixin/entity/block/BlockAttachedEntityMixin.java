package com.github.cao.awa.lycoris.mixin.entity.block;

import com.github.cao.awa.lycoris.Lycoris;
import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.BlockAttachedEntity;
import net.minecraft.entity.decoration.LeashKnotEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BlockAttachedEntity.class)
public class BlockAttachedEntityMixin {
    @Inject(
            method = "tick",
            at = @At("RETURN")
    )
    public void randomDiscard(CallbackInfo ci) {
        // Let the leash knot randomly discard.
        if (((Entity) (Object) this) instanceof LeashKnotEntity leashKnot) {
            // Only 1000 of 1.
            if (Lycoris.RANDOM.nextInt(1000) == 0) {
                leashKnot.discard();
            }
        }
    }
}
