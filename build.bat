@echo off
setlocal

rem === Настройки ===
set SRC=src
set OUT=out
set JAR=util.jar
set MANIFEST=manifest.txt

rem === Очистка старых файлов ===
if exist %OUT% rmdir /s /q %OUT%
mkdir %OUT%

rem === Компиляция исходников ===
echo Compiling Java source files...
javac -d %OUT% %SRC%\*.java
if errorlevel 1 (
    echo [ERROR] Compilation failed.
    pause
    exit /b 1
)

rem === Проверка манифеста ===
if not exist %MANIFEST% (
    echo Main-Class: FileFilterUtil> %MANIFEST%
    echo.>> %MANIFEST%
)

rem === Сборка jar ===
echo Creating JAR...
jar cfm %JAR% %MANIFEST% -C %OUT% .

echo Done. JAR created: %JAR%
pause
