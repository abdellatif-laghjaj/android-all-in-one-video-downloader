#!/usr/bin/env bash
set -euo pipefail

if [ $# -ne 1 ]; then
  echo "Usage: $0 <versionName, e.g. 1.1.0>"
  exit 1
fi

NEW_VERSION_NAME="$1"
TAG="v${NEW_VERSION_NAME}"
GRADLE_FILE="app/build.gradle.kts"

if [ ! -f "$GRADLE_FILE" ]; then
  echo "ERROR: $GRADLE_FILE not found. Run this from the project root."
  exit 1
fi

if git rev-parse "$TAG" >/dev/null 2>&1; then
  echo "ERROR: tag $TAG already exists."
  exit 1
fi

CURRENT_VERSION_CODE=$(grep 'versionCode' "$GRADLE_FILE" | head -1 | tr -dc '0-9')
NEW_VERSION_CODE=$((CURRENT_VERSION_CODE + 1))

echo "versionCode: $CURRENT_VERSION_CODE -> $NEW_VERSION_CODE"
echo "versionName: -> $NEW_VERSION_NAME"

sed -i "s/versionCode = [0-9]\+/versionCode = ${NEW_VERSION_CODE}/" "$GRADLE_FILE"
sed -i "s/versionName = \"[^\"]*\"/versionName = \"${NEW_VERSION_NAME}\"/" "$GRADLE_FILE"

git add "$GRADLE_FILE"
git commit -m "chore: release ${TAG}"
git tag -a "$TAG" -m "Release ${TAG}"
git push origin HEAD
git push origin "$TAG"

echo ""
echo "Pushed ${TAG}. GitHub Actions will now build, sign, and publish the release."
