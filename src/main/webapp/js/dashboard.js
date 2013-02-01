// Cache of hidden forms
$responseForms = new Array();
$convo_navbar = $("#convo-navbar");

// The page load handler
$(document).ready(function() {
	// Hide response boxes to start
	$('.responseForm').hide();

	// Hide the search bar to start
	$("#conversationSearch").hide();
	$searchBarHidden = true;

	// Make the converation bar move with the stuff on the way down
	/*$(window).scroll(function() {
		floatConvoMenu();
	});*/
	
	// Handle the changing styles for the services item.
	$(".service-menu-item").click(function() {
		if ($(this).hasClass("active")) {
			$(this).removeClass("active");	
		} else {
			$(this).addClass("active");
		}
	});

	// Hide/show the delete button when conversations are hovered over.
	$('#deleteButton').hide(); // hide by default
	$('.convo-list-item-msg').hover(
		function() {
			console.log("hover...");
        	$(this).find('#deleteButton').show();
    	},
    	function () {
        	$(this).find('#deleteButton').hide();
    	}
	);
	console.log("set up show/hide");
});

/*
	$('#text-toggle-button').toggleButtons({
    	width: 220,
    	label: {
        	enabled: "Lorem Ipsum",
        	disabled: "Dolor Sit"
    	}
	});
*/

/*
function floatConvoMenu() {
    var scrollAmount = $(document).scrollTop();
    var newPosition = $convo_navbar.height() + scrollAmount;
    if($(window).height() < $convo_navbar.height() + $convo_navbar.height()){
        $convo_navbar.css("top", menuPosition);
    } else {
        $convo_navbar.css("top", newPosition);
    }
}*/

function showResponseForm(id) {
	if ($responseForms.indexOf('#response-form-' + id) != -1) {
		$responseForms.push('#response-form-' + id);
		$responseForms['#response-form-' + id] = true;
		$('#response-form-' + id).show();
	} else {
		if ($responseForms['#response-form-' + id] == true) {
			$responseForms['#response-form-' + id] = false;
			$('#response-form-' + id).hide();
		} else {
			$responseForms['#response-form-' + id] = true;
			$('#response-form-' + id).show();
		}
	}
}

function renderTree(obj, id) {
	if (obj != null) {
		console.log("Trying to render the reply tree");
  		var f, li, ul = document.createElement ("ul");

  		// debug
  		console.log(obj);
  		console.log(obj.reply.length);

  		for (f = 0; f < obj.reply.length; f++) {
    		li = document.createElement ("li");
    		li.appendChild (document.createTextNode (obj.reply[f].msg));
    		// if the child has a 'reply' prop on its own, recurse...
    		/*if (obj.reply[f].reply) {
      			li.appendChild (to_ul (obj.reply[f].reply));
    		}*/
    		ul.appendChild (li);
  		}
  		console.log(ul);
  		console.log($('#reply-tree-' + id).html());
  		$('#reply-tree-' + id).html(ul);
  		console.log($('#reply-tree-' + id).html());
	} else {
		console.log("There are no replies for this conversation yet.");
	}
}