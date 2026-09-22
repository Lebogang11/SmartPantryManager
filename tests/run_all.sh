#!/usr/bin/env bash
# Runs every automated test that does not need an emulator. Requires: node 18+, JDK 17+ (javac).
set -e
cd "$(dirname "$0")"
echo "== 1/3  JavaScript engine tests (web preview) =="
(cd web && node engine.test.js)
echo; echo "== 2/3  Web UI tests (simulated browser) =="
(cd web && { [ -d node_modules ] || npm install --no-audit --no-fund; } && node ui.test.js)
echo; echo "== 3/3  Java matching-engine tests (same logic as the Android app) =="
JAVA_SRC=../android-app/app/src/main/java/com/smartpantry
OUT="$(mktemp -d)"
# Use javac if available, otherwise the compiler module bundled with the JRE.
if command -v javac >/dev/null 2>&1; then JAVAC="javac"; else JAVAC="java -m jdk.compiler/com.sun.tools.javac.Main"; fi
$JAVAC -d "$OUT" $JAVA_SRC/logic/*.java $JAVA_SRC/data/SeedData.java $JAVA_SRC/util/Formatters.java java-engine/EngineTest.java
java -cp "$OUT" EngineTest
echo; echo "All automated tests passed."
