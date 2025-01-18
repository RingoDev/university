set JAVA_HOME="C:\Program Files\Java\jdk-13.0.2"
set PATH=%JAVA_HOME%\bin

java -jar Coco.jar ScrumTaskboard.atg -frames frm -o src
javac.exe -d cls src\Parser.java src\Scanner.java src\ScrumTaskboard.java 
jar cfmv ScrumTaskboard.jar ScrumTaskboard.mf -C cls .
pause