var goToManageGroup;

var chooseOffererPage = {
	onInit: function(){
		chooseOffererUI = $.extend(UI, {
			selectOffererInput: $('#chooseOfferer_offerer-input'),
			manageGroupBtn: $('#chooseOfferer_manageGroup-button')
		});
		
		chooseOffererUI.selectOffererInput.autocomplete({
			source: availableUser,
			minLength: 1,
			select: function(event, ui){
				showLoadingMask(function(){
					$.post('chooseOfferedsPage', {
						groupId: groupId, 
						offerer: partecipatingUserMap[ui.item.value]
					}).done(function(response){
						if(response.substr(0, 9) == 'redirect_'){
							response = response.substr(9);
							redirect(response);
						} else {
							$('#pageContainer').html($.trim(response));
							chooseOfferedsPage.onInit();
	            			hideLoadingMask();
						}
					});
				});
			}
		});
		
		goToManageGroup = function(){
			showLoadingMask(function(){
				$.post('manageGroupPage', {
					groupId: groupId
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
		
		chooseOffererUI.manageGroupBtn.on('click', goToManageGroup);
		
		statusBar.onInit();
	}
};