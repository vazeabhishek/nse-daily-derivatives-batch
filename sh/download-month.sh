month=$1
year=$2
for day in 01 02 03 04 05 06 07 08 09 10 11 12 13 14 15 16 17 18 19 20 21 22 23 24 25 26 27 28 29 30 31}
do
  sh download-single.sh $day $month $year
done
mkdir $month
mv *.csv $month