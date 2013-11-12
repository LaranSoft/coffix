$(function(){
	UI = $.extend(UI, {
		manageGroupInviteUserBtn: $('#inviteUserBtn'),
		manageGroupUsernameInput: $('#manageGroup_usernameInput'),
		manageGroupPartecipantContainer: $('#manageGroup_partecipantContainer'),
		manageGroupErrorLabel: $('#manageGroupErrorLabel')
	}); 
	
	var resetInput = function(){
		UI.manageGroupUsernameInput.val('');
	};
	
	var id = 0;
	var createListItem = function(name){
		var html = '<div id="' + id + '" class="listItemWrapper">';
		html += '<label class="defaultLabel noWrap">' + name + '</label>';
		html += '</div>';
		
		return {html: html,	id: id};
	};
	
	var createInvitedListItem = function(username, basename, hidden){
		id++;
		hidden = hidden == null ? false : hidden;
		
		var manageInvitationAttr = 'manageInvitation';
		var html = '<div id="' + id + '" username="' + username + '" class="listItemWrapper' + (hidden ? ' noDisplay' : '') + '">';
		html += '<label class="defaultLabel noWrap">' + basename + '</label>';
		
		html += '<div class="invitedListItemCommandContainer rFloating">';
		html += '<label class="defaultLabel noWrap invitedLabel">' + bundles.invitedWaitConfirm + '<br/></label>';
		html += '<button ' + manageInvitationAttr + ' groupId="' + groupId + '" action="0" username="' + username + '" class="defaultButton red small invitedListItemButton" type="button">';
		html +=	'<span class="defaultButtonLabel blueButtonLabel">' + bundles.cancel + '</span>';
		html +=	'</button>';
		html += '</div>';
		
		html += '</div>';
		
		return {html: html,	wrapperId: id, buttonAttrName: manageInvitationAttr};
	};
	
	var bindControlsToInvitedListItem = function(listItem){
		$('[' + listItem.buttonAttrName + ']').removeAttr(listItem.buttonAttrName).on('click', function(){
			var self = $(this);
			if(self.attr('disabled') != 'true'){
				var groupId = self.attr('groupId');
				var action = self.attr('action');
				var username = self.attr('username');
				self.attr('disabled', true); 
			
				$.post('manageInvitation', {
					groupId: groupId, 
					action: action,
					username: username
				}).done(function(response){
					if(response.substr(0, 9) == 'redirect_'){
						response = response.substr(9);
						redirect(response);
					} else if(response.substr(0, 6) == 'error_'){
						response = response.substr(6);
						UI.manageGroupErrorLabel.html(errorCodes['manageInvitation_' + response]).removeClass('noDisplay');
				    } else if(response == ''){
				    	$('#' + listItem.wrapperId).slideUp(600, function(){
				    		$('#' + listItem.wrapperId).remove();
				    	});
				    }
					self.removeAttr('disabled');
				});
			}
		})
	};
	
	var inviteUser = function(){
		$.post('inviteUser', {
			groupId: groupId,
			username: UI.manageGroupUsernameInput.val()
		}).done(function(response){
			if(response.substr(0, 6) == 'error_'){
				response = response.substr(6);
				UI.manageGroupErrorLabel.html(errorCodes[response]);
			} else if(response.substr(0, 9) == 'redirect_'){
				response = response.substr(9);
				redirect(response);
			} else {
				response = JSON.parse(response);
				var listItem = createInvitedListItem(response.username, response.basename, true);
				UI.manageGroupPartecipantContainer.prepend(listItem.html);
				
				bindControlsToInvitedListItem(listItem);
				$('#' + listItem.wrapperId).slideDown(600);
				
			}
			resetInput();
		}).fail(resetInput);
	}
	
	UI.manageGroupUsernameInput.on('keypress', function(event){
		if(event.keyCode == 13){
			inviteUser();
		}
	});
	
	UI.manageGroupInviteUserBtn.bind('click', inviteUser);
	
	for(var invitedUsername in invitedUserMap){
		var listItem = createInvitedListItem(invitedUsername, invitedUserMap[invitedUsername]);
		UI.manageGroupPartecipantContainer.append(listItem.html);
		bindControlsToInvitedListItem(listItem);
	}
	
	for(var i=0; i<groupPartecipant.length; i++){
		var listItem = createListItem(groupPartecipant[i]);
		UI.manageGroupPartecipantContainer.append(listItem.html);
	}
});