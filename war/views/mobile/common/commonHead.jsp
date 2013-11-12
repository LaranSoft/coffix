<meta name="viewport" content="width=device-width, initial-scale=1"/>
<title<fmt:message key="COMMON.TITLE"/></title>

<link rel="stylesheet" href="/resources/styles/common/common.css" />
<link rel="stylesheet" href="/resources/styles/mobile/common/common.css" />
<link rel="stylesheet" href="/resources/styles/mobile/ext/my-custom-theme.css" />
<link rel="stylesheet" href="http://code.jquery.com/mobile/1.3.2/jquery.mobile.structure-1.3.2.css" />

<script src="http://code.jquery.com/jquery-1.9.1.min.js"></script>
<script src="http://code.jquery.com/mobile/latest/jquery.mobile.min.js"></script>

<script>
	var commonErrorCodes = {
		'100': '<fmt:message key="ERROR_CODES.100"/>',
		'101': '<fmt:message key="ERROR_CODES.101"/>'
	};
	
	var commonBundles = {
		accept: '<fmt:message key="COMMON.ACCEPT"/>',
    	deny: '<fmt:message key="COMMON.DENY"/>',
    	remove: '<fmt:message key="COMMON.REMOVE"/>',
    	cancel: '<fmt:message key="COMMON.CANCEL"/>'
	};

	var redirect = function(code){
		if(code == '0'){
			window.location.replace('login');
		} else if(code == '1'){
			window.location.replace('home');
		} 
	};
	
	var UI = {};
</script>