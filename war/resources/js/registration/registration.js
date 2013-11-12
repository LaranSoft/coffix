$(function(){
	$('#index_registration-submit-button').bind('click', function() {
		$('#errorMessage').addClass('noDisplay');
		var self = $(this);
		
		self.attr('disabled', true);
		
		$.post('registration', {
			u: $('#index_username-input').val(),
			p: MD5($('#index_password-input').val()),
			cp: MD5($('#index_confirmpassword-input').val()),
			d: $('#index_displayName-input').val()
		}).done(function(response){
			if(response.substr(0, 9) == 'redirect_'){
				response = response.substr(9);
				redirect(response);
			} else if(response.substr(0, 6) == 'error_'){
				response = response.substr(6);
				// TODO gestire gli errori
		    } else {
		    	window.location.replace(response);
		    }
		});
	});
			
	if(errorCode){
		$('#errorMessage').html(errorCodes[errorCode]).removeClass('noDisplay');
	}
	
	$('#index_registration-cancel-button').bind('click', function(){
		window.history.back();
	});
});	