let StopWatch = require('./StopWatch');
let FindNthPrime = require('./FindNthPrime');
let FindNthCharInFile = require('./FindNthCharInFile');
let q = require('q');

class RequestProcessor {

	constructor(workerNumber, numQueries, queryTime, nthPrimeToFind, pathToTestFile, onComplete) {
		this.workerNumber = workerNumber;
		this.numQueries = numQueries;
		this.queryTime = queryTime;
		this.nthPrimeToFind = nthPrimeToFind;
		this.pathToTestFile = pathToTestFile;
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
		let defer = q.defer();
		q.all(promises)
				.then(function() {
					//do some work
					self.doSomeComputation();
					self.getEveryNthChar().then(function() {
								let totalTime = swTotal.getTime();
								console.log("Request " + self.workerNumber + " took " + swTotal.getTime() + " millis to complete");
								if(self.onComplete){
									self.onComplete();
								}
								defer.resolve(totalTime);
							});
				});

		return defer.promise;
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

		FindNthPrime.find(this.nthPrimeToFind + this.workerNumber);

		//console.log("Computation " + this.workerNumber + " took " + sw.getTime() + " millis");
	}

	getEveryNthChar() {
	let sw = new StopWatch();
	sw.start();

		let self = this;
		if(this.pathToTestFile != null) {
			return FindNthCharInFile.getEveryNthChar('../../../'+this.pathToTestFile, 50000 + this.workerNumber).then(function(result) {
						//console.log("Result of nth char for worker " + self.workerNumber + " took " + sw.getTime() + " millis. This is the result " + result);
					});
		} else {
			let defer = q.defer();
			defer.resolve();
			return defer.promise;
		}

}
}

module.exports = RequestProcessor;
