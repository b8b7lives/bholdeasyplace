#!/usr/bin/env bash
# Static mixin-target verification against arbitrary litematica/malilib jars.
# Checks signatures AND call sites (javap -c), which runtime reflection cannot.
# Usage: verify-targets.sh [litematica.jar] [malilib.jar]   (defaults: libs/)
set -uo pipefail
cd "$(dirname "$0")/.."

LITEMATICA=${1:-$(ls libs/litematica-*.jar 2>/dev/null | head -1)}
MALILIB=${2:-$(ls libs/malilib-*.jar 2>/dev/null | head -1)}
[ -f "$LITEMATICA" ] && [ -f "$MALILIB" ] || { echo "FAIL: jars not found (litematica='$LITEMATICA' malilib='$MALILIB')"; exit 1; }
echo "litematica: $LITEMATICA"
echo "malilib:    $MALILIB"

docker build -q -f Dockerfile.build -t bholdeasyplace-build:local . >/dev/null

OUT=$(docker run --rm -v "$PWD":/work -w /work -u "$(id -u):$(id -g)" bholdeasyplace-build:local sh -c "
L='$LITEMATICA'; M='$MALILIB'
sig() { javap -p -classpath \"\$1\" \"\$2\" 2>/dev/null; }
dis() { javap -c -p -classpath \"\$1\" \"\$2\" 2>/dev/null; }

WU=\$(sig \"\$L\" fi.dy.masa.litematica.util.WorldUtils)
WUC=\$(dis \"\$L\" fi.dy.masa.litematica.util.WorldUtils)
EPU=\$(sig \"\$L\" fi.dy.masa.litematica.util.EasyPlaceUtils)
EPUC=\$(dis \"\$L\" fi.dy.masa.litematica.util.EasyPlaceUtils)
EU=\$(sig \"\$L\" fi.dy.masa.litematica.util.EntityUtils)
IU=\$(sig \"\$L\" fi.dy.masa.litematica.util.InventoryUtils)
CG=\$(sig \"\$L\" 'fi.dy.masa.litematica.config.Configs\$Generic')
HK=\$(sig \"\$L\" fi.dy.masa.litematica.config.Hotkeys)
MIU=\$(sig \"\$M\" fi.dy.masa.malilib.util.InfoUtils)

r() { echo \"\$2\" | grep -qE \"\$3\" && echo \"PASS: \$1\" || echo \"FAIL: \$1\"; }

r 'WorldUtils.doEasyPlaceAction (invoker)' \"\$WU\" 'private static net.minecraft.world.InteractionResult doEasyPlaceAction\(net.minecraft.client.Minecraft\)'
r 'EasyPlaceUtils.handleEasyPlaceWithMessage' \"\$EPU\" 'public static boolean handleEasyPlaceWithMessage\(\)'
r 'EasyPlaceUtils.placementRestrictionInEffect noarg' \"\$EPU\" 'private static boolean placementRestrictionInEffect\(\)'
r 'EasyPlaceUtils.placementRestrictionInEffect mc' \"\$EPU\" 'static boolean placementRestrictionInEffect\(net.minecraft.client.Minecraft\)'
r 'EntityUtils.getUsedHandForItem lenient' \"\$EU\" 'getUsedHandForItem\(net.minecraft.world.entity.player.Player, net.minecraft.world.item.ItemStack\)'
r 'EntityUtils.getUsedHandForItem strict' \"\$EU\" 'getUsedHandForItem\(net.minecraft.world.entity.LivingEntity, net.minecraft.world.item.ItemStack, boolean\)'
r 'InventoryUtils.schematicWorldPickBlock' \"\$IU\" 'public static void schematicWorldPickBlock\(net.minecraft.world.item.ItemStack, net.minecraft.core.BlockPos, net.minecraft.world.level.Level, net.minecraft.client.Minecraft\)'
r 'InventoryUtils.canPickToSlot' \"\$IU\" 'private static boolean canPickToSlot\(net.minecraft.world.entity.player.Inventory, int\)'
r 'Configs.Generic.OPTIONS field' \"\$CG\" 'ImmutableList.* OPTIONS'
r 'Configs.Generic.EASY_PLACE_MODE' \"\$CG\" 'EASY_PLACE_MODE'
r 'Configs.Generic.EASY_PLACE_POST_REWRITE' \"\$CG\" 'EASY_PLACE_POST_REWRITE'
r 'Hotkeys.HOTKEY_LIST field' \"\$HK\" 'HOTKEY_LIST'
r 'Hotkeys.EASY_PLACE_ACTIVATION' \"\$HK\" 'EASY_PLACE_ACTIVATION'
r 'InfoUtils.printActionbarMessage' \"\$MIU\" 'printActionbarMessage\(java.lang.String, java.lang.Object\.\.\.\)'

strict=\$(echo \"\$EPUC\" | grep -c 'Method fi/dy/masa/litematica/util/EntityUtils.getUsedHandForItem:(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/item/ItemStack;Z)')
guard=\$(echo \"\$WUC\" | grep -c 'Method fi/dy/masa/litematica/util/EasyPlaceUtils.placementRestrictionInEffect')
[ \"\$strict\" -ge 1 ] && echo \"PASS: strict hand-check call site (\$strict >= 1)\" || echo \"FAIL: strict hand-check call site (\$strict < 1)\"
[ \"\$guard\" -ge 1 ] && echo \"PASS: legacy guard call site (\$guard >= 1)\" || echo \"FAIL: legacy guard call site (\$guard < 1)\"
" 2>&1)

echo "$OUT"
echo "$OUT" | grep -q '^FAIL' && exit 1 || exit 0
