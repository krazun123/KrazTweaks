package me.krazun.project.mixin;

import com.mojang.authlib.yggdrasil.ServicesKeySet;
import com.mojang.authlib.yggdrasil.ServicesKeyType;
import me.krazun.project.KrazTweaks;
import net.minecraft.util.SignatureValidator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;

@Mixin(SignatureValidator.class)
public interface SignatureValidatorMixin {

    @Inject(method = "from(Ljava/security/PublicKey;Ljava/lang/String;)Lnet/minecraft/util/SignatureValidator;",
            at = @At("RETURN"), cancellable = true)
    private static void kraztweaks$from$ignoreSignatureErrors(PublicKey publicKey, String algorithm, CallbackInfoReturnable<SignatureValidator> cir) {
        final var ignoreSignatureErrors = KrazTweaks.CONFIG.configInstance().miscCategory.ignoreSignatureErrors;

        if (ignoreSignatureErrors) {
            cir.setReturnValue((updatable, signatureData) -> {
                try {
                    Signature signature = Signature.getInstance(algorithm);
                    signature.initVerify(publicKey);
                    return SignatureValidator.verifySignature(updatable, signatureData, signature);
                } catch (Exception exception) {
                    return false;
                }
            });
        }
    }

    @Inject(method = "from(Lcom/mojang/authlib/yggdrasil/ServicesKeySet;Lcom/mojang/authlib/yggdrasil/ServicesKeyType;)Lnet/minecraft/util/SignatureValidator;",
            at = @At("RETURN"), cancellable = true)
    private static void kraztweaks$from$ignoreSignatureErrors(ServicesKeySet servicesKeySet,
                                                              ServicesKeyType servicesKeyType,
                                                              CallbackInfoReturnable<SignatureValidator> cir) {
        final var ignoreSignatureErrors = KrazTweaks.CONFIG.configInstance().miscCategory.ignoreSignatureErrors;

        if (ignoreSignatureErrors) {
            final var collection = servicesKeySet.keys(servicesKeyType);

            cir.setReturnValue((updatable, signatureData) -> collection.stream().anyMatch(keyInfo -> {
                Signature signature = keyInfo.signature();
                try {
                    return SignatureValidator.verifySignature(updatable, signatureData, signature);
                } catch (SignatureException signatureException) {
                    return false;
                }
            }));
        }
    }
}