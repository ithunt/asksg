// TODO: comment later...
function updateAnalytics(data) {
  console.log(data);

    // Clear the current graphs
    d3.select("svg").remove();

  var margin = {top: 30, right: 80, bottom: 50, left: 50},
      width = 640 - margin.left - margin.right,
      height = 380 - margin.top - margin.bottom;

  // Pull out the time range
  // Relies on sorting in the dates array, and assumes all data objects have the same time span...
  var minDate = data[0].dates[0];
  var maxDate = data[0].dates[data[0].dates.length - 1];
  console.log(minDate);
  console.log(maxDate);

  // var x = d3.time.scale().domain([minDate, maxDate]).range([0, width]);
  var x = d3.scale.linear().domain([minDate, maxDate]).range([0, width]);
  var y = d3.scale.linear()
      .domain([d3.min(data, function(d) { return d3.min(d.wordCounts); }),
               d3.max(data, function(d) { return d3.max(d.wordCounts); })])
      .range([height, 0]);

  var xAxis = d3.svg.axis()
      .scale(x)
      .tickFormat(d3.format('d'))
      .orient("bottom");

  var yAxis = d3.svg.axis()
      .scale(y)
      .orient("left");

  var line = d3.svg.line()
      .interpolate("basis")
      .x(function(d, i) { return x(d.xVal); }) 
      .y(function(d, i) { return y(d.yVal); });

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
        .call(xAxis);
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
        .attr("x",0 - ((height / 2) - margin.bottom))
        .attr("dy", "1em")
        .style("text-anchor", "end")
        .text("Word Count");

    var lineData = [];
    data.forEach(function(topic, i) { 
      var tmp = [];
      lastX = 0;
      lastY = 0;
      for (var i = 0; i < topic.dates.length; i++) {
        tmp.push({xVal:topic.dates[i], yVal:topic.wordCounts[i]});
        lastX = topic.dates[i];
        lastY = topic.wordCounts[i];
      }
      lineData.push(tmp);

      // Pull out the name and then get ready to plot them...
      topicName = topic.topic;

      console.log(lastX);
      console.log(x(lastX));
      console.log(lastY);
      console.log(y(lastY));
      console.log(topicName);

      svg.append("text")
        .attr("transform", "rotate(-90)")
        .attr("y", y(lastY)) // 0 - margin.left
        .attr("x", x(lastX))
        .attr("dy", "1em")
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
      .domain([d3.min(data, function(d) { return d3.min(d.sentiments); }),
               d3.max(data, function(d) { return d3.max(d.sentiments); })])
      .range([height, 0]);

  var xAxis = d3.svg.axis()
      .scale(x)
      .tickFormat(d3.format('d'))
      .orient("bottom");

  var yAxis = d3.svg.axis()
      .scale(y)
      .orient("left");

  var line = d3.svg.line()
      .interpolate("basis")
      .x(function(d, i) { return x(d.xVal); }) 
      .y(function(d, i) { return y(d.yVal); });

  var svg = d3.select("#semanticAnalytics").append("svg")
      .attr("width", width + margin.left + margin.right)
      .attr("height", height + margin.top + margin.bottom)
    .append("g")
      .attr("transform", "translate(" + margin.left + "," + margin.top + ")");

    // Append the x-axis and a label
    svg.append("g")
        .attr("class", "x axis")
        .attr("transform", "translate(0," + height + ")")
        .call(xAxis);
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
        .attr("x",0 - ((height / 2) - margin.bottom))
        .attr("dy", "1em")
        .style("text-anchor", "end")
        .text("Semantic Score");

    var lineData = [];
    data.forEach(function(topic, i) { 
      var tmp = [];
      lastX = 0;
      lastY = 0;
      for (var i = 0; i < topic.dates.length; i++) {
        tmp.push({xVal:topic.dates[i], yVal:topic.sentiments[i]});
        lastX = topic.dates[i];
        lastY = topic.sentiments[i];
      }
      lineData.push(tmp);

      // Pull out the name and then get ready to plot them...
      topicName = topic.topic;

      console.log(lastX);
      console.log(x(lastX));
      console.log(lastY);
      console.log(y(lastY));
      console.log(topicName);

      svg.append("text")
        .attr("transform", "rotate(-90)")
        .attr("y", y(lastY)) // 0 - margin.left
        .attr("x", x(lastX))
        .attr("dy", "1em")
        .style("text-anchor", "end")
        .text(topicName);
    });
    console.log(lineData);

    // Add the semantic lines...
    svg.selectAll(".line").data(lineData)
    .enter().append("path")
      .attr("class", "line")
      .attr("d", line);
}

