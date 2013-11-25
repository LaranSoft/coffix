var loginPage = {

	statusBarOptions: {
		showProfileControls: false,
		useCustomStatusBarContent: true,
		statusBarCustomContent: '<span class="statusBarLoginTitle lFloating white fontLarge">Login</span>',
		showBackButton: true,
		backPage: 'homePage'
	},
		
	render: function(data){
		
		var html = statusBar.render(loginPage.statusBarOptions);
		
		html += '<div class="row high noDisplay">';
		html +=     '<label id="errorMessage" class="defaultLabel red smallLabel noDisplay"></label>';
		html += '</div>';
		html += '<div class="row lTextAligned">';
		html +=     '<form id="index_login-form">';
		html +=         '<input id="index_username-input" class="defaultInput" name="username" type="text" autocomplete="off" placeholder="' + data.bundles.usernameLabel + '"/>';
		html +=         '<input id="index_password-input" class="defaultInput" name="password" type="password" autocomplete="off" placeholder="' + data.bundles.passwordLabel + '"/>';
		html +=     '</form>';
		html += '</div>';
		html += '<div class="row rTextAligned">';
		html +=     '<button id="index_login-submit-button" class="defaultButton blue" type="button">';
		html +=         '<span class="defaultButtonLabel blueButtonLabel">' + data.bundles.buttonLabel + '</span>';
		html +=     '</button>';
		html +=     '<div id="noRegistrationLinkContainer">';
		html +=         '<label class="defaultLabel smallLabel blockDisplay">' + data.bundles.noRegistrationMessage + '</label>';
		html +=         '<label id="registrationLink" class="defaultLabel smallLabel link">' + data.bundles.noRegistrationLink + '</label>';
		html +=     '</div>';
		html += '</div>';
		
		return html;
	},
		
	onInit: function(){
	    var loginUI = $.extend({}, UI, {
	        errorMessageLabel: $('#errorMessage'),
	        submitLoginBtn: $('#index_login-submit-button'),
	        passwordInput: $('#index_password-input'),
	        usernameInput: $('#index_username-input'),
	        registrationLink: $('#registrationLink')
	    });
	    
	    var onFail = function(errorCode){
	        loginUI.errorMessageLabel.html(errorCodes[errorCode]).removeClass('noDisplay');
	        loginUI.submitLoginBtn.attr('disabled', false);
	        loginUI.passwordInput.val('');
	        loginUI.usernameInput.val('').focus();
	        hideLoadingMask();
	    };
	    
	    loginUI.submitLoginBtn.bind('click', function() {
	        var self = $(this);
	        loginUI.errorMessageLabel.addClass('noDisplay');
	        
	        self.attr('disabled', true);
	        showLoadingMask(function(){
	            $.post('loginService', {
	                u: loginUI.usernameInput.val(), 
	                p: MD5(loginUI.passwordInput.val())
	            }).done(function(response){
	                response = JSON.parse(response);
	                
	                if(response.status == 'KO'){
	                	onFail(response.errorCode);
	                } else {
	                	getPage('homePage', {}, function(){
	            			hideLoadingMask();
	                	});
	                }
	            }).fail(function(){
	                onFail('101');
	            });
	        });
	    });
	    
	    $(loginUI.passwordInput).bind('keypress', function(event){
	        if(event.keyCode == 13){
	            loginUI.submitLoginBtn.click();
	        }
	    });
	    
	    loginUI.registrationLink.on('click', function(){
	        loadPage('registrationPage');
	    });
	    
	    statusBar.onInit();
	}
};