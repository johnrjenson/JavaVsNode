public class FindNthPrime {
	public static long find(int n) {
		int primeCount = 0;
		long currentNumber = 0;
		while(primeCount < n) {
			currentNumber++;
			if(isPrime(currentNumber)) {
//				System.out.println(currentNumber);
				primeCount++;
			}
		}
		return currentNumber;
	}

	private static boolean isPrime(long candidate) {
		for (long divisor = 2; divisor < candidate -1; divisor++) {
			if(candidate % divisor == 0) {
				return false;
			}
		}
		return true;
	}
}
