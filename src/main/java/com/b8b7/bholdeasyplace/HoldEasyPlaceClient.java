package com.b8b7.bholdeasyplace;

import com.b8b7.bholdeasyplace.mixin.WorldUtilsInvoker;
import fi.dy.masa.litematica.config.Configs;
import fi.dy.masa.litematica.config.Hotkeys;
import fi.dy.masa.litematica.data.DataManager;
import fi.dy.masa.litematica.tool.ToolMode;
import fi.dy.masa.litematica.util.EasyPlaceUtils;
import fi.dy.masa.malilib.hotkeys.KeyCallbackToggleBooleanConfigWithMessage;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.Minecraft;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HoldEasyPlaceClient implements ClientModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("bholdeasyplace");

    @Override
    public void onInitializeClient() {
        ConfigsExt.HOLD_EASY_PLACE_TOGGLE.getKeybind()
                .setCallback(new KeyCallbackToggleBooleanConfigWithMessage(ConfigsExt.HOLD_EASY_PLACE));

        ClientTickEvents.END_CLIENT_TICK.register(HoldEasyPlaceClient::onEndTick);
        // separate registration: the watchdog must run with screens open,
        // which onEndTick's early return excludes
        ClientTickEvents.END_CLIENT_TICK.register(Watchdog::clientTick);

        if (Boolean.getBoolean("bholdeasyplace.loadcheck")) {
            LOGGER.info("bholdeasyplace loadcheck PASS");
        }
    }

    private static boolean tickFailed = false;

    private static void onEndTick(Minecraft mc) {
        if (tickFailed || !CompatCheck.ok()) {
            return;
        }
        try {
            tickBody(mc);
        } catch (Throwable t) {
            tickFailed = true;
            LOGGER.warn("bhold: tick handler failed, hold easy place disabled this session", t);
        }
    }

    private static void tickBody(Minecraft mc) {
        if (mc.player == null || mc.gui.screen() != null) {
            return;
        }
        if (!ConfigsExt.HOLD_EASY_PLACE.getBooleanValue()
                || !Configs.Generic.EASY_PLACE_MODE.getBooleanValue()
                || DataManager.getToolMode() == ToolMode.REBUILD) {
            return;
        }
        // Key physically held: litematica's own paths already run this tick.
        if (Hotkeys.EASY_PLACE_ACTIVATION.getKeybind().isKeybindHeld()) {
            return;
        }

        if (Configs.Generic.EASY_PLACE_POST_REWRITE.getBooleanValue()) {
            // Post-rewrite guard set minus the key-held check; fail tips only
            // fire on real first clicks, so tick calls stay quiet.
            EasyPlaceUtils.handleEasyPlaceWithMessage();
        } else {
            // Legacy path: bypass handleEasyPlace's per-call fail message.
            WorldUtilsInvoker.invokeDoEasyPlaceAction(mc);
        }
    }
}
