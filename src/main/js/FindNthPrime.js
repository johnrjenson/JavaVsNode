class FindNthPrime {
	static find (n) {
		let primeCount = 0;
		let currentNumber = 0;
		while(primeCount < n) {
			currentNumber++;
			if(FindNthPrime.isPrime(currentNumber)) {
				//console.log(currentNumber);
				primeCount++;
			}
		}
		return currentNumber;
	}

	static isPrime(candidate) {
	for (let divisor = 2; divisor < candidate - 1; divisor++) {
		if(candidate % divisor == 0) {
			return false;
		}
	}
		return true;
	}
}

module.exports = FindNthPrime;
