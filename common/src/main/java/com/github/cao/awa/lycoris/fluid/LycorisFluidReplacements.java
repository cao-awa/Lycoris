package com.github.cao.awa.lycoris.fluid;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;

import java.util.Random;

public class LycorisFluidReplacements {
    private static final BlockState[] STATES = new BlockState[]{
            Blocks.COBBLESTONE.getDefaultState(),
            Blocks.COBBLED_DEEPSLATE.getDefaultState(),
            Blocks.NETHERRACK.getDefaultState(),
            Blocks.END_STONE.getDefaultState(),
            Blocks.OBSIDIAN.getDefaultState(),
            Blocks.CRYING_OBSIDIAN.getDefaultState(),
            Blocks.STONE.getDefaultState(),
            Blocks.GRASS_BLOCK.getDefaultState(),
            Blocks.BRICKS.getDefaultState(),
            Blocks.BLACKSTONE.getDefaultState(),
    };
    private static final Random RANDOM = new Random();

    public static BlockState getReplacement() {
        return STATES[RANDOM.nextInt(STATES.length)];
    }
}
