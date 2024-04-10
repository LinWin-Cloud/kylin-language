#! /bin/bash

find src/. | grep .java > source.txt
cd src/
javac Main.java -d ../out/production/kylin_ke/ 
cd ../out/production/kylin_ke/
jar -cfm kylin_language.jar ../../../bin/META-INF/MANIFEST.MF ./
mv kylin_language.jar ../../../release/
echo '[INFO] build ok.'
cd ../../../bin/
./kylin ../hello.ky
