package me.krazun.project.mixin;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.mojang.authlib.minecraft.MinecraftProfileTextures;
import com.mojang.authlib.minecraft.MinecraftSessionService;
import com.mojang.authlib.properties.Property;
import net.minecraft.client.texture.PlayerSkinProvider;
import net.minecraft.client.util.SkinTextures;
import net.minecraft.util.Util;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.nio.file.Path;
import java.time.Duration;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@Mixin(value = PlayerSkinProvider.class, remap = false)
public abstract class PlayerSkinProviderMixin {

    @Shadow
    @Final
    private MinecraftSessionService sessionService;

    @Shadow
    abstract CompletableFuture<SkinTextures> fetchSkinTextures(UUID uuid, MinecraftProfileTextures textures);

    @Unique
    private Executor executor;

    @Inject(method = "<init>", at = @At("CTOR_HEAD"))
    public void kraztweaks$init$fetchExecutor(Path directory, MinecraftSessionService sessionService, Executor executor, CallbackInfo ci) {
        this.executor = executor;
    }

    @Redirect(method = "<init>", at = @At(value = "INVOKE", target = "Lcom/google/common/cache/CacheBuilder;build(Lcom/google/common/cache/CacheLoader;)Lcom/google/common/cache/LoadingCache;"))
    public LoadingCache<PlayerSkinProvider.Key, CompletableFuture<Optional<SkinTextures>>> kraztweaks$init$ignoreSignatureErrors
            (CacheBuilder<?, ?> instance,
             CacheLoader<
                     PlayerSkinProvider.Key,
                     CompletableFuture<Optional<SkinTextures>>
                     > loader)
    {
        return CacheBuilder.newBuilder().expireAfterAccess(Duration.ofSeconds(15L)).build(new CacheLoader<>() {
            @Override
            public @NotNull CompletableFuture<Optional<SkinTextures>> load(@NotNull PlayerSkinProvider.Key key) {
                return CompletableFuture.supplyAsync(() -> {
                    Property property = key.packedTextures();
                    if (property == null) {
                        return MinecraftProfileTextures.EMPTY;
                    }

                    return sessionService.unpackTextures(property);

                    }, Util.getMainWorkerExecutor().named("unpackSkinTextures"))
                        .thenComposeAsync(textures ->
                                fetchSkinTextures(
                                        key.profileId(),
                                        textures),
                                executor)
                        .handle((skinTextures, throwable) -> {
                            if (throwable != null) {
                                PlayerSkinProvider.LOGGER.warn("Failed to load texture for profile {}", key.profileId(), throwable);
                            }
                            return Optional.ofNullable(skinTextures);
                        });
            }
        });
    }
}