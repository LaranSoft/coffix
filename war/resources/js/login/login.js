$(function(){
        
    UI = $.extend(UI, {
        errorMessageLabel: $('#errorMessage'),
        submitLoginBtn: $('#index_login-submit-button'),
        passwordInput: $('#index_password-input'),
        usernameInput: $('#index_username-input'),
        registrationLink: $('#registrationLink')
    });
    
    var onFail = function(errorCode){
        UI.errorMessageLabel.html(errorCodes[errorCode]).removeClass('noDisplay');
        UI.submitLoginBtn.attr('disabled', false);
        UI.passwordInput.val('');
        UI.usernameInput.val('').focus();
        hideLoadingMask();
    };
    
    UI.submitLoginBtn.bind('click', function() {
        var self = $(this);
        UI.errorMessageLabel.addClass('noDisplay');
        
        self.attr('disabled', true);
        showLoadingMask(function(){
            $.post('login', {
                u: UI.usernameInput.val(), 
                p: MD5(UI.passwordInput.val()),
                fwd: fwd
            }).done(function(response){
                window.location.replace(response);
            }).fail(function(){
                onFail('101');
            });
        });
    });

    
    $(UI.passwordInput).bind('keypress', function(event){
        if(event.keyCode == 13){
            UI.submitLoginBtn.click();
        }
    });
    
    UI.registrationLink.on('click', function(){
        showLoadingMask(function(){
            $.post('registration', {
            }).done(function(response){
                window.location.href = response;
            }).fail(function(){
                onFail('101');
            });
        });
    });
    
    if(errorCode){
        UI.errorMessageLabel.html(errorCodes[errorCode]).removeClass('noDisplay');
    }
}); 