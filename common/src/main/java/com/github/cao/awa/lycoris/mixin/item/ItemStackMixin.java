package com.github.cao.awa.lycoris.mixin.item;

import com.github.cao.awa.lycoris.config.LycorisConfig;
import com.github.cao.awa.lycoris.disc.FlyingDiscEntity;
import net.minecraft.block.Blocks;
import net.minecraft.component.ComponentType;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.MergedComponentMap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.entity.projectile.thrown.SnowballEntity;
import net.minecraft.item.*;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static com.github.cao.awa.lycoris.disc.MusicDiscManager.isMusicDisc;
import static net.minecraft.item.EggItem.POWER;

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

    @Shadow public abstract ItemStack copy();

    @Shadow protected abstract ItemStack copyComponentsToNewStackIgnoreEmpty(ItemConvertible item, int count);

    @Shadow public abstract Item getItem();

    @Inject(method = "use",
            at = @At("HEAD"),
            cancellable = true
    )//make discs throwable
    public void use(World world, PlayerEntity user, Hand hand, CallbackInfoReturnable<ActionResult> cir) {
        ItemStack itemStack = user.getStackInHand(hand);
        if(isMusicDisc(itemStack)){
            world.playSound(
                    null,
                    user.getX(),
                    user.getY(),
                    user.getZ(),
                    SoundEvents.ENTITY_SNOWBALL_THROW,
                    SoundCategory.NEUTRAL,
                    0.5F,
                    0.4F / (world.getRandom().nextFloat() * 0.4F + 0.8F)
            );
            if (world instanceof ServerWorld serverWorld) {
                ProjectileEntity.spawnWithVelocity(FlyingDiscEntity::new, serverWorld, itemStack, user, 0.0F, POWER, 1.0F);
            }
            user.incrementStat(Stats.USED.getOrCreateStat(this.getItem()));
            itemStack.decrementUnlessCreative(1, user);
            cir.setReturnValue(ActionResult.SUCCESS);
        }
    }

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
            if (entity instanceof PlayerEntity playerEntity) {
                // Only enable this feature in survival mode.
                if (playerEntity.isCreative() || playerEntity.isSpectator() || playerEntity.isSubmergedInWater()) {
                    return;
                }

                int lavaMeltBucketTime = LycorisConfig.lavaMeltBucketTicks;

                // Count the melt time.
                set(DataComponentTypes.MAX_DAMAGE, lavaMeltBucketTime);
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
