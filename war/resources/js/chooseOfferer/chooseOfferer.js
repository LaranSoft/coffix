var goToManageGroup;

$(function(){
	UI = $.extend(UI, {
		selectOffererInput: $('#chooseOfferer_offerer-input'),
		manageGroupBtn: $('#chooseOfferer_manageGroup-button')
	});
	
	UI.selectOffererInput.autocomplete({
		source: availableUser,
		minLength: 1,
		select: function(event, ui){
			showLoadingMask(function(){
				$.post('chooseOffereds', {
					groupId: groupId, 
					offerer: partecipatingUserMap[ui.item.value]
				}).done(function(response){
					if(response.substr(0, 9) == 'redirect_'){
						response = response.substr(9);
						redirect(response);
					} else if(response.substr(0, 6) == 'error_'){
						response = response.substr(6);
						// TODO mostrare a video il messaggio di errore
					} else {
						window.location.href = response;
					}
				});
			});
		}
	});
	
	goToManageGroup = function(){
		showLoadingMask(function(){
			$.post('manageGroup', {
				groupId: groupId
			}).done(function(response){
				if(response.substr(0, 9) == 'redirect_'){
					response = response.substr(9);
					redirect(response);
				} else if(response.substr(0, 6) == 'error_'){
					response = response.substr(6);
					// TODO mostrare a video il messaggio di errore
				} else {
					window.location.href = response;
				}
			});
		});
	};
	
	UI.manageGroupBtn.on('click', goToManageGroup);
});