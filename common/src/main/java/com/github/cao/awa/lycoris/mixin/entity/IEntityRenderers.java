package com.github.cao.awa.lycoris.mixin.entity;

import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.EntityRenderers;
import net.minecraft.entity.EntityType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;

@Mixin(EntityRenderers.class)
public interface IEntityRenderers {
    /**
     * Get the renderer factories map.
     *
     * @author Ryan100c
     * @author cao_awa
     *
     * @return the factories map
     */
    @Accessor("RENDERER_FACTORIES")
    static Map<EntityType<?>, EntityRendererFactory<?>> getRendererFactories() {
        throw new AssertionError();
    }
}