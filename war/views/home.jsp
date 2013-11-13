<%@include file="common/common.jsp"%>

<html>
	<head>
		<%@include file="common/common_head.jsp"%>
	
		<link type="text/css" rel="stylesheet" href="${contextPath}/resources/styles/common/home/home.css"/>
		<link type="text/css" rel="stylesheet" href="${contextPath}/resources/styles/${cssPath}/home/home.css"/>
		<link type="text/css" rel="stylesheet" href="${contextPath}/resources/styles/desktop/ext/jquery.ui.min.css"/>
		
		<script>
			var currentPage = 'home';
			var loggedUser = '${user}';
			
			var errorCodes = $.extend(commonErrorCodes, {
				'200': '<fmt:message key="ERROR_CODES.CREATE_GROUP.200"/>',
				'201': '<fmt:message key="ERROR_CODES.CREATE_GROUP.201"/>',
				'MANAGE_INVITATION_200': '<fmt:message key="ERROR_CODES.CREATE_GROUP.100"/>',
				'MANAGE_INVITATION_201': '<fmt:message key="ERROR_CODES.CREATE_GROUP.100"/>',
				'MANAGE_INVITATION_202': '<fmt:message key="ERROR_CODES.CREATE_GROUP.100"/>',
				'MANAGE_INVITATION_203': '<fmt:message key="ERROR_CODES.CREATE_GROUP.100"/>',
				'MANAGE_INVITATION_204': '<fmt:message key="ERROR_CODES.CREATE_GROUP.100"/>'
			});
			
			var bundles = $.extend(commonBundles, {
				noGroup: '<fmt:message key="HOME.NO_GROUP"/>',
				myGroups: '<fmt:message key="HOME.MY_GROUPS"/>',
				myInvitation: '<fmt:message key="HOME.MY_INVITATION"/>'
			});
			
			var groupMap = {};
			var groupEntry = '';
			<c:forEach var="groupEntry" items="${groupMap}" varStatus="status">
				groupEntry = '${groupEntry}'.split('=');
				groupMap[groupEntry[0]] = groupEntry[1];
			</c:forEach>
			
			var invitationMap = {};
			var invitationEntry = '';
			<c:forEach var="invitationEntry" items="${invitationMap}" varStatus="status">
				invitationEntry = '${invitationEntry}'.split('=');
				invitationMap[invitationEntry[0]] = invitationEntry[1];
			</c:forEach>
		</script>
		
		<script type="text/javascript" src="${contextPath}/resources/js/ext/jquery.ui.min.js"></script>
		<script type="text/javascript" src="${contextPath}/resources/js/home/home.js"></script>
	</head>
	
	
	<body>
		<%@include file="common/common_body.jsp"%>
		
		<div id="main-container">
			<div id="main-content-wrapper">
				<div id="main-content">
					<div id="homeContainer">
						<div id="blockMask" class="noDisplay"></div>
						<div id="createGroupMask" class="dialogMask noDisplay">
							<div id="createGroupMaskInner">
								<label class="defaultLabel smallLabel dialogLabel" toggleSet="createGroup"><fmt:message key="CREATE_GROUP.CHOOSE_NAME.LABEL"/></label>
								<br>
								<label id="createGroupErrorLabel" class="noDisplay defaultLabel smallLabel red dialogLabel" toggleSet="createGroup"><fmt:message key="CREATE_GROUP.CHOOSE_NAME.LABEL"/></label>
								<br>
								<input id="createGroup_name-input" toggleSet="createGroup" class="defaultInput" name="name" type="text" placeholder="<fmt:message key="CREATE_GROUP.NAME_INPUT.PLACEHOLDER"/>"/>
								<br>
								<button id="createGroup-cancel-button" toggleSet="createGroup" class="defaultButton red lFloating dialogButton" type="button">
									<span class="defaultButtonLabel blueButtonLabel"><fmt:message key="COMMON.CANCEL"/></span>
								</button>
								<button id="createGroup-confirm-button" toggleSet="createGroup" class="defaultButton blue rFloating dialogButton" type="button">
									<span class="defaultButtonLabel blueButtonLabel"><fmt:message key="COMMON.CONFIRM"/></span>
								</button>
							</div>
						</div>
						<c:set var="hideBackButton">true</c:set>
						<c:set var="showPlusBtn">${user != null}</c:set>
						<c:set var="plusBtnIcon">group_add.png</c:set>
						<c:set var="plusBtnOnClick">showCreateGroupDialog</c:set>
						<c:set var="showCoffixIcon">${user != null}</c:set>
						<c:if test="${user != null}">
							<c:set var="useCustomStatusBarContent">true</c:set>
							<c:set var="statusBarCustomContent">
								<span class="statusBarHomeTitle lFloating white">Coffix</span>
							</c:set>
						</c:if>
						<%@include file="common/statusBar.jsp"%>
						<div class="row noDisplay">
							<label id="errorMessage" class="defaultLabel red smallLabel noDisplay"></label>
						</div>
						<c:choose>
							<c:when test="${user == null}">
								<div class="row">
									<label id="firstLabel" class="defaultLabel"><fmt:message key="HOME.COFFIX"/></label>
								</div>
								<div class="row">
									<label id="secondLabel" class="defaultLabel"><fmt:message key="HOME.THE"/></label>
								</div>
								<div class="row">
									<label id="thirdLabel" class="defaultLabel"><fmt:message key="HOME.COFFEE"/></label>
								</div>
								<div class="row">
									<label id="fourthLabel" class="defaultLabel"><fmt:message key="HOME.MATRIX"/></label>
								</div>
							</c:when>
							<c:otherwise>
								<div class="row noDisplay">
									<div id="invitationHeaderLabelContainer">
										<label id="invitationHeaderLabel" class="defaultLabel noWrap"></label>
									</div>
								</div>
								<div class="row noDisplay">
									<div id="invitationContainer" class="listContainer"></div>
								</div>
								<div class="row high noDisplay">
									<label id="headerLabel" class="defaultLabel"></label>
								</div>
								<div class="row full noDisplay">
									<div id="groupContainer" class="listContainer"></div>
								</div>
							</c:otherwise>
						</c:choose>
					</div>
				</div>
			</div>
		</div>
	</body>
</html>
