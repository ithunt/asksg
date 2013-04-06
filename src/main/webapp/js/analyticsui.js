var margin = {top: 20, right: 80, bottom: 30, left: 50},
    width = 800 - margin.left - margin.right,
    height = 500 - margin.top - margin.bottom;

// var parseDate = d3.time.format("%Y%m%d").parse;

var x = d3.scale.linear()
    .range([0, width]);

var y = d3.scale.linear()
    .range([height, 0]);

// var color = d3.scale.category10();

var xAxis = d3.svg.axis()
    .scale(x)
    .orient("bottom");

var yAxis = d3.svg.axis()
    .scale(y)
    .orient("left");

var svg = d3.select(".mainAnalytics").append("svg")
    .attr("width", width + margin.left + margin.right)
    .attr("height", height + margin.top + margin.bottom)
  .append("g")
    .attr("transform", "translate(" + margin.left + "," + margin.top + ")");

d3.json("test.json", function(error, data) {
  alert("OPENING THE D3 DATA");
  console.log(data);

  // parse each of the times now??
  data.forEach(function(d) {
    console.log(d.values);
    d.values.forEach(function(t) {
      // console.log(t.time);
      // t.time = parseDate(t.time);
      // console.log(t.time);
    })
  });

  // Pull all of the times into a single collection
  scores = []
  times = []
  data.forEach(function(d) {
    console.log(d.values);
    d.values.forEach(function(t) {
      times = times.concat(t.time);
      scores = scores.concat({"time":t.time, "value":t.sentiment});
    })
  });

  // Set the x/y range
  console.log(times);
  console.log(scores);

  // Set the domains...
  x.domain(d3.extent(times));
  y.domain(d3.extent([-1,1]));

  console.log(x.domain());
    // [
    //   d3.min(data, function(word) { return d3.min(word.values, function(v) { return v.time; }); }),
    //   d3.max(data, function(word) { return d3.max(word.values, function(v) { return v.time; }); })
    // ]);
  //   d3.extent(times)
  // );


  console.log(d3.extent(times));

  svg.append("g")
      .attr("class", "x axis")
      .attr("transform", "translate(0," + height + ")")
      .call(xAxis)
      .append("text")
      .style("text-anchor", "end")
      .text("Time");

  svg.append("g")
      .attr("class", "y axis")
      .call(yAxis)
      .append("text")
      .attr("transform", "rotate(-90)")
      .attr("y", 6)
      .attr("dy", ".71em")
      .style("text-anchor", "end")
      .text("Sentiment Score");

  var line = d3.svg.line()
    .interpolate("basis")
    .x(function(d) { return x(d.time); })
    .y(function(d) { return y(d.value); });


// TODO: not parsing correctly...
  svg.selectAll(".line")
    .data(scores)
    .enter().append("path")
    .attr("class", "line")
    .attr("d", line);

  // svg.append("path")
  //   .datum(data[0].values)
  //   .attr("class", "line")
  //   .attr("d", line);

  // var city = svg.selectAll(".city")
  //     .data(cities)
  //     .enter().append("g")
  //     .attr("class", "city");

  // city.append("path")
  //     .attr("class", "line")
  //     .attr("d", function(d) { return line(d.values); })
  //     .style("stroke", function(d) { return color(d.name); });

  // city.append("text")
  //     .datum(function(d) { return {name: d.name, value: d.values[d.values.length - 1]}; })
  //     .attr("transform", function(d) { return "translate(" + x(d.value.date) + "," + y(d.value.temperature) + ")"; })
  //     .attr("x", 3)
  //     .attr("dy", ".35em")
  //     .text(function(d) { return d.name; });
});