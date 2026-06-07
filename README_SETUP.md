# DigitalDiary — Setup & Running the JUnit Tests

If your IDE shows errors like:

```
package org.junit.jupiter.api does not exist
cannot find symbol class Test
cannot find symbol class DisplayName
```

…it simply means the **JUnit 5 libraries are not on your project's classpath yet**.
This document explains how to fix that. The required JAR files are already
included in the **`lib/`** folder of this project — you just need to tell your
IDE to use them.

---

## 1. What's in the `lib/` folder

| JAR | Purpose |
|-----|---------|
| `junit-jupiter-api-5.10.2.jar` | The `@Test`, `@DisplayName`, `Assertions`, etc. you write against |
| `junit-jupiter-engine-5.10.2.jar` | Runs the Jupiter tests |
| `junit-jupiter-params-5.10.2.jar` | Parameterized-test support |
| `junit-platform-commons-1.10.2.jar` | Shared platform utilities |
| `junit-platform-engine-1.10.2.jar` | Test-engine SPI |
| `junit-platform-launcher-1.10.2.jar` | Lets the IDE launch the tests |
| `opentest4j-1.3.0.jar` | Assertion-failure model used by JUnit 5 |
| `apiguardian-api-1.1.2.jar` | API annotations used internally by JUnit |
| `junit-platform-console-standalone.jar` | **Self-contained** jar for running tests from the command line |

> The 8 individual jars are for the **IDE**. The single `console-standalone`
> jar is for the **command line** (`run_tests.sh` / `run_tests.bat`).
> Do **not** put both groups on the same classpath at once — pick one.

---

## 2. IntelliJ IDEA

You have two options. **Option A is already done for you** — try it first.

### Option A — Use the bundled project configuration (fastest)

This project already ships an IntelliJ library definition
(`.idea/libraries/junit5.xml`) and a module file (`DigitalDiaryProject.iml`)
that references it.

1. Open the `DigitalDiary_Final` folder in IntelliJ
   (**File ▸ Open…** and select the folder).
2. If IntelliJ asks to *trust* the project, click **Trust Project**.
3. Wait for indexing to finish. The JUnit errors should disappear.
4. If they don't, do **File ▸ Invalidate Caches… ▸ Invalidate and Restart**,
   or follow Option B below.

### Option B — Add the library manually (always works)

1. **File ▸ Project Structure…** (`Ctrl+Alt+Shift+S`).
2. Select **Libraries** in the left pane.
3. Click the **`+`** button ▸ **Java**.
4. Navigate to this project's **`lib`** folder, select **all 8 individual
   jars** (you can skip `junit-platform-console-standalone.jar`), and click
   **OK**.
5. When asked which module to add the library to, choose
   **DigitalDiaryProject** and click **OK**.
6. Still in *Project Structure*, open **Modules ▸ DigitalDiaryProject ▸
   Sources**, right-click the **`src/test`** folder and mark it as
   **Tests** (green) so JUnit is recognized only where it's needed
   (optional but recommended).
7. Click **Apply ▸ OK**.

### Running the tests in IntelliJ

- Right-click the **`src/test`** folder ▸ **Run 'Tests in test'**, **or**
- Open any test class (e.g. `ModelTest`) and click the green ▶ gutter icon
  next to the class or a method.

> **JDK note:** This project targets Java 17+. Make sure
> **File ▸ Project Structure ▸ Project ▸ SDK** is set to a JDK 17 or newer
> (JDK 25 is fine). JUnit 5.10 requires Java 8 as a minimum.

---

## 3. Eclipse

### Option A — Use Eclipse's built-in JUnit 5 (simplest)

Eclipse bundles JUnit 5, so you usually don't even need the `lib/` jars:

1. **File ▸ Import… ▸ General ▸ Existing Projects into Workspace** (or
   **File ▸ Open Projects from File System…**) and select this folder.
2. Right-click the project ▸ **Build Path ▸ Add Libraries… ▸ JUnit ▸
   Next ▸ JUnit 5 ▸ Finish**.
3. Right-click the **`src/test`** folder ▸ **Run As ▸ JUnit Test**.

### Option B — Add the bundled jars manually

1. Right-click the project ▸ **Properties ▸ Java Build Path ▸ Libraries**.
2. Select **Classpath** ▸ **Add JARs…**.
3. Expand the project, open **`lib`**, select the **8 individual jars**, and
   click **OK ▸ Apply and Close**.
3. Right-click `src/test` ▸ **Run As ▸ JUnit Test**.

---

## 4. Command line (no IDE)

From the project root:

**macOS / Linux**
```bash
./run_tests.sh
```

**Windows**
```bat
run_tests.bat
```

Both scripts compile everything into a `build/` folder and run the full suite
using the self-contained `junit-platform-console-standalone.jar`.

Expected output ends with:
```
[        49 tests found           ]
[        49 tests successful      ]
[         0 tests failed          ]
```

### Manual command-line invocation (if you prefer)

```bash
# 1. compile main + test sources
javac -d build $(find src -name '*.java' -not -path 'src/test/*')
javac -cp "build:lib/junit-platform-console-standalone.jar" -d build $(find src/test -name '*.java')

# 2. run the suite
java -jar lib/junit-platform-console-standalone.jar execute \
     --class-path build --scan-class-path
```

On Windows use `;` instead of `:` as the classpath separator.

---

## 5. Running the application itself

The tests are independent of the GUI. To launch the app:

```bash
javac -d build $(find src -name '*.java' -not -path 'src/test/*')
java -cp build Main
```

(or just run `Main.java` from your IDE).

---

## 6. Re-downloading the jars (only if `lib/` is missing)

All jars come from Maven Central. For example:

```bash
cd lib
BASE=https://repo1.maven.org/maven2
curl -LO $BASE/org/junit/jupiter/junit-jupiter-api/5.10.2/junit-jupiter-api-5.10.2.jar
curl -LO $BASE/org/junit/jupiter/junit-jupiter-engine/5.10.2/junit-jupiter-engine-5.10.2.jar
curl -LO $BASE/org/junit/jupiter/junit-jupiter-params/5.10.2/junit-jupiter-params-5.10.2.jar
curl -LO $BASE/org/junit/platform/junit-platform-commons/1.10.2/junit-platform-commons-1.10.2.jar
curl -LO $BASE/org/junit/platform/junit-platform-engine/1.10.2/junit-platform-engine-1.10.2.jar
curl -LO $BASE/org/junit/platform/junit-platform-launcher/1.10.2/junit-platform-launcher-1.10.2.jar
curl -LO $BASE/org/opentest4j/opentest4j/1.3.0/opentest4j-1.3.0.jar
curl -LO $BASE/org/apiguardian/apiguardian-api/1.1.2/apiguardian-api-1.1.2.jar
curl -LO $BASE/org/junit/platform/junit-platform-console-standalone/1.10.2/junit-platform-console-standalone-1.10.2.jar
```
