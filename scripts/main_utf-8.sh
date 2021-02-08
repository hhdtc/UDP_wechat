javac -encoding utf-8 -d ../bin ../src/*.java
javadoc -encoding utf-8 -d ../docs -author -version ../src/*.java
java -Dfile.encoding=utf-8 -cp "../bin" BB_gui
