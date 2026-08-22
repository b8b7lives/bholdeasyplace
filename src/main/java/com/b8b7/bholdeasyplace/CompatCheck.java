package com.b8b7.bholdeasyplace;

import fi.dy.masa.litematica.config.Configs;
import fi.dy.masa.litematica.config.Hotkeys;

/**
 * Fail soft. With defaultRequire 0 a mixin missing its target on a
 * changed litematica leaves features dead instead of crashing; this
 * detects the dead state once and says so in the log.
 */
public final class CompatCheck {
    private static boolean checked;
    private static boolean ok;

    private CompatCheck() {}

    public static boolean ok() {
        if (!checked) {
            checked = true;
            try {
                ok = Configs.Generic.OPTIONS.contains(ConfigsExt.HOLD_EASY_PLACE)
                        && Hotkeys.HOTKEY_LIST.contains(ConfigsExt.HOLD_EASY_PLACE_TOGGLE);
            } catch (Throwable t) {
                ok = false;
            }
            if (!ok) {
                HoldEasyPlaceClient.LOGGER.warn(
                        "bhold: litematica config injection did not apply, hold easy place disabled (incompatible litematica version?)");
            }
        }
        return ok;
    }
}
