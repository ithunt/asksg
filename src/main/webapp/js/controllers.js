// Create the conversation controller
function ConversationController($scope, $http) {
	// Feed some false data...
	var testConvo = angular.fromJson('[{"created":{"centuryOfEra":20,"chronology":{"base":{"approxMillisAtEpochDividedByTwo":31083597720000,"averageMillisPerMonth":2629746000,"averageMillisPerYear":31556952000,"averageMillisPerYearDividedByTwo":15778476000,"base":null,"daysInMonthMax":31,"daysInYearMax":366,"maxMonth":12,"maxYear":292278993,"minYear":-292275054,"minimumDaysInFirstWeek":4,"param":null,"zone":{"ID":"UTC","fixed":true}},"param":null,"zone":{"ID":"UTC","fixed":true}},"dayOfMonth":18,"dayOfWeek":5,"dayOfYear":18,"era":1,"hourOfDay":19,"localMillis":1358536706230,"millisOfDay":69506230,"millisOfSecond":230,"minuteOfHour":18,"monthOfYear":1,"secondOfMinute":26,"weekOfWeekyear":3,"weekyear":2013,"year":2013,"yearOfCentury":13,"yearOfEra":2013},"id":1,"modified":{"centuryOfEra":20,"chronology":{"base":{"approxMillisAtEpochDividedByTwo":31083597720000,"averageMillisPerMonth":2629746000,"averageMillisPerYear":31556952000,"averageMillisPerYearDividedByTwo":15778476000,"base":null,"daysInMonthMax":31,"daysInYearMax":366,"maxMonth":12,"maxYear":292278993,"minYear":-292275054,"minimumDaysInFirstWeek":4,"param":null,"zone":{"ID":"UTC","fixed":true}},"param":null,"zone":{"ID":"UTC","fixed":true}},"dayOfMonth":18,"dayOfWeek":5,"dayOfYear":18,"era":1,"hourOfDay":19,"localMillis":1358536706230,"millisOfDay":69506230,"millisOfSecond":230,"minuteOfHour":18,"monthOfYear":1,"secondOfMinute":26,"weekOfWeekyear":3,"weekyear":2013,"year":2013,"yearOfCentury":13,"yearOfEra":2013},"version":0}]');
	console.log(testConvo);

	// Extract the relevant information from the JSON data
	testConvo[0].created_at = new Date(testConvo[0].created.localMillis).toString('dddd, MMMM ,yyyy');
	testConvo[0].modified_at = new Date(testConvo[0].modified.localMillis).toString('dddd, MMMM ,yyyy');

	// Persist the test conversation data
	$scope.convos = testConvo;

//***********************************
// Throw in some dummy data for local tests
//***********************************
	/*$scope.convos = [
	 {"id" : 1,
	 "name": "Convo 1",
	 "service" : "twitter",
	 "msg": "MSG 1 contents",
	 "reply" : ""},
	 {"id" : 2,
	 "name": "Convo 2",
	 "service" : "facebook",
	 "msg": "MSG 2 contents",
	 "reply" : ""},
	 {"id" : 3,
	 "name": "Convo 3",
	 "service" : "reddit",
	 "msg": "MSG 3 contents",
	 "reply" :
	 [ {
	 "name": "Convo 2",
	 "msg": "Reply contents go here",
	 "reply" : ""
	 }
	 ]
	 }
	 ];
	 */

//***********************************
// Fetch the initial batch of conversations
//***********************************
	/*
	 $http.jsonp('/asksg/conversations').success(function(data){
	 $scope.convos = data;
	 });

	 $http.get('/asksg/conversations').success(function(data) {
	 $scope.convos = data;
	 });*/

//***********************************
// Set up the default conversation filters
//***********************************
	$scope.convoCategory = "";
	$scope.convoFilter = "";

	// Array to store the state of active conversation filters
	// We can hard-code this because it won't change...
	$scope.filterArray = Array();
	$scope.filterArray['email'] = false;
	$scope.filterArray['sms'] = false;
	$scope.filterArray['facebook'] = false;
	$scope.filterArray['twitter'] = false;
	$scope.filterArray['reddit'] = false;
	//console.log("set up the filter array");

//***********************************
// Fetch conversations from the server using HTTP request
//***********************************
	/*$http.get('../test/convos.json').then(function(data) {
	 console.log(data);
	 $scope.convos = data;
	 });*/

//***********************************
// Filter functions
//***********************************

	$scope.filterAll = function () {
		$scope.convoCategory = "";
	};

//***********************************
// End filter functions
//***********************************
}

// The menu controller 
//function MenuController($scope) {
//	$scope.userName = "Admin";
//}

// super good example of setting up the angular modules
// http://jsfiddle.net/ThomasBurleson/8Gzj9/
