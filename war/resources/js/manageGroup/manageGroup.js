var manageGroupPage = {
		
	render: function(data){
		
		var statusBarOptions = {
			showBackButton: true,
			backPage: 'homePage',
			user: data.user
		};
		
		var html = statusBar.render(statusBarOptions);

		html += '<div class="row high">';
		html +=     '<label class="defaultLabel">' + data.bundles.group + '&nbsp' + data.groupName + '</label>';
		html += '</div>';
		html += '<div class="row">';
		html +=     '<label id="manageGroupErrorLabel" class="defaultLabel noWrap smallLabel red"></label>';
		html +=     '<label class="defaultLabel noWrap smallLabel">' + data.bundles.insertUsernameLabel + '</label>';
		html +=     '<br>';
		html +=     '<input id="manageGroup_usernameInput" class="defaultInput" name="username" type="text"/>';
		html +=     '<br>';
		html += '</div>';
		html += '<div class="row">';
		html +=     '<button id="inviteUserBtn" class="defaultButton red" type="button">';
		html +=         '<span class="defaultButtonLabel blueButtonLabel">Invita</span>';
		html +=     '</button>';
		html += '</div>';
		html += '<div class="row">';
		html +=     '<label class="defaultLabel noWrap">' + data.bundles.partecipantsLabel + '</label>';
		html += '</div>';
		html += '<div class="row">';
		html +=     '<div id="manageGroup_partecipantContainer" class="listContainer"></div>';
		html += '</div>';
		
		return html;
	},
		
		
	onInit: function(data){
		
		var manageGroupUI = $.extend(UI, {
			manageGroupInviteUserBtn: $('#inviteUserBtn'),
			manageGroupUsernameInput: $('#manageGroup_usernameInput'),
			manageGroupPartecipantContainer: $('#manageGroup_partecipantContainer'),
			manageGroupErrorLabel: $('#manageGroupErrorLabel')
		}); 
		
		var resetInput = function(){
			manageGroupUI.manageGroupUsernameInput.val('');
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
			html += '<label class="defaultLabel noWrap invitedLabel">' + data.bundles.invitedWaitConfirm + '<br/></label>';
			html += '<button ' + manageInvitationAttr + ' groupId="' + data.groupId + '" action="0" username="' + username + '" class="defaultButton red small invitedListItemButton" type="button">';
			html +=	'<span class="defaultButtonLabel blueButtonLabel">' + data.bundles.cancel + '</span>';
			html +=	'</button>';
			html += '</div>';
			
			html += '</div>';
			
			return {html: html,	wrapperId: id, buttonAttrName: manageInvitationAttr};
		};
		
		var bindControlsToInvitedListItem = function(listItem){
			$('[' + listItem.buttonAttrName + ']').removeAttr(listItem.buttonAttrName).on('click', function(){
				var self = $(this);
				
				showLoadingMask(function(){
					var groupId = self.attr('groupId');
					var action = self.attr('action');
					var username = self.attr('username');
				
					$.post('manageInvitationService', {
						groupId: groupId, 
						action: action,
						username: username
					}).done(function(response){
						response = JSON.parse(response);
		                
		                if(response.status == 'KO'){
		                	onFail(response.errorCode);
		                } else {
					    	$('#' + listItem.wrapperId).slideUp(600, function(){
					    		$('#' + listItem.wrapperId).remove();
					    	});
					    }
					}).always(function(){
						hideLoadingMask();
					});
				});
			})
		};
		
		var inviteUser = function(){
			$.post('inviteUserService', {
				groupId: data.groupId,
				username: manageGroupUI.manageGroupUsernameInput.val()
			}).done(function(response){
				response = JSON.parse(response);
                
                if(response.status == 'KO'){
                	onFail(response.errorCode);
                } else {
					response = response.data;
					var listItem = createInvitedListItem(response.username, response.basename, true);
					manageGroupUI.manageGroupPartecipantContainer.prepend(listItem.html);
					
					bindControlsToInvitedListItem(listItem);
					$('#' + listItem.wrapperId).slideDown(600);
					
				}
				resetInput();
			}).fail(resetInput);
		}
		
		manageGroupUI.manageGroupUsernameInput.on('keypress', function(event){
			if(event.keyCode == 13){
				inviteUser();
			}
		});
		
		manageGroupUI.manageGroupInviteUserBtn.bind('click', inviteUser);
		
		for(var invitedUsername in data.invitedUserMap){
			var listItem = createInvitedListItem(invitedUsername, data.invitedUserMap[invitedUsername]);
			manageGroupUI.manageGroupPartecipantContainer.append(listItem.html);
			bindControlsToInvitedListItem(listItem);
		}
		
		for(var i=0; i<data.groupPartecipantList.length; i++){
			var listItem = createListItem(data.groupPartecipantList[i]);
			manageGroupUI.manageGroupPartecipantContainer.append(listItem.html);
		}
		
		statusBar.onInit();
	}
};