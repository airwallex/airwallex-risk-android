#!/usr/bin/env bash
set -euo pipefail

if [ -z "${VERSION}" ]; then
  echo "Missing version."
  exit 1
fi

echo "Updating version to $VERSION."

find . -type f -name "gradle.properties" | xargs sed -i '' -e 's/\(airwallexSdkVersion*="\)[^"]*/\1'"$VERSION"'/g'
find . -type f -name "README.md" | xargs sed -i '' -e 's/\(''io.github.airwallex:risk-sdk:*\).*/\1'"$VERSION"'''/g'
sed -i '' -e 's/\(airwallexSdkVersion = '\''\)[^'\'']*/\1'"$VERSION"'/g' ./build.gradle
