javac -d ../bin ../src/*.java
javadoc -d ../docs -author -version ../src/*.java
java -cp "../bin" BB_gui
