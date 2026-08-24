package com.b8b7.bholdeasyplace;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

/**
 * Type-only fallbacks for litematica's component-strict item checks.
 * A required stack derived from a plain block state carries no component
 * patch; schematic block-entity data (shulker contents, banner patterns)
 * produces one, and those comparisons stay strict.
 */
public final class ItemMatch {
    private ItemMatch() {}

    public static boolean isPlain(ItemStack stack) {
        try {
            return stack.getComponentsPatch().isEmpty();
        } catch (Throwable t) {
            // unknown stack shape: keep strict behavior
            return false;
        }
    }

    public static boolean hasCustomComponents(ItemStack stack) {
        return stack != null && !stack.isEmpty() && !isPlain(stack);
    }

    /** Mirrors EntityUtils' lenient two-arg overload: type only, offhand only when main is empty. */
    public static InteractionHand lenientHand(LivingEntity entity, ItemStack required) {
        if (ItemStack.isSameItem(entity.getMainHandItem(), required)) {
            return InteractionHand.MAIN_HAND;
        }
        if (entity.getMainHandItem().isEmpty()
                && ItemStack.isSameItem(entity.getOffhandItem(), required)) {
            return InteractionHand.OFF_HAND;
        }
        return null;
    }
}
