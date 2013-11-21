<%@include file="common/common.jsp"%>

<script>
	var groupId = '${groupId}';

       var bundles = $.extend(commonBundles, {
      		invitedWaitConfirm: '<fmt:message key="MANAGE_GROUP.INVITED_WAIT_CONFIRM"/>' 	
       });
       
       var errorCodes = $.extend(commonErrorCodes, {
		'200': '<fmt:message key="ERROR_CODES.INVITE_USER.200"/>',
		'201': '<fmt:message key="ERROR_CODES.INVITE_USER.201"/>',
		'202': '<fmt:message key="ERROR_CODES.INVITE_USER.202"/>',
		'203': '<fmt:message key="ERROR_CODES.INVITE_USER.203"/>'
	});

	var groupPartecipant = [];
	
	<c:forEach var="groupPartecipant" items="${groupPartecipantList}" varStatus="status">
		groupPartecipant.push('${groupPartecipant}');
	</c:forEach>
	
    var invitedUserMap = {};
    var invitedUserMapEntry = '';
          <c:forEach var="invitedUserMapEntry" items="${invitedUserMap}" varStatus="status">
           invitedUserMapEntry = '${invitedUserMapEntry}'.split('=');
           invitedUserMap[invitedUserMapEntry[0]] = invitedUserMapEntry[1];
          </c:forEach>
</script>

<script type="text/javascript" src="/resources/js/test/manageGroup/manageGroup.js"></script>
		
<link type="text/css" rel="stylesheet" href="/resources/styles/common/manageGroup/manageGroup.css"/>
<link type="text/css" rel="stylesheet" href="/resources/styles/${cssPath}/manageGroup/manageGroup.css"/>

<c:set var="backPage">homePage</c:set>	
<%@include file="common/statusBar.jsp"%>

<div class="row high">
	<label class="defaultLabel"><fmt:message key="COMMON.GROUP"/>&nbsp;${groupName}</label>
</div>
<div class="row">
	<label id="manageGroupErrorLabel" class="defaultLabel noWrap smallLabel red"></label>
  	<label class="defaultLabel noWrap smallLabel"><fmt:message key="MANAGE_GROUP.INSERT_USERNAME.LABEL"/></label>
	<br>
	<input id="manageGroup_usernameInput" class="defaultInput" name="username" type="text"/>
	<br>
</div>
<div class="row">
	<button id="inviteUserBtn" class="defaultButton red" type="button">
		<span class="defaultButtonLabel blueButtonLabel">Invita</span>
	</button>
</div>
<div class="row">
	<label class="defaultLabel noWrap"><fmt:message key="MANAGE_GROUP.PARTECIPANTS.LABEL"/></label>
</div>
<div class="row">
	<div id="manageGroup_partecipantContainer" class="listContainer"></div>
</div>