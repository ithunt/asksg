// Cache of hidden forms
$responseForms = new Array();
$convo_navbar = $("#convo-navbar");

// The page load handler
$(document).ready(function () {
	// Hide response boxes to start
	$('.responseForm').hide();

	// Handle the changing styles for the services item.
	$('#sidepane div ul').on('click', '.service-menu-item', function () {
		if ($(this).hasClass("active")) {
			$(this).removeClass("active");
		} else {
			$(this).addClass("active");
		}
	});

	// debug for peace of mind
	$('.deleteButton').click(function () {
		console.log("Trying to delete something");
	});

	// Set up tabs
	$('.nav-tabs a').click(function (e) {
		e.preventDefault();
		$(this).tab('show');
	});

	// Set up button expanstion for submitting new services
	$('#twilioSubmit').css('display', 'none');
	$('#twilioSubmitButton').click(function () {
		$('#twilioSubmit').slideToggle('slow');
		$(this).toggleClass('slideSign');
		return false;
	});
	$('#emailSubmit').css('display', 'none');
	$('#emailSubmitButton').click(function () {
		$('#emailSubmit').slideToggle('slow');
		$(this).toggleClass('slideSign');
		return false;
	});
	$('#twitterSubmit').css('display', 'none');
	$('#twitterSubmitButton').click(function () {
		$('#twitterSubmit').slideToggle('slow');
		$(this).toggleClass('slideSign');
		return false;
	});
	$('#facebookSubmit').css('display', 'none');
	$('#facebookSubmitButton').click(function () {
		$('#facebookSubmit').slideToggle('slow');
		$(this).toggleClass('slideSign');
		return false;
	});
	$('#redditSubmit').css('display', 'none');
	$('#redditSubmitButton').click(function () {
		$('#redditSubmit').slideToggle('slow');
		$(this).toggleClass('slideSign');
		return false;
	});


	// Hide/show the delete button when conversations are hovered over.
	$('.deleteButton').hide(); // hide by default
	$('.convo-list-item-msg').hover(
		function () {
			console.log("hover...");
			$(this).find('.deleteButton').show();
		},
		function () {
			$(this).find('.deleteButton').hide();
		}
	);
	console.log("set up show/hide");

	//setup tagging
	whenAddingTag = function (tag) {
		console.log(tag);
		// maybe fetch some content for the tag popover (can be HTML)
	};
	tagRemoved = function (tag) {
		debugger;
		//find tag context and remove from conversation
	};
	/*
	 $('.tag-list').tags({
	 suggestions: ["suggestions"],
	 tagData: ["tag a", "tag b", "tag c", "tag d"],
	 excludeList: ["the", "is", "a"],
	 whenAddingTag: whenAddingTag,
	 tagRemoved: tagRemoved
	 });*/


});

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
		var f, li, ul = document.createElement("ul");

		// debug
		console.log(obj);
		console.log(obj.reply.length);

		for (f = 0; f < obj.reply.length; f++) {
			li = document.createElement("li");
			li.appendChild(document.createTextNode(obj.reply[f].msg));
			ul.appendChild(li);
		}
		console.log(ul);
		console.log($('#reply-tree-' + id).html());
		$('#reply-tree-' + id).html(ul);
		console.log($('#reply-tree-' + id).html());
	} else {
		console.log("There are no replies for this conversation yet.");
	}
}