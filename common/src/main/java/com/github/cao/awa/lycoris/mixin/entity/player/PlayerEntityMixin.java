package com.github.cao.awa.lycoris.mixin.entity.player;

import com.github.cao.awa.lycoris.Lycoris;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Items;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends Entity {
    @Shadow
    public float strideDistance;

    @Shadow
    @Final
    PlayerInventory inventory;

    public PlayerEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Shadow
    public abstract void setFireTicks(int fireTicks);

    @Inject(
            method = "tick",
            at = @At("HEAD")
    )
    public void tickFlintAndSteelFire(CallbackInfo ci) {
        // Let player in fire when player has flint and steel.
        if (this.strideDistance > 0F) {
            if (this.inventory.contains(
                    stack -> stack.getItem() == Items.FLINT_AND_STEEL
            ) && Lycoris.RANDOM.nextInt(100) == 0) {
                // Fire for 4 seconds.
                setOnFireForTicks(80);
            }
        }
    }
}
