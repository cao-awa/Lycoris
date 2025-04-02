package com.github.cao.awa.lycoris.mixin.client.title;

import com.github.cao.awa.lycoris.config.LycorisConfig;
import com.github.cao.awa.lycoris.mixin.client.button.ButtonBuilderAccessor;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableTextContent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(TitleScreen.class)
public class TitleScreenMixin extends Screen {
    private int quitButtonY = 0;

    protected TitleScreenMixin(Text title) {
        super(title);
    }

    @WrapOperation(
            method = "init",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/widget/ButtonWidget$Builder;dimensions(IIII)Lnet/minecraft/client/gui/widget/ButtonWidget$Builder;"
            )
    )
    protected ButtonWidget.Builder initQuit(ButtonWidget.Builder instance, int x, int y, int width, int height, Operation<ButtonWidget.Builder> original) {
        if (LycorisConfig.titleScreenSwapQuitGameAndSingleplayerButtons) {
            if (((ButtonBuilderAccessor) instance).getButtonMessage() instanceof MutableText mutableText) {
                if (mutableText.getContent() instanceof TranslatableTextContent translatable) {
                    // Swap button position of quit game and single play.
                    if (translatable.getKey().equals("menu.quit")) {
                        y = this.height / 4 + 48;

                        return instance.dimensions(
                                this.width / 2 - 100, y, 200, 20
                        );
                    }
                }
            }
        }
        return original.call(instance, x, y, width, height);
    }

    @WrapOperation(
            method = "addNormalWidgets",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/widget/ButtonWidget$Builder;dimensions(IIII)Lnet/minecraft/client/gui/widget/ButtonWidget$Builder;"
            )
    )
    protected ButtonWidget.Builder initSinglePlay(ButtonWidget.Builder instance, int x, int y, int width, int height, Operation<ButtonWidget.Builder> original) {
        if (LycorisConfig.titleScreenSwapQuitGameAndSingleplayerButtons) {
            if (((ButtonBuilderAccessor) instance).getButtonMessage() instanceof MutableText mutableText) {
                if (mutableText.getContent() instanceof TranslatableTextContent translatable) {
                    // Swap button position of quit game and single play.
                    if (translatable.getKey().equals("menu.singleplayer")) {
                        y = this.height / 4 + 48;

                        return instance.dimensions(
                                this.width / 2 + 2, y + 36 + 36 + 12, 98, 20
                        );
                    }
                }
            }
        }
        return original.call(instance, x, y, width, height);
    }
}
