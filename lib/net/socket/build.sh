javac src/KySocket.java -d ./out/
cd out
jar -cvfm socket.jar ../../../../bin/META-INF/KySocket.MF *
mv socket.jar ../