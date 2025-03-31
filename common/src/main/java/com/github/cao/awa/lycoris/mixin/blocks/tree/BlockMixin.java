package com.github.cao.awa.lycoris.mixin.blocks.tree;

import com.github.cao.awa.lycoris.Lycoris;
import com.github.cao.awa.lycoris.tree.TreeAnalyzer;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.FallingBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static com.github.cao.awa.lycoris.tree.TreeAnalyzer.isLog;

@Mixin(Block.class)
public abstract class BlockMixin {
    @Shadow public abstract BlockState getDefaultState();

    @Inject(
            method = "onBreak",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/block/Block;spawnBreakParticles(Lnet/minecraft/world/World;Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;)V"
            )
    )
    private void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player, CallbackInfoReturnable<BlockState> cir) {
        //tree randomly falls down when player mining log blocks without sneaking
        if(Lycoris.RANDOM.nextInt(0,100) < 30 && !player.isCreative() && !player.isSneaking() && isLog(this.getDefaultState().getBlock())) {
            Set<BlockPos> treeBlocks = TreeAnalyzer.findTree(world, pos);
            Map<BlockPos,BlockState> blocks = new HashMap<>();
            //delete all the blocks before turing them into falling blocks to prevent instant-converting
            for(BlockPos blockPos : treeBlocks){
                blocks.put(blockPos, (world.getBlockState(blockPos)));
                world.setBlockState(blockPos, state.getFluidState().getBlockState(), 3);
            }
            for(Map.Entry<BlockPos, BlockState> target : blocks.entrySet()){
                FallingBlockEntity fallingBlockEntity = FallingBlockEntity.spawnFromBlock(world, target.getKey(), target.getValue());
                lycoris$configureFallingBlockEntity(fallingBlockEntity,player,0.12f);
            }

        }
    }
    @Unique
    protected void lycoris$configureFallingBlockEntity(FallingBlockEntity entity, PlayerEntity player, float speed) {
        entity.setHurtEntities(2.0F, 20);
        double distance = entity.getEyePos().distanceTo(player.getEyePos());
        double gravity = entity.getFinalGravity();
        double time = Math.sqrt(distance / speed);
        double yVelocity = (player.getEyePos().y - entity.getEyePos().y + 0.5 * gravity * time * time) / time;
        Vec3d direction = new Vec3d(player.getEyePos().x - entity.getEyePos().x,
                0, player.getEyePos().z - entity.getEyePos().z).normalize();
        entity.setVelocity(direction.x * speed, yVelocity, direction.z * speed);
        entity.setCustomName(Text.translatable(entity.getBlockState().getBlock().getTranslationKey()));
        entity.setCustomNameVisible(false);
    }

}
