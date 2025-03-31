package com.github.cao.awa.lycoris;

import com.github.cao.awa.lycoris.gameRules.LycorisFabricGameRules;
import net.fabricmc.api.ModInitializer;

public class FabricLycoris implements ModInitializer {
    @Override
    public void onInitialize() {
        LycorisFabricGameRules.registerRules();
        Lycoris.init();
    }
}