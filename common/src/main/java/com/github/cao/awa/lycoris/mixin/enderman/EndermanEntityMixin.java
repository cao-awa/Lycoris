package com.github.cao.awa.lycoris.mixin.enderman;

import com.github.cao.awa.lycoris.enderman.goal.EndermanMakeHoleGoal;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.GoalSelector;
import net.minecraft.entity.ai.goal.RevengeGoal;
import net.minecraft.entity.mob.EndermanEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EndermanEntity.class)
public class EndermanEntityMixin extends MobEntity {
    protected EndermanEntityMixin(EntityType<? extends MobEntity> entityType, World world) {
        super(entityType, world);
    }

    @Unique
    private EndermanEntity beEnderman() {
        return (EndermanEntity) (Object) this;
    }

    @Inject(
            method = "initGoals",
            at = @At("RETURN")
    )
    public void initMakeHoleGoal(CallbackInfo ci) {
        // Let the enderman make hole near the player.
        this.targetSelector.add(0, new EndermanMakeHoleGoal(beEnderman()));
    }
}
