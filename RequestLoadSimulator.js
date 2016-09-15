let StopWatch = require('./StopWatch.js');
let FindNthPrime = require('./FindNthPrime.js');
let q = require('q');
let cluster = require('cluster');

const NUMBER_OF_REQUESTS = 512;
const AVG_QUERIES_PER_REQUEST = 6;
const AVG_QUERY_TIME_MILLIS = 60;

// increase this to make the computation more difficult. 1500 is about 77 millis worth of work.
const NTH_PRIME_TO_FIND = 4000;

var numComplete = 0;

function incrementNumComplete() {
	numComplete++;
}

class RequestProcessor {
	constructor(workerNumber) {
		this.workerNumber = workerNumber;
	}

	call() {
		let swTotal = new StopWatch();
		swTotal.start();

		var promises = [];
		for (let i = 0; i < AVG_QUERIES_PER_REQUEST; i++) {
			promises.push(this.doQuery(i));
		}

		let self = this;
		return q.all(promises)
				.then(function() {
					//do some work
					self.doSomeComputation();

					let totalTime = swTotal.getTime();
					console.log("Request " + self.workerNumber + " took " + swTotal.getTime() + " millis to complete");

					incrementNumComplete();

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
		}, AVG_QUERY_TIME_MILLIS);

		return defer.promise;
	}

	doSomeComputation() {
		let sw = new StopWatch();
		sw.start();

		FindNthPrime.find(NTH_PRIME_TO_FIND);

		//console.log("Computation " + this.workerNumber + " took " + sw.getTime() + " millis");
	}
}

function setupSimulation() {
	if (cluster.isMaster) {

		let numCPUs = require('os').cpus().length;

		console.log(numCPUs+' CPUs detected');

		let sw = new StopWatch();
		sw.start();

		let startingIndex = 0;
		let requestsPerCpu = Math.floor(NUMBER_OF_REQUESTS / numCPUs);

		// start worker processes.
		for (var i = 0; i < numCPUs; i++) {
			let worker = cluster.fork();
			worker.send({
				startingIndex: startingIndex,
				endingIndex: startingIndex + requestsPerCpu
			});

			startingIndex += requestsPerCpu;
		}

		let numWorkersComplete = 0;
		cluster.on('message', function(worker, message, handle) {
			if(message.done) {
				numWorkersComplete++;
				if(numWorkersComplete >= numCPUs) {
					let totalMillis = sw.getTime();

					console.log("The program took "+totalMillis+" millis to process "+NUMBER_OF_REQUESTS+" requests which is "+NUMBER_OF_REQUESTS/totalMillis*1000+" requests per second");
					process.exit();
				}
			}
		});

		// Listen for dying workers
		cluster.on('exit', function (worker) {
			console.log('Worker ' + worker.id + ' died.');
		});


	} else {
		process.on('message', function(message) {

			let executors = [];

			for (let i = message.startingIndex; i < message.endingIndex; i++) {
				executors.push(new RequestProcessor(i));
			}

			console.log(executors.length + " requests were created");

			runSimulation(executors);
		});
	}
}

function runSimulation(executors) {
	let sw = new StopWatch();

	sw.start();

	let promises = invokeAll(executors);

	q.all(promises)
			.then(function() {
				process.send({done:true});
			});
}

function invokeAll(executors) {
	let promises = [];
	for (var i = 0; i < executors.length; i++) {
		var executor = executors[i];
		promises.push(executor.call());
	}
	return promises;
}

setupSimulation();


