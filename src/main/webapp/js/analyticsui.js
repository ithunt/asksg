function contains(a, obj) {
    var i = a.length;
    while (i--) {
       if (a[i] === obj) {
           return true;
       }
    }
    return false;
}

// Render the analytics plots on the dashboard
function updateAnalytics(data) {
    console.log(data);

    // Clear the current graphs
    d3.select("svg").remove();

    var margin = {top: 50, right: 100, bottom: 120, left: 80},
        width = 640 - margin.left - margin.right,
        height = 400 - margin.top - margin.bottom;

    var colorscale = d3.scale.category10();

    // Pull out the time range
    // Relies on sorting in the dates array, and assumes all data objects have the same time span...
    var minDate = data[0].dates[0];
    var maxDate = data[0].dates[data[0].dates.length - 1];

    // var x = d3.time.scale().domain([minDate, maxDate]).range([0, width]);
    var x = d3.scale.linear().domain([minDate, maxDate]).range([0, width]);
    var y = d3.scale.linear()
        .domain([d3.min(data, function (d) {
            return d3.min(d.wordCounts);
        }),
            d3.max(data, function (d) {
                return d3.max(d.wordCounts);
            })])
        .range([height, 0]);

    var xAxis = d3.svg.axis()
        .scale(x)
        // .tickFormat(d3.format('d'))
        .tickFormat(function(d){return d3.time.format('%I:%M %d-%b')(new Date(d));})
        .orient("bottom");

    var yAxis = d3.svg.axis()
        .scale(y)
        .orient("left");

    var line = d3.svg.line()
        .interpolate("basis")
        .x(function (d, i) {
            return x(d.xVal);
        })
        .y(function (d, i) {
            return y(d.yVal);
        });

    // BUILD WORDCOUNT GRAPH

    var svg = d3.select("#countAnalytics").append("svg")
        .attr("width", width + margin.left + margin.right)
        .attr("height", height + margin.top + margin.bottom)
        .append("g")
        .attr("transform", "translate(" + margin.left + "," + margin.top + ")");

    // Append the x-axis and a label
    svg.append("g")
        .attr("class", "x axis")
        .attr("transform", "translate(0," + height + ")")
        .call(xAxis)
        .selectAll("text")  
            .style("text-anchor", "end")
            .attr("dx", "-.8em")
            .attr("dy", ".15em")
            .attr("transform", function(d) {
                return "rotate(-65)" 
                });
    svg.append("text")
        .attr("transform", "translate(" + (width / 2) + " ," + (height + margin.bottom) + ")")
        .style("text-anchor", "middle")
        .text("Date");

    // Append the y-axis and a label
    svg.append("g")
        .attr("class", "y axis")
        .call(yAxis);
    svg.append("text")
        .attr("transform", "rotate(-90)")
        .attr("y", 0 - margin.left) // 0 - margin.left
        .attr("x", 0 - ((height / 2) - margin.bottom + 80))
        .attr("dy", "1em")
        .style("text-anchor", "end")
        .text("Word Count");

    var lineData = [];
    data.forEach(function (topic, i) {
        var tmp = [];
        lastX = 0;
        lastY = 0;
        for (var i = 0; i < topic.dates.length; i++) {
            tmp.push({xVal: topic.dates[i], yVal: topic.wordCounts[i]});
            lastX = topic.dates[i];
            lastY = topic.wordCounts[i];
        }
        // Pull out the name and then get ready to plot them...
        topicName = topic.topic;

        lineData.push(tmp);
        console.log(lastX);
        console.log(x(lastX));
        console.log(lastY);
        console.log(y(lastY));
        console.log(topicName);

        svg.append("text")
            .attr("y", y(lastY)) // 0 - margin.left
            .attr("x", x(lastX + topicName.length))
            .attr("dy", "1em")
            // .attr("stroke", function(d, i) { return colorscale(i); });
            .style("text-anchor", "end")
            .text(topicName);
    });
    console.log(lineData);

    // Add the line data.
    svg.selectAll(".line").data(lineData)
        .enter().append("path")
        .attr("class", "line")
        .attr("d", line);

    // ******************************************
    // BUILD SEMANTIC GRAPH
    // ******************************************
    var x = d3.scale.linear().domain([minDate, maxDate]).range([0, width]);
    var y = d3.scale.linear()
        .domain([d3.min(data, function (d) {
            return d3.min(d.sentiments);
        }),
            d3.max(data, function (d) {
                return d3.max(d.sentiments);
            })])
        .range([height, 0]);

    var xAxis = d3.svg.axis()
        .scale(x)
        .tickFormat(function(d){return d3.time.format('%I:%M %d-%b')(new Date(d));})
        .orient("bottom");

    var yAxis = d3.svg.axis()
        .scale(y)
        .orient("left");

    var line = d3.svg.line()
        .interpolate("basis")
        .x(function (d, i) {
            return x(d.xVal);
        })
        .y(function (d, i) {
            return y(d.yVal);
        });

    var svg2 = d3.select("#semanticAnalytics").append("svg")
        .attr("width", width + margin.left + margin.right)
        .attr("height", height + margin.top + margin.bottom)
        .append("g")
        .attr("transform", "translate(" + margin.left + "," + margin.top + ")");

    // Append the x-axis and a label
    svg2.append("g")
        .attr("class", "x axis")
        .attr("transform", "translate(0," + height + ")")
        .call(xAxis)
        .selectAll("text")  
            .style("text-anchor", "end")
            .attr("dx", "-.8em")
            .attr("dy", ".15em")
            .attr("transform", function(d) {
                return "rotate(-65)" 
                });
    svg2.append("text")
        .attr("transform", "translate(" + (width / 2) + " ," + (height + margin.bottom) + ")")
        .style("text-anchor", "middle")
        .text("Date");

    // Append the y-axis and a label
    svg2.append("g")
        .attr("class", "y axis")
        .call(yAxis);
    svg2.append("text")
        .attr("transform", "rotate(-90)")
        .attr("y", 0 - margin.left) // 0 - margin.left
        .attr("x", 0 - ((height / 2) - margin.bottom + 80))
        .attr("dy", "1em")
        .style("text-anchor", "end")
        .text("Semantic Score");

    var lineData = [];
    data.forEach(function (topic, i) {
        var tmp = [];
        lastX = 0;
        lastY = 0;
        for (var i = 0; i < topic.dates.length; i++) {
            tmp.push({xVal: topic.dates[i], yVal: topic.sentiments[i]});
            lastX = topic.dates[i];
            lastY = topic.sentiments[i];
        }
        // Pull out the name and then get ready to plot them...
        topicName = topic.topic;

        lineData.push(tmp);

        svg2.append("text")
            .attr("y", y(lastY)) // 0 - margin.left
            .attr("x", x(lastX + topicName.length))
            .attr("dy", "1em")
            // .attr("stroke", function(d, i) { return colorscale(i); });
            .style("text-anchor", "end")
            .text(topicName);
    });

    // Add the semantic lines...
    svg2.selectAll(".line").data(lineData)
        .enter().append("path")
        .attr("class", "line")
        .attr("d", line);
}

