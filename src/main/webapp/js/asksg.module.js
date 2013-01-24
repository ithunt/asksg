/**
 * Message object definition.
 */
function Message(author, content, conversation) {
	this.author = author;
	this.content = content;
	this.conversation = conversation;
	this.analytics = null;
}

/**
 * Construct the conversation controller. :-)
 *
 * @param scope - the angular scope
 * @param asksg - the custom asksg service
 * @param log - the log (optional, not currently used)
 */
function ConversationController($scope, $asksg, $log) {
	//***********************************
	// Expose a function to post a message to response
	//***********************************
	/*$scope.postResponse = function(convo, messageId, message) {
		console.log("post response");
		console.log(convo.id);
		console.log(message);
	};*/

	// Set the user name
	$scope.userName = "Admin"; // this should be replaced by response from server

	//***********************************
	// Call the initial configuration functions
	//***********************************
	$asksg.fetchConvos(-1, true).
		success(function(data, status, headers, config) {
			console.log("Got conversation data back from the server...");
			console.log(data);
			// Parse the JSON response data
			var conversationList = angular.fromJson(data);
			for (var i = 0; i < conversationList.length; i++) {
				conversationList[i].created_at = new Date(conversationList[i].created.localMillis).toString('dddd, MMMM ,yyyy');
				conversationList[i].modified_at = new Date(conversationList[i].modified.localMillis).toString('dddd, MMMM ,yyyy');
			}

			console.log("updated conversations");
			console.log(conversationList);

			// Now return the result as an object
			$scope.convos = conversationList;
		}).
		error(function(data, status, headers, config) {
			return null;
		});


	/**
	 * Perform a message post
	 */
	$scope.doPostMessage = function(convo, messageId, message, author) {
		$asksg.postResponse(convo, messageId, message, author).
			success(function(data, status, headers, config) {
				console.log("Success");
				console.log(data);
				console.log(status);
			}).
			error(function(data, status, headers, config) {
				console.log("Error... :(");
			});
	};



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
			var convoSeedUrl = '/asksg/conversations/seed';
			var messageUrl = '/asksg/messages';
			var messageSeedUrl = '/asksg/messages/seed';

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
						$http.post(convoSeedUrl).then(function(resp) {
							console.log("Conversations seeded.");
						});
					} else {
						console.log("Conversations not seeded.");
					}

					return $http({method: 'GET', url: convoUrl});
				},

				/**
				 * Submit a message response to a conversation.
				 * 
				 * @param convo - the host conversation ID
				 * @param messageId - optional message, which indicates that this is a nested response (a la Reddit)
				 * @param message - the message to insert
				 */ 
				postResponse : function(convo, messageId, message, author) {
					console.log("Building message...");
					messageResp = new Message(author, message, convo);
					console.log(messageResp);

					// Return the HTTP response
					return $http({method: 'POST', url: messageUrl, data: JSON.stringify(messageResp)});
				}				
			};
		});
	});
}

// TODO: invoke the AsksgService constructor here...
console.log("Jumping into the constructor...");
AsksgService();


