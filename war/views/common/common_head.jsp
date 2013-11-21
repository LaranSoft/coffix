<meta http-equiv="content-type"  content="text/html" charset="UTF-8">
<meta name="viewport" content="height=device-height, width=device-width, user-scalable=no"/>
<title><fmt:message key="COMMON.TITLE"/></title>

<link type="text/css" rel="stylesheet" href="${contextPath}/resources/styles/common/common.css"/>
<link type="text/css" rel="stylesheet" href="${contextPath}/resources/styles/${cssPath}/common/common.css"/>
<link type="text/css" rel="stylesheet" href="${contextPath}/resources/styles/${cssPath}/common/button.css"/>
<link type="text/css" rel="stylesheet" href="${contextPath}/resources/styles/desktop/ext/jquery.qtip.min.css"/>
<link type="text/css" rel="stylesheet" href="${contextPath}/resources/styles/desktop/ext/jquery.validationEngine.css"/>
<link type="text/css" rel="stylesheet" href="/resources/styles/desktop/ext/jquery.ui.min.css"/>

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

<script type="text/javascript" src="${contextPath}/resources/js/ext/webtoolkit.md5.js"></script>				
<script type="text/javascript" src="resources/js/ext/jquery.min.js"></script>
<script type="text/javascript" src="/resources/js/ext/jquery.ui.min.js"></script>				
<script type="text/javascript" src="${contextPath}/resources/js/ext/center.js"></script>				
<script type="text/javascript" src="${contextPath}/resources/js/ext/smartresize.js"></script>				
<script type="text/javascript" src="${contextPath}/resources/js/ext/jquery.validationEngine.js"></script>				
<script type="text/javascript" src="${contextPath}/resources/js/ext/jquery.validationEngine-it.js"></script>
