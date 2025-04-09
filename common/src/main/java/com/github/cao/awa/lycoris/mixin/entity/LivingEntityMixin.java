package com.github.cao.awa.lycoris.mixin.entity;

import com.github.cao.awa.lycoris.Lycoris;
import com.github.cao.awa.lycoris.config.LycorisConfig;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.DeathProtectionComponent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.ItemStack;
import net.minecraft.item.consume.ConsumeEffect;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {
    @Shadow
    public abstract void setHealth(float health);

    @Inject(
            method = "tryUseDeathProtector",
            at = @At(
                    value = "HEAD"
            ),
            cancellable = true
    )
    public void tryUseDeathProtector(DamageSource source, CallbackInfoReturnable<Boolean> cir) {
        // Stop entity from dying is attacker has a totem in hand
        if (LycorisConfig.enemyUseDeathProtector && !source.isIn(DamageTypeTags.BYPASSES_INVULNERABILITY)) {
            Entity attacker = source.getAttacker();
            boolean cantDie = false;
            ItemStack itemStack = null;
            DeathProtectionComponent deathProtectionComponent = null;

            if (attacker instanceof LivingEntity livingAttacker) {
                if (attacker instanceof ServerPlayerEntity serverPlayerEntity) {
                    itemStack = serverPlayerEntity.getStackInHand(Hand.OFF_HAND);
                    deathProtectionComponent = itemStack.get(DataComponentTypes.DEATH_PROTECTION);
                    if (deathProtectionComponent != null) {
                        serverPlayerEntity.incrementStat(Stats.USED.getOrCreateStat(itemStack.getItem()));
                        Criteria.USED_TOTEM.trigger(serverPlayerEntity, itemStack);
                        serverPlayerEntity.emitGameEvent(GameEvent.ITEM_INTERACT_FINISH);
                        cantDie = true;
                    }
                } else {
                    for (Hand hand : Hand.values()) {
                        itemStack = livingAttacker.getStackInHand(hand);
                        deathProtectionComponent = itemStack.get(DataComponentTypes.DEATH_PROTECTION);
                        if (deathProtectionComponent != null) {
                            itemStack.decrement(1);
                            cantDie = true;
                            break;
                        }
                    }
                }
            }
            if (cantDie) {
                setHealth(1.0F);
                itemStack.decrement(1);
                deathProtectionComponent.applyDeathEffects(itemStack, (LivingEntity) (Object) this);
                attacker.getWorld().sendEntityStatus((LivingEntity) (Object) this, (byte) 35);
                cir.setReturnValue(true);
            }
        }
    }
}
