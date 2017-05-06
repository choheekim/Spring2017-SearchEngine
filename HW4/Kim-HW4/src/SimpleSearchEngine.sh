#!/bin/bash

javac -cp .:../lib/kstem-3.4.jar ./com/chohee/inverted_index_part1/*.java ./com/chohee/query_evaluation_part2/*.java ./com/chohee/*.java &&
java -cp ".:../lib/kstem-3.4.jar" com.chohee.SimpleSearchEngine
