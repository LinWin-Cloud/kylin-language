javac .\src\ -d .\out\production\kylin_ke
cd .\out\production\kylin_ke
jar -cfm kylin_language.jar ..\..\..\bin\META-INF\MANIFEST.MF *
copy kylin_language.jar ..\..\..\release\
echo '[INFO] build ok.'
