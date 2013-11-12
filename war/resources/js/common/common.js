function bindQTips(){
	$('[hasQTip]').each(function(){
		var at = $(this).attr('qtipPosition');
		var position = {
			my: 'top-center',
			at: 'bottom-center'
		};
		if(at == 'right'){
			position.my = 'left-center';
			position.at = 'right-center';
		}
		$(this).qtip({
			content: $(this).attr('hasQTip'),
			style: {
				classes: 'qtip-tipsy qtip-shadow'
			},
			position: position
		});
	});
}