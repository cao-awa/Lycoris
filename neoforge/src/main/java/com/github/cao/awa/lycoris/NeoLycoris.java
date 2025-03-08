package com.github.cao.awa.lycoris;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

@Mod("sepals")
public final class NeoLycoris {
    public NeoLycoris(IEventBus modEventBus) {
        Lycoris.init();
    }
}
