var showCreateGroupDialog;

var homePage = {
	onInit: function(){
		homePageUI = $.extend({}, UI, {
			errorMessageLabel: $('#errorMessage'),
			homeContainer: $('#homeContainer'),
			headerLabel: $('#headerLabel'),
			invitationHeaderLabel: $('#invitationHeaderLabel'),
			groupContainer: $('#groupContainer'),
			invitationContainer: $('#invitationContainer'),
			createGroupBtn: $('#createGroupBtn'),
			createGroupMaskToggleSet: $('[toggleSet=createGroup]'),
			createGroupMask: $('#createGroupMask'),
			createGroupMaskInner: $('#createGroupMaskInner'),
			createGroupErrorLabel: $('#createGroupErrorLabel'),
			createGroupNameInput: $('#createGroup_name-input'),
			createGroupCancelBtn: $('#createGroup-cancel-button'),
			createGroupConfirmBtn: $('#createGroup-confirm-button')
		});
		
		var onFail = function(errorCode){
			homePageUI.errorMessageLabel.html(errorCodes[errorCode]).parent().removeClass('noDisplay');
			hideLoadingMask();
		};
		
		var createGroupList = function(groupMap){
			for(var group in groupMap){
				groupNumber++;
				var html = '<div class="listItemWrapper clickable" newGroup group="' + groupMap[group] + '">';
				html += '<label class="defaultLabel noWrap">' + group + '</label>';
				html += '<div class="iconR arrowR rFloating"></div>';
				html += '</div>';
				
				homePageUI.groupContainer.append(html);
			}
			
			$('[newGroup]').removeAttr('newGroup').on('click', function(){
				var self = this;
				showLoadingMask(function(){
					$.post('groupOverviewPage', {
						groupId: $(self).attr('group')
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
						onFail('101');
					});
				});
			});
			
			if(groupNumber == 0){
				homePageUI.headerLabel.html(bundles.noGroup).parent().removeClass('noDisplay');
			} else {
				homePageUI.groupContainer.parent().removeClass('noDisplay');
			}
		};
		var groupNumber = 0;
		createGroupList(groupMap);
		
		var createInvitationList = function(invitationMap){
			for(var invitation in invitationMap){
				invitationNumber++;
				var html = '<div id="' + invitationMap[invitation] + '" class="listItemWrapper">';
				html += '<label class="defaultLabel noWrap">' + invitation + '</label>';
				
				html += '<button manageInvitation groupId="' + invitationMap[invitation] + '" groupName="' + invitation + '" action="0" class="defaultButton red small rFloating" type="button">';
				html +=	'<span class="defaultButtonLabel blueButtonLabel">' + bundles.deny + '</span>';
				html +=	'</button>';
				
				html += '<button manageInvitation groupId="' + invitationMap[invitation] + '" groupName="' + invitation + '" action="1" class="defaultButton blue small rFloating" type="button">';
				html +=	'<span class="defaultButtonLabel blueButtonLabel">' + bundles.accept + '</span>';
				html +=	'</button>';
				
				html += '</div>';
				
				homePageUI.invitationContainer.append(html);
			}
			
			$('[manageInvitation]').removeAttr('manageInvitation').on('click', function(){
				var self = $(this);
				showLoadingMask(function(){
					var groupId = self.attr('groupId');
					var action = self.attr('action');
					var groupName = self.attr('groupName');
					$.post('manageInvitationService', {
						groupId: groupId, 
						action: action
					}).done(function(response){
						response = JSON.parse(response);
		                
		                if(response.status == 'KO'){
		                	onFail('MANAGE_INVITATION_' + response.errorCode);
		                } else {
							hideLoadingMask(function(){
								$('#' + groupId).slideUp(600, function(){
									$('#' + groupId).remove();
									invitationNumber--;
									if(invitationNumber == 0){
										homePageUI.invitationHeaderLabel.parent().addClass('noDisplay');
										homePageUI.invitationContainer.parent().addClass('noDisplay');
									}
									if(action == '1'){
										var groupDescription = {};
										groupDescription[groupName] = groupId;
										createGroupList(groupDescription);
									}
								});
							});
						}
					}).fail(function(){
						onFail('101');
					}).always(function(){
						self.removeAttr('disabled');
					});
				});
			});
			
			if(invitationNumber > 0){
				homePageUI.invitationHeaderLabel.html(bundles.myInvitation).parent().removeClass('noDisplay');
				homePageUI.invitationContainer.parent().removeClass('noDisplay');
			} else {
				homePageUI.invitationHeaderLabel.parent().addClass('noDisplay');
				homePageUI.invitationContainer.parent().addClass('noDisplay');
			}
		};
		var invitationNumber = 0;
		createInvitationList(invitationMap);
		
		homePageUI.createGroupBtn.bind('click', function(){
			if(homePageUI.createGroupBtn.attr('disabled') != 'true'){
				homePageUI.createGroupBtn.attr('disabled', 'true');
				
				showLoadingMask(function(){
					homePageUI.createGroupMask.removeClass('noDisplay').center('absolute', null, null, '10000');
					homePageUI.createGroupNameInput.focus();
				});
			}
		});
		
		showCreateGroupDialog = function(){
			showLoadingMask(function(){
				homePageUI.createGroupMask.removeClass('noDisplay').center('absolute', null, null, '10000');
				homePageUI.createGroupNameInput.focus();
			});
		};
		
		homePageUI.createGroupNameInput.bind('keypress', function(event){
			if(event.keyCode == 13){
				homePageUI.createGroupConfirmBtn.trigger('click');
			}
		});
		
		homePageUI.createGroupCancelBtn.on('click', function(){
			resetAndHideCreateGroupMask();
		});
		
		homePageUI.createGroupConfirmBtn.on('click', function(){
			var groupName = homePageUI.createGroupNameInput.val();
			homePageUI.createGroupMaskToggleSet.addClass('noVisibility');
			homePageUI.createGroupMaskInner.addClass('loading');
			
			$.post('createGroupService', {
				n: groupName
			}).done(function(response){
				response = JSON.parse(response);
                
                if(response.status == 'KO'){
                	onFail(response.errorCode);
                } else {
					createGroupList(response.data);
					resetAndHideCreateGroupMask();
				}
			}).fail(function(){
				onFail('101');
				removeLoading();
			});
		});
		
		var removeLoading = function(){
			homePageUI.createGroupMaskToggleSet.removeClass('noVisibility');
			homePageUI.createGroupMaskInner.removeClass('loading');
		};
		
		var resetAndHideCreateGroupMask = function(){
			homePageUI.createGroupNameInput.val('');
			homePageUI.createGroupMask.addClass('noDisplay');
			
			hideLoadingMask(function(){
				homePageUI.createGroupBtn.removeAttr('disabled');
			});
		};
		
		statusBar.onInit();
	}	
};