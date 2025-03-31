package com.github.cao.awa.lycoris.config;

import com.alibaba.fastjson2.JSONObject;
import com.github.cao.awa.sinuatum.util.io.IOUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class LycorisConfig {
    public static final File CONFIG_FILE = new File("config/lycoris.json");
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

    public void loadConfig() throws IOException {
        loadConfig(
                JSONObject.parse(IOUtil.read(new FileReader(CONFIG_FILE)))
        );
    }

    public void loadConfig(JSONObject json) {
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

    }
}
