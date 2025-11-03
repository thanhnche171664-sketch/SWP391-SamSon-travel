@echo off
echo Rebuilding SamSon Booking Service with Java 17...

REM Clean previous build
if exist build\web\WEB-INF\classes rmdir /s /q build\web\WEB-INF\classes
mkdir build\web\WEB-INF\classes

REM Compile with Java 17 and UTF-8 encoding
"C:\Program Files\Java\jdk-17\bin\javac.exe" -encoding UTF-8 -cp "lib\*" -d build\web\WEB-INF\classes -sourcepath src\java src\java\dao\*.java src\java\entity\*.java src\java\controller\*.java src\java\filter\*.java src\java\util\*.java

if %ERRORLEVEL% EQU 0 (
    echo Compilation successful!
    echo Copying web files...
    xcopy /E /Y web\* build\web\
    echo Build completed successfully!
) else (
    echo Compilation failed!
    exit /b 1
)

pause



