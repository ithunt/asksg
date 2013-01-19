// Create the conversation controller
function ConversationController($scope, $http) {

//***********************************
// Throw in some dummy data for local tests
//***********************************
 $scope.convos = [
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

// The menu controller 
function MenuController($scope) {
	$scope.userName = "Admin";
}
