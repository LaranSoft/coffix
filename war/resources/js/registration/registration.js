var registrationPage = {
		
	statusBarOptions: {
		showProfileControls: false,
		useCustomStatusBarContent: true,
		statusBarCustomContent: '<span class="statusBarRegistrationTitle">Registrazione</span>',
		showBackButton: true,
		backPage: 'homePage'
	},
		
	render: function(data){
		var html = statusBar.render(registrationPage.statusBarOptions);
		
		html += '<div class="row high">';
		html +=     '<label id="errorMessage" class="defaultLabel red smallLabel noDisplay"></label>';
		html += '</div>';
		html += '<div class="row">';
		html +=     '<div class="lTextAligned">';
		html +=         '<form id="index_registration-form">';
		html +=             '<input id="index_username-input" class="defaultInput" name="username" type="email" placeholder="' + data.bundles.usernameLabel + '"/>';
		html +=             '<input id="index_password-input" class="defaultInput" name="password" type="password" placeholder="' + data.bundles.passwordLabel + '"/>';
		html +=             '<input id="index_confirmpassword-input" class="defaultInput" name="confirmPassword" type="password" placeholder="' + data.bundles.confirmPasswordLabel + '"/>';
		html +=             '<input id="index_displayName-input" class="defaultInput" name="displayName" type="text" placeholder="' + data.bundles.displayNameLabel + '"/>';
		html +=         '</form>';
		html +=     '</div>';
		html += '</div>';
		html += '<div class="row">';
		html +=     '<button id="index_registration-submit-button" class="defaultButton blue rFloating" type="button">';
		html +=         '<span class="defaultButtonLabel blueButtonLabel">' + data.bundles.buttonLabel + '</span>';
		html +=     '</button>';
		html += '</div>';
	
		return html;
	},
		
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
	                	loadPage('homePage', function(){
	            			hideLoadingMask();
	                	});
				    }
				});
			});
		});
				
		statusBar.onInit();
	}
};	