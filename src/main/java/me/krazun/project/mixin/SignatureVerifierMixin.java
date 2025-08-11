package me.krazun.project.mixin;

import com.mojang.authlib.yggdrasil.ServicesKeyInfo;
import com.mojang.authlib.yggdrasil.ServicesKeySet;
import com.mojang.authlib.yggdrasil.ServicesKeyType;
import me.krazun.project.KrazTweaks;
import net.minecraft.network.encryption.SignatureVerifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;

@Mixin(SignatureVerifier.class)
public interface SignatureVerifierMixin {

    @Inject(method = "create(Ljava/security/PublicKey;Ljava/lang/String;)Lnet/minecraft/network/encryption/SignatureVerifier;",
            at = @At("RETURN"), cancellable = true)
    private static void kraztweaks$create$ignoreSignatureErrors(PublicKey publicKey, String algorithm, CallbackInfoReturnable<SignatureVerifier> cir) {
        final var ignoreSignatureErrors = KrazTweaks.CONFIG.configInstance().miscCategory.ignoreSignatureErrors;

        if(ignoreSignatureErrors) {
            cir.setReturnValue((updatable, signatureData) -> {
                try {
                    Signature signature = Signature.getInstance(algorithm);
                    signature.initVerify(publicKey);
                    return SignatureVerifier.verify(updatable, signatureData, signature);
                } catch (Exception exception) {
                    return false;
                }
            });
        }
    }

    @Inject(method = "create(Lcom/mojang/authlib/yggdrasil/ServicesKeySet;Lcom/mojang/authlib/yggdrasil/ServicesKeyType;)Lnet/minecraft/network/encryption/SignatureVerifier;",
            at = @At("RETURN"), cancellable = true)
    private static void kraztweaks$create$ignoreSignatureErrors(ServicesKeySet servicesKeySet,
                                                                ServicesKeyType servicesKeyType,
                                                                CallbackInfoReturnable<SignatureVerifier> cir)
    {
        final var ignoreSignatureErrors = KrazTweaks.CONFIG.configInstance().miscCategory.ignoreSignatureErrors;

        if(ignoreSignatureErrors) {
            final var collection = servicesKeySet.keys(servicesKeyType);

            cir.setReturnValue((updatable, signatureData) -> collection.stream().anyMatch(keyInfo -> {
                Signature signature = keyInfo.signature();
                try {
                    return SignatureVerifier.verify(updatable, signatureData, signature);
                } catch (SignatureException signatureException) {
                    return false;
                }
            }));
        }
    }
}