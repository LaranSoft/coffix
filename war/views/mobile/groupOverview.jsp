<!DOCTYPE html>
<%@include file="common/common.jsp"%>

<html>
	<head>
		<%@include file="common/commonHead.jsp"%>
		
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
		
		<script type="text/javascript" src="/resources/js/mobile/groupOverview/groupOverview.js"></script>
	</head>
	
	
	<body>
		<div data-role="page" id="groupOverviewPage">
			<div data-role="header" data-position="fixed">
				<a href="#" data-icon="arrow-l" data-rel="back" data-mini="true" data-theme="c">Indietro</a>
				<h1>Coffix</h1>
			</div>
			<div id="mainContent" data-role="content" class="cTextAligned" >
				<c:choose>
					<c:when test="${nextOfferer == null}">
						<p class="blue"><fmt:message key="INDEX.NO_USER_PRESENT"/></p>
					</c:when>
					<c:otherwise>
						<p id="positionLabel" class="blue bigLabel2"><fmt:message key="INDEX.TODAY_IS"/></p>
						<p class="blue bold bigLabel3">${nextOfferer}</p>
						<a id="otherwiseBtn" data-role="button"><fmt:message key="COMMON.QUESTION.OTHERWISE"/></a>
						<a id="addCoffeerBtn" data-role="button"><fmt:message key="INDEX.CREATE_COFFER"/></a>
						<div id="cofferContainer" data-role="collapsible-set" data-inset="true" data-theme="a"></div>
					</c:otherwise>
				</c:choose>
			</div>
		</div>
	</body>
</html>
