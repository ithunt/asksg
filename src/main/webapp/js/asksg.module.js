// Create the conversation controller
function ConversationController($scope, $asksg, $log) {
	// Feed some false data...
	/*var testConvo = angular.fromJson('[{"created":{"centuryOfEra":20,"chronology":{"base":{"approxMillisAtEpochDividedByTwo":31083597720000,"averageMillisPerMonth":2629746000,"averageMillisPerYear":31556952000,"averageMillisPerYearDividedByTwo":15778476000,"base":null,"daysInMonthMax":31,"daysInYearMax":366,"maxMonth":12,"maxYear":292278993,"minYear":-292275054,"minimumDaysInFirstWeek":4,"param":null,"zone":{"ID":"UTC","fixed":true}},"param":null,"zone":{"ID":"UTC","fixed":true}},"dayOfMonth":18,"dayOfWeek":5,"dayOfYear":18,"era":1,"hourOfDay":19,"localMillis":1358536706230,"millisOfDay":69506230,"millisOfSecond":230,"minuteOfHour":18,"monthOfYear":1,"secondOfMinute":26,"weekOfWeekyear":3,"weekyear":2013,"year":2013,"yearOfCentury":13,"yearOfEra":2013},"id":1,"modified":{"centuryOfEra":20,"chronology":{"base":{"approxMillisAtEpochDividedByTwo":31083597720000,"averageMillisPerMonth":2629746000,"averageMillisPerYear":31556952000,"averageMillisPerYearDividedByTwo":15778476000,"base":null,"daysInMonthMax":31,"daysInYearMax":366,"maxMonth":12,"maxYear":292278993,"minYear":-292275054,"minimumDaysInFirstWeek":4,"param":null,"zone":{"ID":"UTC","fixed":true}},"param":null,"zone":{"ID":"UTC","fixed":true}},"dayOfMonth":18,"dayOfWeek":5,"dayOfYear":18,"era":1,"hourOfDay":19,"localMillis":1358536706230,"millisOfDay":69506230,"millisOfSecond":230,"minuteOfHour":18,"monthOfYear":1,"secondOfMinute":26,"weekOfWeekyear":3,"weekyear":2013,"year":2013,"yearOfCentury":13,"yearOfEra":2013},"version":0}]');
	console.log(testConvo);

	// Extract the relevant information from the JSON data
	testConvo[0].created_at = new Date(testConvo[0].created.localMillis).toString('dddd, MMMM ,yyyy');
	testConvo[0].modified_at = new Date(testConvo[0].modified.localMillis).toString('dddd, MMMM ,yyyy');

	// Persist the test conversation data
	$scope.convos = testConvo;*/

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
	/*$http.jsonp('/asksg/conversations').success(function(data){
        $scope.convos = data;
    });*/

	/*$http.get('/asksg/conversations').success(function(data) {
		$scope.convos = data;
	});*/

	//***********************************
	// Call the initial configuration functions
	//***********************************
	$asksg.fetchConvos(-1, true).then(function(response) {
        //var merge = (searchTerm == $scope.refreshURL);
        // Call private merge() method
        //angular.bind($scope, updateTweets)(response.items, merge);
        //$scope.countDown  = 180;
        //$scope.lastQuery  = $scope.query;
        //$scope.refreshURL = response.refreshURL;
        //$scope.searching  = false;

        // Save the conversation list
        console.log("Persisting the conversation list to the scope.");
        $scope.convos = response.conversations;
      });

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
	// Scope functons for handling user input (e.g. publish message)
	//***********************************
	$scope.postResponse = function(convoId, messageId, response) {
		$asksg.postResponse(convoId, messageId, response);
	};
}

/**
 * Create the ASKSG module for the dashboard app.
 */
AsksgService = function() {

	// Indicate that we're starting up...
	console.log("Starting the AsksgService...");

	// Create the ASKSG module
	angular.module('ASKSG', [], function($provide) {

		// Create and inject the asksg service into the ConversationController.
		$provide.factory('$asksg', function($http, $log) {

			// Store the service URLs locally here
			var convoUrl = '/asksg/conversations';
			var seedConvoUrl = '/asksg/conversations/seed';

			// Publish the $asksg API here
			return {
				/**
				 * Fetch all conversations with a specified ID (-1 or nil require us to fetch all of them...)
				 *
				 * @param convoId - target conversation to receive
				 * @return map of conversation data
				 */
				fetchConvos : function(convoId, seed) {
					// Check to see if we should seed the conversataion repository
					if (seed == true) {
						$http.post(seedConvoUrl).then(function(resp) {
							console.log("Conversation data seeded.");
						});
					}

					// Return an HTTP promise (a la Java future...)
					return $http.jsonp(convoUrl).then(function(resp) {

						console.log("Got conversation data back from the server...");

						// Parse the JSON response data
						// TODO: recursively parse the nested messages?
						var conversationList = angular.fromJson(resp);
						for (var i = 0; i < conversationList.length; i++) {
							conversationList[i].created_at = new Date(conversationList[i].created.localMillis).toString('dddd, MMMM ,yyyy');
							conversationList[i].modified_at = new Date(conversationList[i].modified.localMillis).toString('dddd, MMMM ,yyyy');
						}

						// Now return the result as an object
						return {
							conversations : conversationList
							// TODO: add more elements to this as needed
						};
					});
				}, // DO NOT FORGET THE COMMA >.<

				/**
				 * Submit a message response to a conversation.
				 * 
				 * @param convoId - the host conversation ID
				 * @param messageId - optional message, which indicates that this is a nested response (a la Reddit)
				 * @param msgResponse - the message to insert
				 */ 
				postResponse : function(convoId, messageId, msgResponse) {
					console.log("Submitting response to (c,m) = (" + convoId + "," + messageId + ")");

					// TODO: insert $http.post, http://docs.angularjs.org/api/ng.$http
				}
			};
		});
	});
}

// TODO: invoke the AsksgService constructor here...
console.log("Jumping into the constructor...");
AsksgService();


