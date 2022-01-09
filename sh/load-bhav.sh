DIR=$1
for entry in "$DIR"/*
do
   java -jar nse-daily.jar "$entry"
done