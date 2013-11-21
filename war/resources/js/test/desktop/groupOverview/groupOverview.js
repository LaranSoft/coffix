var formatRemainingTime = function(value){
	if(isNaN(value)){
		return null;
	}
	var now = new Date().getTime() + delta;
	value -= now;
	
	if(value <= 999){
		return null;
	}
	
	var hours = Math.floor(value / 3600000);
	hours = hours < 10 ? '0' + hours : hours;
	
	value -= hours * 3600000;
	
	var minutes = Math.floor(value / 60000);
	minutes = minutes < 10 ? '0' + minutes : minutes;
	
	value -= minutes * 60000;
	
	value = Math.floor(value / 1000);
	
	value = value < 10? '0' + value : value;
	
	return hours + ':' + minutes + ':' + value;
};

var bindCofferControls = function(){
	$('[offeredContainer]').each(function(){
		var self = this;
		$(this).on('mouseover', function(){
			$(self).attr('hovered', '1').find('[toggleVisibility]').removeClass('noVisibility');
		});
		$(this).on('mouseout', function(){
			$(self).attr('hovered', '0').find('[toggleVisibility]').addClass('noVisibility');
		});
	});
};