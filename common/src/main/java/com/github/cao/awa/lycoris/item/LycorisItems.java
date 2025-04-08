package com.github.cao.awa.lycoris.item;

import com.github.cao.awa.lycoris.item.pickaxe.LycorisPickaxeItem;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.item.ToolMaterial;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

import java.util.function.Function;

public class LycorisItems {
    public static final Item LYCORIS_WOODEN_PICKAXE = register(
            "lycoris_wooden_pickaxe",
            settings -> new LycorisPickaxeItem(
                    ToolMaterial.WOOD,
                    1.0F,
                    -2.8F,
                    settings
            )
    );
    public static final Item LYCORIS_STONE_PICKAXE = register(
            "lycoris_stone_pickaxe",
            settings -> new LycorisPickaxeItem(
                    ToolMaterial.STONE,
                    1.0F,
                    -2.8F,
                    settings
            )
    );
    public static final Item LYCORIS_GOLDEN_PICKAXE = register(
            "lycoris_golden_pickaxe",
            settings -> new LycorisPickaxeItem(
                    ToolMaterial.GOLD,
                    1.0F,
                    -2.8F,
                    settings
            )
    );
    public static final Item LYCORIS_IRON_PICKAXE = register(
            "lycoris_iron_pickaxe",
            settings -> new LycorisPickaxeItem(
                    ToolMaterial.IRON,
                    1.0F,
                    -2.8F,
                    settings
            )
    );
    public static final Item LYCORIS_DIAMOND_PICKAXE = register(
            "lycoris_diamond_pickaxe",
            settings -> new LycorisPickaxeItem(
                    ToolMaterial.DIAMOND,
                    1.0F,
                    -2.8F,
                    settings
            )
    );
    public static final Item LYCORIS_NETHERITE_PICKAXE = register(
            "lycoris_netherite_pickaxe",
            settings -> new LycorisPickaxeItem(
                    ToolMaterial.NETHERITE,
                    1.0F,
                    -2.8F,
                    settings
            )
    );

    private static Item register(String id, Function<Item.Settings, Item> factory) {
        return Items.register(keyOf(id), factory, new Item.Settings());
    }

    private static RegistryKey<Item> keyOf(String id) {
        return RegistryKey.of(RegistryKeys.ITEM, Identifier.of("lycoris", id));
    }

    public static void init() {
        // Nothing here.
    }
}
