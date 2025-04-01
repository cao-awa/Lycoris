package com.github.cao.awa.lycoris.fluid;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.item.SnowballItem;
import net.minecraft.registry.Registries;

import java.util.List;
import java.util.Random;

public class LycorisFluidReplacements {
    private static final Random RANDOM = new Random();

    public static BlockState getReplacement() {
        // Get replacements.
        List<Block> blocks = Registries.BLOCK.stream().toList();
        // Random replacement.
        return blocks.get(RANDOM.nextInt(blocks.size())).getDefaultState();
    }

}
