function stripWordCount(data) {
	countData = [];
	var low = 0;
	var total = 0;
	for (var i = 0; i < data.length; i++) {
		var sum = 0;
		for (var j = 0; j < data[i].wordCounts.length; j++) {
			sum = sum + data[i].wordCounts[j];
		}
		total = total + sum;
		countData.push({size:sum * 2, text: data[i].topic});
	}

	// Bump up smaller numbers to the average size (so they're actually visible)
	var avg = total / data.length;
	for (var i = 0; i < data.length; i++) {
		if (countData[i].size < avg) {
			countData[i].size = avg;
		}
	}

	return countData;
}