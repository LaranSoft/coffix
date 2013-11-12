<%@include file="common/common.jsp"%>

<html>
	<head>
		<%@include file="common/common_head.jsp"%>

		<script>
			var offerer = '${offerer}';
			var offererUsername = '${offererUsername}';
			var partecipatingUserMap = {};
			
			var partecipatingUserEntry = [];
			<c:forEach var="partecipatingUser" items="${partecipatingUserMap}" varStatus="status">
				partecipatingUserEntry = '${partecipatingUser}'.split('=');
				partecipatingUserMap[partecipatingUserEntry[1]] = partecipatingUserEntry[0];
			</c:forEach>
			
			var bundles = $.extend(commonBundles, {			
			});
			
			var groupId = '${groupId}';
			
			var availableUser = $.map(partecipatingUserMap, function(value, index){
				return index;
			});
		</script>

		<script type="text/javascript" src="${contextPath}/resources/js/ext/jquery.ui.min.js"></script>
		<script type="text/javascript" src="${contextPath}/resources/js/chooseOffereds/chooseOffereds.js"></script>
		
		<link type="text/css" rel="stylesheet" href="${contextPath}/resources/styles/desktop/ext/jquery.ui.min.css"/>
		
		<link type="text/css" rel="stylesheet" href="${contextPath}/resources/styles/common/chooseOffereds/chooseOffereds.css"/>
		<link type="text/css" rel="stylesheet" href="${contextPath}/resources/styles/${cssPath}/chooseOffereds/chooseOffereds.css"/>
	</head>
	
	
	<body>
		<%@include file="common/common_body.jsp"%>
		
		<div id="main-container">
			<div id="main-content-wrapper">
				<div id="main-content">
					<div id="blockMask" class="noDisplay"></div>
					<%@include file="common/statusBar.jsp"%>
					<div id="chooseOffereds-container">
						<div class="row high">
							<label class="defaultLabel verticalMiddleAligned"><fmt:message key="CHOOSE_OFFEREDS.CURRENT_ACTION.LABEL"/></label>
						</div>
						
						<div class="row high">
							<label id="chooseOffereds_whoIsOfferer" class="defaultLabel"><fmt:message key="CHOOSE_OFFEREDS.SELECT_OFFERED.LABEL"/></label>
						</div>
						
						<div class="row">
							<input id="chooseOffereds_offered-input" class="defaultInput" name="offered" type="text" placeholder="<fmt:message key="CHOOSE_OFFEREDS.SELECT_OFFERED.PLACEHOLDER"/>"/>
						</div>
						
						<div class="row">
							<label class="defaultLabel singleLineLabel centeredLabel smallLabel"><fmt:message key="COMMON.SUMMARIZE"/></label>
						</div>
						
						<div class="row">
							<div id="chooseOffereds_offererContainer" class="listContainer">
								<div class="listItemWrapper">
									<label class="defaultLabel noWrap">${offerer}</label>
									<button id="chooseOffereds_changeOfferer-button" class="defaultButton blue small rFloating" type="button">
										<span class="defaultButtonLabel blueButtonLabel"><fmt:message key="COMMON.CHANGE"/></span>
									</button>
								</div>
							</div>
						</div>
						
						<div class="row">
							<label class="defaultLabel singleLineLabel centeredLabel smallLabel"><fmt:message key="CHOOSE_OFFEREDS.WHOIS_OFFERER.LABEL"/></label>
						</div>
						
						<div class="row">
							<label id="chooseOffereds_noOffered" class="defaultLabel singleLineLabel centeredLabel smallLabel">-</label>
							<div id="chooseOffereds_offeredContainer" class="listContainer noDisplay"></div>
						</div>
						
						<div class="row">
							<button id="chooseOffereds_createCoffer-confirm-button" class="defaultButton red noDisplay" type="button">
								<span class="defaultButtonLabel blueButtonLabel"><fmt:message key="CHOOSE_OFFEREDS.CREATE_COFFER.CONFIRM.BUTTON_LABEL"/></span>
							</button>
						</div>
					</div>
				</div>
			</div>
		</div>
	</body>
</html>