package com.github.cao.awa.lycoris.mixin.block.entity;

import com.github.cao.awa.lycoris.Lycoris;
import com.github.cao.awa.lycoris.tree.TreeAnalyzer;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SideShapeType;
import net.minecraft.block.entity.HopperBlockEntity;
import net.minecraft.entity.FallingBlockEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Mixin(HopperBlockEntity.class)
public class HopperBlockEntityMixin {
    @Inject(
            method = "insert",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/item/ItemStack;getCount()I"
            ),
            cancellable = true
    )
    private static void insert(
            World world, BlockPos pos, HopperBlockEntity blockEntity,
            CallbackInfoReturnable<Boolean> cir,
            @Local Direction direction,
            @Local ItemStack itemstack,
            @Local int i
    ) {
        // Drops item if back of the hopper isn't blocked
        BlockState blockState = world.getBlockState(blockEntity.getPos().offset(direction));
        if (blockState.isAir()) {
            ItemStack stack = itemstack.copy();
            stack.setCount(Lycoris.RANDOM.nextInt(1, Math.max(2, itemstack.getCount() / 2)));
            blockEntity.removeStack(i, stack.getCount());
            Block.dropStack(world, blockEntity.getPos().offset(direction), stack);
            cir.setReturnValue(true);
        }
    }
}
