<!DOCTYPE html>
<%@include file="common/common.jsp"%>
<html>
	<head>
		<meta name="viewport" content="width=device-width, initial-scale=1"/>
		<title>Coffix</title>
		
		<link rel="stylesheet" href="/resources/styles/common/common.css" />
		<link rel="stylesheet" href="/resources/styles/mobile/registration/registration.css" />
		<link rel="stylesheet" href="/resources/styles/mobile/ext/my-custom-theme.css" />
		<link rel="stylesheet" href="http://code.jquery.com/mobile/1.3.2/jquery.mobile.structure-1.3.2.css" />
		
		<script>
			var errorCode = '${errorCode}';
			
			var errorCodes = $.extend(commonErrorCodes, {
				'200': '<fmt:message key="ERROR_CODES.REGISTRATION.200"/>',
				'201': '<fmt:message key="ERROR_CODES.REGISTRATION.201"/>',
				'202': '<fmt:message key="ERROR_CODES.REGISTRATION.202"/>',
				'203': '<fmt:message key="ERROR_CODES.REGISTRATION.203"/>',
				'300': '<fmt:message key="ERROR_CODES.REGISTRATION.300"/>',
				'301': '<fmt:message key="ERROR_CODES.REGISTRATION.301"/>'
			});
		</script>
		
		<script src="http://code.jquery.com/jquery-1.9.1.min.js"></script>
		<script type="text/javascript" src="/resources/js/ext/webtoolkit.md5.js"></script>
		<script type="text/javascript" src="/resources/js/mobile/registration/registration.js"></script>
		<script src="http://code.jquery.com/mobile/latest/jquery.mobile.min.js"></script>
	</head>
	
	
	<body>
		<div data-role="page" id="registrationPage">
			<div data-role="header">
				<a href="#" data-icon="arrow-l" data-rel="back" data-mini="true" data-theme="c">Indietro</a>
				<h1>Coffix</h1>
			</div>
			<div id="mainContent" data-role="content">
				<div data-role="fieldcontain">
					<label for="name" class="blue"><fmt:message key="REGISTRATION.REGISTRATION_FORM.USERNAME_LABEL"/></label>
					<input id="index_username-input" type="text" name="name" id="name" value="" />
				</div>
				<div data-role="fieldcontain">
					<label for="password" class="blue"><fmt:message key="REGISTRATION.REGISTRATION_FORM.PASSWORD_LABEL"/></label>
					<input id="index_password-input" type="password" name="password" id="password" value=""/>
				</div>
				<div data-role="fieldcontain">
					<label for="confirmPassword" class="blue"><fmt:message key="REGISTRATION.REGISTRATION_FORM.CONFIRM_PASSWORD_LABEL"/></label>
					<input id="index_confirmpassword-input" type="password" name="confirmPassword" id="confirmPassword" value=""/>
				</div>
				<div data-role="fieldcontain">
					<label for="displayName" class="blue"><fmt:message key="REGISTRATION.REGISTRATION_FORM.DISPLAY_NAME_LABEL"/></label>
					<input id="index_displayName-input" type="text" name="displayName" id="displayName" value=""/>
				</div>
				<div class="rTextAligned">
					<a id="registrationButton" data-role="button" data-inline="true" data-theme="b" href="#">Registrati</a>
				</div>
			</div>
		</div>
	</body>
</html>