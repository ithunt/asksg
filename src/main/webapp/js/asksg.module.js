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
function Conversation(id, author, subject, snippet, messages, created, modified, service) {
    this.id = id;
    this.author = author;
    this.subject = subject;
    this.created_at = created;
    this.modified_at = modified;
    this.active = false;
    this.service = service;

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
function SocialSubscription(authenticated, config, id, name, version) {
    this.authenticated = authenticated;
    this.config = config;
    this.id = id;
    this.name = name;
    this.version = version;
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
                        conversation.createdDate, conversation.modifiedDate,
                        conversation.service);
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
                console.log(data);

                // Always rebuild the social subscription data...
                $scope.test = "it works...";
                $scope.subscriptions = new Array();
                for (var i = 0; i < data.length; i++) {
                    var subData = angular.fromJson(data[i]);
                    console.log(subData.name);
                    if ((subData.name in $scope.subscriptions) == false) {
                        $scope.subscriptions[subData.name] = new Array();
                        console.log($scope.subscriptions[subData.name]);
                    } 
                    console.log($scope.subscriptions);
                    console.log($scope.subscriptions[subData.name]);
                    $scope.subscriptions[subData.name].push(
                        new SocialSubscription(subData.authenticated, subData.config, 
                            subData.id, subData.name, subData.version));
                }

                // debug
                console.log($scope.subscriptions);
            }).
            error(function (data, status, headers, config) {
                console.log("Failed grabbing the social subscriptions");
                return null;
            });
    }

    /**
     * Invoke the ASKSG message post function
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

    /**
     * Invoke the ASKSG service post function
     */
    $scope.doAddService = function(service) {
        // post the message - on success re-fetch the conversations so the most up-to-date convos are viewed
        $asksg.postNewService(service).
            success(function (data, status, headers, config) {
                console.log("Success");
                console.log(data);
                console.log(status);
                $scope.refreshSubscriptions();
            }).
            error(function (data, status, headers, config) {
                console.log("Error... :(");
            });
    }

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
     * Set up the default conversation filters.
     */
    $scope.convoCategory = "";
    $scope.convoFilter = "";

    // Array to store the state of active conversation filters
    $scope.filterConvoArray = Array();
    $scope.filterConvoArray['Email'] = false;
    $scope.filterConvoArray['Twilio'] = false;
    $scope.filterConvoArray['Facebook'] = false;
    $scope.filterConvoArray['Twitter'] = false;
    $scope.filterConvoArray['Reddit'] = false;

    // Array to store the state of the active tag filters
    $scope.filterTagArray = Array();
    $scope.filterTagArray['read'] = false;
    $scope.filterTagArray['unread'] = false;

    /*
     * Filter function for the conversations based on their service.
     */
    $scope.filterConvo = function (convo) {
        return !($scope.filterConvoArray[convo.service.name]);
    };

    /*
     * Filter function for the conversations.
     */
    $scope.filterTag = function (tag) {
        return !($scope.filterTagArray[tag]);
    };

    /*
     * Filter functions...
     */
    $scope.filterEmail = function () {
        if ($scope.filterConvoArray['Email']) {
            $scope.filterConvoArray['Email'] = false;
        } else {
            $scope.filterConvoArray['Email'] = true;
        }
    };
    $scope.filterSms = function () {
        if ($scope.filterConvoArray['Twilio']) {
            console.log("filtering disabled...");
            $scope.filterConvoArray['Twilio'] = false;
        } else {
            console.log("set to true???");
            $scope.filterConvoArray['Twilio'] = true;
        }
    };
    $scope.filterFacebook = function () {
        if ($scope.filterConvoArray['Facebook']) {
            $scope.filterConvoArray['Facebook'] = false;
        } else {
            $scope.filterConvoArray['Facebook'] = true;
        }
    };
    $scope.filterTwitter = function () {
        if ($scope.filterConvoArray['Twitter']) {
            $scope.filterConvoArray['Twitter'] = false;
        } else {
            $scope.filterConvoArray['Twitter'] = true;
        }
    };
    $scope.filterReddit = function () {
        if ($scope.filterConvoArray['Reddit']) {
            $scope.filterConvoArray['Reddit'] = false;
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

    // Set the user name
    $scope.userName = "Admin"; // this should be replaced by response from server

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
            var servicesUrl = '/asksg/services'

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
                 * Submit a new service to the system.
                 *
                 * @param service - new service to add
                 */
                postNewService: function (service) {
                    console.log("TODO");
                    // Return the HTTP response
                    //return $http({method: 'POST', url: messageUrl, data: JSON.stringify(messageResp)});
                },

                /**
                 * Fetch the social subscriptions currently in use for the system.
                 */
                fetchSubscriptions: function () {
                    return $http({method: 'GET', url: servicesUrl});
                }
            };
        });
    });
}

// Invoke the ASKSG service constructor...
console.log("Jumping into the constructor...");
AsksgService();

