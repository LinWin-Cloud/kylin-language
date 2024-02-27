#! /bin/bash

find src/. | grep .java > source.txt
javac -source 1.8 -target 1.8  -d ./out/production/kylin_ke/ @source.txt
cd ./out/production/kylin_ke/
jar -cfm kylin_language.jar ../../../bin/META-INF/MANIFEST.MF *
cp kylin_language.jar ../../../release/
echo '[INFO] build ok.'
cd ../../../bin/
./kylin ../hello.ky
