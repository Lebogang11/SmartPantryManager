@echo off
REM Windows version of run_all.sh. Requires node 18+ and a JDK (javac) on PATH.
cd /d "%~dp0"
echo == 1/3 JavaScript engine tests ==
pushd web
node engine.test.js || exit /b 1
echo == 2/3 Web UI tests ==
if not exist node_modules call npm install --no-audit --no-fund
node ui.test.js || exit /b 1
popd
echo == 3/3 Java matching-engine tests ==
set SRC=..\android-app\app\src\main\java\com\smartpantry
set OUT=%TEMP%\spm_out
if exist "%OUT%" rmdir /s /q "%OUT%"
mkdir "%OUT%"
javac -d "%OUT%" %SRC%\logic\*.java %SRC%\data\SeedData.java %SRC%\util\Formatters.java java-engine\EngineTest.java || exit /b 1
java -cp "%OUT%" EngineTest || exit /b 1
echo All automated tests passed.
