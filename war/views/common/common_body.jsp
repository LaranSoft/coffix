<!-- RECOMMENDED if your web app will not function without JavaScript enabled -->
<noscript>
	<div style="width: 22em; position: absolute; left: 50%; margin-left: -11em; color: red; background-color: white; border: 1px solid red; padding: 4px; font-family: sans-serif">
		Your web browser must have JavaScript enabled in order for this	application to display correctly.</div>
</noscript>

<style>
	.scrollbar-measure {
		width: 100px;
		height: 100px;
		overflow: scroll;
		position: absolute;
		top: -9999px;
	}
</style>

<script>
    jQuery.fn.center.options = {
        absolute: null,
        horizontal: true,
        vertical: false
    };
    
    if($.ui && $.ui.autocomplete){
	    $.ui.autocomplete.filter = function (array, term) {
	        var matcher = new RegExp("^" + $.ui.autocomplete.escapeRegex(term), "i");
	        return $.grep(array, function (value) {
	            return matcher.test(value.label || value.value || value);
	        });
	    };
    }
    
    var showLoadingMask = function(callback){
    	callback = callback || function(){};
    	UI.blockMask.removeClass('noDisplay').animate({
			'opacity': 0.6
		}, 300, callback);
    };
    
    var hideLoadingMask = function(callback){
    	callback = callback || function(){};
    	UI.blockMask.animate({
			'opacity': 0
		}, 300, function(){
			UI.blockMask.addClass('noDisplay');
			callback();
		});
    };
    
    $(function(){
    	UI = $.extend(UI, {
    		blockMask: $('#blockMask')
    	});
    	
    	UI.blockMask.on('click', function(event){
            event.preventDefault();
        });
    	
		var scrollDiv = document.createElement('div');
		scrollDiv.className = 'scrollbar-measure';
		document.body.appendChild(scrollDiv);
		
		var scrollbarWidth = scrollDiv.offsetWidth - scrollDiv.clientWidth;
		var browserWidth = $(window).width();
		
		$('#main-content-wrapper').css('width', browserWidth - scrollbarWidth);
		
		document.body.removeChild(scrollDiv);
		
		$(window).on('resize', function(){
			var browserWidth = $(window).width();
			$('#main-content-wrapper').css('width', browserWidth - scrollbarWidth);
		});
	});
</script>