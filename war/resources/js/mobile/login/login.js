$(document).on('pageinit', function(){
	$('#loginButton').bind('tap', function(){
		$.post('login', {
			u: $('#index_username-input').val(), 
			p: MD5($('#index_password-input').val()),
			fwd: fwd
		}).done(function(response){
			if(response.substr(0, 9) == 'redirect_'){
				response = response.substr(9);
				redirect(response);
				onFail(response);
			} else if(response.substr(0, 6) == 'error_'){
				response = response.substr(6);
				UI.createGroupErrorLabel.html(errorCodes[response]).removeClass('noDisplay');
				removeLoading();
				onFail(response);
			} else {
		    	window.location.href = response;
			}
		});
	});
	
	$('#registrationLink').bind('tap', function(){
		$.post('registration', {
		}).done(function(response){
			if(response.substr(0, 6) == 'error_'){
				response = response.substr(6);
				// TODO mostrare l'errore
			} else {
		    	window.location.href = response;
			}
		});
	});
});