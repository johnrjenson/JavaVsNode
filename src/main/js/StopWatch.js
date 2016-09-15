class StopWatch {
	start() {
		this.startingMillis = new Date().getTime();
	}

	getTime() {
		return new Date().getTime() - this.startingMillis;
	}
}

module.exports = StopWatch;