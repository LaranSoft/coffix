<%@include file="common/common.jsp"%>

<script type="text/javascript" src="/resources/js/test/registration/registration.js"></script>

<link type="text/css" rel="stylesheet" href="/resources/styles/common/registration/registration.css"/>
<link type="text/css" rel="stylesheet" href="/resources/styles/${cssPath}/registration/registration.css"/>

<c:set var="hideProfileControls">true</c:set>
<c:set var="useCustomStatusBarContent">true</c:set>
<c:set var="backPage">homePage</c:set>
<c:set var="statusBarCustomContent">
	<span class="statusBarRegistrationTitle">Registrazione</span>
</c:set>
<%@include file="common/statusBar.jsp"%>

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