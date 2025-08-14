package me.krazun.project.mixin;

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

import java.util.List;

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