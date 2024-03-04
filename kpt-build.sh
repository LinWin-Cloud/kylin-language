#!/bin/bash
cd kpt/
javac -source 1.8 *.java -d ../out/production/kpt/
cd ../out/production/kpt/
jar -cfm kpt.jar ../../../bin/KPT-INF/MANIFEST.MF *
mv kpt.jar ../../../release/
