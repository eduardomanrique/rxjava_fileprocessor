# Time Series fetching and calculations

## Prerequisites
Maven, java8

## How to build
-To build the project, on a terminal go the root folder of the project, and run:

mvn clean install

## How to RUN
-To run the project, on a terminal go the root folder of the project, and run: 

mvn spring-boot:run

## Adding files to process
  After running, a folder with name "to_process" on the project's root folder will be created. To process a file 
JUST ADD FILES TO THIS FOLDER. The processed files will be moved to the folder "processed" after processed.
  To change the "to_process" and "processed" folder names, edit the properties "source.dir" and "dest.dir" of the file
"src/resources/application.properties".
 
## About the project

  The project build using SpringBoot, JPA, lombok and RXJava. The RXJava is a reactive java library I used to allow multpile 
different algorithms to run in a async manner an the same stream of a file. A simple non daemon thread keeps checking the source dir folder 
to see if there are more files to process. Lombok is a java preprocessor that uses annotations to generate getters, setters
log etc. 
  The JPA is used in conjunction with spring repositories and spring services with a derby embedded db. The spring service 
has a cacheable operation with TTL of 5 seconds. The configuration for the cache are the properties:
 
  -src/resources/application.properties: all properties starting with "spring.cache"
     
### Algorithms
  To create new algorithms just implement the interface "com.eduardomanrique.tsrd.datasource.Algorithm". Spring will 
automatically add the algorithm to the processes. The implemented class will receive an observable each time a new file
is added to be processed. With the observable, you can filter, scan, and subscribe. The filters and scanning used in 
the algorithm is only visible to the algorithm itself.
  There are 5 algorithms. They are located in the package "com.eduardomanrique.tsrd.algorithms":
  
  -Instrument1Algorithm: Filters instruments INSTRUMENT1 and find the mean value
  
  -Instrument2Algorithm: Filters instruments INSTRUMENT2 which have date greater then NOV/2014 and find the mean value
  
  -Instrument3Algorithm: Filters instruments INSTRUMENT3 and find the min and max values
  
  -LoggingAlgorithm: This algorithm is used just to print the processing lines of the files
  
  -OtherInstrumentsAlgorithm: Filters instruments different of INSTRUMENT1, INSTRUMENT2 and INSTRUMENT3 and get the 
  sum of the 10 last values. This algorithm does not use takeLast from rxjava because although the instruments are
  ordered by date, they are firstly ordered by instrument name.
  
### Global filters
  To create global filters implement the interface com.eduardomanrique.tsrd.datasource.Filter. Spring will automatically 
add to the processes. The filter will be applied before sending to algorithms. There is one global filter:
  
  -com.eduardomanrique.tsrd.preprocessors.BusinessDaysFilter: Filter business days.
  
### Global modifiers
  To create a modifier implement the interface com.eduardomanrique.tsrd.datasource.Modifier. Spring will automatically
add the modifier to the processes. The modifier will run on a map function and is able to change the events. As the 
events are immutable, so if needed a new event must be created from the old one. There is one modifier:
  
  -com.eduardomanrique.tsrd.preprocessors.PriceModifier: This modifiers tries to get a record from 
  instrument_price_modifier table and if found, changes the instrument by multiplying its value by the multiplier column.
  
### Project structure
  
  Main source (src/main/java):
  
  -com.eduardomanrique.tsrd.algorithms.*: Algorithms that process the file
  -com.eduardomanrique.tsrd.algorithms.helper.*: Common helper classes for algorithm. 
  -com.eduardomanrique.tsrd.config.*: SpringBoot configuration class
  -com.eduardomanrique.tsrd.datasource.*: The processing framework
  -com.eduardomanrique.tsrd.entities.*: JPA entities
  -com.eduardomanrique.tsrd.preprocessors.*: Global filters and modifiers
  -com.eduardomanrique.tsrd.repositories.*: Spring repositires
  -com.eduardomanrique.tsrd.services.*: Spring services
  -com.eduardomanrique.tsrd.util.*: Utilitary classes
  -com.eduardomanrique.tsrd.TsrdApplication: Main class 
  
  Main resources (src/main/resources)
  
  -sql/*: ddl and dml database scripts
  -/application.properties: Spring properties file
  
  Test sources (src/test/java)
  
  -com.eduardomanrique.tsrd.algorithms.*: Unit tests for algorithms classes
  -com.eduardomanrique.tsrd.integratedtests.*: Integrated tests. Simulates a complete test over the application
  -com.eduardomanrique.tsrd.preprocessors.*: Unit tests for filters and modifiers.
  
  Test resources (src/test/resources)
  
  -application-test.properties: configuration for running tests
  -example_input.txt: sample file to run the integrated test
  
  
              