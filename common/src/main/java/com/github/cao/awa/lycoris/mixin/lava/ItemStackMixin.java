package com.github.cao.awa.lycoris.mixin.lava;

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

import static com.github.cao.awa.lycoris.gameRules.LycorisFabricGameRules.ENABLE_LAVA_MELT;
import static com.github.cao.awa.lycoris.gameRules.LycorisFabricGameRules.LAVA_MELT_TIME;

@Mixin(ItemStack.class)
public class ItemStackMixin {
    @Unique private int burningTime = 0;
    @Final
    @Shadow private Item item;
    @Final
    @Shadow MergedComponentMap components;

    @Shadow
    public <T> T set(ComponentType<? super T> type, @Nullable T value) {
        return this.components.set(type, value);
    }
    @Shadow private int count;
    @Shadow
    public int getCount() {
        return this.isEmpty() ? 0 : this.count;
    }
    @Shadow
    public boolean isEmpty() {
        return this.item.getDefaultStack() == ItemStack.EMPTY || this.item == Items.AIR || this.count <= 0;
    }
    //Lava bucket will melt
    @Inject(
            method = "inventoryTick",
            at = @At("HEAD")
    )
    public void inventoryTick(World world, Entity entity, int slot, boolean selected, CallbackInfo ci)  {
        if(this.item != Items.LAVA_BUCKET) return;

        if(!world.isClient) {
            if(!world.getServer().getGameRules().getBoolean(ENABLE_LAVA_MELT)) return;

            this.set(DataComponentTypes.MAX_DAMAGE, world.getServer().getGameRules().getInt(LAVA_MELT_TIME));
            if(entity instanceof PlayerEntity playerEntity){
                if(playerEntity.isCreative() || playerEntity.isSpectator() || playerEntity.isSubmergedInWater()) return;
                playerEntity.getInventory().getStack(slot).setDamage(burningTime);
                burningTime++;
                if(burningTime > world.getServer().getGameRules().getInt(LAVA_MELT_TIME)){
                    world.setBlockState(entity.getBlockPos(), Blocks.LAVA.getDefaultState());
                    playerEntity.getInventory().setStack(slot, new ItemStack(Items.MAGMA_BLOCK, this.getCount()));
                    burningTime = 0;
                }
            }
        }else{
            for(int i = 0;i < (int) (Math.min(burningTime,60)/2);i++){
                world.addParticle(
                        ParticleTypes.FALLING_LAVA,
                        entity.getX() + (Math.random() - 0.5) * 0.5,
                        entity.getY() + 0.5 + Math.random() * 0.5,
                        entity.getZ() + (Math.random() - 0.5) * 0.5,
                        0.0, 0.0, 0.0
                );
            }
        }

    }
}
