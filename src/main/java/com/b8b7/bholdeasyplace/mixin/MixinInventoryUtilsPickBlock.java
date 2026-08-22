package com.b8b7.bholdeasyplace.mixin;

import com.b8b7.bholdeasyplace.ConfigsExt;
import com.b8b7.bholdeasyplace.ItemMatch;
import fi.dy.masa.litematica.util.InventoryUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Two inventory-safety rules for custom-item servers (#1). Skip pick-block
 * entirely when what is held already satisfies the target, so litematica
 * never switches away from a held custom item toward a plain stack. Never
 * pull a material into a hotbar slot occupied by a custom-component stack,
 * so a server-side consume-restore window cannot collide with a swap.
 * require = 0: failure keeps stock behavior.
 */
@Mixin(value = InventoryUtils.class, remap = false)
public class MixinInventoryUtilsPickBlock {
    @Inject(method = "schematicWorldPickBlock", at = @At("HEAD"), cancellable = true, require = 0)
    private static void bhold$skipWhenHeldSatisfies(ItemStack stack, BlockPos pos, Level world,
            Minecraft mc, CallbackInfo ci) {
        try {
            if (ConfigsExt.SKIP_PICK_WHEN_HELD.getBooleanValue()
                    && mc.player != null && stack != null && !stack.isEmpty()
                    && ItemMatch.isPlain(stack)
                    && ItemMatch.lenientHand(mc.player, stack) != null) {
                ci.cancel();
            }
        } catch (Throwable ignored) {
        }
    }

    @Inject(method = "canPickToSlot", at = @At("HEAD"), cancellable = true, require = 0)
    private static void bhold$protectCustomStacks(Inventory inventory, int slotNum,
            CallbackInfoReturnable<Boolean> cir) {
        try {
            if (ConfigsExt.PROTECT_CUSTOM_STACKS.getBooleanValue()
                    && ItemMatch.hasCustomComponents(inventory.getItem(slotNum))) {
                cir.setReturnValue(false);
            }
        } catch (Throwable ignored) {
        }
    }
}
