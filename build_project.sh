#! /bin/bash

find src/. | grep .java > source.txt
javac -d ./out/production/kylin_ke/ @source.txt
cd ./out/production/kylin_ke/
jar -cvfm kylin_language.jar ../../../bin/META-INF/MANIFEST.MF *
cp kylin_language.jar ../../../bin/
