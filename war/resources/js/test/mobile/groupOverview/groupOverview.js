var formatRemainingTime = function(value){
	if(isNaN(value)){
		return null;
	}
	var now = new Date().getTime() + delta;
	value -= now;
	
	if(value >= 3600000){
		var hours = '~';
		var h = Math.floor(value / 3600000);
		hours = h < 10 ? '0' + h : h;
		return hours + 'h';
	}
	
	if(value >= 60000){
		var minutes = '~';
		var m = Math.floor(value / 60000);
		minutes += m < 10 ? '0' + m : m;
		return minutes + 'm';
	}
	
	if(value >= 1000){
		var seconds = '';
		var s = Math.floor(value / 1000);
		seconds = s < 10 ? '0' + s : s;
		
		value -= s * 1000;
		return seconds;
	}
	
	return null;
};

$(function(){
	$('[offeredContainer]').find('[toggleVisibility]').removeClass('noVisibility');
});