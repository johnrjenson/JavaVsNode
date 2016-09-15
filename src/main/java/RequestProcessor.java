import java.util.concurrent.Callable;

class RequestProcessor implements Callable<Long> {

	private long workerNumber;
	private int numQueries;
	private long queryTime;
	private int nthPrimeToFind;
	private Callback onComplete;

	public RequestProcessor(long workerNumber, int numQueries, long queryTime, int nthPrimeToFind, Callback onComplete) {
		this.workerNumber = workerNumber;
		this.numQueries = numQueries;
		this.queryTime = queryTime;
		this.nthPrimeToFind = nthPrimeToFind;
		this.onComplete = onComplete;
	}

	@Override
	public Long call() throws Exception {
		StopWatch swTotal = new StopWatch();
		swTotal.start();

		for (int i = 0; i < numQueries; i++) {
			//do the blocking call
			doQuery(i);
		}

		//do some work
		doSomeComputation();


		long totalTime = swTotal.getTime();
		System.out.println("Request " + workerNumber + " took " + swTotal.getTime() + " millis to complete");

		onComplete.execute();
		return totalTime;
	}

	private void doQuery(int queryNumber) throws InterruptedException {
		StopWatch sw = new StopWatch();
		sw.start();
		Thread.sleep(queryTime);
//			System.out.println("Query " + workerNumber + "-" + queryNumber + " took " + sw.getTime() + " millis");
	}

	private void doSomeComputation() {
		StopWatch sw = new StopWatch();
		sw.start();
		FindNthPrime.find(nthPrimeToFind);
//			System.out.println("Computation " + workerNumber + " took " + sw.getTime() + " millis");
	}
}