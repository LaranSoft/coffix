<%@include file="common/common.jsp"%>

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
		
<script type="text/javascript" src="/resources/js/test/chooseOfferer/chooseOfferer.js"></script>
		
<link type="text/css" rel="stylesheet" href="/resources/styles/common/chooseOfferer/chooseOfferer.css"/>
<link type="text/css" rel="stylesheet" href="/resources/styles/${cssPath}/chooseOfferer/chooseOfferer.css"/>
	
<c:set var="hideProfileBtn">true</c:set>
<c:set var="backPage">homePage</c:set>
<c:if test="${user != null}">
	<c:set var="useCustomStatusBarContent">true</c:set>
	<c:set var="showPlusBtn">true</c:set>
	<c:set var="plusBtnIcon">user_add.png</c:set>
	<c:set var="plusBtnOnClick">goToManageGroup</c:set>
	<c:set var="statusBarCustomContent">
		<span class="statusBarChooseOffererTitle lFloating white fontLarge">Nuovo Coffer</span>
	</c:set>
</c:if>
<%@include file="common/statusBar.jsp"%>

<div class="row high">
	<div id="chooseOfferer-container">
		<label class="defaultLabel noWrap fontMedium"><fmt:message key="CHOOSE_OFFERER.SELECT_OFFERER.LABEL"/></label>
		<br>
		<input id="chooseOfferer_offerer-input" class="defaultInput" name="offerer" type="text" placeholder="<fmt:message key="CHOOSE_OFFERER.SELECT_OFFERER.PLACEHOLDER"/>"/>
	</div>
</div>