$(function(){
	
	var id = 0;
	var createInvitedListItem = function(username, basename){
		id++;
		
		var manageInvitationAttr = 'manageInvitation';
		var html = '<li id="' + id + '"><a>' + basename + '</a><a groupId="' + groupId + '" ' + manageInvitationAttr + ' action="0" username="' + username + '" data-icon="delete"></a></li>';
		
		return {html: html,	wrapperId: id, buttonAttrName: manageInvitationAttr};
	};
	
	var bindControlsToInvitedListItem = function(listItem){
		$('[' + listItem.buttonAttrName + ']').removeAttr(listItem.buttonAttrName).on('tap', function(){
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
				    	$('#' + listItem.wrapperId).remove();
				    	invitedNumber--;
				    	if(invitedNumber == 0 && isInvitedDividerVisible){
							isInvitedDividerVisible = false;
							$('#invitedListDivider').remove();
						}
				    	$('#manageGroupPartecipantListContainer').listview('refresh');
				    }
					self.removeAttr('disabled');
				});
			}
		})
	};
	
	$('#inviteUserBtn').on('tap', function(event){
		$.post('inviteUser', {
			groupId: groupId,
			username: $('#manageGroup_usernameInput').val()
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
				
				if(!isInvitedDividerVisible){
					isInvitedDividerVisible = true;
					$('#manageGroupPartecipantListContainer').append('<li data-role="list-divider" data-theme="a">Invitati</li>');
				}
				$('#manageGroupPartecipantListContainer').append(listItem.html).listview('refresh');
				
				bindControlsToInvitedListItem(listItem);
			}
			$('#manageGroup_usernameInput').val('');
		});
	});
	
	var isInvitedDividerVisible = false;
	var invitedNumber = 0;
	
	for(var invitedUsername in invitedUserMap){
		var listItem = createInvitedListItem(invitedUsername, invitedUserMap[invitedUsername]);
		invitedNumber++;
		if(!isInvitedDividerVisible){
			isInvitedDividerVisible = true;
			$('#manageGroupPartecipantListContainer').append('<li id="invitedListDivider" data-role="list-divider" data-theme="a">Invitati</li>');
		}
		$('#manageGroupPartecipantListContainer').append(listItem.html).listview('refresh');
		bindControlsToInvitedListItem(listItem);
	}
});