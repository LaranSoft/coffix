<%@include file="common/common.jsp"%>

<html>
	<head>
		<%@include file="common/common_head.jsp"%>
		
		<script>
			var fwd = '${fwd}';
			if(fwd != ''){
				fwd = '/' + fwd;
			}
			
			var errorCode = '${errorCode}';
			
			var errorCodes = $.extend(commonErrorCodes, {
				'200': '<fmt:message key="ERROR_CODES.LOGIN.200"/>',
				'201': '<fmt:message key="ERROR_CODES.LOGIN.201"/>',
				'300': '<fmt:message key="ERROR_CODES.LOGIN.300"/>'
			});
		</script>
		<script type="text/javascript" src="${contextPath}/resources/js/login/login.js"></script>
		
		<link type="text/css" rel="stylesheet" href="${contextPath}/resources/styles/common/login/login.css"/>
		<link type="text/css" rel="stylesheet" href="${contextPath}/resources/styles/${cssPath}/login/login.css"/>
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
						<span class="statusBarLoginTitle">Login</span>
					</c:set>
					<%@include file="common/statusBar.jsp"%>
					<div id="login-container">
						<div class="row high noDisplay">
							<label id="errorMessage" class="defaultLabel red smallLabel noDisplay"></label>
						</div>
						<div class="row lTextAligned">
							<form id="index_login-form">
								<input id="index_username-input" class="defaultInput" name="username" type="text" autocomplete="off" placeholder="<fmt:message key="INDEX.LOGIN_FORM.USERNAME_LABEL"/>"/>
								<input id="index_password-input" class="defaultInput" name="password" type="password" autocomplete="off" placeholder="<fmt:message key="INDEX.LOGIN_FORM.PASSWORD_LABEL"/>"/>
							</form>
						</div>
						<div class="row rTextAligned">
							<button id="index_login-submit-button" class="defaultButton blue" type="button">
								<span class="defaultButtonLabel blueButtonLabel"><fmt:message key="INDEX.LOGIN_FORM.BUTTON_LABEL"/></span>
							</button>
							<div id="noRegistrationLinkContainer">
								<label class="defaultLabel smallLabel blockDisplay"><fmt:message key="INDEX.NO_REGISTRATION.MESSAGE"/></label>
								<label id="registrationLink" class="defaultLabel smallLabel link"><fmt:message key="INDEX.NO_REGISTRATION.LINK"/></label>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</body>
</html>