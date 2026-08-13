package com.b8b7.bholdeasyplace.mixin;

import fi.dy.masa.litematica.util.WorldUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.world.InteractionResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(value = WorldUtils.class, remap = false)
public interface WorldUtilsInvoker {
    @Invoker("doEasyPlaceAction")
    static InteractionResult invokeDoEasyPlaceAction(Minecraft mc) {
        throw new AssertionError();
    }
}
