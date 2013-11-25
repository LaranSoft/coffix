var statusBar = {
	
	callback: [],
	
	onInit: function(){
		for(var i=0; i<statusBar.callback.length; i++){
			statusBar.callback[i]();
		}
		statusBar.callback = [];
	},
		
	defaultOptions: {
		showProfileBtn: true,
		plusBtnIcon: 'add.png',
		showProfileControls: true,
		showCoffixIcon: true
	},
	
	render: function(options){
		
		options = $.extend({}, statusBar.defaultOptions, options);
		
		var html = '';
		
		if(options.user && options.showProfileBtn == true){
			html += '<div id="changePasswordMask" class="dialogMask noDisplay">';
			html +=     '<div id="changePasswordMaskInner">';
			html +=         '<label class="defaultLabel smallLabel dialogLabel" toggleSet="changePassword">Cambia password</label>';
			html +=         '<br>';
			html +=         '<input id="changePassword_actual-input" toggleSet="changePassword" class="defaultInput" name="actual" type="password" placeholder="Inserisci la password attuale"/>';
			html +=         '<br>';
			html +=         '<input id="changePassword_new-input" toggleSet="changePassword" class="defaultInput" name="new" type="password" placeholder="Inserisci la nuova password"/>';
			html +=         '<br>';
			html +=         '<input id="changePassword_confirmNew-input" toggleSet="changePassword" class="defaultInput" name="confirmNew" type="password" placeholder="Conferma la nuova password"/>';
			html +=         '<br>';
			html +=         '<button id="changePassword-cancel-button" toggleSet="changePassword" class="defaultButton red lFloating dialogButton" type="button">';
			html +=             '<span class="defaultButtonLabel blueButtonLabel">Annulla</span>';
			html +=         '</button>';
			html +=         '<button id="changePassword-confirm-button" toggleSet="changePassword" class="defaultButton blue rFloating dialogButton" type="button">';
			html +=             '<span class="defaultButtonLabel blueButtonLabel">Conferma</span>';
			html +=         '</button>';
			html +=     '</div>';
			html += '</div>';
		}
		
		html += '<div id="profileContainer">';
		if(options.showBackButton == true){
			html += '<span id="backBtn" class="statusBar_btn lFloating white">&#x27A7;</span>';
			statusBar.callback.push(function(){
				UI = $.extend(UI, {
	                 backBtn: $('#backBtn')
	            });
					
				UI.backBtn.on('click', function(){
                	showLoadingMask(function(){
                		// HACK da rimuovere una volta che il refactoring per il caricamento delle pagine sarà completato
                		if(options.backPage == 'homePage'){
                			getPage(options.backPage, {}, function(response){
                				hideLoadingMask();
                			});
                		} else {
	                		$.post(options.backPage, function(response){
	                			$('#pageContainer').html($.trim(response));
	                			window[options.backPage].onInit();
		            			hideLoadingMask();
	                		});
                		}
                	});
                });
			});
		}
		
		if(options.showCoffixIcon == true){
			html += '<span id="statusBarCoffixIcon" class="lFloating"></span>';
			
			if(options.showBackButton == true){
				statusBar.callback.push(function(){
					UI = $.extend(UI, {
						statusBarCoffixIcon: $('#statusBarCoffixIcon')
		            });
						
					UI.statusBarCoffixIcon.addClass('cursorPointer').on('click', function(){
	                	UI.backBtn.click();
	                });
				});
			}
		}
		
		if(options.showProfileControls == true){
			if(options.user){
				if(options.showProfileBtn == true){
					html += '<span id="statusBar_profileBtnContainer" class="statusBar_btnContainer rFloating">';
					html +=     '<div id="statusBar_profileBtn" class="statusBar_btn"></div>';
					html += '</span>';
				}
				
				if(options.showPlusBtn == true){
					html += '<span id="statusBar_plusBtn" class="statusBar_Btn rFloating">';
					html +=     '<img id="statusBar_plusBtnIcon" src="/resources/images/' + options.plusBtnIcon + '"/>';
					html += '</span>';
					
					statusBar.callback.push(function(){
						UI = $.extend(UI, {
			                 statusBarPlusBtn: $('#statusBar_plusBtn')
			            });
						
						UI.statusBarPlusBtn.on('click', options.plusBtnOnClick);
					});
				}
				
				if(options.showProfileBtn == true){
					html += '<div id="profileMenu" class="noDisplay">';
					html +=     '<label id="changePwdBtn" class="defaultLabel noWrap first menuItem">Cambia password</label>';
					html +=     '<label id="logoutBtn" class="defaultLabel noWrap last menuItem">Esci</label>';
					html += '</div>';
		
		        	statusBar.callback.push(function(){
			            UI = $.extend(UI, {
			            	profileBtn: $('#statusBar_profileBtn'),
			            	profileBtnContainer: $('#statusBar_profileBtnContainer'),
			            	profileMenu: $('#profileMenu'),
			                linkMailAddressMask: $('#linkMailAddressMask'),
			                changePwdBtn: $('#changePwdBtn'),
				            changePwdActualInput: $('#changePassword_actual-input'),
				            changePwdNewInput: $('#changePassword_new-input'),
				            changePwdConfirmNewInput: $('#changePassword_confirmNew-input'),
				            changePwdMask: $('#changePasswordMask'),
				            changePwdCancelBtn: $('#changePassword-cancel-button'),
				            changePwdConfirmBtn: $('#changePassword-confirm-button')
			            });
			            
		                UI.profileBtn.on('click', function(){
		                    if(UI.profileMenu.attr('d') == '1'){
		                    	UI.profileBtnContainer.removeClass('statusBar_btnContainerPressed');
		                    	UI.profileMenu.attr('d', '0').slideUp(400);
		                    } else {
		                    	UI.profileBtnContainer.addClass('statusBar_btnContainerPressed');
		                    	UI.profileMenu.attr('d', '1').slideDown(400);
		                    }
		                });
		                
		                $('#logoutBtn').on('click', function(){
		                	showLoadingMask(function(){
			                	$.post('logoutService').done(function(response){
			                		response = JSON.parse(response);
					                
					                if(response.status == 'KO'){
					                	onFail(response.errorCode);
					                } else {
					                	$.post('homePage').done(function(response){
					                		$('#pageContainer').html($.trim(response));
					            			homePage.onInit();
					            			hideLoadingMask();
					                	});
				        			}
			                	});
		                	});
		                });
		                
			            UI.changePwdBtn.on('click', function(){
		        			showLoadingMask(function(){
		        				UI.changePwdMask.removeClass('noDisplay').center('absolute', null, null, '10000');
		        				UI.changePwdActualInput.focus();
		        			});
			            });
			            
			            UI.changePwdCancelBtn.on('click', function(){
			        		resetAndHideChangePasswordMask();
			        	});
			            
			            UI.changePwdConfirmBtn.on('click', function(){
			            	$.post('changePasswordService', {
			            		a: MD5(UI.changePwdActualInput.val()),
			            		n: MD5(UI.changePwdNewInput.val()),
			            		cn: MD5(UI.changePwdConfirmNewInput.val())
			        		}).done(function(response){
			        			response = JSON.parse(response);
				                
				                if(response.status == 'KO'){
				                	onFail(response.errorCode);
				                } else {
			        				resetAndHideChangePasswordMask();
			        			}
			        		});
			            });
			            
			            var resetAndHideChangePasswordMask = function(){
			        		UI.changePwdActualInput.val('');
			        		UI.changePwdNewInput.val('');
			        		UI.changePwdConfirmNewInput.val('');
			        		UI.changePwdMask.addClass('noDisplay');
			        		
			        		hideLoadingMask(function(){
			        			UI.changePwdBtn.removeAttr('disabled');
			        		});
			        	};
		        	});
				} 
			} else {
				
				html += '<div id="loginButtonsContainer">';
				html +=     '<span id="registerBtn" class="white small rFloating">Registrati</span>';
				html +=     '<span id="loginBtn" class="white small rFloating">Accedi</span>';
				html += '</div>';
			
				statusBar.callback.push(function(){
					$('#loginBtn').on('click', function(){
	            		loadPage('loginPage');
		            });
	            
		            $('#registerBtn').on('click', function(){
		            	loadPage('registrationPage');
	                });
				});
			}
		}
		
		if(options.useCustomStatusBarContent == true){
			html += options.statusBarCustomContent;
		}
		
		html += '</div>';
		
		statusBar.callback.push(function(){
			$('#main-container').on('scroll touchmove', function(){
				var margin = 25 - $(this).scrollTop();
				if(margin < 0){
					$('#profileContainer').addClass('shadow');
				} else {
					$('#profileContainer').removeClass('shadow');
				}
			});
		
			$('#profileContainer').css('position', 'fixed');
		
			$(window).smartresize(function(){
				$('#profileContainer').css('position', 'absolute');
				setTimeout(function(){
					$('#profileContainer').css('position', 'fixed');	
				}, 0);
			});
		});
		
		return html;
	}
};