var chooseOfferedsPage = {
	onInit: function(){
	
		chooseOfferedsUI = $.extend(UI, {
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
			
			chooseOfferedsUI.offeredContainer.prepend(html);
			
			var removeOfferedBtn = $('[removeOfferedFromCoffer]'); 
			var removeOfferedContainer = $('[listItemOfferedContainer]');
			
			removeOfferedContainer.removeAttr('listItemOfferedContainer').slideDown(600, function(){
				chooseOfferedsUI.createCofferBtn.removeClass('noDisplay');
				chooseOfferedsUI.noOfferedLabel.hide();
				chooseOfferedsUI.offeredContainer.show();
			});
			
			removeOfferedBtn.removeAttr('removeOfferedFromCoffer').on('click', function(){
				removeOfferedContainer.remove();
				availableUser.push(removeOfferedBtn.attr('displayName'));
				
			    chooseOfferedsUI.selectOfferedsInput.autocomplete('option', 'source', availableUser);
			    
				if(chooseOfferedsUI.offeredContainer.children().length == 0){
					chooseOfferedsUI.createCofferBtn.addClass('noDisplay');
					chooseOfferedsUI.noOfferedLabel.show();
					chooseOfferedsUI.offeredContainer.hide();
				}
				chooseOfferedsUI.selectOfferedsInput.focus();
			});
			
			chooseOfferedsUI.selectOfferedsInput.focus();
		};
		
		chooseOfferedsUI.selectOfferedsInput.autocomplete({
			source: availableUser,
			minLength: 1,
			select: function(event, ui){
				event.preventDefault();
				
				chooseOfferedsUI.selectOfferedsInput.val('');
				addOffered(ui.item.value, partecipatingUserMap[ui.item.value]);
			}
		});
		
		chooseOfferedsUI.selectOfferedsInput.bind('autocompleteselect', function (event, ui) {
		   availableUser.splice(availableUser.indexOf(ui.item.value), 1);
		   chooseOfferedsUI.selectOfferedsInput.autocomplete('option', 'source', availableUser);
		});
		
		chooseOfferedsUI.createCofferBtn.on('click', function(){
			showLoadingMask(function(){
				var offereds = [];
				chooseOfferedsUI.offeredContainer.find('[username]').each(function(){
					offereds.push($(this).attr('username'));
				});
				
				$.post('createCofferService', {
					offerer: offererUsername,
					offereds: offereds.join(';'),
					groupId: groupId
				}).done(function(response){
					response = JSON.parse(response);
	                
	                if(response.status == 'KO'){
	                	onFail(response.errorCode);
	                } else {
						$.post('groupOverviewPage', {
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
					// TODO deve diventare una gestione che mostri un messaggio di errore
					hideLoadingMask();
				});
			});
		});
		
		chooseOfferedsUI.changeOffererBtn.on('click', function(){
			window.history.back();
		});
		
		chooseOfferedsUI.selectOfferedsInput.focus();
		
		statusBar.onInit();
	}
};