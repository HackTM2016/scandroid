#!/bin/bash
#
# Script to download apk file from Google Play a.k.a Android Market.
# Accepts package name or Google Play URL as input.
# Files are download from apk-dl.com
# author : Arul (@arulrajnet)
# mihai - heavily modified so it now works
#

#APK_DL_URL="http://apk-dl.com/store/apps/details?id=%s"

APK_DL_URL="http://apk-dl.com/%s"

PACKAGE_NAME=""

# Parse the user given input
function parse() {
    # TODO for parse package and google play url
    package_re="/^[a-z][a-z0-9_]*(\.[a-z0-9_]+)+[0-9a-z_]$/i"
    if [[ $1 =~ $package_re ]]; then
        echo ${BASH_REMATCH[1]}
    fi
    url_re="*play.google.com/store/apps/details?id=*"
    PACKAGE_NAME=$1
}

# Download the apk file
function download() {
    echo -e "Downloading..."
    local ua="Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/38.0.2125.104 Safari/537.36"
    local url=`printf ${APK_DL_URL} ${PACKAGE_NAME}`
    curl "$url" -o "apks/$PACKAGE_NAME.apk.details" --write-out "%{http_code}" --compressed --retry 10 --retry-max-time 0 \
        -H "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8" \
        -H "Connection: keep-alive" -H "Accept-Encoding: gzip,deflate,sdch" \
        -H "Accept-Language: en-US,en;q=0.8,ta;q=0.6" \
        -H "User-Agent: $ua"

        local apk_url=`grep -o apk-dl.com/files/[a-zA-Z0-9.-/]* apks/$PACKAGE_NAME.apk.details | tail -n 1`
	echo $apk_url

    curl "http://$apk_url" -o "apks/$PACKAGE_NAME.apk.details2" --write-out "%{http_code}" --compressed --retry 10 --retry-max-time 0 \
        -H "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8" \
        -H "Connection: keep-alive" -H "Accept-Encoding: gzip,deflate,sdch" \
        -H "Accept-Language: en-US,en;q=0.8,ta;q=0.6" \
        -H "User-Agent: $ua"

	local final_apk_url=`grep -o http://dl[a-zA-Z0-9\ -_]*apk? apks/$PACKAGE_NAME.apk.details2 | tail -1`
	echo $final_apk_url

    curl "$final_apk_url" -o "apks/$PACKAGE_NAME.apk" --write-out "%{http_code}" --compressed --retry 10 --retry-max-time 0 \
        -H "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8" \
        -H "Connection: keep-alive" -H "Accept-Encoding: gzip,deflate,sdch" \
        -H "Accept-Language: en-US,en;q=0.8,ta;q=0.6" \
        -H "User-Agent: $ua"

}



# Validate basic requirement for this script
function validate() {
    # Commands used in this script
    local CMDS="curl"

    for i in ${CMDS}
    do
        type -P ${i} &>/dev/null  && continue  || { echo "$i command not found."; exit 1; }
    done
}

function usage() {
    echo -e "Usage: \n\t$0 com.whatsapp \n\t$0 https://play.google.com/store/apps/details?id=com.whatsapp"
    exit 1
}

# How its called
if test -z "$1"
then
    usage
fi

validate
parse $1
download ${PACKAGE_NAME}

exit 0
