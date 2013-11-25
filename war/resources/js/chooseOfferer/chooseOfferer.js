var goToManageGroup = function(){};

var chooseOffererPage = {
		
	statusBarOptions: {
		showProfileBtn: false,
		backPage: 'homePage'
	},
		
	render: function(data){
		
		var statusBarOptions = {};
		if(data.user != null){
			statusBarOptions.user = data.user;
			statusBarOptions.useCustomStatusBarContent = true;
			statusBarOptions.statusBarCustomContent = '<span class="statusBarChooseOffererTitle lFloating white fontLarge">Nuovo Coffer</span>';
			statusBarOptions.showPlusBtn = true;
			statusBarOptions.plusBtnIcon = 'user_add.png';
			statusBarOptions.plusBtnOnClick = goToManageGroup;
		}
		
		statusBarOptions = $.extend({}, chooseOffererPage.statusBarOptions, statusBarOptions); 
		
		var html = statusBar.render(statusBarOptions);
		
		html += '<div class="row high">';
		html +=     '<div id="chooseOfferer-container">';
		html +=         '<label class="defaultLabel noWrap fontMedium">' + data.bundles.selectOffererLabel + '</label>';
		html +=         '<br>';
		html +=         '<input id="chooseOfferer_offerer-input" class="defaultInput" name="offerer" type="text" placeholder="' + data.bundles.selectOffererLabel + '"/>';
		html +=     '</div>';
		html += '</div>';
		
		return html;
	},
		
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