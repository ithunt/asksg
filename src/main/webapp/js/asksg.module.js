// Check to see if we're returning from Facebook
function getQueryVariable(variable) {
    var query = window.location.search.substring(1);
    var vars = query.split('&');
    for (var i = 0; i < vars.length; i++) {
        var pair = vars[i].split('=');
        if (decodeURIComponent(pair[0]) == variable) {
            return decodeURIComponent(pair[1]);
        }
    }
    return null;
}

/**
 * Message object constructor.
 */
function Message(author, content, conversationId, privateMessage) {
    this.author = author;
    this.content = content;
    this.privateMessage = privateMessage;
    this.conversationId = conversationId;
}

/**
 * MessageResp object constructor.
 */
function MessageResp(content, conversation) {
    this.content = content;
    this.conversation = conversation;
}

/**
 * Conversation object constructor.
 */
function Conversation(id, author, subject, snippet, messages, created, modified, service, read, hidden, privateConversation) {
    this.id = id;
    this.author = author;
    this.subject = subject;
    this.created_at = created;
    this.modified_at = modified;
    this.active = false;
    this.service = service;
    this.read = read;
    this.hidden = hidden; // hidden
    this.privateConversation = privateConversation;

    // Run through the messages list and create the appropriate Message objects
    this.messages = new Array();
    for (var i = messages.length - 1; i >= 0; i--) {
        this.messages[i] = new Message(messages[i].author, messages[i].content, id, messages[i].privateMessage);
    }

    // The message snippet
    this.snippet = snippet;

    // Function to set this conversation as "active" in the UI
    this.setActive = function (flag) {
        this.active = flag;
    }
}

/**
 * Provider config provider object constructor.
 */
    //TODO: RENAME FUCKER
function ProviderConfig(id, authenticated, enabled, config, name, version, maxcalls, updatefreq, lastupdate, currentcalls) {
    this.id = id;
    this.authenticated = authenticated;
    this.name = name;
    this.enabled = enabled;
    this.config = config;
    this.version = version;
    this.maxcalls = maxcalls;
    this.updatefreq = updatefreq;
    this.lastupdate = lastupdate;
    this.currentcalls = currentcalls;
}

function Twilio(providerConfig, authenticated) {
    this.authenticated = authenticated;
    this.config = providerConfig;
    this.name = "Twilio";
}

function Email(providerConfig, authenticated) {
    this.authenticated = authenticated;
    this.config = providerConfig;
    this.name = "Email";
}

function Facebook(providerConfig, authenticated) {
    this.authenticated = authenticated;
    this.config = providerConfig;
    this.name = "Facebook";
}

function Twitter(providerConfig, authenticated) {
    this.authenticated = authenticated;
    this.config = providerConfig;
    this.name = "Twitter";
}

function Reddit(providerConfig, authenticated) {
    this.authenticated = authenticated;
    this.config = providerConfig;
    this.name = "Reddit";
}

function User(id, name, username, password, phoneNumber, email, role, enabled) {
    this.id = id;
    this.name = name;
    this.userName = username;
    this.password = password;
    this.phoneNumber = phoneNumber;
    this.email = email;
    this.role = role;
    this.enabled = enabled;
}

function Role(name) {
    this.name = name;
}

/**
 * Construct the main page controller. >.<
 */
function MainController($scope, $asksg, $log) {
    // Defaults until entered otherwise on the main page...
    $scope.localUserName = "";
    $scope.password = "";

    /**
     * Handle the login events.
     */
    $scope.login = function () {
        console.log("username and password: " + $scope.localUserName + ", " + $scope.password);
        //$scope.$emit('event:loginRequest', $scope.localUserName, $scope.password);
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
     * Attempt to render some analytics data
     */
    $scope.renderAnalyticsData = function () {
        console.log("invoking the update analytics method...");
        $asksg.fetchAnalyticsData(analyticsStartDate, analyticsEndDate).
            success(function (data, status, headers, config) {
                console.log("Got analytics data back from the server");
                console.log(data);
                updateAnalytics(data);
            }).
            error(function (data, status, headers, config) {
                console.log("Failed grabbing the social subscriptions");
                return null;
            });
    }

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

                    var createdDate = new Date(conversation.created.localMillis)
                    var modifiedDate = new Date(conversation.modified.localMillis);
                    console.log(createdDate);

                    // Create the object and store it
                    $scope.convos[i] = new Conversation(conversation.id,
                        conversation.author, conversation.subject,
                        conversation.snippet, conversation.messages,
                        createdDate, modifiedDate,
                        conversation.service, conversation.read, conversation.hidden,
                        conversation.privateConversation);
                    $scope.convoMap[conversation.id] = $scope.convos[i];
                }
            }).
            error(function (data, status, headers, config) {
                console.log("Failed refreshing convos...");
                return null;
            });
    };

    /*
     * Populate the social subscription content
     */
    $scope.refreshSubscriptions = function () {
        $asksg.fetchSubscriptions().
            success(function (data, status, headers, config) {
                console.log("Got social data back from the server");
                console.log(data);

                // Always rebuild the social subscription data...
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
                        new ProviderConfig(subData.id, subData.authenticated, subData.enabled, subData.config,
                            subData.name, subData.version, subData.maxCalls, subData.updateFrequency,
                            subData.lastUpdate, subData.currentCalls));
                }

                // debug
                console.log($scope.subscriptions);
            }).
            error(function (data, status, headers, config) {
                console.log("Failed grabbing the social subscriptions");
                return null;
            });
    };

    /**
     * Invoke the ASKSG message post function
     */
    $scope.doPostMessage = function (message, convoId) {
        // post the message - on success re-fetch the conversations so the most up-to-date convos are viewed
        var messageResp = new MessageResp(message, convoId);
        $asksg.postResponse(messageResp).
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
        $(":input", "#response-form-" + convoId).not(":button")[0].value = '';
    };

    /**
     * Invoke the ASKSG service post function
     */
    $scope.doAddServiceTwilio = function () {
        // post the message - on success re-fetch the conversations so the most up-to-date convos are viewed
        console.log($scope.twilioUsername + " " + $scope.twilioAuthToken + " " + $scope.twilioNumber);
        config = {authenticationToken: $scope.twilioAuthToken, createdBy: null, host: "",
            password: "", username: $scope.twilioUsername, phoneNumber: $scope.twilioNumber};
        //newConfig = new ProviderConfig(false, config, "Twilio", 0);
        newService = new Twilio(config, false);
        $asksg.postNewService(newService).
            success(function (data, status, headers, config) {
                console.log("Success adding the new service!");
                console.log(data);
                console.log(status);
                $scope.refreshSubscriptions();
            }).
            error(function (data, status, headers, config) {
                console.log("Error... :(");
            });
        $scope.twilioAuthToken = '';
        $scope.twilioUsername = '';
        $scope.twilioNumber = '';
    };

    /**
     * Invoke the ASKSG service post function
     */
    $scope.doAddServiceEmail = function () {
        // post the message - on success re-fetch the conversations so the most up-to-date convos are viewed
        console.log($scope.emailUsername + " " + $scope.emailPassword);
        // config, name, version)
        config = {createdBy: null, host: "", password: $scope.emailPassword, username: $scope.emailUsername};
        newService = new Email(config, false);
        $asksg.postNewService(newService).
            success(function (data, status, headers, config) {
                console.log("Success adding the new service!");
                console.log(data);
                console.log(status);
                $scope.refreshSubscriptions();
            }).
            error(function (data, status, headers, config) {
                console.log("Error... :(");
            });
        $scope.emailUsername = '';
        $scope.emailPassword = '';
    };


    /**
     * Invoke the ASKSG service post function
     */
    $scope.doAddServiceTwitter = function () {
        //twitterUrl, twitterConsumerKey, twitterConsumerSecret, twitterAccessToken, twitterAccessSecret
        // post the message - on success re-fetch the conversations so the most up-to-date convos are viewed
        console.log($scope.twitterUrl + " " + $scope.twitterConsumerKey + " " + $scope.twitterConsumerSecret + " " + $scope.twitterAccessToken + " " + $scope.twitterAccessSecret);
        // config, name, version)
        config = {url: $scope.twitterUrl, consumerkey: $scope.twitterConsumerKey, consumersecret: $scope.twitterConsumerSecret,
            accesstoken: $scope.twitterAccessToken, accesstokensecret: $scope.twitterAccessSecret,
            authenticationToken: "", createdBy: null, host: "", password: "", username: ""};
        newService = new Twitter(config, false);
        $asksg.postNewService(newService).
            success(function (data, status, headers, config) {
                console.log("Success adding the new service!");
                console.log(data);
                console.log(status);
                $scope.refreshSubscriptions();
            }).
            error(function (data, status, headers, config) {
                console.log("Error... :(");
            });
        $scope.twitterUrl = '';
        $scope.twitterConsumerKey = '';
        $scope.twitterConsumerSecret = '';
        $scope.twitterAccessToken = '';
        $scope.twitterAccessSecret = '';
    };

    /**
     * Invoke the ASKSG service post function.
     */
    $scope.doAddServiceFacebook = function () {
        // post the message - on success re-fetch the conversations so the most up-to-date convos are viewed
        //console.log($scope.facebookUrl + " " + $scope.facebookConsumerKey + " " + $scope.facebookConsumerSecret + " " + $scope.facebookAccessToken + " " + $scope.facebookAccessSecret);
        // config, name, version)
        config = {url: "", consumerKey: $scope.facebookConsumerKey,
            consumerSecret: $scope.facebookConsumerSecret, accessToken: "", accessTokenSecret: "",
            authenticationToken: "", createdBy: null, host: "", password: "", username: ""};
        newService = new Facebook(config, false);
        $asksg.postNewService(newService).
            success(function (data, status, headers, config) {
                console.log("Success adding the new service!");
                console.log(data);
                console.log(status);
                var newService = angular.fromJson(data[0]);
                $scope.refreshSubscriptions();
            }).
            error(function (data, status, headers, config) {
                console.log("Error... :(");
            });
        $scope.facebookConsumerKey = '';
        $scope.facebookConsumerSecret = '';

    };

    $scope.addUser = function () {
        $asksg.postNewUser(new User(null, $scope.userName, $scope.userUsername, $scope.userPassword, $scope.userPhone, $scope.userEmail, $scope.userRole, true)).
            success(function (data, status, headers, config) {
                $scope.refreshUsers();
            });
        $scope.userName = '';
        $scope.userUsername = '';
        $scope.userPassword = '';
        $scope.userPhone = '';
        $scope.userEmail = '';
        $scope.userRole = '';
    }

    /**
     * Populate the users content
     */
    $scope.refreshUsers = function () {
        $asksg.fetchUsers().
            success(function (data, status, headers, config) {
                console.log("Retrieved user data from server");
                console.log(data);
                $scope.users = new Array();
                for (var i = 0; i < data.length; i++) {
                    var userData = angular.fromJson(data[i]);
                    $scope.users.push(new User(userData.id, userData.name, userData.userName, '', userData.phoneNumber, userData.email, new Role(data[i].role.name), userData.enabled));
                }
            }).error(function (data, status, headers, config) {
                console.log("Failed to retrieve users");
                return null;
            });
    }

    $scope.refreshRoles = function () {
        $asksg.fetchRoles().
            success(function (data, status, headers, config) {
                console.log("Retrieved role data from server");
                console.log(data);
                $scope.roles = new Array();
                for (var i = 0; i < data.length; i++) {
                    var roleData = angular.fromJson(data[i]);
                    $scope.roles.push(new Role(roleData.name));
                }
            }).error(function (data, status, headers, config) {
                console.log("Failed to retrieve users");
                return null;
            });
    }

    /*
     * Add a new social subscription...
     */
    $scope.addSocialSubscription = function (id, socialSubHandle, socialSubName) {
        $asksg.addSubscription(id, socialSubHandle, socialSubName).
            success(function (data, status, headers, config) {
                $scope.refreshSubscriptions();
            });
    }

    /*
     * Update the update limits of the config
     */
    $scope.updateApiLimits = function (config, maxcalls, updateFreq) {
        config.maxcalls = maxcalls;
        config.updatefreq = updateFreq;
        $asksg.updateConfig(config)
    }

    /*
     * Delete a conversation.
     */
    $scope.deleteConvo = function (convoId) {
        $asksg.deleteConvo(convoId);
        $scope.refreshConvos();
    };

    /*
     * Determine if a conversation is active.
     */
    $scope.isConvoActive = function (convoId) {
        return $scope.convoMap[convoId].active;
    }

    $scope.toggleReadConvo = function (convoId) {
        $scope.convoMap[convoId].read = !$scope.convoMap[convoId].read;
        $asksg.updateConvo($scope.convoMap[convoId]);
    }

    /*
     * Hide the specified conversation (mark as read).
     */
    $scope.toggleHideConvo = function (convoId) {
        $scope.convoMap[convoId].hidden = !$scope.convoMap[convoId].hidden;
        $asksg.updateConvo($scope.convoMap[convoId]);
    }

    $scope.toggleEnabledService = function (service) {
        service.enabled = !service.enabled;
        $asksg.updateService(service).success(function () {
            $scope.refreshSubscriptions();
        });
    }

    $scope.deleteService = function (serviceId) {
        $asksg.deleteService(serviceId).success(function () {
            $scope.refreshSubscriptions();
        });
    }

    $scope.toggleEnabledUser = function (user) {
        user.enabled = !user.enabled;
        $asksg.updateUser(user).success(function () {
            $scope.refreshUsers();
        });
    }

    $scope.deleteUser = function (userId) {
        $asksg.deleteUser(userId).success(function () {
            $scope.refreshUsers();
        });
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
    $scope.filterTagArray['read'] = true;
    $scope.filterTagArray['unread'] = false;

    /*
     * Filter function for the conversations based on their service.
     */
    $scope.filterConvo = function (convo) {
        if (convo.service != null) {
            return !($scope.filterConvoArray[convo.service.name]);
        }
        return false;
    };

    /*
     * Filter function for the conversations based on read/unread tags.
     */
    $scope.filterTag = function (convo) {
        if ($scope.filterTagArray['read'] && convo.read) {
            return false;
        }
        if ($scope.filterTagArray['unread'] && !(convo.read)) {
            return false;
        }
        return true;
    };

    /*
     * Filter functions...
     */
    $scope.filterEmail = function () {
        $scope.filterConvoArray['Email'] = !$scope.filterConvoArray['Email'];
    };
    $scope.filterSms = function () {
        $scope.filterConvoArray['Twilio'] = !$scope.filterConvoArray['Twilio'];
    };
    $scope.filterFacebook = function () {
        $scope.filterConvoArray['Facebook'] = !$scope.filterConvoArray['Facebook'];
    };
    $scope.filterTwitter = function () {
        $scope.filterConvoArray['Twitter'] = !$scope.filterConvoArray['Twitter'];
    };
    $scope.filterReddit = function () {
        $scope.filterConvoArray['Reddit'] = !$scope.filterConvoArray['Reddit'];
    };
    $scope.filterRead = function () {
        $scope.filterTagArray['read'] = !$scope.filterTagArray['read'];
    };
    $scope.filterUnread = function () {
        $scope.filterTagArray['unread'] = !$scope.filterTagArray['unread'];
    };

    // Set the user name
    $scope.localUserName = "Admin"; // this should be replaced by response from server

    // Populate the model with the initial data.
    $scope.refreshConvos();
    $scope.refreshSubscriptions();
    $scope.refreshUsers();
    $scope.refreshRoles();

    // Handle facebook authentication
    var facebookCode = getQueryVariable("code");
    if (facebookCode != null) {
        var serviceID = getQueryVariable("state");
        if (serviceID != null) {
            $asksg.authenticateFacebook(facebookCode, serviceID);
            $scope.refreshSubscriptions();
        }
    }

    // Scope vars for start/end dates
    analyticsStartDate = "";
    analyticsEndDate = "";
    socialSubHandle = "";
    socialSubName = "";
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
            var servicesUrl = '/asksg/services';
            var usersUrl = '/asksg/users';
            var rolesUrl = '/asksg/roles';
            var analyticsUrl = '/asksg/analytics/words';
            var subscriptionsUrl = '/asksg/socialsubscriptions';
            var configsUrl = '/asksg/providerconfigs';

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
                 * @param message - the message to insert
                 * @param convoId - the host conversation ID
                 * @param messageId - TODO:  optional message, which indicates that this is a nested response (a la Reddit)
                 */
                postResponse: function (messageResp) {
                    console.log(messageResp);
                    // Return the HTTP response
                    return $http({method: 'POST', url: messageUrl, data: JSON.stringify(messageResp)});
                },

                /**
                 * Update a conversation.
                 *
                 * @param convo - the conversation to update.
                 */
                updateConvo: function (convo) {
                    return $http({method: 'UPDATE', url: convoUrl, data: JSON.stringify(convo)});
                },

                /**
                 * Delete a conversation.
                 *
                 * @param convo - the conversation to update.
                 */
                deleteConvo: function (convoId) {
                    return $http({method: 'DELETE', url: (convoUrl + "/" + convoId)});
                },

                /**
                 * Submit a new service to the system.
                 *
                 * @param service - new service to add
                 */
                postNewService: function (service) {
                    console.log(JSON.stringify(service));
                    return $http({method: 'POST', url: servicesUrl, data: JSON.stringify(service)});
                },

                /**
                 * Hit the service controller to add a new subscription.
                 */
                addSubscription: function (ssid, ssname, sshandle) {
                    target = servicesUrl + "/subscribe?id=" + ssid + "&name=" + ssname + "&handle=" + sshandle;
//                    console.log("shipping subscription data off to the server.");
//                    tmp = JSON.stringify({id: ssid, name: ssname, handle: sshandle});
//                    console.log(tmp);
                    return $http({method: 'POST', url: target});
                },

                /**
                 * Updates a service
                 * @param service
                 */
                updateService: function (service) {
                    return $http({method: 'PUT', url: servicesUrl, data: JSON.stringify(service)});
                },

                deleteService: function (serviceId) {
                    return $http({method: 'DELETE', url: (servicesUrl + "/" + serviceId)});
                },

                updateConfig: function(updatedConfig) {
                    return $http({method: 'PUT', url: configsUrl, data: JSON.stringify(updatedConfig)});
                },

                /**
                 * Updates a user
                 * @param user
                 */
                updateUser: function (user) {
                    return $http({method: 'PUT', url: usersUrl, data: JSON.stringify(user)});
                },

                deleteUser: function (userId) {
                    return $http({method: 'DELETE', url: (usersUrl + "/" + userId)});
                },

                /**
                 * Fetch the social subscriptions currently in use for the system.
                 */
                fetchSubscriptions: function () {
                    return $http({method: 'GET', url: servicesUrl});
                },

                postNewUser: function (user) {
                    return $http({method: 'POST', url: usersUrl, data: JSON.stringify(user)});
                },

                /**
                 * Fetch users and roles on asksg side
                 */
                fetchUsers: function () {
                    return $http({method: 'GET', url: usersUrl});
                },

                fetchRoles: function () {
                    return $http({method: 'GET', url: rolesUrl});
                },

                authenticateFacebook: function (code, serviceID) {
                    return $http({method: 'POST', url: servicesUrl + "/facebookToken?id=" + serviceID + "&code=" + code});
                },

                fetchAnalyticsData: function (start, end) {
                    target = analyticsUrl;
                    if (start != null && start.length > 0) {
                        target = target + "?since=" + start;
                        if (end != null && end.lenght > 0) {
                            target = target + "&until=" + end;
                        }
                    }
                    return $http({method: 'GET', url: target});
                }
            };
        });
    });

    // var directives = angular.module('directives', []);
    directives.directive('myDatepicker', function ($parse) {
        return function (scope, element, attrs, controller) {
            var ngModel = $parse(attrs.ngModel);
            $(function () {
                element.datepicker({
                    inline: true,
                    dateFormat: 'dd.mm.yy',
                    onSelect: function (dateText, inst) {
                        scope.$apply(function (scope) {
                            // Change binded variable
                            ngModel.assign(scope, dateText);
                        });
                    }
                });
            });
        }
    });
};

// Invoke the ASKSG service constructor...
console.log("Jumping into the constructor...");
AsksgService();
