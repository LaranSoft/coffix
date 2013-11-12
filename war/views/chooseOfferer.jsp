<%@include file="common/common.jsp"%>

<html>
	<head>
		<%@include file="common/common_head.jsp"%>
		
		<script>
			var partecipatingUserMap = {};
			
			var partecipatingUserEntry = [];
			<c:forEach var="partecipatingUser" items="${partecipatingUserMap}" varStatus="status">
				partecipatingUserEntry = '${partecipatingUser}'.split('=');
				partecipatingUserMap[partecipatingUserEntry[1]] = partecipatingUserEntry[0];
			</c:forEach>
			
			var availableUser = $.map(partecipatingUserMap, function(value, index){
				return index;
			});
			
			var groupId = '${groupId}';
		</script>
		
		<script type="text/javascript" src="${contextPath}/resources/js/ext/jquery.ui.min.js"></script>
		<script type="text/javascript" src="${contextPath}/resources/js/chooseOfferer/chooseOfferer.js"></script>
		
		<link type="text/css" rel="stylesheet" href="${contextPath}/resources/styles/desktop/ext/jquery.ui.min.css"/>
		<link type="text/css" rel="stylesheet" href="${contextPath}/resources/styles/common/chooseOfferer/chooseOfferer.css"/>
		<link type="text/css" rel="stylesheet" href="${contextPath}/resources/styles/${cssPath}/chooseOfferer/chooseOfferer.css"/>
	</head>
	
	
	<body>
		<%@include file="common/common_body.jsp"%>
		
		<div id="main-container">
			<div id="main-content-wrapper">
				<div id="main-content">
					<div id="blockMask" class="noDisplay"></div>
					<%@include file="common/statusBar.jsp"%>
					<div id="chooseOfferer_mainContent">
						<div class="row high">
							<label class="defaultLabel verticalMiddleAligned"><fmt:message key="CHOOSE_OFFERER.CURRENT_ACTION.LABEL"/></label>
						</div>
						
						<div class="row high">
							<div id="chooseOfferer-container">
								<label class="defaultLabel singleLineLabel centeredLabel"><fmt:message key="CHOOSE_OFFERER.SELECT_OFFERER.LABEL"/></label>
								<br>
								<input id="chooseOfferer_offerer-input" class="defaultInput" name="offerer" type="text" placeholder="<fmt:message key="CHOOSE_OFFERER.SELECT_OFFERER.PLACEHOLDER"/>"/>
								<div id="chooseOfferer_inviteUser-container" class="rTextAligned">
									<label class="defaultLabel smallLabel">Non trovi chi stai cercando?</label>
									<br>
									<label class="defaultLabel smallLabel">Forse non è nel gruppo:</label>
									<br>
									<button id="chooseOfferer_manageGroup-button" class="defaultButton blue" type="button">
										<span class="defaultButtonLabel blueButtonLabel">Invitalo</span>
									</button>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</body>
</html>
