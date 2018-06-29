# JavaVsNode

This is a project that is designed to analyze the efficiency of both Java and JavaScript when processing a large number of requests. 
This is supporting code for my blog post [Performance Comparison: Java vs Node](https://www.tandemseven.com/blog/performance-java-vs-node/). In my blog post I provide runtime results of this implementation as well as the justification for the implementation choices that I made. 
In short, I chose to simulate blocking IO for the Java implementation. Some may argue that using blocking IO isn’t a fair comparison of Java’s performance, but here is why I think it is both accurate and fair. First, Java’s JDBC spec remains a blocking spec. 
That means that whenever anyone connects to a relational database using a standard JDBC driver, they have to block. Secondly, Apache Tomcat 8.5 finished implementing the first non-blocking servlet spec only seven months ago in June of 2016, so that means that the overwhelming majority of production Java applications still block when they do IO. 
So since Java is being used in a blocking way currently by most organizations, I feel it is the most representative of the Java to block in these simulations as well. 

Open project from pom.xml file in the Java IDE of choice

##Java

Open src/main/java/RequestLoadSimulator.java

Setup your simulation by setting the following constants at top of RequestLoadSimulator to the values desired for your test.

`NUMBER_OF_REQUESTS` - The number of requests that will be processed by the simulator. 
`THREAD_POOL_SIZE` - The size of the pool that will be used to process the requests. 
`AVG_QUERIES_PER_REQUEST` - The number of times the simulator will block per simulated request. 
`AVG_QUERY_TIME_MILLIS` - The number of times the simulator will block per simulated request. 
`NTH_PRIME_TO_FIND` - the computational load that will be processed on each request. Increase this to make the computation more difficult. 1500 is about 77 millis worth of work. 
`PATH_TO_TEST_FILE` - point the code to a file that you want it to load and process on each request. The bigger the file, the more the IO and the more CPU will be used to process it. 

Run src/main/java/RequestLoadSimulator.java

##Node

Open src/main/js/RequestLoadSimulator.js

Setup your simulation by setting the following constants at top of RequestLoadSimulator to the values desired for your test.

`NUMBER_OF_REQUESTS` - The number of requests that will be processed by the simulator. 
`NUMBER_OF_PROCESSES` - The number of Node processes that will be used to process your request. Defaults to the number of CPUs detected. 
`AVG_QUERIES_PER_REQUEST` - The number of times the simulator will block per simulated request. 
`AVG_QUERY_TIME_MILLIS` - The number of times the simulator will block per simulated request. 
`NTH_PRIME_TO_FIND` - the computational load that will be processed on each request. Increase this to make the computation more difficult. 1500 is about 77 millis worth of work. 
`PATH_TO_TEST_FILE` - point the code to a file that you want it to load and process on each request. The bigger the file, the more the IO and the more CPU will be used to process it. 

Run src/main/js/RequestLoadSimulator.js
