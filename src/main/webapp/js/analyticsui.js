//d3.json("test.json", function(error, data) {

var data = [
    {tag: 'alice', values: [0,1,3,9, 8, 7]},
    {tag: 'bob', values: [0, 10, 7, 1, 1, 11]}
];

var margin = {top: 30, right: 80, bottom: 50, left: 50},
    width = 640 - margin.left - margin.right,
    height = 380 - margin.top - margin.bottom;


var x = d3.scale.linear()
    .domain([0, d3.max(data, function(d) { return d.values.length - 1; })])
    .range([0, width]);
            
var y = d3.scale.linear()
    .domain([d3.min(data, function(d) { return d3.min(d.values); }),
             d3.max(data, function(d) { return d3.max(d.values); })])
    .range([height, 0]);

var color = d3.scale.category10()
    .domain(d3.keys(data[0]).filter(function(key) { return key === "tag"; }));

var xAxis = d3.svg.axis()
    .scale(x)
    .tickFormat(d3.format('d'))
    .orient("bottom");

var yAxis = d3.svg.axis()
    .scale(y)
    .orient("left");

var line = d3.svg.line()
    .interpolate("basis")
    .x(function(d, i) { return x(i); })
    .y(function(d, i) { return y(d); });

var svg = d3.select("#mainAnalytics").append("svg")
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
      .text("Time");

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
      .text("Sentiment Score");

  // Append paths for each tab of interest we get back from the analyics controller      
  var tags = svg.selectAll(".tags")
      .data(data)
    .enter().append("g")
      .attr("class", "tags");
  tags.append("path")
      .attr("class", "line")
      .attr("d", function(d) { return line(d.values); })
      .style("stroke", function(d) { return color(d.name); });
