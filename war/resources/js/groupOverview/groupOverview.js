var createCoffer = function(){};
var delta = 0;

var groupOverviewPage = {
	
	render: function(data){
		
		createCoffer = function(){
			loadPage('chooseOffererPage', {
				groupId: data.groupId
			});
		};
		
		var statusBarOptions = {
			showPlusBtn: true,
			plusBtnOnClick: createCoffer,
			plusBtnIcon: 'coffee_add.png',
			useCustomStatusBarContent: true,
			statusBarCustomContent: '<span class="statusBarGroupOverviewTitle">' + data.groupName + '<br><span class="statusBarGroupOverviewSubTitle">' + data.bundles.groupDetailsLink + '</span></span>',
			showProfileBtn: false,
			showBackButton: true,
			backPage: 'homePage',
			user: data.user
		};
		
		var html = statusBar.render(statusBarOptions);
		
		if(!data.nextOfferer){
			html += '<div class="row high">';
			html +=     '<label class="defaultLabel">' + data.bundles.noUserPresent + '</label>';
			html += '</div>';
			html += '<button id="inviteUserBtn" class="defaultButton blue" type="button">';
			html +=     '<span class="defaultButtonLabel blueButtonLabel">' + data.bundles.addUserToGroup + '</span>';
			html += '</button>';
		} else {
			html += '<div class="row high">';
			html +=     '<label id="positionLabel" class="defaultLabel">' + data.bundles.todayIs + '</label>';
			html += '</div>';
			html += '<div class="row">';
			html +=     '<span class="defaultLabel noWrapLabel">' + data.nextOfferer + '</span>';
			html += '</div>';
			html += '<div class="row">';
			html +=     '<button id="otherwiseBtn" class="defaultButton blue small" type="button">';
			html +=         '<span class="defaultButtonLabel blueButtonLabel">' + data.bundles.otherwise + '</span>';
			html +=     '</button>';
			html += '</div>';
			html += '<div class="row full high lTextAligned">';
			html +=     '<div id="index_activeCoffers"></div>';
			html += '</div>';
		}
		
		return html;
	},
		
	onInit: function(data){
	
		var nowByServer = data.now;
		var nowByClient = new Date().getTime();
		
		delta = Number(nowByServer) - nowByClient;
		
		var groupOverviewUI = $.extend({}, UI, {
			inviteUserBtn: $('#inviteUserBtn'), 
			otherwiseBtn: $('#otherwiseBtn'),
			positionLabel: $('#positionLabel'),
			statusBarGroupOverviewTitle: $('.statusBarGroupOverviewTitle')
		});
		
		var goToManageGroupPage = function(){
			showLoadingMask(function(){
				$.post('manageGroupPage', {
					groupId: data.groupId
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
		
		groupOverviewUI.otherwiseBtn.bind('click', function(){
			showLoadingMask(function(){
				loadPage('groupOverviewPage', {
					i: data.index, 
					groupId: data.groupId
				});
			});
		});
		
		for(var i=0; i<data.coffers.length; i++){
			var coffer = data.coffers[i];
			
			coffer.creator = JSON.parse(coffer.creator);
			coffer.offerer = JSON.parse(coffer.offerer);
			coffer.offereds = JSON.parse(coffer.offereds);
			
			var html = '<h3 offeredContainer>';
			html += '<span class="cofferExpirationTime expirationTimeLabel noWrap" key="' + coffer.key + '" state="' + coffer.state + '" expirationTime="' + coffer.expireTime + '"></span>';
			
			var offererColorClass = coffer.offerer.hasNegate ? ' negate': '';
			
			html += '<span class="coffererDisplayName' + offererColorClass + ' noWrap">' + coffer.offerer.displayName + '</span>';
			
			if(data.user == coffer.offerer.displayName){
				if(coffer.offerer.hasNegate){
					html += '<button id="' + coffer.key + ';' + coffer.offerer.username + ';1" offered toggleVisibility class="confirmCoffer defaultButton red small rFloating noVisibility" type="button">';
					html +=	'<span class="defaultButtonLabel blueButtonLabel">' + data.bundles.confirm + '</span>';
					html +=	'</button>';
				} else {
					html += '<button id="' + coffer.key + ';' + coffer.offerer.username + ';0" offered toggleVisibility class="confirmCoffer defaultButton blue small rFloating noVisibility" type="button">';
					html +=	'<span class="defaultButtonLabel blueButtonLabel">' + data.bundles.negate + '</span>';
					html +=	'</button>';
				}
			}
			
			html += '<span toggleVisibility class="cofferCreator noVisibility">' + data.bundles.createdBy + '&nbsp;' + coffer.creator.displayName + '</span>';
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
				
				if(data.user == offered.displayName){
					if(offered.hasNegate){
						html += '<button id="' + coffer.key + ';' + offered.username + ';1" offered toggleVisibility class="confirmCoffer defaultButton red small rFloating noVisibility" type="button">';
						html +=	'<span class="defaultButtonLabel blueButtonLabel">' + data.bundles.confirm + '</span>';
						html +=	'</button>';
					} else {
						html += '<button id="' + coffer.key + ';' + offered.username + ';0" offered toggleVisibility class="confirmCoffer defaultButton blue small rFloating noVisibility" type="button">';
						html +=	'<span class="defaultButtonLabel blueButtonLabel">' + data.bundles.negate + '</span>';
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
					groupId: data.groupId
				}).done(function(response){
					response = JSON.parse(response);
	                
	                if(response.status == 'KO'){
	                	onFail(response.errorCode);
	                } else {
	                	loadPage('groupOverviewPage', {
	    					groupId: data.groupId
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
		
		if(data.index && Number(data.index) > 2){
			groupOverviewUI.positionLabel.html(data.bundles.positionLabel);
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
								groupId: data.groupId
							}).done(function(response){
								response = JSON.parse(response);
				                
				                if(response.status == 'KO'){
				                	onFail(response.errorCode);
				                } else {
				                	loadPage('groupOverviewPage', {
				    					i: data.index, 
				    					groupId: data.groupId
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