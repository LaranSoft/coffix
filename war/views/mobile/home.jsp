<!DOCTYPE html>
<%@include file="common/common.jsp"%>
<html>
	<head>
	
		<%@include file="common/commonHead.jsp"%>
		<link rel="stylesheet" href="/resources/styles/mobile/home/home.css" />
		
		<script>
			var currentPage = 'home';
			var loggedUser = '${user}';
			
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
		
		<script type="text/javascript" src="/resources/js/ext/webtoolkit.md5.js"></script>
		<script type="text/javascript" src="/resources/js/mobile/home/home.js"></script>
	</head>
	<body>
		<div data-role="page" id="homePage">
			<c:choose>
				<c:when test="${user == null}">
					<div data-role="header" data-position="fixed">
						<h1>Coffix</h1>
					</div>
					<div id="mainContent" data-role="content">
						<p id="theLabel" class="blue bold bigLabel4">The Coffee Matrix</p>
						<ul data-role="listview" data-inset="true" data-theme="a">
							<li><a id="loginLink" href="#" rel="external">Accedi</a></li>
							<li><a id="registrationLink" href="#" rel="external">Registrati</a></li>
						</ul>
					</div>
				</c:when>
				<c:otherwise>
					<div data-role="header" data-position="fixed">
						<h1>Coffix</h1>
						<a href="#createGroupPopup" data-rel="popup" data-position-to="window" data-transition="pop" class="ui-btn-right" data-role="button" data-theme="c" data-icon="plus" data-iconpos="notext">Crea Gruppo</a>
					</div>
					<div id="mainContent" data-role="content">
						<div id="inviteWrapper">
							<ul id="inviteContainer" data-role="listview" data-theme="a" data-inset="true">
								<li data-role="list-divider" data-theme="a">Inviti</li>
							</ul>
						</div>
					
						<div>
							<ul id="groupContainer" data-role="listview" data-theme="a"></ul>
						</div>
					</div>
					
					<div data-role="popup" id="createGroupPopup" data-overlay-theme="a">
						<a href="#" data-rel="back" data-role="button" data-theme="a" data-icon="delete" data-iconpos="notext" class="ui-btn-right">Close</a>
						<div style="padding: 10px 20px;'">
							<p>Scegli il nome del gruppo</p>
							<input id="index_username-input" type="text" name="name" id="name" value="" placeholder="da 4 a 30 caratteri"/>
							<div class="rTextAligned">
								<a id="createGroupButton" data-role="button" data-mini="true" data-inline="true" data-theme="b" href="#">Crea</a>
							</div>
						</div>
					</div>
				</c:otherwise>
			</c:choose>
		</div>
	</body>
</html>
