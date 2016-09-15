public class StopWatch {
	private long startingMillis;
	public void start() {
		startingMillis = System.currentTimeMillis();
	}
	public long getTime() {
		return System.currentTimeMillis() - startingMillis;
	}
}
