var createCoffer;

var groupOverviewPage = {
	onInit: function(){
	
		var groupOverviewUI = $.extend({}, UI, {
			inviteUserBtn: $('#inviteUserBtn'), 
			otherwiseBtn: $('#otherwiseBtn'),
			positionLabel: $('#positionLabel'),
			statusBarGroupOverviewTitle: $('.statusBarGroupOverviewTitle')
		});
		
		var goToManageGroupPage = function(){
			showLoadingMask(function(){
				$.post('manageGroupPage', {
					groupId: groupId
				}).done(function(response){
					if(response.substr(0, 9) == 'redirect_'){
						response = response.substr(9);
						redirect(response);
					} else {
						$('#pageContainer').html($.trim(response));
	            		manageGroupPage.onInit();
            			hideLoadingMask();
					}
				});
			});
		};
		
		groupOverviewUI.inviteUserBtn.bind('click', goToManageGroupPage);
		groupOverviewUI.statusBarGroupOverviewTitle.bind('click', goToManageGroupPage);
		
		createCoffer = function(){
			showLoadingMask(function(){
				$.post('chooseOffererPage', {
					groupId: groupId
				}).done(function(response){
					if(response.substr(0, 9) == 'redirect_'){
						response = response.substr(9);
						redirect(response);
					} else {
						$('#pageContainer').html($.trim(response));
	            		chooseOffererPage.onInit();
            			hideLoadingMask();
				    }
				});
			});
		};
		
		groupOverviewUI.otherwiseBtn.bind('click', function(){
			showLoadingMask(function(){
				$.post('groupOverviewPage', {
					i: index, 
					groupId: groupId
				}).done(function(response){
					if(response.substr(0, 9) == 'redirect_'){
						response = response.substr(9);
						redirect(response);
					} else {
						$('#pageContainer').html($.trim(response));
	            		groupOverviewPage.onInit();
            			hideLoadingMask();
				    }
				}).fail(function(){
					hideLoadingMask();
				});
			});
		});
		
		for(var i=0; i<coffers.length; i++){
			var coffer = coffers[i];
			
			var html = '<h3 offeredContainer>';
			html += '<span class="cofferExpirationTime expirationTimeLabel noWrap" key="' + coffer.key + '" state="' + coffer.state + '" expirationTime="' + coffer.expireTime + '"></span>';
			
			var offererColorClass = coffer.offerer.hasNegate ? ' negate': '';
			
			html += '<span class="coffererDisplayName' + offererColorClass + ' noWrap">' + coffer.offerer.displayName + '</span>';
			
			if(loggedUser == coffer.offerer.displayName){
				if(coffer.offerer.hasNegate){
					html += '<button id="' + coffer.key + ';' + coffer.offerer.username + ';1" offered toggleVisibility class="confirmCoffer defaultButton red small rFloating noVisibility" type="button">';
					html +=	'<span class="defaultButtonLabel blueButtonLabel">' + bundles.confirm + '</span>';
					html +=	'</button>';
				} else {
					html += '<button id="' + coffer.key + ';' + coffer.offerer.username + ';0" offered toggleVisibility class="confirmCoffer defaultButton blue small rFloating noVisibility" type="button">';
					html +=	'<span class="defaultButtonLabel blueButtonLabel">' + bundles.negate + '</span>';
					html +=	'</button>';
				}
			}
			
			html += '<span toggleVisibility class="cofferCreator noVisibility">' + bundles.createdBy + '&nbsp;' + coffer.creator.displayName + '</span>';
			html += '</h3>'
			
			html += '<div>';
			for(var j=0; j<coffer.offereds.length; j++){
				var offered = coffer.offereds[j];
				
				html += '<div offeredContainer class="offeredContainer';
				if(j == coffer.offereds.length - 1){
					html += ' last';
				}
				html += '">';
				
				html += '<p';
				
				if(offered.hasNegate){
					html += ' class="negate"';
				}
				
				html += '>' + offered.displayName + '</p>';
				
				if(loggedUser == offered.displayName){
					if(offered.hasNegate){
						html += '<button id="' + coffer.key + ';' + offered.username + ';1" offered toggleVisibility class="confirmCoffer defaultButton red small rFloating noVisibility" type="button">';
						html +=	'<span class="defaultButtonLabel blueButtonLabel">' + bundles.confirm + '</span>';
						html +=	'</button>';
					} else {
						html += '<button id="' + coffer.key + ';' + offered.username + ';0" offered toggleVisibility class="confirmCoffer defaultButton blue small rFloating noVisibility" type="button">';
						html +=	'<span class="defaultButtonLabel blueButtonLabel">' + bundles.negate + '</span>';
						html +=	'</button>';
					}
				}
				html += '</div>';
			}
			html += '</div>';
			
			$('#index_activeCoffers').append(html);
		}
		
		$('[offered]').on('click', function(event){
			var self = this;
			showLoadingMask(function(){
				$.post('confirmCofferService', {
					a: $(self).attr('id'),
					groupId: groupId
				}).done(function(response){
					response = JSON.parse(response);
	                
	                if(response.status == 'KO'){
	                	onFail(response.errorCode);
	                } else {
	                	$.post('groupOverviewPage', {
	    					i: index, 
	    					groupId: groupId
	    				}).done(function(response){
	    					if(response.substr(0, 9) == 'redirect_'){
	    						response = response.substr(9);
	    						redirect(response);
	    					} else {
	    						$('#pageContainer').html($.trim(response));
	    	            		groupOverviewPage.onInit();
	                			hideLoadingMask();
	    				    }
	    				}).fail(function(){
	    					hideLoadingMask();
	    				});
				    }
				}).fail(function(){
					hideLoadingMask();
				});
			});
		});
		
		$('#index_activeCoffers').accordion({
			collapsible: true,
			active: false,
			heightStyle: 'content',
			activate: function(event, ui){
				$('#overflow').css('height', $('#main-content').outerHeight() + (25 - $('#index-container').attr('m')));
			}
		});
		
		if(index && Number(index) > 2){
			groupOverviewUI.positionLabel.html(bundles.positionLabel);
		}
		
		var updateRemainingTimes = function(){
			$('[expirationTime]').each(function(){
				var remainingTime = formatRemainingTime($(this).attr('expirationTime'));
				if(remainingTime){
					$(this).html(remainingTime);
				} else {
					$(this).text('').removeClass('expirationTimeLabel');
					var state = $(this).attr('state');
					var icon = 'save';
					var action = 1;
					if(state == '-1'){
						icon = 'cancel';
						action = 0;
					}
					
					var html = '<img registerCoffer action="' + action + '" key="' + $(this).attr('key') + '" class="registerCoffer" src="/resources/images/coffee_' + icon + '.png"/>';
					
					$(this).removeAttr('expirationTime').append(html);
					$(this).find('[registerCoffer]').removeAttr('registerCoffer').on('click', function(){
						var self = $(this);
						showLoadingMask(function(){
							$.post('registerCofferService', {
								k: self.attr('key'),
								a: self.attr('action'),
								groupId: groupId
							}).done(function(response){
								response = JSON.parse(response);
				                
				                if(response.status == 'KO'){
				                	onFail(response.errorCode);
				                } else {
				                	$.post('groupOverviewPage', {
				    					i: index, 
				    					groupId: groupId
				    				}).done(function(response){
				    					if(response.substr(0, 9) == 'redirect_'){
				    						response = response.substr(9);
				    						redirect(response);
				    					} else {
				    						$('#pageContainer').html($.trim(response));
				    	            		groupOverviewPage.onInit();
				                			hideLoadingMask();
				    				    }
				    				}).fail(function(){
				    					hideLoadingMask();
				    				});
							    }
							}).fail(function(){
								hideLoadingMask();
							});
						});
					});
				}
			});
		};
		
		var startUpdating = function(){
			updateRemainingTimes();
			setTimeout(startUpdating, 1000);
		};
		
		bindCofferControls();
		
		startUpdating();
		
		statusBar.onInit();
	}
};