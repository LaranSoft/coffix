$(document).on('pageinit', function(){
	$('#loginLink').bind('tap', function(){
		window.location.href = '/login';
	});
	
	$('#registrationLink').bind('tap', function(){
		$.post('registration', {
		}).done(function(response){
			if(response.substr(0, 6) == 'error_'){
				response = response.substr(6);
				// TODO mostrare l'errore
			} else {
		    	window.location.href = response;
			}
		});
	});
	
	var createGroupList = function(groupMap){
		for(var group in groupMap){
			var html = '<li><a newGroup group="' + groupMap[group] + '" href="#">' + group + '</a></li>';
			$('#groupContainer').append(html);
		}
		
		$('#groupContainer').listview('refresh');
		
		$('[newGroup]').removeAttr('newGroup').on('tap', function(){
			$.post('groupOverview', {
				groupId: $(this).attr('group')
			}).done(function(response){
				if(response.substr(0, 9) == 'redirect_'){
					response = response.substr(9);
					redirect(response);
				} else if(response.substr(0, 6) == 'error_'){
					response = response.substr(6);
					// TODO gestire gli errori
			    } else {
			    	window.location.href = response;
			    }
			});
		});
	};
	createGroupList(groupMap);
	
	var createInvitationList = function(invitationMap){
		for(var invitation in invitationMap){
			invitationNumber++;
			var html = '<li id="' + invitationMap[invitation] + '">';
			html += '<a>' + invitation + '</a>';
			
			html += '<a manageInvitation groupId="' + invitationMap[invitation] + '" groupName="' + invitation + '" action="0" data-icon="delete"></a>';
			html +=	'</li>';
			
			$('#inviteContainer').append(html);
		}
		
		$('#inviteContainer').listview('refresh');
		
		$('[manageInvitation]').removeAttr('manageInvitation').on('tap', function(){
			var self = $(this);
			if(self.attr('disabled') != 'true'){
				var groupId = self.attr('groupId');
				var action = self.attr('action');
				var groupName = self.attr('groupName');
				self.attr('disabled', true);
				$.post('manageInvitation', {
					groupId: groupId, 
					action: action
				}).done(function(response){
					if(response.substr(0, 9) == 'redirect_'){
						response = response.substr(9);
						redirect(response);
					} else if(response.substr(0, 6) == 'error_'){
						response = response.substr(6);
						// TODO gestire il caso di errore
					} else if(response == ''){
						$('#' + groupId).remove();
						invitationNumber--;
						if(invitationNumber == 0){
							$('#inviteWrapper').hide();
						}
						if(action == '1'){
							var groupDescription = {};
							groupDescription[groupName] = groupId;
							createGroupList(groupDescription);
						}
					}
					self.removeAttr('disabled');
				});
			}
		});
		
		if(invitationNumber == 0){
			$('#inviteWrapper').hide();
		} else {
			$('#inviteWrapper').show();
		}
	};
	var invitationNumber = 0;
	createInvitationList(invitationMap);
	
	$('#createGroupButton').on('click', function(){
		var groupName = $('#index_username-input').val();
		
		$.post('createGroup', {
			n: groupName
		}).done(function(response){
			if(response.substr(0, 9) == 'redirect_'){
				response = response.substr(9);
				redirect(response);
			} else if(response.substr(0, 6) == 'error_'){
				response = response.substr(6);
				// TODO gestire il caso di errore
			} else {
				response = JSON.parse(response);
				createGroupList(response);
				$('#createGroupPopup').popup('close');
			}
		});
	});
});