# Bash shell script for running  MovieLab CMR Editor
#
# -------------------------------------------------

start_dir=`pwd`

os=`uname`
echo Runing in a $os environment
# OS specific support.  $var _must_ be set to either true or false.
cygwin=false
os400=false
case "`uname`" in
    CYGWIN*)cygwin=true;;
    OS400*) os400=true;;
esac

SEP=":"
# ----------------------------------------------------------------------
#   check that required environment is present
# ----------------------------------------------------------------------
if [ -z "$JAVA_HOME" ]; then
    echo "JAVA_HOME not found"
    exit
fi

# ----------------------------------------------------------------------
# set up the classpath
# ----------------------------------------------------------------------

CLASSPATH="" 
cd ./lib
echo checking for jars, pwd is `pwd`
jarFile=$(ls )
for f in $jarFile
do
    if [ -f "$f" ]; then
	echo Adding: $f to classpath
	CLASSPATH="${CLASSPATH}./lib/$f$SEP"
    fi
done
echo CLasspath set to ${CLASSPATH}
# For Cygwin, switch paths to Windows format before running java
if $cygwin; then
  echo Converting classpath for CYGWIN compatibility....
  CLASSPATH=`cygpath --path --windows "$CLASSPATH"`
fi

cd ..
echo CLasspath set to ${CLASSPATH}
mainClass=com.callc.movielab.ratings.client.RatingsEditor

java -Xmx512m -Dsun.java2d.noddraw=true  -Djava.library.path=".\bin"  -classpath $CLASSPATH $mainClass %* 
