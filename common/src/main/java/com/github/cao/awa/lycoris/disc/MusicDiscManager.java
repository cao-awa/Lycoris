package com.github.cao.awa.lycoris.disc;

import com.github.cao.awa.lycoris.Lycoris;
import com.github.cao.awa.lycoris.mixin.entity.IEntityRenderers;
import dev.architectury.registry.client.level.entity.EntityRendererRegistry;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.EntityRenderers;
import net.minecraft.client.render.entity.FlyingItemEntityRenderer;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.projectile.thrown.SnowballEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.ProjectileItem;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;

import java.lang.reflect.Method;
import java.util.Set;

public class MusicDiscManager {
    public static final Identifier FLYING_DISC_ENTITY_ID = Identifier.of(Lycoris.MOD_ID, "flying-disc");
    public static final RegistryKey<EntityType<?>> FLYING_DISC_ENTITY_KEY = RegistryKey.of(Registries.ENTITY_TYPE.getKey(), FLYING_DISC_ENTITY_ID);
    public static final EntityType<FlyingDiscEntity> FLYING_DISC_ENTITY = Registry.register(
            Registries.ENTITY_TYPE,
            FLYING_DISC_ENTITY_ID,
            EntityType.Builder.<FlyingDiscEntity>create(FlyingDiscEntity::new, SpawnGroup.MISC)
                    .dimensions(0.25F, 0.25F)
                    .maxTrackingRange(4)
                    .trackingTickInterval(10)
                    .build(FLYING_DISC_ENTITY_KEY)
    );
    public static void init(){
        /*try {
            Method registerMethod = EntityRenderers.class.getDeclaredMethod("register", EntityType.class, EntityRendererFactory.class);
            registerMethod.setAccessible(true);

            registerMethod.invoke(null, MusicDiscManager.FLYING_DISC_ENTITY, (EntityRendererFactory<FlyingDiscEntity>) FlyingItemEntityRenderer::new);

            Lycoris.LOGGER.info("Successfully registered FlyingDiscEntity renderer using reflection");
        } catch (Exception e) {
            Lycoris.LOGGER.error("Failed to register FlyingDiscEntity renderer", e);
            throw new RuntimeException("Renderer registration failed", e);
        }*/
        IEntityRenderers.getRENDERER_FACTORIES().put(MusicDiscManager.FLYING_DISC_ENTITY, (EntityRendererFactory<FlyingDiscEntity>)FlyingItemEntityRenderer::new);
    }
    private static final Set<Item> MUSIC_DISCS = Set.of(
            Items.MUSIC_DISC_13,
            Items.MUSIC_DISC_CREATOR,
            Items.MUSIC_DISC_CREATOR_MUSIC_BOX,
            Items.MUSIC_DISC_PRECIPICE,
            Items.MUSIC_DISC_CAT,
            Items.MUSIC_DISC_BLOCKS,
            Items.MUSIC_DISC_CHIRP,
            Items.MUSIC_DISC_FAR,
            Items.MUSIC_DISC_MALL,
            Items.MUSIC_DISC_MELLOHI,
            Items.MUSIC_DISC_STAL,
            Items.MUSIC_DISC_STRAD,
            Items.MUSIC_DISC_WARD,
            Items.MUSIC_DISC_11,
            Items.MUSIC_DISC_WAIT,
            Items.MUSIC_DISC_OTHERSIDE,
            Items.MUSIC_DISC_5,
            Items.MUSIC_DISC_PIGSTEP,
            Items.MUSIC_DISC_RELIC
    );

    public static boolean isMusicDisc(ItemStack stack) {
        if (stack == null || stack.isEmpty()) {
            return false;
        }
        return MUSIC_DISCS.contains(stack.getItem());
    }
}
