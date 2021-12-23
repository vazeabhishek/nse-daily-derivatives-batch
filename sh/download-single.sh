day=$1
month=$2
year=$3
url="https://www1.nseindia.com/content/historical/DERIVATIVES/${year}/${month}/fo${day}${month}${year}bhav.csv.zip"
echo " Downloading $url"
curl $url --output temp.zip --silent --fail || exit 1
unzip temp.zip 
rm -rf temp.zip