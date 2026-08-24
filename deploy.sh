#!/usr/bin/env bash
# Build, archive, deploy to the target Minecraft client host.
# Usage: deploy.sh [note-for-archive-filename] [--dry-run]
set -euo pipefail
cd "$(dirname "$0")"

HOST=${HOST:-andrea}
MODS=${MODS:-'~/Minecraft/Instances/primary/minecraft/mods'}
ARCHIVE=${ARCHIVE:-'~/Minecraft/mod-backups/holdeasyplace'}

NOTE=""
DRY=0
for arg in "$@"; do
  case "$arg" in
    --dry-run) DRY=1 ;;
    --*)       echo "FAIL: unknown flag $arg"; exit 2 ;;
    *)         NOTE="$arg" ;;
  esac
done

step() { echo "==> $*"; }
die()  { echo "FAIL: $*" >&2; exit 1; }

step "checking working tree"
if [ -n "$(git status --porcelain -- . 2>/dev/null)" ]; then
  die "working tree dirty; commit first"
fi

VERSION=$(rg -o '^mod_version=(.+)$' -r '$1' gradle.properties || true)
case "$VERSION" in
  ""|*[![:print:]]*) die "could not read mod_version from gradle.properties" ;;
esac
SHA=$(git rev-parse --short HEAD)
STAMP=$(date +%Y%m%d)
JAR="build/libs/bholdeasyplace-${VERSION}.jar"
SUFFIX="${STAMP}-${SHA}${NOTE:+-$NOTE}"

step "checking Minecraft is closed on ${HOST}"
if ssh "$HOST" "pgrep -f '[n]et\.minecraft\.client\.main\.Main' >/dev/null"; then
  die "Minecraft is running on $HOST; close it first"
fi

step "building"
docker build -q -f Dockerfile.build -t bhep-build:local . >/dev/null
mkdir -p .gradle-home
docker run --rm --name "bholdeasyplace-deploy-$$" \
  -v "$PWD/.gradle-home":/gradle -v "$PWD":/work -w /work \
  -e GRADLE_USER_HOME=/gradle -e CI=true -u "$(id -u):$(id -g)" bhep-build:local \
  ./gradlew build --no-daemon --console=plain >/dev/null || die "gradle build"

[ -f "$JAR" ] || die "$JAR not produced"
unzip -l "$JAR" | grep -q LICENSE_bholdeasyplace || die "LICENSE_bholdeasyplace missing from jar"

LOCAL=$(md5sum "$JAR" | cut -d' ' -f1)

if [ "$DRY" = 1 ]; then
  echo
  echo "dry run, nothing sent."
  echo "  would archive: ${ARCHIVE}/bholdeasyplace-${VERSION}-${SUFFIX}.jar"
  echo "  would deploy:  ${MODS}/bholdeasyplace-${VERSION}.jar  (md5 ${LOCAL})"
  ssh "$HOST" "ls ${MODS}/*holdeasyplace* 2>/dev/null" | while read -r f; do
    case "$f" in
      */bholdeasyplace-${VERSION}.jar) echo "  would replace: $f" ;;
      *)                               echo "  would remove:  $f" ;;
    esac
  done
  exit 0
fi

step "archiving before touching the live instance"
ssh "$HOST" "mkdir -p ${ARCHIVE}"
scp -q "$JAR" "$HOST:${ARCHIVE}/bholdeasyplace-${VERSION}-${SUFFIX}.jar"
ARCHIVED=$(ssh "$HOST" "md5sum ${ARCHIVE}/bholdeasyplace-${VERSION}-${SUFFIX}.jar" | cut -d' ' -f1)
[ "$LOCAL" = "$ARCHIVED" ] || die "archive checksum mismatch; live instance untouched"

step "deploying"
scp -q "$JAR" "$HOST:${MODS}/"
REMOTE=$(ssh "$HOST" "md5sum ${MODS}/bholdeasyplace-${VERSION}.jar" | cut -d' ' -f1)
[ "$LOCAL" = "$REMOTE" ] || die "deployed checksum mismatch"

# Only after the new jar verifies: retire superseded copies, including any
# pre-rename holdeasyplace-* jars.
step "removing superseded jars"
ssh "$HOST" "find ${MODS} -maxdepth 1 \( -name 'bholdeasyplace-*.jar' -o -name 'holdeasyplace-*.jar' \) ! -name 'bholdeasyplace-${VERSION}.jar' -print -delete"

echo
echo "deployed  bholdeasyplace-${VERSION} (${SHA}) to ${HOST}"
echo "archived  bholdeasyplace-${VERSION}-${SUFFIX}.jar"
