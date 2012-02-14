#!/bin/sh

work_folder=src
replace_from=$1
replace_to=$2
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

