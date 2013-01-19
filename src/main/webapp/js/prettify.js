// Load callback
$(document).ready(function() {
    // Handle scrolling to the center of the page
    $('.scrollToLearnMore').click(function() {
        //$(document).scrollTo('#learn-more');
        $('html, body').animate({scrollTop: $("#learn-more").offset().top}, 750); // seems reasonably quick enough
    });

    /*
    // Show the modal by revealing the mask and dropping in the login modal
    $('a.login-window').click(function() {
        // Getting the variable's value from a link 
        var loginBox = $(this).attr('href');

        // Fade in the Popup
        $(loginBox).fadeIn(300);
         
        // Set the center alignment padding + border see css style
        var popMargTop = ($(loginBox).height() + 24) / 2; 
        var popMargLeft = ($(loginBox).width() + 24) / 2; 
        
        $(loginBox).css({ 
            'margin-top' : -popMargTop,
            'margin-left' : -popMargLeft
        });
        
        // Add the mask to body
        $('body').append('<div id="mask"></div>');
        $('#mask').fadeIn(300);
        
        return false;
    });
    
    // When clicking on the button close or the mask layer the popup closed
    $('a.close, #mask').live('click', function() { 
        $('#mask , .login-popup').fadeOut(300 , function() {
            $('#mask').remove();  
        }); 
        return false;
    });
    */
});
 

// Function to reposition the navigation bar
/*function floatMenu() {
    var scrollAmount = $(document).scrollTop();
    var newPosition = menuPosition + scrollAmount;
    if($(window).height() < $fl_menu.height() + $fl_menu_menu.height()) {
        $fl_menu.css("top", menuPosition);
    } else {
        $fl_menu.css("top", newPosition);
    }
}*/