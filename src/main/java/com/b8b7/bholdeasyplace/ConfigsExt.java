package com.b8b7.bholdeasyplace;

import com.google.common.collect.ImmutableList;
import fi.dy.masa.malilib.config.IConfigBase;
import fi.dy.masa.malilib.config.options.ConfigBoolean;
import fi.dy.masa.malilib.config.options.ConfigHotkey;

public class ConfigsExt {
    public static final ConfigBoolean HOLD_EASY_PLACE =
            (ConfigBoolean) new ConfigBoolean("holdEasyPlace", false).apply("bholdeasyplace.generic");

    public static final ConfigBoolean LENIENT_ITEM_MATCH =
            (ConfigBoolean) new ConfigBoolean("lenientItemMatch", true).apply("bholdeasyplace.generic");

    public static final ConfigBoolean SKIP_PICK_WHEN_HELD =
            (ConfigBoolean) new ConfigBoolean("skipPickBlockWhenHeld", true).apply("bholdeasyplace.generic");

    public static final ConfigBoolean PROTECT_CUSTOM_STACKS =
            (ConfigBoolean) new ConfigBoolean("protectCustomStacks", true).apply("bholdeasyplace.generic");

    public static final ConfigBoolean LOSS_WATCHDOG =
            (ConfigBoolean) new ConfigBoolean("customItemLossWatchdog", true).apply("bholdeasyplace.generic");

    public static final ConfigHotkey HOLD_EASY_PLACE_TOGGLE =
            (ConfigHotkey) new ConfigHotkey("holdEasyPlaceToggle", "").apply("bholdeasyplace.hotkey");

    public static final ImmutableList<IConfigBase> EXTENDED_OPTIONS = ImmutableList.of(
            HOLD_EASY_PLACE,
            LENIENT_ITEM_MATCH,
            SKIP_PICK_WHEN_HELD,
            PROTECT_CUSTOM_STACKS,
            LOSS_WATCHDOG
    );

    public static final ImmutableList<ConfigHotkey> EXTENDED_HOTKEYS = ImmutableList.of(
            HOLD_EASY_PLACE_TOGGLE
    );
}
