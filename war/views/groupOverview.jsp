<%@include file="common/common.jsp"%>

<html>
	<head>
		<%@include file="common/common_head.jsp"%>
		
		<link type="text/css" rel="stylesheet" href="${contextPath}/resources/styles/common/groupOverview/groupOverview.css"/>
		<link type="text/css" rel="stylesheet" href="${contextPath}/resources/styles/${cssPath}/groupOverview/groupOverview.css"/>
		<link type="text/css" rel="stylesheet" href="${contextPath}/resources/styles/desktop/ext/jquery.ui.min.css"/>
		
		<script>
			var currentPage = 'groupOverview';
			var groupId = '${groupId}';
			var loggedUser = '${user}';
			var index = '${index}';
			
			var bundles = {
				createdBy: '<fmt:message key="COMMON.CREATED_BY"/>',
				confirm: '<fmt:message key="INDEX.CANCEL_NEGATE"/>',
				negate: '<fmt:message key="COMMON.NEGATE"/>',
				registerCoffer: '<fmt:message key="INDEX.REGISTER_COFFER"/>',
				erase: '<fmt:message key="COMMON.ERASE"/>',
				positionLabel: '<fmt:message key="INDEX.USER_AT_POSITION_TURN"><fmt:param value="' + (Number(index) - 1 ) + '"></fmt:param></fmt:message>'
			};
			
			var nowByServer = '${now}';
			var nowByClient = new Date().getTime();
			
			var delta = Number(nowByServer) - nowByClient;
			
			var coffers = [];
			
			<c:forEach var="coffer" items="${coffers}" varStatus="status">
				var coffer = {};
				
				coffer['creator'] = JSON.parse('${coffer.creator}');
				
				coffer['offerer'] = JSON.parse('${coffer.offerer}');
				
				coffer['expireTime'] = '${coffer.expireTime}';
				coffer['key'] = '${coffer.key}';
				coffer['state'] = '${coffer.state}';
				
				coffer['offereds'] = JSON.parse('${coffer.offereds}');
				
				coffers.push(coffer);
			</c:forEach>
		
		</script>
		
		<script type="text/javascript" src="${contextPath}/resources/js/ext/jquery.ui.min.js"></script>
		<script type="text/javascript" src="${contextPath}/resources/js/common/groupOverview/groupOverview.js"></script>
		<script type="text/javascript" src="${contextPath}/resources/js/${cssPath}/groupOverview/groupOverview.js"></script>
	</head>
	
	
	<body>
		<%@include file="common/common_body.jsp"%>
		
		<div id="main-container">
			<div id="main-content-wrapper">
				<div id="main-content">
					<div id="blockMask" class="noDisplay"></div>
					<c:set var="showPlusBtn">true</c:set>
					<c:set var="plusBtnOnClick">createCoffer</c:set>
					<%@include file="common/statusBar.jsp"%>
					<div id="index-container">
						<c:choose>
							<c:when test="${nextOfferer == null}">
								<div class="row high">
									<label class="defaultLabel"><fmt:message key="INDEX.NO_USER_PRESENT"/></label>
								</div>
								<button id="inviteUserBtn" class="defaultButton blue" type="button">
									<span class="defaultButtonLabel blueButtonLabel"><fmt:message key="INDEX.ADD_USERS_TO_GROUP"/></span>
								</button>
							</c:when>
							<c:otherwise>
								<div class="row high">
									<label id="positionLabel" class="defaultLabel"><fmt:message key="INDEX.TODAY_IS"/></label>
								</div>
								<div class="row">
									<label class="defaultLabel noWrapLabel">${nextOfferer}</label>
									<button id="otherwiseBtn" class="defaultButton blue small" type="button">
										<span class="defaultButtonLabel blueButtonLabel"><fmt:message key="COMMON.QUESTION.OTHERWISE"/></span>
									</button>
								</div>
								<div class="row full high">
									<div id="index_activeCoffers"></div>
								</div>
							</c:otherwise>
						</c:choose>
					</div>
				</div>
			</div>
		</div>
	</body>
</html>
