// Create the conversation controller
function ConversationController($scope, $asksg, $log) {
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
	// Filter function for the conversations
	//***********************************
	$scope.filterConvo = function(convo) {
		//console.log(convo.service);
		//console.log($scope.filterArray[convo.service]);
		return !($scope.filterArray[convo.service]);
    };

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

	$scope.filterAll = function() {
		$scope.convoCategory = "";
	};

	$scope.filterEmail = function() {
		//$scope.convoCategory = "EMAIL";
		if ($scope.filterArray['email']) {
			$scope.filterArray['email'] = false;
		} else {
			$scope.filterArray['email'] = true;
		}
	};

	$scope.filterSms = function() {
		//$scope.convoCategory = "SMS";
		if ($scope.filterArray['sms']) {
			$scope.filterArray['sms'] = false;
		} else {
			$scope.filterArray['sms'] = true;
		}
	};

	$scope.filterFacebook = function() {
		//$scope.convoCategory = "FACEBOOK";
		if ($scope.filterArray['facebook']) {
			$scope.filterArray['facebook'] = false;
		} else {
			$scope.filterArray['facebook'] = true;
		}
	};

	$scope.filterTwitter = function() {
		//$scope.convoCategory = "TWITTER";
		if ($scope.filterArray['twitter']) {
			$scope.filterArray['twitter'] = false;
		} else {
			$scope.filterArray['twitter'] = true;
		}
	};

	$scope.filterReddit = function() {
		//$scope.convoCategory = "REDDIT";
		if ($scope.filterArray['reddit']) {
			$scope.filterArray['reddit'] = false;
		} else {
			$scope.filterArray['reddit'] = true;
		}
	};

	//***********************************
	// End filter functions
	//***********************************
}

/**
 * Create the ASKSG module for the dashboard app.
 */
AsksgService = function() {
	// Create the ASKSG module
	angular.module('ASKSG', [], function($provide) {

		// Create and inject the asksg service into the ConversationController.
		$provide.factory('$asksg', function($http, $log) {

			// Store the service URLs locally here
			var convoUrl('/asksg/conversations');

			// Publish the $asksg API here
			return {
				fetchConvos : function(convoId) {

					// Return an HTTP promise (a la Java future...)
					return $http.jsonp(convoUrl).then(function(resp) {

						// Parse the JSON response data
						// TODO: recursively parse the nested messages?
						var conversationList = angular.fromJson(resp);
						for (var i = 0; i < conversationList.length; i++) {
							conversationList[i].created_at = new Date(conversationList[i].created.localMillis).toString('dddd, MMMM ,yyyy');
							conversationList[i].modified_at = new Date(conversationList[i].modified.localMillis).toString('dddd, MMMM ,yyyy');
						}

						// Now return the result as a map
						return {
							conversations : conversationList
							// TODO: add more elements to this as needed
						}
					});
				}
			}
		});

	});
}

// TODO: invoke the AsksgService constructor here...
//AsksgService();


