let StopWatch = require('./StopWatch');
let RequestProcessor = require('./RequestProcessor');
let q = require('q');
let cluster = require('cluster');
let os = require('os');

const NUMBER_OF_REQUESTS = 512;
const NUMBER_OF_PROCESSES = null; // defaults to the number of CPUs on the machine
const AVG_QUERIES_PER_REQUEST = 6;
const AVG_QUERY_TIME_MILLIS = 60;
const NTH_PRIME_TO_FIND = 700; // increase this to make the computation more difficult. 1500 is about 77 millis worth of work.
const PATH_TO_TEST_FILE = '1mb.txt';

function setupSimulation() {
	if (cluster.isMaster) {

		let numCPUs = os.cpus().length;

		let numProcesses = NUMBER_OF_PROCESSES || numCPUs;

		console.log(numCPUs+' CPUs detected. Creating '+numProcesses+' workers');

		let startingIndex = 0;
		let requestsPerCpu = Math.floor(NUMBER_OF_REQUESTS / numProcesses);


		let sw = new StopWatch();
		sw.start();

		// start worker processes.
		for (var i = 0; i < numProcesses; i++) {
			let worker = cluster.fork();
			worker.send({
				startingIndex: startingIndex,
				endingIndex: startingIndex + requestsPerCpu,
				workerNumber: worker.id
			});

			startingIndex += requestsPerCpu;
		}

		let numWorkersComplete = 0;
		cluster.on('message', function(worker, message, handle) {
			if(message.done) {
				numWorkersComplete++;
				if(numWorkersComplete >= numProcesses) {
					let totalMillis = sw.getTime();

					console.log('');
					console.log("NUMBER_OF_REQUESTS: "+NUMBER_OF_REQUESTS);
					console.log("NUMBER_OF_PROCESSES: "+numProcesses);
					console.log("AVG_QUERIES_PER_REQUEST: "+AVG_QUERIES_PER_REQUEST);
					console.log("AVG_QUERY_TIME_MILLIS: "+AVG_QUERY_TIME_MILLIS);
					console.log("NTH_PRIME_TO_FIND: "+NTH_PRIME_TO_FIND);
					console.log("PATH_TO_TEST_FILE: "+PATH_TO_TEST_FILE);
					console.log('');
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
				executors.push(new RequestProcessor(i, AVG_QUERIES_PER_REQUEST, AVG_QUERY_TIME_MILLIS, NTH_PRIME_TO_FIND, PATH_TO_TEST_FILE));
			}

			console.log(executors.length + " requests were created for worker " + message.workerNumber);

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


