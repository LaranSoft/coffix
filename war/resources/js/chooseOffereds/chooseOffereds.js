var chooseOfferedsPage = {
		
	statusBarOptions: {
		showProfileBtn: false,
		showBackButton: true,
		backPage: 'homePage'
	},
		
	render: function(data){
		
		var statusBarOptions = {};
		if(data.user != null){
			statusBarOptions.user = data.user;
			statusBarOptions.useCustomStatusBarContent = true;
			statusBarOptions.statusBarCustomContent = '<span class="statusBarChooseOfferedsTitle lFloating white fontLarge">Nuovo Coffer</span>';
		}
		
		statusBarOptions = $.extend({}, chooseOfferedsPage.statusBarOptions, statusBarOptions); 
		
		var html = statusBar.render(statusBarOptions);
		
		html += '<div class="row high">';
		html +=     '<label id="chooseOffereds_whoIsOfferer" class="defaultLabel">' + data.bundles.selectOfferedLabel + '</label>';
		html +=     '<br/>';
		html +=     '<input id="chooseOffereds_offered-input" class="defaultInput" name="offered" type="text" placeholder="' + data.bundles.selectOfferedPlaceholder + '"/>';
		html += '</div>';

		html += '<div class="row">';
		html +=     '<label class="defaultLabel singleLineLabel centeredLabel smallLabel">' + data.bundles.summarize + '</label>';
		html += '</div>';

		html += '<div class="row">';
		html +=     '<div id="chooseOffereds_offererContainer" class="listContainer">';
		html +=         '<div class="listItemWrapper">';
		html +=             '<label class="defaultLabel noWrap">' + data.offerer + '</label>';
		html +=             '<button id="chooseOffereds_changeOfferer-button" class="defaultButton blue small rFloating" type="button">';
		html +=                 '<span class="defaultButtonLabel blueButtonLabel">' + data.bundles.change + '</span>';
		html +=             '</button>';
		html +=         '</div>';
		html +=     '</div>';
		html += '</div>';

		html += '<div class="row">';
		html +=     '<label class="defaultLabel singleLineLabel centeredLabel smallLabel">' + data.bundles.whoIsOfferer + '</label>';
		html += '</div>';
							
		html += '<div class="row">';
		html +=     '<label id="chooseOffereds_noOffered" class="defaultLabel singleLineLabel centeredLabel smallLabel">-</label>';
		html +=     '<div id="chooseOffereds_offeredContainer" class="listContainer noDisplay"></div>';
		html += '</div>';

		html += '<div class="row">';
		html +=     '<button id="chooseOffereds_createCoffer-confirm-button" class="defaultButton red noDisplay" type="button">';
		html +=         '<span class="defaultButtonLabel blueButtonLabel">' + data.bundles.createCofferConfirmButtonLabel + '</span>';
		html +=     '</button>';
		html += '</div>';
		
		return html;
	},
		
	onInit: function(data){
	
		var availableUser = $.map(data.partecipatingUserMap, function(value, index){
			return index;
		});
		
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
	        html += '<span class="defaultButtonLabel blueButtonLabel">' + data.bundles.remove + '</span>';
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
				addOffered(ui.item.value, data.partecipatingUserMap[ui.item.value]);
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
					offerer: data.offererUsername,
					offereds: offereds.join(';'),
					groupId: data.groupId
				}).done(function(response){
					response = JSON.parse(response);
	                
	                if(response.status == 'KO'){
	                	onFail(response.errorCode);
	                } else {
						getPage('groupOverviewPage', {
	    					groupId: data.groupId
	    				}, function(){
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
			loadPage('chooseOffererPage', {
				groupId: data.groupId
			});
		});
		
		chooseOfferedsUI.selectOfferedsInput.focus();
		
		statusBar.onInit();
	}
};