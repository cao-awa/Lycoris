package com.github.cao.awa.lycoris.mixin.client.button;

import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ButtonWidget.Builder.class)
public interface ButtonBuilderAccessor {
    @Accessor("message")
    Text getButtonMessage();
}
