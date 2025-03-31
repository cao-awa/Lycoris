package com.github.cao.awa.lycoris.mixin.lava;

import com.github.cao.awa.lycoris.config.LycorisConfig;
import net.minecraft.block.Blocks;
import net.minecraft.component.ComponentType;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.MergedComponentMap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {
    @Unique
    private int burningTime = 0;
    @Final
    @Shadow
    private Item item;
    @Final
    @Shadow
    MergedComponentMap components;

    @Shadow
    public <T> T set(ComponentType<? super T> type, @Nullable T value) {
        return this.components.set(type, value);
    }

    @Shadow public abstract int getCount();

    @Inject(
            method = "inventoryTick",
            at = @At("HEAD")
    )
    public void inventoryTick(World world, Entity entity, int slot, boolean selected, CallbackInfo ci) {
        if (this.item != Items.LAVA_BUCKET || !LycorisConfig.lavaMeltBucket) {
            return;
        }

        if (world.isClient) {
            for (int i = 0; i < (Math.min(this.burningTime, 60) / 2); i++) {
                world.addParticle(
                        ParticleTypes.FALLING_LAVA,
                        entity.getX() + (Math.random() - 0.5) * 0.5,
                        entity.getY() + 0.5 + Math.random() * 0.5,
                        entity.getZ() + (Math.random() - 0.5) * 0.5,
                        0.0, 0.0, 0.0
                );
            }
        } else {
            int lavaMeltBucketTime = LycorisConfig.lavaMeltBucketTicks;

            set(DataComponentTypes.MAX_DAMAGE, lavaMeltBucketTime);
            if (entity instanceof PlayerEntity playerEntity) {
                if (playerEntity.isCreative() || playerEntity.isSpectator() || playerEntity.isSubmergedInWater()) {
                    return;
                }
                // Count the melt time.
                playerEntity.getInventory().getStack(slot).setDamage(this.burningTime);
                this.burningTime++;
                // Lava bucket will melt.
                if (this.burningTime > lavaMeltBucketTime) {
                    world.setBlockState(entity.getBlockPos(), Blocks.LAVA.getDefaultState());
                    playerEntity.getInventory().setStack(slot, new ItemStack(Items.MAGMA_BLOCK, getCount()));
                    this.burningTime = 0;
                }
            }
        }
    }
}
