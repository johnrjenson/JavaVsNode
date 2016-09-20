import java.util.concurrent.Callable;

class RequestProcessor implements Callable<Long> {

	private int workerNumber;
	private int numQueries;
	private long queryTime;
	private int nthPrimeToFind;
	private String pathToTestFile;
	private Callback onComplete;
	private StopWatch swTotal;

	public RequestProcessor(int workerNumber, int numQueries, long queryTime, int nthPrimeToFind, String pathToTestFile, Callback onComplete) {
		this.workerNumber = workerNumber;
		this.numQueries = numQueries;
		this.queryTime = queryTime;
		this.nthPrimeToFind = nthPrimeToFind;
		this.pathToTestFile = pathToTestFile;
		this.onComplete = onComplete;
		this.swTotal = new StopWatch();
		this.swTotal.start();
	}

	@Override
	public Long call() throws Exception {
		for (int i = 0; i < this.numQueries; i++) {
			//do the blocking call
			doQuery(i);
		}

		//do some work
		doSomeComputation();

		if(this.pathToTestFile != null) {
			getEveryNthChar();
		}


		long totalTime = swTotal.getTime();
		System.out.println("Request " + this.workerNumber + " took " + this.swTotal.getTime() + " millis to complete");

		this.onComplete.execute();

		return totalTime;
	}

	private void doQuery(int queryNumber) throws InterruptedException {
		StopWatch sw = new StopWatch();
		sw.start();
		Thread.sleep(this.queryTime);
//			System.out.println("Query " + workerNumber + "-" + queryNumber + " took " + sw.getTime() + " millis");
	}

	private void doSomeComputation() {
		StopWatch sw = new StopWatch();
		sw.start();
		FindNthPrime.find(this.nthPrimeToFind + this.workerNumber);
//			System.out.println("Computation " + workerNumber + " took " + sw.getTime() + " millis");
	}

	private void getEveryNthChar() {
		StopWatch sw = new StopWatch();
		sw.start();
		String result = FindNthCharInFile.getEveryNthChar(this.pathToTestFile, 50000 + this.workerNumber);
//		System.out.println("Result of nth char for worker " + workerNumber + " took " + sw.getTime() + " millis. This is the result " + result);
	}
}