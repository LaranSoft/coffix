<script>
	var statusBar = {
		callback: [],
		onInit: function(){
			for(var i=0; i<statusBar.callback.length; i++){
				statusBar.callback[i]();
			}
		}	
	};
</script>

<c:if test="${user != null && hideProfileBtn != true}">
	<div id="changePasswordMask" class="dialogMask noDisplay">
		<div id="changePasswordMaskInner">
			<label class="defaultLabel smallLabel dialogLabel" toggleSet="changePassword">Cambia password</label>
			<br>
			<input id="changePassword_actual-input" toggleSet="changePassword" class="defaultInput" name="actual" type="password" placeholder="Inserisci la password attuale"/>
			<br>
			<input id="changePassword_new-input" toggleSet="changePassword" class="defaultInput" name="new" type="password" placeholder="Inserisci la nuova password"/>
			<br>
			<input id="changePassword_confirmNew-input" toggleSet="changePassword" class="defaultInput" name="confirmNew" type="password" placeholder="Conferma la nuova password"/>
			<br>
			<button id="changePassword-cancel-button" toggleSet="changePassword" class="defaultButton red lFloating dialogButton" type="button">
				<span class="defaultButtonLabel blueButtonLabel"><fmt:message key="COMMON.CANCEL"/></span>
			</button>
			<button id="changePassword-confirm-button" toggleSet="changePassword" class="defaultButton blue rFloating dialogButton" type="button">
				<span class="defaultButtonLabel blueButtonLabel"><fmt:message key="COMMON.CONFIRM"/></span>
			</button>
		</div>
	</div>
</c:if>

<div id="profileContainer">
	<c:if test="${hideBackButton != true}">
		<span id="backBtn" class="statusBar_btn lFloating white">&#x27A7;</span>
		<script>
			statusBar.callback.push(function(){
				UI = $.extend(UI, {
	                 backBtn: $('#backBtn')
	            });
				
				UI.backBtn.on('click', function(){
                	showLoadingMask(function(){
                		$.post('${backPage}', function(response){
                			$('#pageContainer').html($.trim(response));
	            			${backPage}.onInit();
	            			hideLoadingMask();
                		});
                	});
                });
			});
		</script>
	</c:if>
	
	<c:if test="${showCoffixIcon != false}">
		<span id="statusBarCoffixIcon" class="lFloating"></span>
		
		<c:if test="${hideBackButton != true}">
			<script>
				statusBar.callback.push(function(){
					UI = $.extend(UI, {
						statusBarCoffixIcon: $('#statusBarCoffixIcon')
		            });
					
					UI.statusBarCoffixIcon.addClass('cursorPointer').on('click', function(){
	                	UI.backBtn.click();
	                });
				});
			</script>
		</c:if>
	</c:if>
	
	<c:if test="${hideProfileControls != true}">
		<c:choose>
			<c:when test="${user != null}">
			
				<c:if test="${hideProfileBtn != true}">
					<span id="statusBar_profileBtnContainer" class="statusBar_btnContainer rFloating">
						<div id="statusBar_profileBtn" class="statusBar_btn"></div>
					</span>
				</c:if>
				
				<c:if test="${showPlusBtn == true}">
					<c:if test="${plusBtnIcon == null}">
						<c:set var="plusBtnIcon">add.png</c:set>
					</c:if>
					<span id="statusBar_plusBtn" class="statusBar_Btn rFloating">
						<img id="statusBar_plusBtnIcon" src="/resources/images/${plusBtnIcon}"/>
					</span>
					<script>
						statusBar.callback.push(function(){
							UI = $.extend(UI, {
				                 statusBarPlusBtn: $('#statusBar_plusBtn')
				            });
							
							UI.statusBarPlusBtn.on('click', ${plusBtnOnClick});
						});
					</script>
				</c:if>

				<c:if test="${hideProfileBtn != true}">
					<div id="profileMenu" class="noDisplay">
						<label id="changePwdBtn" class="defaultLabel noWrap first menuItem">Cambia password</label>
						<label id="logoutBtn" class="defaultLabel noWrap last menuItem"><fmt:message key="COMMON.LOGOUT"/></label>
					</div>
			
			        <script>
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
		                
		            </script>
	            </c:if>
	        
		    </c:when>
		    <c:otherwise>
				<div id="loginButtonsContainer">
					<span id="registerBtn" class="white small rFloating"><fmt:message key="COMMON.REGISTER"/></span>
					<span id="loginBtn" class="white small rFloating"><fmt:message key="COMMON.LOGIN"/></span>
				</div>
				
				<script>
					$('#loginBtn').on('click', function(){
		            	showLoadingMask(function(){
		            		$.post('loginPage', function(response){
		            			$('#pageContainer').html($.trim(response));
		            			loginPage.onInit();
		            			hideLoadingMask();
		            		});
		            	});
		            });
		            
		            $('#registerBtn').on('click', function(){
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
		        </script>
		        
		    </c:otherwise>
		</c:choose>
	</c:if>
	<c:if test="${useCustomStatusBarContent == true}">${statusBarCustomContent}</c:if>
</div>