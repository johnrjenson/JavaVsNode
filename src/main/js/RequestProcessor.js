let StopWatch = require('./StopWatch');
let FindNthPrime = require('./FindNthPrime');
let q = require('q');

class RequestProcessor {

	constructor(workerNumber, numQueries, queryTime, nthPrimeToFind, onComplete) {
		this.workerNumber = workerNumber;
		this.numQueries = numQueries;
		this.queryTime = queryTime;
		this.nthPrimeToFind = nthPrimeToFind;
		this.onComplete = onComplete;
	}

	call() {
		let swTotal = new StopWatch();
		swTotal.start();

		var promises = [];
		for (let i = 0; i < this.numQueries; i++) {
			promises.push(this.doQuery(i));
		}

		let self = this;
		return q.all(promises)
				.then(function() {
					//do some work
					self.doSomeComputation();

					let totalTime = swTotal.getTime();
					console.log("Request " + self.workerNumber + " took " + swTotal.getTime() + " millis to complete");

					self.onComplete();

					return totalTime;
				});
	}

	doQuery(queryNumber) {
		let defer = q.defer();

		let sw = new StopWatch();
		sw.start();

		let self = this;
		setTimeout(function() {
			//console.log("Query " + self.workerNumber + "-" + queryNumber + " took " + sw.getTime() + " millis");
			defer.resolve();
		}, self.queryTime);

		return defer.promise;
	}

	doSomeComputation() {
		let sw = new StopWatch();
		sw.start();

		FindNthPrime.find(this.nthPrimeToFind);

		//console.log("Computation " + this.workerNumber + " took " + sw.getTime() + " millis");
	}
}

module.exports = RequestProcessor;
