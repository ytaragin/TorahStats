# TorahStats
Project to analyize patterns in the Teamim in the Torah

This project contains two parts:
## data
Text files that contains the Torah with teamim as well
## Generate
This is a Scala project that reads in teh data files and creates csv files that breakdown the frequency and patterns of the Teamim at various levels:  
- Sefer
- Parsha
- Perek
- Aliyah  

### Building
Right now it is very straightforward so I haven't bothered to create a make file of any kind. So to build run:  
```
    mkdir output
    scalac -d output Generate/com/yt/patterns/*.scala
```
To run, just execute:  
```
    scala -classpath output com.yt.patterns.App
```

## Upload
This is a node tool that uploads the genereated CSV files into an existing google spreadsheet for easier viewing and sharing.
You need to put a file called credentials.json that contains you client key and secret that you get from Google.

