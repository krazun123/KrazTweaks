package me.krazun.project.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import me.krazun.project.KrazTweaks;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.Cancellable;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Mixin(AbstractContainerScreen.class)
public abstract class AbstractContainerScreenMixin {

    @Shadow
    @Nullable
    protected Slot hoveredSlot;

    @Shadow
    protected abstract List<Component> getTooltipFromContainerItem(ItemStack itemStack);

    @Inject(method = "renderSlotHighlightBack", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;blitSprite(Ljava/util/function/Function;Lnet/minecraft/resources/ResourceLocation;IIII)V"), cancellable = true)
    public void kraztweaks$renderSlotHighlightBack$cancelSlotHighlightingWhenTooltipIsEmpty(GuiGraphics guiGraphics, CallbackInfo ci) {
        final var cancelSlotHighlightingWhenTooltipIsEmpty = KrazTweaks.CONFIG.configInstance().visualCategory.inventoryCategory.cancelSlotHighlightingWhenTooltipIsEmpty;

        if (cancelSlotHighlightingWhenTooltipIsEmpty) {
            kraztweaks$cancelClicksOnSlotWhenTooltipIsEmpty(ci);
        }
    }

    @Inject(method = "renderSlotHighlightFront", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;blitSprite(Ljava/util/function/Function;Lnet/minecraft/resources/ResourceLocation;IIII)V"), cancellable = true)
    public void kraztweaks$renderSlotHighlightFront$cancelSlotHighlightingWhenTooltipIsEmpty(GuiGraphics guiGraphics, CallbackInfo ci) {
        final var cancelSlotHighlightingWhenTooltipIsEmpty = KrazTweaks.CONFIG.configInstance().visualCategory.inventoryCategory.cancelSlotHighlightingWhenTooltipIsEmpty;

        if (cancelSlotHighlightingWhenTooltipIsEmpty) {
            kraztweaks$cancelClicksOnSlotWhenTooltipIsEmpty(ci);
        }
    }

    @Inject(method = "mouseClicked", at = @At(value = "HEAD"), cancellable = true)
    public void kraztweaks$mouseClicked$cancelClicksOnSlotWhenTooltipIsEmpty(double mouseX, double mouseY, int button, CallbackInfoReturnable<Boolean> cir) {
        final var cancelClicksOnSlotWhenTooltipIsEmpty = KrazTweaks.CONFIG.configInstance().visualCategory.inventoryCategory.cancelClicksOnSlotWhenTooltipIsEmpty;

        if (cancelClicksOnSlotWhenTooltipIsEmpty) {
            kraztweaks$cancelClicksOnSlotWhenTooltipIsEmpty(cir);
        }
    }

    @Inject(method = "keyPressed", at = @At("HEAD"), cancellable = true)
    public void kraztweaks$keyPressed$cancelClicksOnSlotWhenTooltipIsEmpty(int keyCode, int scanCode, int modifiers, CallbackInfoReturnable<Boolean> cir) {
        final var cancelClicksOnSlotWhenTooltipIsEmpty = KrazTweaks.CONFIG.configInstance().visualCategory.inventoryCategory.cancelClicksOnSlotWhenTooltipIsEmpty;

        if (cancelClicksOnSlotWhenTooltipIsEmpty) {
            kraztweaks$cancelClicksOnSlotWhenTooltipIsEmpty(cir);
        }
    }

    @ModifyReturnValue(method = "getTooltipFromContainerItem", at = @At("RETURN"))
    public List<Component> kraztweaks$getTooltipFromContainerItem$hideEnchantmentDescriptions(List<Component> original) {
        final boolean hideEnchantmentDescriptions = KrazTweaks.CONFIG.configInstance()
                .hypixelCategory
                .hypixelSkyBlockCategory
                .hideEnchantmentDescriptions;

        if (hideEnchantmentDescriptions) {
            final var modifiedList = new ArrayList<>(original);
            final var colorCodePattern = Pattern.compile("§[0-9a-fklmnor]");
            final var enchantmentPattern = Pattern.compile("^.+\\s(I|II|III|IV|V|VI|VII|VIII|IX|X)$");

            for (int i = 0; i < modifiedList.size() - 1; ) {
                final var component = modifiedList.get(i);
                final var color = component.getStyle().getColor();

                if (color != null && color.getValue() == 0xAAAAAA) {
                    modifiedList.remove(i);
                    continue;
                }

                final var rawString = colorCodePattern.matcher(component.getString()).replaceAll("");

                if (enchantmentPattern.matcher(rawString).matches()) {
                    int endIndex = -1;

                    for (int j = i + 1; j < modifiedList.size(); j++) {
                        final var nextRaw = colorCodePattern.matcher(modifiedList.get(j).getString()).replaceAll("");
                        if (nextRaw.isBlank() || enchantmentPattern.matcher(nextRaw).matches()) {
                            endIndex = j;
                            break;
                        }
                    }
                    if (endIndex > i + 1) {
                        modifiedList.subList(i + 1, endIndex).clear();
                    }
                }
                i++;
            }
            return modifiedList;
        }
        return original;
    }

    @Unique
    public void kraztweaks$cancelClicksOnSlotWhenTooltipIsEmpty(Cancellable cancellable) {
        if (hoveredSlot == null) return;

        final var itemStack = hoveredSlot.getItem();
        final var tooltipList = getTooltipFromContainerItem(itemStack);

        if (tooltipList.isEmpty()) {
            cancellable.cancel();
        } else if (tooltipList.getFirst().getString().isBlank()) {
            cancellable.cancel();
        }
    }
}