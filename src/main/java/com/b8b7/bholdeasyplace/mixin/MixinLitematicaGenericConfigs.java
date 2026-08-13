package com.b8b7.bholdeasyplace.mixin;

import com.b8b7.bholdeasyplace.ConfigsExt;
import com.google.common.collect.ImmutableList;
import fi.dy.masa.litematica.config.Configs;
import fi.dy.masa.malilib.config.IConfigBase;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

// Shadow-initializer reassign: merged <clinit> appends our entries before any
// reader touches OPTIONS. Same pattern as upstream Hold-Easy-Place and
// litematica-printer.
@Mixin(value = Configs.Generic.class, remap = false)
public class MixinLitematicaGenericConfigs {
    @Mutable
    @Final
    @Shadow
    public static ImmutableList<IConfigBase> OPTIONS = new ImmutableList.Builder<IConfigBase>()
            .addAll(ConfigsExt.EXTENDED_OPTIONS)
            .addAll(Configs.Generic.OPTIONS)
            .build();
}
