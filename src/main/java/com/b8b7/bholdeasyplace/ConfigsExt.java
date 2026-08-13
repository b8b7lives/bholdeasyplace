package com.b8b7.bholdeasyplace;

import com.google.common.collect.ImmutableList;
import fi.dy.masa.malilib.config.IConfigBase;
import fi.dy.masa.malilib.config.options.ConfigBoolean;
import fi.dy.masa.malilib.config.options.ConfigHotkey;

public class ConfigsExt {
    public static final ConfigBoolean HOLD_EASY_PLACE =
            (ConfigBoolean) new ConfigBoolean("holdEasyPlace", false).apply("bholdeasyplace.generic");

    public static final ConfigHotkey HOLD_EASY_PLACE_TOGGLE =
            (ConfigHotkey) new ConfigHotkey("holdEasyPlaceToggle", "").apply("bholdeasyplace.hotkey");

    public static final ImmutableList<IConfigBase> EXTENDED_OPTIONS = ImmutableList.of(
            HOLD_EASY_PLACE
    );

    public static final ImmutableList<ConfigHotkey> EXTENDED_HOTKEYS = ImmutableList.of(
            HOLD_EASY_PLACE_TOGGLE
    );
}
