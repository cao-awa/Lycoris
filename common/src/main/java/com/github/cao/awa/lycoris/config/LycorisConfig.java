package com.github.cao.awa.lycoris.config;

import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONWriter;
import com.github.cao.awa.sinuatum.resource.loader.ResourceLoader;
import com.github.cao.awa.sinuatum.util.io.IOUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;

public class LycorisConfig {
    private static final Logger LOGGER = LogManager.getLogger("LycorisConfig");
    public static final File CONFIG_FILE = new File("config/lycoris.json");
    public static JSONObject configJson = new JSONObject();
    public static boolean tntFollowPlayer = true;
    public static boolean tntImmediatelyExplodeRandomly = true;
    public static boolean tntFailureExplodeRandomly = true;
    public static boolean bedExplodeInOverworld = true;
    public static boolean lavaAndWaterGenerateMoreBlocks = true;
    public static boolean entitiesLeaveLeashKnotRandomly = true;
    public static boolean waterCanPlacesInAllWorld = true;
    public static boolean onFireWhenWithFlintAndSteelRandomly = true;
    public static int onFireWhenWithFlintAndSteelTicks = 80;
    public static boolean treesFallingDownWhenBreakTrunkRandomly = true;
    public static boolean lavaMeltBucket = true;
    public static int lavaMeltBucketTicks = 200;

    public static void makeConfig() {
        try {
            if (CONFIG_FILE.isFile()) {
                readConfig();
            } else {
                createDefault();
                readConfig();
            }

            try {
                writeConfig();
            }catch (IOException ex) {
                LOGGER.warn("Failed to save lycoris config", ex);
            }
        } catch (IOException ex) {
            LOGGER.warn("Failed to read lycoris config", ex);
        }
    }

    public static void writeConfig() throws IOException {
        IOUtil.write(
                new FileWriter(CONFIG_FILE),
                configJson.toString(JSONWriter.Feature.PrettyFormat)
        );
    }

    public static void createDefault() throws IOException {
        IOUtil.write(
                new FileWriter(CONFIG_FILE),
                IOUtil.read(ResourceLoader.get("assets/lycoris/config/default_config.json"))
        );
    }

    public static void readConfig() throws IOException {
        configJson = JSONObject.parse(IOUtil.read(new FileReader(CONFIG_FILE)));

        loadConfig(
                configJson
        );
    }

    public static void loadConfig(JSONObject json) {
        tntFollowPlayer = json.getBooleanValue("tnt_follow_player", true);
        tntImmediatelyExplodeRandomly = json.getBooleanValue("tnt_immediately_explode_randomly", true);
        tntFailureExplodeRandomly = json.getBooleanValue("tnt_failure_explode_randomly", true);
        bedExplodeInOverworld = json.getBooleanValue("bed_explode_in_overworld", true);
        lavaAndWaterGenerateMoreBlocks = json.getBooleanValue("lava_and_water_generate_more_blocks", true);
        entitiesLeaveLeashKnotRandomly = json.getBooleanValue("entities_leave_leash_knot_randomly", true);
        waterCanPlacesInAllWorld = json.getBooleanValue("water_can_places_in_all_world", true);
        onFireWhenWithFlintAndSteelRandomly = json.getBooleanValue("on_fire_when_with_flint_and_steel_randomly", true);
        onFireWhenWithFlintAndSteelTicks = json.getIntValue("on_fire_when_with_flint_and_steel_ticks", 80);
        treesFallingDownWhenBreakTrunkRandomly = json.getBooleanValue("trees_falling_down_when_break_trunk_randomly", true);
        lavaMeltBucket = json.getBooleanValue("lava_melt_bucket", true);
        lavaMeltBucketTicks = json.getIntValue("lava_melt_bucket_ticks", 200);
    }
}
