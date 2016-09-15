import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class RequestLoadSimulator {

	public static final long NUMBER_OF_REQUESTS = 512;
	public static final int THREAD_POOL_SIZE = 300;
	public static final int AVG_QUERIES_PER_REQUEST = 6;
	public static final int AVG_QUERY_TIME_MILLIS = 60;

	// increase this to make the computation more difficult. 1500 is about 77 millis worth of work.
	public static final int NTH_PRIME_TO_FIND = 4000;

	private int numComplete = 0;

	public static void main(String[] args) throws InterruptedException {
		RequestLoadSimulator requestLoadSimulator = new RequestLoadSimulator();
		List<RequestProcessor> executors = requestLoadSimulator.setupSimulation();
		requestLoadSimulator.runSimulation(executors);
	}

	private List<RequestProcessor> setupSimulation() throws InterruptedException {
		List<RequestProcessor> executors = new ArrayList<RequestProcessor>();

		for (long i = 0; i < NUMBER_OF_REQUESTS; i++) {
			RequestProcessor requestProcessor = new RequestProcessor(i, AVG_QUERIES_PER_REQUEST, AVG_QUERY_TIME_MILLIS, NTH_PRIME_TO_FIND, new Callback() {
				@Override
				public void execute() {
					incrementNumComplete();
				}
			});
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

		synchronized (RequestLoadSimulator.class) {
			while (numComplete < NUMBER_OF_REQUESTS) {
				RequestLoadSimulator.class.wait();
			}
			long totalMillis = sw.getTime();
			System.out.println("The program took "+totalMillis+" millis to process "+NUMBER_OF_REQUESTS+" requests which is "+new BigDecimal(((float)NUMBER_OF_REQUESTS/(float)totalMillis*(float)1000)).round(MathContext.DECIMAL32)+" requests per second");
			System.exit(0);
		}
	}

	private void incrementNumComplete() {
		synchronized (RequestLoadSimulator.class) {
			numComplete++;
		}

		RequestLoadSimulator.class.notifyAll();
	}

}
