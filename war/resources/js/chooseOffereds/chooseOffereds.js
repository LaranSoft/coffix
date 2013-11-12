$(function(){
	
	UI = $.extend(UI, {
		changeOffererBtn: $('#chooseOffereds_changeOfferer-button'),
		selectOfferedsInput: $('#chooseOffereds_offered-input'),
		offeredContainer: $('#chooseOffereds_offeredContainer'),
		createCofferBtn: $('#chooseOffereds_createCoffer-confirm-button'),
		noOfferedLabel: $('#chooseOffereds_noOffered')
	});
	
	var addOffered = function(displayName, username){
		
		var html = '<div class="listItemWrapper" listItemOfferedContainer>';
        html += '<label class="defaultLabel noWrap">' + displayName + '</label>';
        html += '<button id="chooseOffereds_changeOfferer-button" removeOfferedFromCoffer displayName="' + displayName + '" username="' + username + '" class="defaultButton blue small rFloating" type="button">';
        html += '<span class="defaultButtonLabel blueButtonLabel">' + bundles.remove + '</span>';
        html += '</button>';
        html += '</div>';
		
		UI.offeredContainer.prepend(html);
		
		var removeOfferedBtn = $('[removeOfferedFromCoffer]'); 
		var removeOfferedContainer = $('[listItemOfferedContainer]');
		
		removeOfferedContainer.removeAttr('listItemOfferedContainer').slideDown(600, function(){
			UI.createCofferBtn.removeClass('noDisplay');
			UI.noOfferedLabel.hide();
			UI.offeredContainer.show();
		});
		
		removeOfferedBtn.removeAttr('removeOfferedFromCoffer').on('click', function(){
			removeOfferedContainer.remove();
			availableUser.push(removeOfferedBtn.attr('displayName'));
			
		    UI.selectOfferedsInput.autocomplete('option', 'source', availableUser);
		    
			if(UI.offeredContainer.children().length == 0){
				UI.createCofferBtn.addClass('noDisplay');
				UI.noOfferedLabel.show();
				UI.offeredContainer.hide();
			}
			UI.selectOfferedsInput.focus();
		});
		
		UI.selectOfferedsInput.focus();
	};
	
	UI.selectOfferedsInput.autocomplete({
		source: availableUser,
		minLength: 1,
		select: function(event, ui){
			event.preventDefault();
			
			UI.selectOfferedsInput.val('');
			addOffered(ui.item.value, partecipatingUserMap[ui.item.value]);
		}
	});
	
	UI.selectOfferedsInput.bind('autocompleteselect', function (event, ui) {
	   availableUser.splice(availableUser.indexOf(ui.item.value), 1);
	   UI.selectOfferedsInput.autocomplete('option', 'source', availableUser);
	});
	
	UI.createCofferBtn.on('click', function(){
		var offereds = [];
		UI.offeredContainer.find('[username]').each(function(){
			offereds.push($(this).attr('username'));
		});
		
		$.post('createCoffer', {
			offerer: offererUsername,
			offereds: offereds.join(';'),
			groupId: groupId
		}).done(function(response){
			if(response == ''){
				$.post('groupOverview', {
					groupId: groupId
				}).done(function(response){
					if(response.substr(0, 9) == 'redirect_'){
						response = response.substr(9);
						redirect(response);
					} else if(response.substr(0, 6) == 'error_'){
						response = response.substr(6);
						UI.manageGroupErrorLabel.html(errorCodes['manageInvitation_' + response]).removeClass('noDisplay');
				    } else {
				    	window.location.replace(response);
				    }
				});
			}
		});
	});
	
	UI.changeOffererBtn.on('click', function(){
		window.history.back();
	});
	
	UI.selectOfferedsInput.focus();
});