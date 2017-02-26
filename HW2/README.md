#Positonal Inverted Index & Free-text Quiery with Proximity Operator 

A simple search engine that parses a given text file and retrieves result of the search - supports proxmity operator.

* Proximity operator: 
	* Syntax : n(t1, t2)
	* n is the proximity window for terms t1 and t2. That is, terms t1 and t2 can have at most n terms between them in the document. The order in wich the terms t1 and t2 appear in the document is important. 

##Installation
Clone using git:  
```
https://github.com/choheekim/Spring2017-SearchEngine.git
```

##Usage
Run the application: 


Change directory:
```
cd Spring2017-SearchEngine/HW2/Kim-HW2/src
```

Run script:  
```
sh SimpleSearchEngine.sh
```

##Output
* Inverted_index_file.txt : contains positional inverted index list
	* [term, document frequency]   
		-> document id, term frequency: [position1, position2, ...] 
* result_file.txt
	* Search result from given terms :	
	nexus like love happy,  
	 asus repair,   
	 0(touch screen) fix repair,  
	 1(great tablet) 2(tablet fast),  
	 tablet
