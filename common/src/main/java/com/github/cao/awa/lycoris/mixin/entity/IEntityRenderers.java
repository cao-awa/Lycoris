package com.github.cao.awa.lycoris.mixin.entity;

import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.EntityRenderers;
import net.minecraft.entity.EntityType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;

@Mixin(EntityRenderers.class)
public interface IEntityRenderers {
    @Accessor
    static Map<EntityType<?>, EntityRendererFactory<?>> getRENDERER_FACTORIES() {
        throw new AssertionError();
    }
}