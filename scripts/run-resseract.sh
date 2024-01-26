#!/bin/bash
cd "$(dirname "$0")"
SCRIPT_DIR="$(pwd)"

export RESSERACT_PROFILE=prod
$SCRIPT_DIR/jre/bin/java -jar $SCRIPT_DIR/resseract.jar