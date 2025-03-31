package com.github.cao.awa.lycoris.mixin.entity.tnt;

import com.github.cao.awa.lycoris.Lycoris;
import net.minecraft.entity.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TntEntity.class)
public abstract class TntEntityMixin extends Entity {
    @Unique
    private boolean isImmediately = false;
    @Unique
    private PlayerEntity target;

    public TntEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Shadow public abstract void setFuse(int fuse);

    @Shadow @Nullable private LivingEntity causingEntity;

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

        followPlayer();
    }

    private void followPlayer() {
        World world = getWorld();

        if (this.target == null) {
            if (this.causingEntity instanceof PlayerEntity causingPlayer) {
                this.target = causingPlayer;
            } else {
                return;
            }
        }

        if (this.target.isSpectator() || this.target.isDead()) {
            this.target = null;
        }

        if (this.target != null) {
            Vec3d targetPos = new Vec3d(
                    this.target.getX() - getX(),
                    this.target.getY() + this.target.getStandingEyeHeight() / 2.0D - getY(),
                    this.target.getZ() - getZ()
            );
            double lengthSquared = targetPos.lengthSquared();
            if (lengthSquared < 256.0) {
                double speed = 1.0 - Math.sqrt(lengthSquared) / 8.0;
                setVelocity(getVelocity().add(targetPos.normalize().multiply(speed * speed * 0.2)));
            }
        }

        move(MovementType.SELF, getVelocity());
        tickBlockCollision();
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
