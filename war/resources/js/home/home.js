var showCreateGroupDialog;

$(function(){
	UI = $.extend(UI, {
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
		UI.errorMessageLabel.html(errorCodes[errorCode]).removeClass('noDisplay');
		hideLoadingMask();
	};
	
	var createGroupList = function(groupMap){
		for(var group in groupMap){
			groupNumber++;
			var html = '<div class="listItemWrapper clickable" newGroup group="' + groupMap[group] + '">';
			html += '<label class="defaultLabel noWrap">' + group + '</label>';
			html += '<div class="iconR arrowR rFloating"></div>';
			html += '</div>';
			
			UI.groupContainer.append(html);
		}
		
		$('[newGroup]').removeAttr('newGroup').on('click', function(){
			var self = this;
			showLoadingMask(function(){
				$.post('groupOverview', {
					groupId: $(self).attr('group')
				}).done(function(response){
					if(response.substr(0, 9) == 'redirect_'){
						response = response.substr(9);
						redirect(response);
					} else {
						window.location.href = response;
					}
				}).fail(function(){
					onFail('101');
				});
			});
		});
		
		var headerBundle = bundles.noGroup;
		if(groupNumber > 0){
			headerBundle = bundles.myGroups;
		}
		UI.headerLabel.html(headerBundle);
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
			
			UI.invitationContainer.append(html);
		}
		
		$('[manageInvitation]').removeAttr('manageInvitation').on('click', function(){
			var self = $(this);
			if(self.attr('disabled') != 'true'){
				var groupId = self.attr('groupId');
				var action = self.attr('action');
				var groupName = self.attr('groupName');
				self.attr('disabled', true);
				
				showLoadingMask(function(){
					$.post('manageInvitation', {
						groupId: groupId, 
						action: action
					}).done(function(response){
						if(response.substr(0, 9) == 'redirect_'){
							response = response.substr(9);
							redirect(response);
						} else if(response.substr(0, 6) == 'error_'){
							response = response.substr(6);
							onFail('MANAGE_INVITATION_' + response);
						} else {
							hideLoadingMask(function(){
								$('#' + groupId).slideUp(600, function(){
									$('#' + groupId).remove();
									invitationNumber--;
									if(invitationNumber == 0){
										UI.invitationHeaderLabel.hide();
										UI.invitationContainer.hide();
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
			}
		});
		
		if(invitationNumber > 0){
			UI.invitationHeaderLabel.html(bundles.myInvitation).show();
			UI.invitationContainer.show();
		} else {
			UI.invitationHeaderLabel.hide();
			UI.invitationContainer.hide();
		}
	};
	var invitationNumber = 0;
	createInvitationList(invitationMap);
	
	UI.createGroupBtn.bind('click', function(){
		if(UI.createGroupBtn.attr('disabled') != 'true'){
			UI.createGroupBtn.attr('disabled', 'true');
			
			showLoadingMask(function(){
				UI.createGroupMask.removeClass('noDisplay').center('absolute', null, null, '10000');
				UI.createGroupNameInput.focus();
			});
		}
	});
	
	showCreateGroupDialog = function(){
		showLoadingMask(function(){
			UI.createGroupMask.removeClass('noDisplay').center('absolute', null, null, '10000');
			UI.createGroupNameInput.focus();
		});
	};
	
	UI.createGroupNameInput.bind('keypress', function(event){
		if(event.keyCode == 13){
			UI.createGroupConfirmBtn.trigger('click');
		}
	});
	
	UI.createGroupCancelBtn.on('click', function(){
		resetAndHideCreateGroupMask();
	});
	
	UI.createGroupConfirmBtn.on('click', function(){
		var groupName = UI.createGroupNameInput.val();
		UI.createGroupMaskToggleSet.addClass('noVisibility');
		UI.createGroupMaskInner.addClass('loading');
		
		$.post('createGroup', {
			n: groupName
		}).done(function(response){
			if(response.substr(0, 9) == 'redirect_'){
				response = response.substr(9);
				redirect(response);
			} else if(response.substr(0, 6) == 'error_'){
				response = response.substr(6);
				UI.createGroupErrorLabel.html(errorCodes[response]).removeClass('noDisplay');
				removeLoading();
			} else {
				response = JSON.parse(response);
				createGroupList(response);
				resetAndHideCreateGroupMask();
			}
		}).fail(function(){
			onFail('101');
			removeLoading();
		});
	});
	
	var removeLoading = function(){
		UI.createGroupMaskToggleSet.removeClass('noVisibility');
		UI.createGroupMaskInner.removeClass('loading');
	};
	
	var resetAndHideCreateGroupMask = function(){
		UI.createGroupNameInput.val('');
		UI.createGroupMask.addClass('noDisplay');
		
		hideLoadingMask(function(){
			UI.createGroupBtn.removeAttr('disabled');
		});
	};
	
});