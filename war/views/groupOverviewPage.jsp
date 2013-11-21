<%@include file="common/common.jsp"%>

<link type="text/css" rel="stylesheet" href="/resources/styles/common/groupOverview/groupOverview.css"/>
<link type="text/css" rel="stylesheet" href="/resources/styles/${cssPath}/groupOverview/groupOverview.css"/>
		
<script>
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
		
<script type="text/javascript" src="/resources/js/groupOverview/groupOverview.js"></script>
<script type="text/javascript" src="/resources/js/${cssPath}/groupOverview/groupOverview.js"></script>
	
<c:set var="showPlusBtn">true</c:set>
<c:set var="plusBtnOnClick">createCoffer</c:set>
<c:set var="plusBtnIcon">coffee_add.png</c:set>
<c:set var="useCustomStatusBarContent">true</c:set>
<c:set var="hideProfileBtn">true</c:set>
<c:set var="backPage">homePage</c:set>
<c:set var="statusBarCustomContent">
	<span class="statusBarGroupOverviewTitle">${groupName}<br>
	    <span class="statusBarGroupOverviewSubTitle"><fmt:message key="GROUP_OVERVIEW.GROUP_DETAILS_LINK.${cssPath}"/></span>
	</span>
</c:set>
<%@include file="common/statusBar.jsp"%>

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
			<span class="defaultLabel noWrapLabel">${nextOfferer}</span>
		</div>
		<div class="row">
			<button id="otherwiseBtn" class="defaultButton blue small" type="button">
				<span class="defaultButtonLabel blueButtonLabel"><fmt:message key="COMMON.QUESTION.OTHERWISE"/></span>
			</button>
		</div>
		<div class="row full high lTextAligned">
			<div id="index_activeCoffers"></div>
		</div>
	</c:otherwise>
</c:choose>
