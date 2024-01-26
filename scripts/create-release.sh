set -e

VERSION="1.0.0"
declare -a supportedPlatforms=(
    "linux-aarch64" 
    "linux-x64" 
    "macos-aarch64"
    "macos-x64"
    "windows-x64"
)

rm -rf resseract-$VERSION 
mkdir resseract-$VERSION 
rm -rf temp
mkdir temp

cd ..
echo "######################################## Build UI ########################################"
cd angular-app/Resseract
# ng build --configuration "production" --named-chunks --verbose --build-optimizer=false --source-map
ng build --configuration "production"
cd ../../

echo "################################### Copy UI to server ####################################"
cp -R angular-app/Resseract/dist/* server/Resseract/resseract-server/src/main/resources/static

echo "###################################### Build Server ######################################"
cd server/Resseract
mvn clean package spring-boot:repackage
cd ../../

echo "####################################### Create JRE #######################################"
cd scripts

for platform in "${supportedPlatforms[@]}"
do
    echo "############################## Create package for $platform ##############################"
    if [ ! -d "jre/${platform}" ]; then
        echo "############################## Create JRE for $platform ##############################"
        rm -rf temp/jdk
        mkdir temp/jdk
        if [[ $platform == windows* ]]
        then
            curl -L https://download.oracle.com/java/17/latest/jdk-17_${platform}_bin.zip -o temp/jdk-17_${platform}_bin.zip
            unzip temp/jdk-17_${platform}_bin.zip -d temp/jdk
            mv temp/jdk/jdk-*/jmods temp/jdk
        else 
            curl -L https://download.oracle.com/java/17/latest/jdk-17_${platform}_bin.tar.gz -o temp/jdk-17_${platform}_bin.tar.gz
            tar -xvzf temp/jdk-17_${platform}_bin.tar.gz -C temp/jdk --strip-components=1
        fi
        jlink --output jre/${platform} --module-path temp/jdk/jmods --compress=2 --no-header-files --no-man-pages --add-modules java.base,java.compiler,java.datatransfer,java.desktop,java.instrument,java.logging,java.management,java.management.rmi,java.naming,java.net.http,java.prefs,java.rmi,java.scripting,java.se,java.security.jgss,java.security.sasl,java.smartcardio,java.sql,java.sql.rowset,java.transaction.xa,java.xml,java.xml.crypto 
    fi

    rm -rf temp/resseract-$VERSION
    mkdir temp/resseract-$VERSION
    if [[ $platform == windows* ]]
    then
        cp run-resseract.bat temp/resseract-$VERSION/run-resseract.bat
        chmod +x temp/resseract-$VERSION/run-resseract.bat
    else 
        cp run-resseract.sh temp/resseract-$VERSION/run-resseract.sh
        chmod +x temp/resseract-$VERSION/run-resseract.sh
    fi
    cp ../server/Resseract/resseract-server/target/resseract-$VERSION.jar temp/resseract-$VERSION/resseract.jar
    mkdir temp/resseract-$VERSION/jre
    cp -R jre/${platform}/* temp/resseract-$VERSION/jre
    cd temp
    zip -r ../resseract-$VERSION/resseract-$VERSION-$platform.zip resseract-$VERSION
    cd ..
done
cp ../server/Resseract/resseract-server/target/resseract-$VERSION.jar resseract-$VERSION/resseract-$VERSION.jar

rm -rf temp