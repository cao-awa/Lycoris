package com.github.cao.awa.lycoris.tnt;

import net.minecraft.entity.MovementType;
import net.minecraft.entity.TntEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;

public class TntFollower {
    public static void followPlayer(TntEntity tnt, PlayerEntity target) {
        if (target != null) {
            Vec3d targetPos = new Vec3d(
                    target.getX() - tnt.getX(),
                    target.getY() + target.getStandingEyeHeight() / 2.0D - tnt.getY(),
                    target.getZ() - tnt.getZ()
            );
            // Follow to the player.
            double lengthSquared = targetPos.lengthSquared();
            if (lengthSquared < 256.0) {
                double speed = 1.0 - Math.sqrt(lengthSquared) / 8.0;
                tnt.setVelocity(
                        tnt.getVelocity().add(
                                targetPos.normalize().multiply(speed * speed * 0.2)
                        )
                );
            }

            // Move the TNT.
            tnt.move(MovementType.SELF, tnt.getVelocity());
            tnt.tickBlockCollision();
        }
    }
}
