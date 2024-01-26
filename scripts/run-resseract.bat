@echo off
cd /d "%~dp0"
set "SCRIPT_DIR=%CD%"

set "RESSERACT_PROFILE=prod"
"%SCRIPT_DIR%\jre\bin\java" -jar "%SCRIPT_DIR%\resseract.jar"