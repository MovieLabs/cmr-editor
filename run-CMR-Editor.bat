REM Windows Batch file for running  MovieLab CMR Editor
@echo off

set MAIN=com.callc.movielab.ratings.client.RatingsEditor
@echo Running %MAIN%

echo Building CLASSPATH.......

set CJD=.\lib\
set JARS=%CJD%CMR_Editor.jar
set JARS=%JARS%;%CJD%jdom-2.0.5.jar%
set JARS=%JARS%;%CJD%jhall.jar%
set JARS=%JARS%;%CJD%saxon9pe.jar%
set JARS=%JARS%;%CJD%xalan.jar%
set JARS=%JARS%;%CJD%xerces-impl.jar%
set JARS=%JARS%;%CJD%xml-apis.jar%





set CLASSPATH=%JARS%

echo Classpath is %CLASSPATH%
echo.


java -Xmx512m -Dsun.java2d.noddraw=true  -Djava.library.path=".\bin"  -classpath %CLASSPATH% %MAIN% %* 
