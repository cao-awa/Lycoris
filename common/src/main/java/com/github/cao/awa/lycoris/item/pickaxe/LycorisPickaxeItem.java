package com.github.cao.awa.lycoris.item.pickaxe;

import com.github.cao.awa.lycoris.item.component.LycorisComponents;
import net.minecraft.block.BlockState;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ToolComponent;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageSources;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.PickaxeItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class LycorisPickaxeItem extends PickaxeItem {
    private final ToolMaterial material;

    public LycorisPickaxeItem(ToolMaterial material, float attackDamage, float attackSpeed, Settings settings) {
        super(material, attackDamage, attackSpeed, settings);
        this.material = material;
    }

    @Override
    public void postDamageEntity(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        doDamage(stack, this, attacker, 2);
    }

    @Override
    public boolean postMine(ItemStack stack, World world, BlockState state, BlockPos pos, LivingEntity miner) {
        ToolComponent toolComponent = stack.get(DataComponentTypes.TOOL);
        if (toolComponent == null) {
            return false;
        } else {
            if (world instanceof ServerWorld serverWorld && state.getHardness(world, pos) != 0.0F && toolComponent.damagePerBlock() > 0) {
                doDamage(stack, this, miner, toolComponent.damagePerBlock());

                miner.damage(
                        serverWorld,
                        world.getDamageSources().magic(),
                        1
                );
            }

            return true;
        }
    }

    private static void doDamage(ItemStack stack, LycorisPickaxeItem item, LivingEntity miner, int amount) {
        Integer realDamage = stack.get(LycorisComponents.REAL_DAMAGE);

        if (realDamage == null) {
            stack.set(
                    LycorisComponents.REAL_DAMAGE,
                    amount
            );

            stack.setDamage(item.material.durability() - 1);
        } else {
            int resultDamage = realDamage + amount;

            if (resultDamage >= item.material.durability()) {
             stack.damage(resultDamage, miner, EquipmentSlot.MAINHAND);
            } else {
                stack.set(
                        LycorisComponents.REAL_DAMAGE,
                        resultDamage
                );

                stack.setDamage(item.material.durability() - resultDamage);
            }
        }
    }
}
