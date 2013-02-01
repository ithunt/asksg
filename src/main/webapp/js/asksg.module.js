/**
 * Message object constructor.
 */
function Message(author, content, conversationId) {
	this.author = author;
	this.content = content;
	this.conversationId = conversationId;
}

/**
 * MessageRespo object constructor.
 */
function MessageResp(author, content, conversation) {
	this.author = author;
	this.content = content;
	this.conversation = conversation;
	this.analytics = null;
	this.posted = false; 
}

/**
 * Conversation object constructor.
 */
function Conversation(id, author, subject, snippet, messages, created, modified) {
	this.id = id;
	this.author = author;
	this.subject = subject;
	this.created_at = created;
	this.modified_at = modified;

	// Run through the messages list and create the appropriate Message objects
	this.messages = new Array();
	for (var i = 0; i < messages.length; i++) {
		this.messages[i] = new Message(messages[i].author, messages[i].content, id);
	}

	// The message snippet
	this.snippet = snippet;
}

/**
 * Construct the main page controller. >.<
 */
function MainController($scope, $asksg, $log) {

	// Defaults until entered otherwise on the main page...
	$scope.username = "";
	$scope.password = "";

	/**
	 * Handle the login events.
	 */
	$scope.login = function () {
		console.log("username and password: " + $scope.username + ", " + $scope.password);
        //$scope.$emit('event:loginRequest', $scope.username, $scope.password);
    }
}

/**
 * Construct the conversation controller. :-)
 *
 * @param scope - the angular scope
 * @param asksg - the custom asksg service
 * @param log - the log (optional, not currently used)
 */
function ConversationController($scope, $asksg, $log) {
	/*
	 * Call the initial configuration functions
	 */
	$scope.refreshConvos = function() {
		$asksg.fetchConvos(-1, false). // don't seed every time...
			success(function(data, status, headers, config) {
				console.log("Got conversation data back from the server...");
				console.log(data);

				// Parse the JSON response data and create a list of Conversation objects to store in the scope
				console.log(data);
				$scope.convos = new Array();
				$scope.convoMap = new Array();
				for (var i = 0; i < data.length; i++) {
					var conversation = angular.fromJson(data[i]);

					var createdDate = new Date(conversation.created.localMillis);
					var modifiedDate = new Date(conversation.modified.localMillis);

					// Create the object and store it
					$scope.convos[i] = new Conversation(conversation.id,
						conversation.author, conversation.subject,
						conversation.snippet, conversation.messages,
						conversation.createdDate, conversation.modifiedDate);
					$scope.convoMap[conversation.id] = $scope.convos[i];

					//conversationList[i].created_at = (createdDate.getMonth() + 1) + "/" + createdDate.getDate() + "/" + createdDate.getFullYear();
					//conversationList[i].modified_at = (modifiedDate.getMonth() + 1) + "/" + modifiedDate.getDate() + "/" + modifiedDate.getFullYear();
				}

				// Now return the result as an object
				//$scope.convos = conversationList;
			}).
			error(function(data, status, headers, config) {
				console.log("Failed refreshing convos...");
				return null;
			});	
	}

	/**
	 * Perform a message post...
	 */
	$scope.doPostMessage = function(convo, messageId, message, author) {
		// post the message - on success re-fetch the conversations so the most up-to-date convos are viewed
		$asksg.postResponse(convo, messageId, message, author).
			success(function(data, status, headers, config) {
				console.log("Success");
				console.log(data);
				console.log(status);

				// Refresh the conversation stuff...
				$scope.refreshConvos();
			}).
			error(function(data, status, headers, config) {
				console.log("Error... :(");
			});
	};

	// Set the user name
	$scope.userName = "Admin"; // this should be replaced by response from server
	$scope.refreshConvos();

	/*
	 * Set up the default conversation filters.
	 */
	$scope.convoCategory = "";
	$scope.convoFilter = "";

	// Array to store the state of active conversation filters
	$scope.filterConvoArray = Array();
	$scope.filterConvoArray['email'] = false;
	$scope.filterConvoArray['sms'] = false;
	$scope.filterConvoArray['facebook'] = false;
	$scope.filterConvoArray['twitter'] = false;
	$scope.filterConvoArray['reddit'] = false;

	// Array to store the state of the active tag filters
	$scope.filterTagArray = Array();
	$scope.filterTagArray['read'] = false;
	$scope.filterTagArray['unread'] = false;

	/*
	 * Filter function for the conversations.
	 */
	$scope.filterConvo = function(convo) {
		//console.log(convo.service);
		//console.log($scope.filterArray[convo.service]);
		return !($scope.filterConvoArray[convo.service]);
    };

    /*
	 * Filter function for the conversations.
	 */
	$scope.filterTag = function(tag) {
		//console.log(convo.service);
		//console.log($scope.filterArray[convo.service]);
		return !($scope.filterTagArray[tag]);
    };

	/*
	 * Filter functions...
	 */
	$scope.filterAll = function() {
		$scope.convoCategory = "";
	};
	$scope.filterEmail = function() {
		if ($scope.filterConvoArray['email']) {
			$scope.filterConvoArray['email'] = false;
		} else {
			$scope.filterConvoArray['email'] = true;
		}
	};
	$scope.filterSms = function() {
		if ($scope.filterConvoArray['sms']) {
			$scope.filterConvoArray['sms'] = false;
		} else {
			$scope.filterConvoArray['sms'] = true;
		}
	};
	$scope.filterFacebook = function() {
		if ($scope.filterConvoArray['facebook']) {
			$scope.filterConvoArray['facebook'] = false;
		} else {
			$scope.filterConvoArray['facebook'] = true;
		}
	};
	$scope.filterTwitter = function() {
		if ($scope.filterConvoArray['twitter']) {
			$scope.filterConvoArray['twitter'] = false;
		} else {
			$scope.filterConvoArray['twitter'] = true;
		}
	};
	$scope.filterReddit = function() {
		if ($scope.filterConvoArray['reddit']) {
			$scope.filterConvoArray['reddit'] = false;
		} else {
			$scope.filterConvoArray['reddit'] = true;
		}
	};
	$scope.filterRead = function() {
		if ($scope.filterTagArray['read']) {
			$scope.filterTagArray['read'] = false;
		} else {
			$scope.filterTagArray['read'] = true;
		}
	};
	$scope.filterUnread = function() {
		if ($scope.filterTagArray['unread']) {
			$scope.filterTagArray['unread'] = false;
		} else {
			$scope.filterTagArray['unread'] = true;
		}
	};

	/*
	 * Scope functons for handling user input (e.g. publish message).
	 */
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
				postResponse : function(convo, message, author) {
					console.log("Building message...");
					messageResp = new MessageResp(author, message, convo);
					console.log(messageResp);

					// Return the HTTP response
					return $http({method: 'POST', url: messageUrl, data: JSON.stringify(messageResp)});
				}				
			};
		});
	});
}

// Invoke the ASKSG service constructor...
console.log("Jumping into the constructor...");
AsksgService();

