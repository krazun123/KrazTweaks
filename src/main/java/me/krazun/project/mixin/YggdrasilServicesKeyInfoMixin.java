package me.krazun.project.mixin;

import com.mojang.authlib.properties.Property;
import com.mojang.authlib.yggdrasil.YggdrasilServicesKeyInfo;
import me.krazun.project.KrazTweaks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = YggdrasilServicesKeyInfo.class, remap = false)
public class YggdrasilServicesKeyInfoMixin {

    @Inject(method = "validateProperty",
            at = @At(value = "INVOKE", target = "Lorg/slf4j/Logger;error(Ljava/lang/String;Ljava/lang/Object;Ljava/lang/Object;)V", ordinal = 1), cancellable = true)
    public void kraztweaks$validateProperty$ignoreSignatureErrors(Property property,
                                                                  CallbackInfoReturnable<Boolean> cir)
    {
        final var ignoreSignatureErrors = KrazTweaks.CONFIG.configInstance().miscCategory.ignoreSignatureErrors;

        if(ignoreSignatureErrors) {
            cir.setReturnValue(false);
        }
    }
}