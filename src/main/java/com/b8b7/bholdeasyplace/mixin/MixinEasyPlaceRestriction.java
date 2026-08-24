package com.b8b7.bholdeasyplace.mixin;

import com.b8b7.bholdeasyplace.ConfigsExt;
import com.b8b7.bholdeasyplace.ItemMatch;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import fi.dy.masa.litematica.util.EasyPlaceUtils;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

/**
 * Easy place's misplacement guard demands full component equality on the
 * held item while the rest of easy place matches by type; custom-component
 * server items (base item + name/lore/data) fail it and every vanilla-hit
 * click cancels. When the required stack is a plain block item, fall
 * back to the same type-only match the direct path uses. Component-bearing
 * required stacks stay strict. require = 0: failure keeps stock behavior.
 */
@Mixin(value = EasyPlaceUtils.class, remap = false)
public class MixinEasyPlaceRestriction {
    @WrapOperation(method = {"placementRestrictionInEffect()Z",
            "placementRestrictionInEffect(Lnet/minecraft/client/Minecraft;)Z"},
            at = @At(value = "INVOKE",
                    target = "Lfi/dy/masa/litematica/util/EntityUtils;getUsedHandForItem(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/item/ItemStack;Z)Lnet/minecraft/world/InteractionHand;"),
            require = 0)
    private static InteractionHand bhold$lenientRestrictionHand(LivingEntity entity, ItemStack required,
            boolean matchComponents, Operation<InteractionHand> original) {
        InteractionHand hand = original.call(entity, required, matchComponents);
        if (hand == null && ConfigsExt.LENIENT_ITEM_MATCH.getBooleanValue() && ItemMatch.isPlain(required)) {
            hand = ItemMatch.lenientHand(entity, required);
        }
        return hand;
    }
}
