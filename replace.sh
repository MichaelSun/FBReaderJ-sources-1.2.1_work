#!/bin/sh

work_folder=src

if [ "$1" == "update" ];then
	UPDATE_CODE=$2
	UPDATE_NAME=$3
	echo " <<<<<<<< only update Android version to versionCode[$UPDATE_CODE] versionName[$UPDATE_NAME] >>>>>>>>"
    sed 's/android:versionCode=\".*\"/android:versionCode="'$UPDATE_CODE'"/' AndroidManifest.xml | sed 's/android:versionName=\"[0-9].[0-9]\"/android:versionName="'$UPDATE_NAME'"/' > AndroidManifest.xml_tmp
	mv AndroidManifest.xml_tmp AndroidManifest.xml
	cat AndroidManifest.xml
	rm -rf AndroidManifest.xml_tmp
	exit 0
fi

TARGET_DIR=$1
echo "[[target_dir]] dir = $TARGET_DIR"
if [ ! -d $TARGET_DIR ];then
    echo "[[target_dir]] dir : $TARGET_DIR not exist, just exit ......"
	exit 0
fi

if [ ! -e "$TARGET_DIR/icon.png" ];then
	echo "$TARGET_DIR/icon.png not exist, just exit ......"
	exit 0
fi

if [ ! -e "$TARGET_DIR/book.epub" ];then
	echo "$TARGET_DIR/book.epub not exist, just exit ......"
	exit 0
fi

cp -rf $TARGET_DIR/icon.png res/drawable/fbreader.png
cp -rf $TARGET_DIR/book.epub assets/book/book.epub

APP_NAME=$2
APP_NAME_FILE=res/values/strings.xml
APP_NAME_FILE_TMP=res/values/strings.xml_tmp
sed 's/app_name.*>/app_name">'$APP_NAME'<\/string>/' $APP_NAME_FILE > $APP_NAME_FILE_TMP
mv $APP_NAME_FILE_TMP $APP_NAME_FILE
cat $APP_NAME_FILE
sleep 1

echo "<<<<<<<< [[repalce key first]] >>>>>>>"
replace_file=src/org/geometerplus/android/fbreader/Config.java
replace_file_tmp=src/org/geometerplus/android/fbreader/Config.java_tmp
APP_ID=$4
APP_SECRET=$5
echo "APP_ID = $APP_ID and  APP_SECRET_KEY = $APP_SECRET"
sed 's/APP_ID.*;/APP_ID = "'$APP_ID'";/' $replace_file > $replace_file_tmp
mv $replace_file_tmp $replace_file
sed 's/APP_SECRET_KEY.*;/APP_SECRET_KEY = "'$APP_SECRET'";/' $replace_file > $replace_file_tmp
mv $replace_file_tmp $replace_file

cat $replace_file

sleep 2

replace_from=`grep "package=" AndroidManifest.xml | sed 's/.*org.geometerplus.zlibrary.ui.//g' | sed 's/\"//'`
echo "[[replace_from]] data = $replace_from >>>>>>"
replace_to=$3
for text_file in `find $work_folder -type f|xargs grep -l $replace_from`
do echo "Editing file $text_file, replace $replace_from with $replace_to"
sed -e "s/$replace_from/$replace_to/g" $text_file > /tmp/fbreplace        
mv -f /tmp/fbreplace $text_file
done  

echo ">>>>> replace done >>>>>>>"
sleep 2

sed -e "s/$replace_from/$replace_to/g" AndroidManifest.xml > /tmp/fbreplace        
mv -f /tmp/fbreplace AndroidManifest.xml
sleep 2

res_dir=res
for res_file in `find $res_dir -type f|xargs grep -l $replace_from`
do echo "Editing file $res_file, replace $replace_from with $replace_to"
sed -e "s/$replace_from/$replace_to/g" $res_file > /tmp/fbreplace        
mv -f /tmp/fbreplace $res_file
done
sleep 2

echo ">>>>> begin mv the src dir >>>>>>"

cd src/org/geometerplus/zlibrary/ui
ls
mv $replace_from $replace_to
cd -

