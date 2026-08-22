package com.b8b7.bholdeasyplace;

import fi.dy.masa.malilib.util.InfoUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Custom-component stacks that leave the inventory outside a container
 * screen and are not restored within the grace window get one warning and
 * one log line with a timestamp: evidence for lost-item tickets (#1).
 * Detection only; the loss itself is server-side. Consumable stacks are
 * excluded (eating is not a loss). Suppressed near screen interaction so
 * chest transfers and manual moves stay quiet.
 */
public final class Watchdog {
    private static final int GRACE_TICKS = 40;
    private static final int SCREEN_QUIET_TICKS = 40;

    private record Pending(long sinceTick, int expected, String name) {}

    private static final Map<Long, Integer> lastCounts = new HashMap<>();
    private static final Map<Long, String> lastNames = new HashMap<>();
    private static final Map<Long, Pending> pending = new HashMap<>();
    private static long tick = 0;
    private static long lastScreenTick = Long.MIN_VALUE;
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
                // slot contents are being rearranged deliberately; pending
                // verdicts from before the screen are ambiguous now
                pending.clear();
            }

            Map<Long, Integer> cur = new HashMap<>();
            Inventory inv = mc.player.getInventory();
            for (int i = 0; i < inv.getContainerSize(); i++) {
                ItemStack stack = inv.getItem(i);
                if (ItemMatch.hasCustomComponents(stack) && !stack.has(DataComponents.CONSUMABLE)) {
                    long sig = sig(stack);
                    cur.merge(sig, stack.getCount(), Integer::sum);
                    lastNames.put(sig, stack.getHoverName().getString());
                }
            }

            boolean screenQuiet = tick - lastScreenTick > SCREEN_QUIET_TICKS;
            for (Map.Entry<Long, Integer> prev : lastCounts.entrySet()) {
                int now = cur.getOrDefault(prev.getKey(), 0);
                if (now < prev.getValue() && screenQuiet && !pending.containsKey(prev.getKey())) {
                    pending.put(prev.getKey(),
                            new Pending(tick, prev.getValue(), lastNames.getOrDefault(prev.getKey(), "unknown item")));
                }
            }

            Iterator<Map.Entry<Long, Pending>> it = pending.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<Long, Pending> e = it.next();
                int now = cur.getOrDefault(e.getKey(), 0);
                if (now >= e.getValue().expected()) {
                    it.remove();
                } else if (tick - e.getValue().sinceTick() > GRACE_TICKS) {
                    fire(e.getValue(), now);
                    it.remove();
                }
            }

            lastCounts.clear();
            lastCounts.putAll(cur);
        } catch (Throwable t) {
            if (!warnedThisSession) {
                warnedThisSession = true;
                HoldEasyPlaceClient.LOGGER.warn("bhold: loss watchdog failed, detection disabled this session", t);
            }
            reset();
        }
    }

    private static void fire(Pending p, int now) {
        String stamp = ZonedDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss zzz"));
        HoldEasyPlaceClient.LOGGER.warn(
                "bhold watchdog: '{}' count dropped {} -> {} and was not restored within 2s at {} - possible custom item loss",
                p.name(), p.expected(), now, stamp);
        InfoUtils.printActionbarMessage("bholdeasyplace.message.watchdog_loss", p.name(), stamp);
    }

    private static long sig(ItemStack stack) {
        long h = BuiltInRegistries.ITEM.getKey(stack.getItem()).hashCode();
        return h * 31 + stack.getComponentsPatch().hashCode();
    }

    private static void reset() {
        lastCounts.clear();
        lastNames.clear();
        pending.clear();
    }
}
