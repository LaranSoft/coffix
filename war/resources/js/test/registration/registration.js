var registrationPage = {
	onInit: function(){
		$('#index_registration-submit-button').bind('click', function() {
			showLoadingMask(function(){
				var self = $(this);
				
				self.attr('disabled', true);
				
				$.post('registrationService', {
					u: $('#index_username-input').val(),
					p: MD5($('#index_password-input').val()),
					cp: MD5($('#index_confirmpassword-input').val()),
					d: $('#index_displayName-input').val()
				}).done(function(response){
					response = JSON.parse(response);
	                
	                if(response.status == 'KO'){
	                	onFail(response.errorCode);
	                } else {
	                	$.post('homePage', function(response){
	                		$('#pageContainer').html($.trim(response));
	            			homePage.onInit();
	            			hideLoadingMask();
	                	});
				    }
				});
			});
		});
				
		statusBar.onInit();
	}
};	