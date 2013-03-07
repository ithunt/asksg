/**
 * Message object constructor.
 */
function Message(author, content, conversationId) {
    this.author = author;
    this.content = content;
    this.conversationId = conversationId;
}

/**
 * MessageResp object constructor.
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
    this.active = false;

    // Run through the messages list and create the appropriate Message objects
    this.messages = new Array();
    for (var i = messages.length - 1; i >= 0; i--) {
        this.messages[i] = new Message(messages[i].author, messages[i].content, id);
    }

    // The message snippet
    this.snippet = snippet;

    // Function to set this conversation as "active" in the UI
    this.setActive = function (flag) {
        console.log("toggling a convo...");
        this.active = flag;
    }
}

/**
 * Social subscription provider object constructor.
 */
function SocialSubscription(handle, provider, person, url, createdBy) {
    this.handle = handle;
    this.provider = provider;
    this.person = person;
    this.url = url;
    this.createdBy = createdBy;
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
     * Refresh the set of conversations on the page
     */
    $scope.refreshConvos = function () {
        $asksg.fetchConvos(-1, false). // don't seed every time...
            success(function (data, status, headers, config) {
                console.log("Got conversation data back from the server...");

                // Parse the JSON response data and create a list of Conversation objects to store in the scope
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
                }
            }).
            error(function (data, status, headers, config) {
                console.log("Failed refreshing convos...");
                return null;
            });
    }

    /*
     * Populate the social subscription content
     */
    $scope.refreshSubscriptions = function () {
        $asksg.fetchSubscriptions().
            success(function (data, status, headers, config) {
                console.log("Got social data back from the server");

                // Parse the JSON response data and create a list of Conversation objects to store in the scope
                console.log(data);
            }).
            error(function (data, status, headers, config) {
                console.log("Failed grabbing the social subscriptions");
                return null;
            });
    }

    /**
     * Invoke the ASKSG message post service
     */
    $scope.doPostMessage = function (convo, messageId, message, author) {
        // post the message - on success re-fetch the conversations so the most up-to-date convos are viewed
        $asksg.postResponse(convo, messageId, message, author).
            success(function (data, status, headers, config) {
                console.log("Success");
                console.log(data);
                console.log(status);

                // Refresh the conversation stuff...
                $scope.refreshConvos();
            }).
            error(function (data, status, headers, config) {
                console.log("Error... :(");
            });
    };

    /*
     * Delete a conversation.
     */
    $scope.deleteConvo = function (convoId) {
        console.log("TODO: send HTML delete to server for convo id = " + convoId);
    };

    /*
     * Determine if a conversation is active.
     */
    $scope.isConvoActive = function (convoId) {
        return $scope.convoMap[convoId].active;
    }

    /*
     * Determine if a conversation is active (hovered over).
     */
    //$scope.active = function(convoId) {
    //	console.log("TODO: send HTML delete to server for convo id = " + convoId);
    //};

    // Set the user name
    $scope.userName = "Admin"; // this should be replaced by response from server

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
    $scope.filterConvo = function (convo) {
        //console.log(convo.service);
        //console.log($scope.filterArray[convo.service]);
        return !($scope.filterConvoArray[convo.service]);
    };

    /*
     * Filter function for the conversations.
     */
    $scope.filterTag = function (tag) {
        //console.log(convo.service);
        //console.log($scope.filterArray[convo.service]);
        return !($scope.filterTagArray[tag]);
    };

    /*
     * Filter functions...
     */
    $scope.filterAll = function () {
        $scope.convoCategory = "";
    };
    $scope.filterEmail = function () {
        if ($scope.filterConvoArray['email']) {
            $scope.filterConvoArray['email'] = false;
        } else {
            $scope.filterConvoArray['email'] = true;
        }
    };
    $scope.filterSms = function () {
        if ($scope.filterConvoArray['sms']) {
            $scope.filterConvoArray['sms'] = false;
        } else {
            $scope.filterConvoArray['sms'] = true;
        }
    };
    $scope.filterFacebook = function () {
        if ($scope.filterConvoArray['facebook']) {
            $scope.filterConvoArray['facebook'] = false;
        } else {
            $scope.filterConvoArray['facebook'] = true;
        }
    };
    $scope.filterTwitter = function () {
        if ($scope.filterConvoArray['twitter']) {
            $scope.filterConvoArray['twitter'] = false;
        } else {
            $scope.filterConvoArray['twitter'] = true;
        }
    };
    $scope.filterReddit = function () {
        if ($scope.filterConvoArray['reddit']) {
            $scope.filterConvoArray['reddit'] = false;
        } else {
            $scope.filterConvoArray['reddit'] = true;
        }
    };
    $scope.filterRead = function () {
        if ($scope.filterTagArray['read']) {
            $scope.filterTagArray['read'] = false;
        } else {
            $scope.filterTagArray['read'] = true;
        }
    };
    $scope.filterUnread = function () {
        if ($scope.filterTagArray['unread']) {
            $scope.filterTagArray['unread'] = false;
        } else {
            $scope.filterTagArray['unread'] = true;
        }
    };

    // Populate the model with the initial data.
    $scope.refreshConvos();
    $scope.refreshSubscriptions();
}

/**
 * Create the ASKSG module for the dashboard app.
 */
AsksgService = function () {

    // Indicate that we're starting up...
    console.log("Starting the AsksgService...");

    // Create the ASKSG module
    var directives = angular.module('ASKSG', [], function ($provide) {

        // Create and inject the asksg service into the ConversationController.
        $provide.factory('$asksg', function ($http, $log) {

            // Store the service URLs locally here
            var convoUrl = '/asksg/conversations';
            var convoSeedUrl = '/asksg/conversations/seed';
            var messageUrl = '/asksg/messages';
            var messageSeedUrl = '/asksg/messages/seed';
            var subscriptionUrl = '/asksg/socialsubscriptions'

            // Publish the $asksg API here
            return {
                /**
                 * Fetch all conversations with a specified ID (-1 or nil require us to fetch all of them...)
                 *
                 * @param convoId - target conversation to receive
                 * @return map of conversation data
                 */
                fetchConvos: function (convoId, seed) {
                    // Check to see if we should seed the conversataion repository
                    if (seed == true) {
                        $http.post(convoSeedUrl).then(function (resp) {
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
                postResponse: function (convo, message, author) {
                    console.log("Building message...");
                    messageResp = new MessageResp(author, message, convo);
                    console.log(messageResp);

                    // Return the HTTP response
                    return $http({method: 'POST', url: messageUrl, data: JSON.stringify(messageResp)});
                },

                /**
                 * Fetch the social subscriptions currently in use for the system.
                 */
                fetchSubscriptions: function () {
                    return $http({method: 'GET', url: convoUrl});
                }
            };
        });
    });
}

// Invoke the ASKSG service constructor...
console.log("Jumping into the constructor...");
AsksgService();

