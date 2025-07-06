@echo off
setlocal enabledelayedexpansion

rem === Настройки ===
set SRC=src
set OUT=out
set JAR=util.jar
set MANIFEST=manifest.txt
set MAIN_CLASS=FileFilterUtil

rem === Очистка и создание выходной директории ===
if exist %OUT% rmdir /s /q %OUT%
mkdir %OUT%

rem === Поиск всех .java файлов ===
set FILES=
for /R %SRC% %%f in (*.java) do (
    set FILES=!FILES! "%%f"
)

rem === Компиляция ===
echo Compiling Java source files...
javac --release 17 -d %OUT% !FILES!
if errorlevel 1 (
    echo [ERROR] Compilation failed.
    pause
    exit /b 1
)

rem === Генерация манифеста ===
if not exist %MANIFEST% (
    echo Main-Class: %MAIN_CLASS%> %MANIFEST%
    echo.>> %MANIFEST%
)

rem === Сборка JAR ===
echo Creating JAR...
jar --create --file %JAR% --manifest %MANIFEST% -C %OUT% .

echo Done. JAR created: %JAR%
pause
