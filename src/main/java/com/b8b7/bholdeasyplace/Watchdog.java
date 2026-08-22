package com.b8b7.bholdeasyplace;

import fi.dy.masa.malilib.util.InfoUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Loss detection by conservation, not identity. Server custom items
 * (infinite blocks, cyclers) mutate their components on every restore
 * and cycle, so exact-stack tracking false-alarms constantly. Instead:
 * the TOTAL count of custom-component stacks must be conserved. A drop
 * outside a container screen that does not recover within the grace
 * window fires one timestamped warning naming the last vanished item.
 * Detection only; the loss itself is server-side (#1).
 */
public final class Watchdog {
    private static final int GRACE_TICKS = 40;
    private static final int SCREEN_QUIET_TICKS = 40;
    private static final int WARN_INTERVAL_TICKS = 600;

    private static long tick = 0;
    private static long lastScreenTick = Long.MIN_VALUE;
    private static long lastWarnTick = Long.MIN_VALUE;
    private static int lastTotal = -1;
    private static String lastSeenName = "unknown item";
    private static long pendingSince = -1;
    private static int pendingExpected = 0;
    private static boolean warnedThisSession = false;

    private Watchdog() {}

    public static void clientTick(Minecraft mc) {
        try {
            tick++;
            if (!ConfigsExt.LOSS_WATCHDOG.getBooleanValue() || mc.player == null || mc.level == null) {
                reset();
                return;
            }
            if (mc.gui.screen() != null) {
                lastScreenTick = tick;
                // deliberate rearranging: any pending verdict is ambiguous
                pendingSince = -1;
            }

            int total = 0;
            Inventory inv = mc.player.getInventory();
            for (int i = 0; i < inv.getContainerSize(); i++) {
                ItemStack stack = inv.getItem(i);
                if (ItemMatch.hasCustomComponents(stack) && !stack.has(DataComponents.CONSUMABLE)) {
                    total += stack.getCount();
                    lastSeenName = stack.getHoverName().getString();
                }
            }

            boolean screenQuiet = tick - lastScreenTick > SCREEN_QUIET_TICKS;
            if (lastTotal >= 0 && total < lastTotal && screenQuiet && pendingSince < 0) {
                pendingSince = tick;
                pendingExpected = lastTotal;
            }

            if (pendingSince >= 0) {
                if (total >= pendingExpected) {
                    pendingSince = -1;
                } else if (tick - pendingSince > GRACE_TICKS) {
                    if (tick - lastWarnTick >= WARN_INTERVAL_TICKS) {
                        lastWarnTick = tick;
                        fire(pendingExpected, total);
                    }
                    pendingSince = -1;
                }
            }

            lastTotal = total;
        } catch (Throwable t) {
            if (!warnedThisSession) {
                warnedThisSession = true;
                HoldEasyPlaceClient.LOGGER.warn("bhold: loss watchdog failed, detection disabled this session", t);
            }
            reset();
        }
    }

    private static void fire(int expected, int now) {
        String stamp = ZonedDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss zzz"));
        HoldEasyPlaceClient.LOGGER.warn(
                "bhold watchdog: custom item total dropped {} -> {} and did not recover within 2s at {} (last seen '{}') - possible custom item loss",
                expected, now, stamp, lastSeenName);
        InfoUtils.printActionbarMessage("bholdeasyplace.message.watchdog_loss", lastSeenName, stamp);
    }

    private static void reset() {
        lastTotal = -1;
        pendingSince = -1;
    }
}
