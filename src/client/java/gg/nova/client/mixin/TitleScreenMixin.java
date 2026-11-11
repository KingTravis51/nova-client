package gg.nova.client.mixin;

import gg.nova.client.NovaTitleScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.TitleScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TitleScreen.class)
public abstract class TitleScreenMixin {
    @Inject(method = "init", at = @At("HEAD"), cancellable = true)
    private void nova$replaceVanillaTitleMenu(CallbackInfo ci) {
        ci.cancel();
        Minecraft.getInstance().setScreen(new NovaTitleScreen());
    }
}
