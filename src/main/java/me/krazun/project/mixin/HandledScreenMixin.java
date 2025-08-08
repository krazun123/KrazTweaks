package me.krazun.project.mixin;

import me.krazun.project.KrazTweaks;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(HandledScreen.class)
public abstract class HandledScreenMixin {

    @Shadow
    @Nullable
    protected Slot focusedSlot;

    @Shadow
    protected abstract List<Text> getTooltipFromItem(ItemStack stack);

    @Inject(method = "drawSlotHighlightBack", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawGuiTexture(Ljava/util/function/Function;Lnet/minecraft/util/Identifier;IIII)V"), cancellable = true)
    public void kraztweaks$drawSlotHighlightBack$hideSlotHighlightingWhenTooltipIsEmpty(DrawContext context, CallbackInfo ci) {
        final var hideSlotHighlightingWhenTooltipIsEmpty = KrazTweaks.CONFIG.configInstance().visualCategory.inventoryCategory.hideSlotHighlightingWhenTooltipIsEmpty;

        if(hideSlotHighlightingWhenTooltipIsEmpty) {
            if(focusedSlot == null) return;
            final var itemStack = focusedSlot.getStack();
            final var tooltipList = getTooltipFromItem(itemStack);

            if(tooltipList.isEmpty()) {
                ci.cancel();
            } else {
                if(tooltipList.getFirst().getString().isBlank()) {
                    ci.cancel();
                }
            }
        }
    }

    @Inject(method = "drawSlotHighlightFront", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawGuiTexture(Ljava/util/function/Function;Lnet/minecraft/util/Identifier;IIII)V"), cancellable = true)
    public void kraztweaks$drawSlotHighlightFront$hideSlotHighlightingWhenTooltipIsEmpty(DrawContext context, CallbackInfo ci) {
        final var hideSlotHighlightingWhenTooltipIsEmpty = KrazTweaks.CONFIG.configInstance().visualCategory.inventoryCategory.hideSlotHighlightingWhenTooltipIsEmpty;

        if(hideSlotHighlightingWhenTooltipIsEmpty) {
            if(focusedSlot == null) return;
            final var itemStack = focusedSlot.getStack();
            final var tooltipList = getTooltipFromItem(itemStack);

            if(tooltipList.isEmpty()) {
                ci.cancel();
            } else {
                if(tooltipList.getFirst().getString().isBlank()) {
                    ci.cancel();
                }
            }
        }
    }
}