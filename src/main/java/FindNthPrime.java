public class FindNthPrime {
	public static int find(int n) {
		int primeCount = 0;
		int currentNumber = 0;
		while(primeCount < n) {
			currentNumber++;
			if(isPrime(currentNumber)) {
//				System.out.println(currentNumber);
				primeCount++;
			}
		}
		return currentNumber;
	}

	private static boolean isPrime(int candidate) {
		for (int divisor = 2; divisor < candidate -1; divisor++) {
			if(candidate % divisor == 0) {
				return false;
			}
		}
		return true;
	}
}
