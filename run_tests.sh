#!/usr/bin/env bash
#
# Compiles the project (main + tests) and runs the full JUnit 5 test suite
# from the command line — no IDE required.
#
# Usage:  ./run_tests.sh
#
set -e

ROOT="$(cd "$(dirname "$0")" && pwd)"
cd "$ROOT"

# The console-standalone jar is self-contained (it bundles the Jupiter API,
# the engine and the launcher), so it is the only jar needed on the classpath
# for command-line compilation + execution.
JUNIT="lib/junit-platform-console-standalone.jar"
BUILD="build"

if [ ! -f "$JUNIT" ]; then
    echo "ERROR: JUnit jar not found at $JUNIT"
    echo "Re-download the libraries (see README_SETUP.md) or run:"
    echo "  curl -L -o $JUNIT \\"
    echo "    https://repo1.maven.org/maven2/org/junit/platform/junit-platform-console-standalone/1.10.2/junit-platform-console-standalone-1.10.2.jar"
    exit 1
fi

echo "==> Cleaning build directory"
rm -rf "$BUILD" && mkdir -p "$BUILD"

echo "==> Compiling main sources"
javac -d "$BUILD" $(find src -name '*.java' -not -path 'src/test/*')

echo "==> Compiling test sources"
javac -cp "$BUILD:$JUNIT" -d "$BUILD" $(find src/test -name '*.java')

echo "==> Running JUnit 5 test suite"
java -jar "$JUNIT" execute \
    --class-path "$BUILD" \
    --scan-class-path \
    --details=tree \
    --disable-banner
