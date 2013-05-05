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

// Busy spinner for the analytics page...
var opts = {
    lines: 13, // The number of lines to draw
    length: 10, // The length of each line
    width: 5, // The line thickness
    radius: 10, // The radius of the inner circle
    corners: 1, // Corner roundness (0..1)
    rotate: 0, // The rotation offset
    direction: 1, // 1: clockwise, -1: counterclockwise
    color: '#000', // #rgb or #rrggbb
    speed: 1, // Rounds per second
    trail: 60, // Afterglow percentage
    shadow: false, // Whether to render a shadow
    hwaccel: false, // Whether to use hardware acceleration
    className: 'spinner', // The CSS class to assign to the spinner
    zIndex: 2e9, // The z-index (defaults to 2000000000)
    top: 'auto', // Top position relative to parent in px
    left: 'auto' // Left position relative to parent in px
};

/**
 * Message object constructor.
 */
function Message(id, author, content, conversationId, privateMessage, tags) {
    this.id = id;
    this.author = author;
    this.content = content;
    this.privateMessage = privateMessage;
    this.conversationId = conversationId;

    // Run through the tag list and create the Tag objects
    this.tags = new Array();
    for (var i = tags.length - 1; i >= 0; i--) {
        this.tags[i] = new Tag(tags[i].id, tags[i].name);
    }
}

function Tag(id, name) {
    this.id = id;
    this.name = name;
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
function Conversation(id, author, subject, messages, created, modified, service, read, hidden, privateConversation) {
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
        this.messages[i] = new Message(messages[i].id, messages[i].author, messages[i].content, id, messages[i].privateMessage, messages[i].tags);
    }

    // Function to set this conversation as "active" in the UI
    this.setActive = function (flag) {
        this.active = flag;
    }
}

/**
 * Provider config provider object constructor.
 */
    //TODO: RENAME
function ProviderConfig(id, authenticated, enabled, config, name, version) {
    this.id = id;
    this.authenticated = authenticated;
    this.name = name;
    this.enabled = enabled;
    this.config = config;
    this.version = version;
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

function Chatterbox(providerConfig, authenticated) {
    this.authenticated = authenticated;
    this.config = providerConfig;
    this.name = "Chatterbox";
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
/**
 * Create the ASKSG module for the dashboard app.
 */

// Create the ASKSG module
var app = angular.module('ASKSG', []);

// Create and inject the asksg service into the ConversationController.
app.factory('$asksg', function ($http, $log) {

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
    var tagsUrl = '/asksg/tags';
    var configsUrl = '/asksg/providerconfigs';
    var exportUrl = '/asksg/analytics/csv'
    var topicUrl = '/asksg/analytics/topics';

    // Publish the $asksg API here
    return {
        /**
         * Fetch all conversations with a specified ID (-1 or nil require us to fetch all of them...)
         *
         * @param convoId - target conversation to receive
         * @return map of conversation data
         */
        fetchConvos: function (convoId, params) {
            var paramstring = decodeURIComponent($.param(params));
            return $http({method: 'GET', url: convoUrl + "?" + paramstring});
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

        updateConfig: function(configId, maxCalls, updateFrequency) {
            return $http({method: 'POST',
                url: configsUrl + "/updateLimits?id=" + configId + "&maxCalls=" + maxCalls + "&updateFrequency=" + updateFrequency});
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

        /**
         * Tag operations
         */
        addTag: function (tagName, messageId) {
            return $http({method: 'POST', url: tagsUrl + "/create?tagName=" + tagName + "&messageId=" + messageId});
        },

        deleteTag: function (tagId, messageId) {
            return $http({method: 'DELETE', url: tagsUrl + "/remove?tagid=" + tagId + "&messageId=" + messageId});
        },

        authenticateFacebook: function (code, serviceID) {
            return $http({method: 'POST', url: servicesUrl + "/facebookToken?id=" + serviceID + "&code=" + code});
        },

        fetchAnalyticsData: function (start, end, includeList) {
            console.log(includeList);
            var target = analyticsUrl;
            if (start != null && start.length > 0) {
                target = target + "?since=" + start;
                if (end != null && end.length > 0) {
                    target = target + "&until=" + end;
                }
            }
            return $http({method: 'GET', url: target});
        },

        exportAnalyticsData: function (start, end) {
            var target = exportUrl;
            if (start != null && start.length > 0) {
                target = target + "?since=" + start;
                if (end != null && end.length > 0) {
                    target = target + "&until=" + end;
                }
            }
            console.log(target);
            window.location = target;
        },

        fetchTopics: function() {
            return $http({method: 'GET', url: topicUrl});
        }
    };
});

app.controller('ConversationController', ['$scope', '$asksg', '$log', function ($scope, $asksg, $log) {

    /*
     * Attempt to render some analytics data
     */
    $scope.renderAnalyticsData = function () {
        console.log("invoking the update analytics method...");
        $('#messagePane').remove('.messagePaneError');
        $('.messagePaneError').remove();
        var target = document.getElementById('messagePane');
        var spinner = new Spinner(opts).spin(target);
        $asksg.fetchAnalyticsData(analyticsStartDate, analyticsEndDate, $scope.includeList).
            success(function (data, status, headers, config) {
                console.log("Got analytics data back from the server");
                console.log(data);
                try {
                    updateAnalytics(data);
                }
                catch (err) {
                    $('#messagePane').append('<p class="messagePaneError">Error occurred while generating the analytics data. Try again...</p>');
                    spinner.stop();
                    console.log("Error occured while trying to render graphs...");
                }
                spinner.stop();
                $('#messagePane').remove('.spinner');
            }).
            error(function (data, status, headers, config) {
                spinner.stop();
                $('#messagePane').remove('.spinner');
                console.log("Failed grabbing the social subscriptions");
                return null;
            });
    }

    $scope.exportData = function(exportStartDate, exportEndDate) {
        console.log("invoking the export analytics method...");
        console.log(exportStartDate);
        console.log(exportEndDate);
        $asksg.exportAnalyticsData(exportStartDate, exportEndDate).
            success(function (data, status, headers, config) {
                console.log(data);
            }).
            error(function (data, status, headers, config) {
                console.log("Error");
                console.log(data);
            });
    }

    /*
     * Refresh the set of conversations on the page
     */
    $scope.refreshConvos = function () {
        $scope.getConversations({});
    };

    $scope.refreshTopics = function () {
        $asksg.fetchTopics().success(function (data, status, headers, config) {
                console.log("Got topics...");
                console.log(data);
                $scope.includeList = [];
                $scope.omitList = [{"id": 5, "topic": "T6"},{"id": 6,"topic": "T7"}];
                for (var i = 0; i < data.length; i++) {
                    // TODO: push topic into the include list as an object with ID and topic name...
                }
            });
    }

    $scope.nextConvos = function () {
        var params = {};
        if ($scope.convos.length > 0) {
            params.until = $scope.convos[0].id;
        }
        $scope.getConversations(params);
    }

    $scope.previousConvos = function () {
        var params = {};
        if ($scope.convos.length > 0) {
            params.since = $scope.convos[$scope.convos.length - 1].id;
        }
        $scope.getConversations(params);
    }

    $scope.getConversations = function (params) {
        var params = $scope.buildConversationParams(params);
        $asksg.fetchConvos(-1, params).
            success(function (data, status, headers, config) {
                console.log("Got conversation data back from the server...");

                // Parse the JSON response data and create a list of Conversation objects to store in the scope
                if (data.length > 0) {
                    $scope.convos = new Array();
                    $scope.convoMap = new Array();
                    for (var i = 0; i < data.length; i++) {
                        var conversation = angular.fromJson(data[i]);

                        var createdDate = new Date(conversation.created.localMillis)
                        var modifiedDate = new Date(conversation.modified.localMillis);
                        console.log(createdDate);

                        // Create the object and store it
                        $scope.convos[i] = new Conversation(conversation.id,
                            conversation.author, conversation.subject, conversation.messages,
                            createdDate, modifiedDate,
                            conversation.service, conversation.read, conversation.hidden,
                            conversation.privateConversation);
                        $scope.convoMap[conversation.id] = $scope.convos[i];
                    }
                }
            }).
            error(function (data, status, headers, config) {
                console.log("Failed refreshing convos...");
                return null;
            });
    }

    $scope.buildConversationParams = function (params) {
        //build filters
        if ($scope.filterTagArray['read'] || $scope.filterTagArray['unread']) {
            params.showRead = $scope.filterTagArray['unread'];
        }
        params.excludeServices = $scope.excludeServices;
        //todo : reenable - needs backend support
        //params.filterString = $scope.refineFilterString;
        if ($scope.runonce) {
            var tagset = $("#tag-filters").tags().getTags();
            if (typeof params.includeTags != "undefined") {
                params.includeTags = $.merge(params.includeTags, tagset);
            }
            else{
                params.includeTags = tagset;
            }
        }
        return params;
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
                $scope.serviceList = new Array();
                $scope.subscriptions = new Array();
                for (var i = 0; i < data.length; i++) {
                    var subData = angular.fromJson(data[i]);
                    console.log(subData.name);
                    if ((subData.name in $scope.subscriptions) == false) {
                        $scope.subscriptions[subData.name] = new Array();
                        $scope.serviceList.push(subData.name);
                        console.log($scope.subscriptions[subData.name]);
                    }
                    console.log($scope.subscriptions);
                    console.log($scope.subscriptions[subData.name]);
                    $scope.subscriptions[subData.name].push(
                        new ProviderConfig(subData.id, subData.authenticated, subData.enabled, subData.config,
                            subData.name, subData.version));
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

    /**
     * Invoke the ASKSG service post function.
     */
    $scope.doAddServiceChatterbox = function() {
        config = {url: "", consumerKey: "", consumerSecret: "", accessToken: "", accessTokenSecret: "",
            authenticationToken: $scope.chatterboxAuthenticationToken, createBy: null, host: "",
            password: "", username: ""};
        newService = new Chatterbox(config, false);
        $asksg.postNewService(newService).
            success(function(data, status, headers, config) {
                console.log("Success adding the new service!");
                console.log(data);
                console.log(status);
                var newService = angular.fromJson(data[0]);
                $scope.refreshSubscriptions();
            }).
            error(function (data, status, headers, config) {
                console.log("Error... :(");
            });
        $scope.chatterboxAuthenticationToken = '';
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
    $scope.updateApiLimits = function (configId, updatedMaxCalls, updatedUpdateFrequency) {
        $asksg.updateConfig(configId, updatedMaxCalls, updatedUpdateFrequency);
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
 

 // Test data for topic selection
    $scope.includeList = [
        {
            "id": 1,
            "topic": "T1"
        },
        {
            "id": 2,
            "topic": "T2"
        },
        {
            "id": 3,
            "topic": "T3"
        },
        {
            "id": 4,
            "topic": "T4"
        }
    ];
 
    $scope.omitList = [
        {
            "id": 7,
            "topic": "T8"
        },
        {
            "id": 8,
            "topic": "T9"
        }
    ];
 
    // watch, use 'true' to also receive updates when values
    // change, instead of just the reference
    $scope.$watch("model", function(value) {
        console.log("Model: " + value.map(function(e){return e.id}).join(','));
    },true);
 
    // watch, use 'true' to also receive updates when values
    // change, instead of just the reference
    $scope.$watch("source", function(value) {
        console.log("Source: " + value.map(function(e){return e.id}).join(','));
    },true);

    /*
     * Set up the default conversation filters.
     */
    $scope.convoCategory = "";
    $scope.convoFilter = "";

    // Array to store the state of active conversation filters
    $scope.excludeServices = Array();

    // Array to store the state of the active tag filters
    $scope.filterTagArray = Array();
    $scope.filterTagArray['read'] = false;
    $scope.filterTagArray['unread'] = false;

    $scope.excludeService = function(service){
        var index = $.inArray(service, $scope.excludeServices);
        if(index === -1){
            $scope.excludeServices.push(service);
        }
        else{
            $scope.excludeServices.splice(index,1);
        }
        $scope.refreshConvos();
    };

    /*
     * Filter functions...
     */
    $scope.filterRead = function () {
        $scope.filterTagArray['read'] = !$scope.filterTagArray['read'];
        $scope.refreshConvos();
    };
    $scope.filterUnread = function () {
        $scope.filterTagArray['unread'] = !$scope.filterTagArray['unread'];
        $scope.refreshConvos();
    };

    // Set the user name
    $scope.localUserName = "Admin"; // this should be replaced by response from server

    $scope.startup = function () {
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
    }

    // Scope vars for start/end dates
    analyticsStartDate = "";
    analyticsEndDate = "";
    exportStartDate = "";
    exportEndDate = "";
    socialSubHandle = "";
    socialSubName = "";
    $scope.runonce = false;
    $scope.startup();
    $scope.runonce = true;
}]);


// var directives = angular.module('directives', []);
app.directive('myDatepicker',function ($parse) {
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
}).directive('myTagbox', ['$asksg', function ($asksg) {
        return {
            scope: {message: '='},
            link: function (scope, element, attributes) {
                $(function () {
                    whenAddingTag = function (tag) {
                        $asksg.addTag(tag, scope.message.id).
                            success(function (data, status, headers, config) {
                                scope.message.tags.push(new Tag(data[0].id, data[0].name));
                            });
                    };
                    tagRemoved = function (tag) {
                        tagid = _.findWhere(scope.message.tags, {name: tag}).id;
                        $asksg.deleteTag(tagid, scope.message.id);
                    };
                    $(element).tags({
                        whenAddingTag: whenAddingTag,
                        tagRemoved: tagRemoved,
                        tagData: _.map(scope.message.tags, function (tag) {
                            return tag.name
                        }),
                        promptText: "Add a tag..."
                    });
                });
            }
        }
    }]).directive('searchTagbox', function ($parse) {
        return {
            link: function (scope, element, attrs) {
                $(function () {
                    whenAddingTag = function (tag) {
                        var invoker = $parse(attrs.ctrlFn);
                        var params = {};
                        params.includeTags = [tag];
                        invoker(scope, {tagval : params});
                    };
                    tagRemoved = function (tag) {
                        var invoker = $parse(attrs.ctrlFn);
                        invoker(scope, {tagval : {}});
                    };
                    $(element).tags({
                        whenAddingTag: whenAddingTag,
                        tagRemoved: tagRemoved,
                        tagData: [],
                        promptText: "Add a tag to filter"
                    });
                });
            }
        }
    }).directive('dndBetweenList', function($parse) {
 
    return function(scope, element, attrs) {
 
        // contains the args for this component
        var args = attrs.dndBetweenList.split(',');
        // contains the args for the target
        var targetArgs = $('#'+args[1]).attr('dnd-between-list').split(',');
 
        // variables used for dnd
        var toUpdate;
        var target;
        var startIndex = -1;
        var toTarget = true;
 
        // watch the model, so we always know what element
        // is at a specific position
        scope.$watch(args[0], function(value) {
            toUpdate = value;
        },true);
 
        // also watch for changes in the target list
        scope.$watch(targetArgs[0], function(value) {
            target = value;
        },true);
 
        // use jquery to make the element sortable (dnd). This is called
        // when the element is rendered
        $(element[0]).sortable({
            items:'li',
            start:function (event, ui) {
                // on start we define where the item is dragged from
                startIndex = ($(ui.item).index());
                toTarget = false;
            },
            stop:function (event, ui) {
                var newParent = ui.item[0].parentNode.id;
 
                // on stop we determine the new index of the
                // item and store it there
                var newIndex = ($(ui.item).index());
                var toMove = toUpdate[startIndex];
 
                // we need to remove him from the configured model
                toUpdate.splice(startIndex,1);
 
                if (newParent == args[1]) {
                    // and add it to the linked list
                    target.splice(newIndex,0,toMove);
                }  else {
                    toUpdate.splice(newIndex,0,toMove);
                }
 
                // we move items in the array, if we want
                // to trigger an update in angular use $apply()
                // since we're outside angulars lifecycle
                scope.$apply(targetArgs[0]);
                scope.$apply(args[0]);
            },
            connectWith:'#'+args[1]
        })
    }
});

/*
 app.run(['ConversationController', function(ConversationController){
 ConversationController.$scope.startup();
 }]);

 */