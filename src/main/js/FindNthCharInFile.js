let fs = require('fs');
let q = require('q');

class FindNthCharInFile {
	static getEveryNthChar(pathToFile, n) {
		return FindNthCharInFile.getFileContents(pathToFile).then(function(fileContents) {
					let result = '';
					for (let i = 0; i < fileContents.length; i += n) {
						result += fileContents.charAt(i);
					}
					return result;
				}).catch(function(error) {
			console.log(error);
		});
	}

	static getFileContents(pathToFile) {
		let defer = q.defer();
		fs.readFile(pathToFile, 'utf8', function(error, data) {
			defer.resolve(data);
		});
		return defer.promise;
	}
}

module.exports = FindNthCharInFile;
