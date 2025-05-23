package com.github.cao.awa.lycoris;

import com.github.cao.awa.lycoris.config.LycorisConfig;
import com.github.cao.awa.lycoris.disc.MusicDiscManager;
import com.github.cao.awa.lycoris.item.LycorisItems;
import com.github.cao.awa.lycoris.item.component.LycorisComponents;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Random;

public class Lycoris {
    public static final Random RANDOM = new Random();
    public static final Logger LOGGER = LogManager.getLogger("Lycoris");
    public static final String VERSION = "1.2.1";
    public static final String MOD_ID = "lycoris";

    public static void init() {
        LycorisConfig.makeConfig();
        MusicDiscManager.init();
        LycorisItems.init();
        LycorisComponents.init();
        LOGGER.info("Lycoris is in your Minecraft~");
    }
}
