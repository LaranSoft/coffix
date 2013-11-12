<!DOCTYPE html>
<%@include file="common/common.jsp"%>

<html>
	<head>
		<meta name="viewport" content="width=device-width, initial-scale=1"/>
		<title>Coffix</title>
		
		<link rel="stylesheet" href="/resources/styles/common/common.css" />
		<link rel="stylesheet" href="/resources/styles/mobile/ext/my-custom-theme.css" />
		<link rel="stylesheet" href="http://code.jquery.com/mobile/1.3.2/jquery.mobile.structure-1.3.2.css" />
	
		<script src="http://code.jquery.com/jquery-1.9.1.min.js"></script>
		<script src="http://code.jquery.com/mobile/latest/jquery.mobile.min.js"></script>
	
		<script>
			var groupId = '${groupId}';
		
		    var invitedUserMap = {};
		    var invitedUserMapEntry = '';
            <c:forEach var="invitedUserMapEntry" items="${invitedUserMap}" varStatus="status">
	            invitedUserMapEntry = '${invitedUserMapEntry}'.split('=');
	            invitedUserMap[invitedUserMapEntry[0]] = invitedUserMapEntry[1];
            </c:forEach>
		</script>
		
		<script type="text/javascript" src="/resources/js/mobile/manageGroup/manageGroup.js"></script>
	</head>
	
	
	<body>
		<div data-role="page" id="homePage">
			<div data-role="header" data-position="fixed">
				<a href="#" data-icon="arrow-l" data-rel="back" data-mini="true" data-theme="c">Indietro</a>
				<h1>${groupName}</h1>
			</div>
			<div id="mainContent" data-role="content">
				<input id="manageGroup_usernameInput" class="defaultInput" name="username" type="text" placeholder="Nome utente di chi vuoi invitare"/>
				
				<div class="rTextAligned">
					<button id="inviteUserBtn" data-inline="true" href="#">Invita</button>
				</div>
						
				<ul id="manageGroupPartecipantListContainer" data-role="listview" data-inset="true" data-theme="a">
					<li data-role="list-divider" data-theme="a">Partecipanti</li>
					<c:forEach var="groupPartecipant" items="${groupPartecipantList}">
						<li>${groupPartecipant}</li>
					</c:forEach>
				</ul>
			</div>
		</div>
	</body>
</html>
