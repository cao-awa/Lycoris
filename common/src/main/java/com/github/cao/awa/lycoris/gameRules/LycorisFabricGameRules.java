package com.github.cao.awa.lycoris.gameRules;

import com.github.cao.awa.lycoris.mixin.GameRulesAccessor;
import net.minecraft.world.GameRules;

public class LycorisFabricGameRules {
    public static final GameRules.Key<GameRules.IntRule> LAVA_MELT_TIME =
            register("lavaMeltBucketTime", GameRules.Category.MISC, GameRules.IntRule.create(200));
    public static final GameRules.Key<GameRules.BooleanRule> ENABLE_LAVA_MELT =
            register("lavaMeltBucket", GameRules.Category.MISC, GameRules.BooleanRule.create(true));

    public static <T extends GameRules.Rule<T>> GameRules.Key<T> register(String name, GameRules.Category category, GameRules.Type<T> type) {
        return GameRulesAccessor.callRegister(name, category, type);
    }
    public static void registerRules(){

    }
}
