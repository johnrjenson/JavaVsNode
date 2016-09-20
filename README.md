# JavaVsNode

This is a project that is designed to analyze the efficiency of both Java and JavaScript when processing a large number of requests. 

Open project from pom.xml file in the Java IDE of choice

##Java

Open src/main/java/RequestLoadSimulator.java

Setup your simulation by setting the following constants at top of RequestLoadSimulator to the values desired for your test.

`NUMBER_OF_REQUESTS` - The number of requests that will be processed by the simulator
`THREAD_POOL_SIZE` - The size of the pool that will be used to process the requests
`AVG_QUERIES_PER_REQUEST` - The number of times the simulator will block per simulated request
`AVG_QUERY_TIME_MILLIS` - The number of times the simulator will block per simulated request
`NTH_PRIME_TO_FIND` - the computational load that will be processed on each request. Increase this to make the computation more difficult. 1500 is about 77 millis worth of work.

Run src/main/java/RequestLoadSimulator.java

##Node

Open src/main/js/RequestLoadSimulator.js

Setup your simulation by setting the following constants at top of RequestLoadSimulator to the values desired for your test.

`NUMBER_OF_REQUESTS` - The number of requests that will be processed by the simulator
`NUMBER_OF_PROCESSES` - The number of Node processes that will be used to process your request. Defaults to the number of CPUs detected.
`AVG_QUERIES_PER_REQUEST` - The number of times the simulator will block per simulated request
`AVG_QUERY_TIME_MILLIS` - The number of times the simulator will block per simulated request
`NTH_PRIME_TO_FIND` - the computational load that will be processed on each request. Increase this to make the computation more difficult. 1500 is about 77 millis worth of work.

Run src/main/java/RequestLoadSimulator.java
