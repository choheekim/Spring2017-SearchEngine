#!/bin/bash

file=$1
javac -cp .:./kstem-3.4.jar *.java && java -cp ".:./kstem-3.4.jar" SimpleSearchEngine "$file"
