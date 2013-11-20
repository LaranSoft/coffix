var loginPage = {

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
	                	$.post('homePage', function(response){
	                		$('#pageContainer').html($.trim(response));
	            			homePage.onInit();
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
	        showLoadingMask(function(){
	        	$.post('registrationPage').done(function(response){
            		$('#pageContainer').html($.trim(response));
            		registrationPage.onInit();
        			hideLoadingMask();
        		}).fail(function(){
        			// TODO mostrare l'errore generico
        		});
	        });
	    });
	    
	    statusBar.onInit();
	}
};