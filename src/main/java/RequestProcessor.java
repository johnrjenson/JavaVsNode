import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;

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
        final int[] count = {0};
        final long totalCount = this.numQueries;

        final CompletableFuture<Void> result = new CompletableFuture<Void>();

        final Object sync = new Object();
        final Timer timer = new Timer();
		for (int i = 0; i < this.numQueries; i++) {
			doQuery(timer, i).thenRun(new Runnable() {
                public void run() {
                    synchronized (sync) {
                        count[0]++;
                    }
                    if (count[0] == totalCount) {
                        result.complete(null);
                    }
                }
            });
		}

		result.get();

		//do some work
		doSomeComputation();

		if(this.pathToTestFile != null) {
			getEveryNthChar();
		}


		long totalTime = swTotal.getTime();
		System.out.println("Request " + this.workerNumber + " took " + this.swTotal.getTime() + " millis to complete");

		if(this.onComplete != null) {
			this.onComplete.execute();
		}

		return totalTime;
	}

	private CompletableFuture<Void> doQuery(Timer timer, int queryNumber) throws InterruptedException {
		final CompletableFuture<Void> responseFuture = new CompletableFuture<Void>();

		timer.schedule(new TimerTask() {
            @Override
            public void run() {
                responseFuture.complete(null);
            }
        }, 0, this.queryTime);

		return responseFuture;
	}

	private void doSomeComputation() {
		StopWatch sw = new StopWatch();
		sw.start();
		FindNthPrime.find(this.nthPrimeToFind + this.workerNumber);
//			System.out.println("Computation " + workerNumber + " took " + sw.getTime() + " millis");
	}

	private void getEveryNthChar() throws IOException {
		StopWatch sw = new StopWatch();
		sw.start();
		String result = FindNthCharInFile.getEveryNthChar(this.pathToTestFile, 50000 + this.workerNumber);
//		System.out.println("Result of nth char for worker " + workerNumber + " took " + sw.getTime() + " millis. This is the result " + result);
	}
}