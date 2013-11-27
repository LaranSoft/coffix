var goToManageGroup = function(){};

var chooseOffererPage = {
		
	statusBarOptions: {
		showProfileBtn: false,
		showBackButton: true,
		backPage: 'homePage'
	},
		
	render: function(data){
		
		goToManageGroup = function(){
			loadPage('manageGroupPage', {
				groupId: data.groupId
			});
		};
		
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
		
	onInit: function(data){
		chooseOffererUI = $.extend(UI, {
			selectOffererInput: $('#chooseOfferer_offerer-input'),
			manageGroupBtn: $('#chooseOfferer_manageGroup-button')
		});
		
		var availableUser = $.map(data.partecipatingUserMap, function(value, index){
			return index;
		});
		
		chooseOffererUI.selectOffererInput.autocomplete({
			source: availableUser,
			minLength: 1,
			select: function(event, ui){
				loadPage('chooseOfferedsPage', {
					groupId: data.groupId, 
					offerer: data.partecipatingUserMap[ui.item.value]
				});
			}
		});
		
		chooseOffererUI.manageGroupBtn.on('click', goToManageGroup);
		
		statusBar.onInit();
	}
};