import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class RequestLoadSimulator {

	public static final int NUMBER_OF_REQUESTS = 512;
	public static final int THREAD_POOL_SIZE = 70;
	public static final int AVG_QUERIES_PER_REQUEST = 6;
	public static final int AVG_QUERY_TIME_MILLIS = 60;
	public static final int NTH_PRIME_TO_FIND = 250; // increase this to make the computation more difficult. 1500 is about 77 millis worth of work.
	public static final String PATH_TO_TEST_FILE = "5mb.txt";

	public static void main(String[] args) throws InterruptedException {
		RequestLoadSimulator requestLoadSimulator = new RequestLoadSimulator();
		List<RequestProcessor> executors = requestLoadSimulator.setupSimulation();
		requestLoadSimulator.runSimulation(executors);
	}

	private List<RequestProcessor> setupSimulation() throws InterruptedException {
		List<RequestProcessor> executors = new ArrayList<RequestProcessor>();

		for (int i = 0; i < NUMBER_OF_REQUESTS; i++) {
			RequestProcessor requestProcessor = new RequestProcessor(i, AVG_QUERIES_PER_REQUEST, AVG_QUERY_TIME_MILLIS, NTH_PRIME_TO_FIND, PATH_TO_TEST_FILE, null);
			executors.add(requestProcessor);
		}

		System.out.println(NUMBER_OF_REQUESTS + " requests were created");

		return executors;
	}

	private void runSimulation(List<RequestProcessor> executors) throws InterruptedException {
		StopWatch sw = new StopWatch();

		sw.start();

		ExecutorService executorService = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
		executorService.invokeAll(executors);

		long totalMillis = sw.getTime();
		System.out.println();
		System.out.println("NUMBER_OF_REQUESTS: "+NUMBER_OF_REQUESTS);
		System.out.println("THREAD_POOL_SIZE: "+THREAD_POOL_SIZE);
		System.out.println("AVG_QUERIES_PER_REQUEST: "+AVG_QUERIES_PER_REQUEST);
		System.out.println("AVG_QUERY_TIME_MILLIS: "+AVG_QUERY_TIME_MILLIS);
		System.out.println("NTH_PRIME_TO_FIND: "+NTH_PRIME_TO_FIND);
		System.out.println("PATH_TO_TEST_FILE: "+PATH_TO_TEST_FILE);
		System.out.println();
		System.out.println("The program took "+totalMillis+" millis to process "+NUMBER_OF_REQUESTS+" requests which is "+new BigDecimal(((float)NUMBER_OF_REQUESTS/(float)totalMillis*(float)1000)).round(MathContext.DECIMAL32)+" requests per second");
		System.exit(0);
	}
}
