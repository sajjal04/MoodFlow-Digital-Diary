@echo off
REM ===========================================================================
REM  Compiles the project (main + tests) and runs the full JUnit 5 test suite
REM  on Windows -- no IDE required.
REM
REM  Usage (from the project folder):  run_tests.bat
REM ===========================================================================
setlocal enabledelayedexpansion

cd /d "%~dp0"

set "JUNIT=lib\junit-platform-console-standalone.jar"
set "BUILD=build"

if not exist "%JUNIT%" (
    echo ERROR: JUnit jar not found at %JUNIT%
    echo See README_SETUP.md for how to download the JUnit libraries.
    exit /b 1
)

echo ==^> Cleaning build directory
if exist "%BUILD%" rmdir /s /q "%BUILD%"
mkdir "%BUILD%"

echo ==^> Collecting source files
if exist sources_main.txt del sources_main.txt
if exist sources_test.txt del sources_test.txt
for /r src %%f in (*.java) do (
    echo %%f | findstr /i "\\test\\" >nul
    if errorlevel 1 (
        echo %%f>> sources_main.txt
    ) else (
        echo %%f>> sources_test.txt
    )
)

echo ==^> Compiling main sources
javac -d "%BUILD%" @sources_main.txt
if errorlevel 1 goto :fail

echo ==^> Compiling test sources
javac -cp "%BUILD%;%JUNIT%" -d "%BUILD%" @sources_test.txt
if errorlevel 1 goto :fail

echo ==^> Running JUnit 5 test suite
java -jar "%JUNIT%" execute --class-path "%BUILD%" --scan-class-path --details=tree --disable-banner

del sources_main.txt sources_test.txt 2>nul
goto :eof

:fail
echo BUILD FAILED
del sources_main.txt sources_test.txt 2>nul
exit /b 1
