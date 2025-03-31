package com.github.cao.awa.lycoris.tree;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.HashSet;
import java.util.Set;

public class TreeAnalyzer {
    private static final Set<BlockPos> treeBlocks = new HashSet<>();
    private static final Set<BlockPos> visited = new HashSet<>();
    private static final int MAX_DISTANCE = 10;

    public static Set<BlockPos> findTree(World world, BlockPos startPos) {
        treeBlocks.clear();
        visited.clear();

        BlockState startState = world.getBlockState(startPos);
        if (!isLog(startState.getBlock())) {
            return treeBlocks;
        }

        analyzeTree(world, startPos, startPos, 0);

        return treeBlocks;
    }

    private static void analyzeTree(World world, BlockPos currentPos, BlockPos startPos, int distance) {
        if (distance > MAX_DISTANCE || visited.contains(currentPos)) {
            return;
        }

        visited.add(currentPos);

        BlockState state = world.getBlockState(currentPos);
        Block block = state.getBlock();

        if (isLog(block) || isLeaves(block)) {
            treeBlocks.add(currentPos);

            for (BlockPos offset : new BlockPos[]{
                    currentPos.up(), currentPos.down(),
                    currentPos.north(), currentPos.south(),
                    currentPos.east(), currentPos.west()
            }) {
                int newDistance = Math.abs(offset.getX() - startPos.getX()) +
                        Math.abs(offset.getY() - startPos.getY()) +
                        Math.abs(offset.getZ() - startPos.getZ());
                analyzeTree(world, offset, startPos, newDistance);
            }
        }
    }

    public static boolean isLog(Block block) {
        return block.getDefaultState().isIn(BlockTags.LOGS);
    }

    private static boolean isLeaves(Block block) {
        return block.getDefaultState().isIn(BlockTags.LEAVES);
    }
}
