<%@include file="common/common.jsp"%>

<html>
	<head>
		<%@include file="common/common_head.jsp"%>

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

		<script type="text/javascript" src="${contextPath}/resources/js/ext/webtoolkit.md5.js"></script>
		<script type="text/javascript" src="${contextPath}/resources/js/registration/registration.js"></script>
		
		<link type="text/css" rel="stylesheet" href="${contextPath}/resources/styles/common/registration/registration.css"/>
		<link type="text/css" rel="stylesheet" href="${contextPath}/resources/styles/${cssPath}/registration/registration.css"/>
	</head>
	
	
	<body>
		<%@include file="common/common_body.jsp"%>
		
		<div id="main-container">
			<div id="main-content-wrapper">
				<div id="main-content">
					<div id="blockMask" class="noDisplay"></div>
					<c:set var="hideProfileControls">true</c:set>
					<c:set var="useCustomStatusBarContent">true</c:set>
					<c:set var="statusBarCustomContent">
						<span class="statusBarRegistrationTitle">Registrazione</span>
					</c:set>
					<%@include file="common/statusBar.jsp"%>
					<div id="registration-container">
						<div class="row high">
							<label id="errorMessage" class="defaultLabel red smallLabel noDisplay"></label>
						</div>
						<div class="row">
							<div class="lTextAligned">
								<form id="index_registration-form">
									<input id="index_username-input" class="defaultInput" name="username" type="email" placeholder="<fmt:message key="REGISTRATION.REGISTRATION_FORM.USERNAME_LABEL"/>"/>
									<input id="index_password-input" class="defaultInput" name="password" type="password" placeholder="<fmt:message key="REGISTRATION.REGISTRATION_FORM.PASSWORD_LABEL"/>"/>
									<input id="index_confirmpassword-input" class="defaultInput" name="confirmPassword" type="password" placeholder="<fmt:message key="REGISTRATION.REGISTRATION_FORM.CONFIRM_PASSWORD_LABEL"/>"/>
									<input id="index_displayName-input" class="defaultInput" name="displayName" type="text" placeholder="<fmt:message key="REGISTRATION.REGISTRATION_FORM.DISPLAY_NAME_LABEL"/>"/>
								</form>
							</div>
						</div>
						<div class="row">
							<button id="index_registration-submit-button" class="defaultButton blue rFloating" type="button">
								<span class="defaultButtonLabel blueButtonLabel"><fmt:message key="REGISTRATION.REGISTRATION_FORM.BUTTON_LABEL"/></span>
							</button>
						</div>
					</div>
				</div>
			</div>
		</div>
	</body>
</html>