:: ---------------------------------------------------------------------
:: JAP COURSE - SCRIPT
:: SCRIPT CST8221 - Spring 2024
:: ---------------------------------------------------------------------

CLS 

:: Define directory paths
SET LIBDIR=lib
SET SRCDIR=src
SET BINDIR=bin
SET BINERR=battleship-javac.err
SET JARNAME=Battleship.jar
SET JARERR=battleship-jar.err
SET JAROUT=battleship-jar.out
SET DOCDIR=doc
SET DOCERR=battleship-doc.err
SET DOCOUT=battleship-doc.out
SET PACKAGE=battleship
SET MAINCLASSSRC=%SRCDIR%\%PACKAGE%\Main.java
SET MAINCLASSBIN=%PACKAGE%.Main
SET RESOURCEDIR=resources

@echo off

ECHO "@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
ECHO "@                                                                   @"
ECHO "@                   #       @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@  @"
ECHO "@                  ##       @  A L G O N Q U I N  C O L L E G E  @  @"
ECHO "@                ##  #      @    JAVA APPLICATION PROGRAMMING    @  @"
ECHO "@             ###    ##     @       S P R I N G  -  2 0 2 4      @  @"
ECHO "@          ###    ##        @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@  @"
ECHO "@        ###    ##                                                  @"
ECHO "@        ##    ###                 ###                              @"
ECHO "@         ##    ###                ###                              @"
ECHO "@           ##    ##               ###   #####  ##     ##  #####    @"
ECHO "@         (     (      ((((()      ###       ## ###   ###      ##   @"
ECHO "@     ((((     ((((((((     ()     ###   ######  ###  ##   ######   @"
ECHO "@        ((                ()      ###  ##   ##   ## ##   ##   ##   @"
ECHO "@         ((((((((((( ((()         ###   ######    ###     ######   @"
ECHO "@         ((         ((           ###                               @"
ECHO "@          (((((((((((                                              @"
ECHO "@   (((                      ((                                     @"
ECHO "@    ((((((((((((((((((((() ))                                      @"
ECHO "@         ((((((((((((((((()                                        @"
ECHO "@                                                                   @"
ECHO "@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"

ECHO "[LABS SCRIPT ---------------------]"

ECHO "0. Preconfiguring ................."
mkdir "%DOCDIR%" 2>nul
mkdir "%BINDIR%" 2>nul
mkdir "%BINDIR%\%PACKAGE%" 2>nul
xcopy /E /I /Y "%RESOURCEDIR%" "%BINDIR%\%RESOURCEDIR%"

ECHO "1. Compiling ......................"
javac -Xlint -cp "%SRCDIR%" "%MAINCLASSSRC%" -d "%BINDIR%" 2> "%BINERR%"

ECHO "2. Creating Jar ..................."
cd "%BINDIR%"
jar cvfe %JARNAME% %MAINCLASSBIN% . > "../%JAROUT%" 2> "../%JARERR%"

ECHO "3. Creating Javadoc ..............."
cd ..
javadoc -cp "%BINDIR%" -d "%DOCDIR%" -sourcepath "%SRCDIR%" -subpackages %PACKAGE% > "%DOCDIR%\%DOCOUT%" 2> "%DOCDIR%\%DOCERR%"


ECHO "4. Running Jar ...................."
start java -jar "%BINDIR%\%JARNAME%"
cd ..

ECHO "[END OF SCRIPT -------------------]"
ECHO "                                   "

@echo on

:: ---------------------------------------------------------------------
:: End of Script (A13 - F23)
:: ---------------------------------------------------------------------