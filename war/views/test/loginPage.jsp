<%@include file="common/common.jsp"%>

<script type="text/javascript" src="/resources/js/test/login/login.js"></script>

<link type="text/css" rel="stylesheet" href="/resources/styles/common/login/login.css"/>
<link type="text/css" rel="stylesheet" href="/resources/styles/${cssPath}/login/login.css"/>

<c:set var="hideProfileControls">true</c:set>
<c:set var="useCustomStatusBarContent">true</c:set>
<c:set var="backPage">homePage</c:set>
<c:set var="statusBarCustomContent">
	<span class="statusBarLoginTitle lFloating white fontLarge">Login</span>
</c:set>
<%@include file="common/statusBar.jsp"%>

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