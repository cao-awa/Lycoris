package com.github.cao.awa.lycoris.enderman.goal;

import com.github.cao.awa.lycoris.Lycoris;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.RevengeGoal;
import net.minecraft.entity.ai.goal.TrackTargetGoal;
import net.minecraft.entity.mob.EndermanEntity;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.util.math.*;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;

import java.util.Optional;

public class EndermanMakeHoleGoal extends TrackTargetGoal {
    private final EndermanEntity enderman;
    private PlayerEntity target;

    public EndermanMakeHoleGoal(EndermanEntity enderman) {
        super(enderman, true);
        this.enderman = enderman;
    }

    @Override
    public boolean canStart() {
        return Lycoris.RANDOM.nextInt(0, 100) == 0;
    }

    public boolean shouldRunEveryTick() {
        return true;
    }

    @Override
    public void start() {
        if (this.target == null) {
            Optional<Entity> nearest = this.enderman.getWorld().getOtherEntities(
                    this.enderman,
                    new Box(
                            this.enderman.getPos().add(
                                    4, 4, 4
                            ),
                            this.enderman.getPos().subtract(
                                    4, 0, 4
                            )
                    )
            ).stream().findAny();

            if (nearest.isPresent() && nearest.get() instanceof PlayerEntity player) {
                this.target = player;
            } else {
                return;
            }
        }

        BlockPos targetPos = this.target.getBlockPos();
        World world = this.enderman.getWorld();
        if (targetPos.isWithinDistance(this.enderman.getBlockPos(), 4)) {
            BlockPos holePos = targetPos.offset(Direction.DOWN, 2);

            if (!world.getBlockState(holePos).isAir()) {
                Vec3d selfPos = this.enderman.getPos();
                BlockPos takeBlockPos = holePos.offset(Direction.UP, 1);

                BlockState blockState = world.getBlockState(takeBlockPos);
                BlockState holeBlockState = world.getBlockState(takeBlockPos);

                world.removeBlock(takeBlockPos, false);
                world.emitGameEvent(GameEvent.BLOCK_DESTROY, takeBlockPos, GameEvent.Emitter.of(this.enderman, blockState));
                this.enderman.setCarriedBlock(blockState.getBlock().getDefaultState());

                world.removeBlock(holePos, false);
                world.emitGameEvent(GameEvent.BLOCK_DESTROY, holePos, GameEvent.Emitter.of(this.enderman, blockState));
                world.spawnEntity(
                        new ItemEntity(
                                world,
                                selfPos.x,
                                selfPos.y,
                                selfPos.z,
                                new ItemStack(holeBlockState.getBlock())
                        )
                );
            }
        } else {
        }
    }
}
