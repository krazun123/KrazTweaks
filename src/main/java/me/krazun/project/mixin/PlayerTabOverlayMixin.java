package me.krazun.project.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import me.krazun.project.KrazTweaks;
import me.krazun.project.utils.MiscUtils;
import net.minecraft.client.gui.components.PlayerTabOverlay;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import java.util.List;

@Mixin(PlayerTabOverlay.class)
public abstract class PlayerTabOverlayMixin {

    @Shadow
    public abstract Component getNameForDisplay(PlayerInfo playerInfo);

    @ModifyReturnValue(method = "getPlayerInfos", at = @At("RETURN"))
    public List<PlayerInfo> kraztweaks$getPlayerInfos$hideNPCSFromTab(List<PlayerInfo> original) {
        if (MiscUtils.isOnServer("hypixel")) {
            final var hideNPCSFromTab = KrazTweaks.CONFIG.configInstance().hypixelCategory.hideNPCSFromTab;

            if (hideNPCSFromTab) {
                final var modifiedList = original
                        .stream()
                        .filter(playerInfo ->
                                !getNameForDisplay(playerInfo).getString().contains("[NPC]"))
                        .toList();
                return modifiedList;
            }
        }

        return original;
    }
}