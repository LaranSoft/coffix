<!DOCTYPE html>
<html>
	<head>
		<meta name="viewport" content="width=device-width, initial-scale=1"/>
		<title>Coffix</title>
		
		<link rel="stylesheet" href="/resources/styles/common/common.css" />
		<link rel="stylesheet" href="/resources/styles/mobile/home/home.css" />
		<link rel="stylesheet" href="/resources/styles/mobile/ext/my-custom-theme.css" />
		<link rel="stylesheet" href="http://code.jquery.com/mobile/1.3.2/jquery.mobile.structure-1.3.2.css" />
		
		<script>
			var fwd = '${fwd}';
			if(fwd != ''){
				fwd = '/' + fwd;
			}
		</script>
		
		<script src="http://code.jquery.com/jquery-1.9.1.min.js"></script>
		<script type="text/javascript" src="/resources/js/ext/webtoolkit.md5.js"></script>
		<script type="text/javascript" src="/resources/js/mobile/login/login.js"></script>
		<script src="http://code.jquery.com/mobile/latest/jquery.mobile.min.js"></script>
	</head>
	<body>
		<div data-role="page" id="loginPage">
			<div data-role="header">
				<a href="#" data-icon="arrow-l" data-rel="back" data-mini="true" data-theme="c">Indietro</a>
				<h1>Coffix</h1>
			</div>
			<div id="mainContent" data-role="content">
				<div data-role="fieldcontain">
					<label for="name" class="blue">Nome utente:</label>
					<input id="index_username-input" type="text" name="name" id="name" value="" />
				</div>
				<div data-role="fieldcontain">
					<label for="email" class="blue">Password:</label>
					<input id="index_password-input" type="text" name="email" id="email" value="" />
				</div>
				<div class="rTextAligned">
					<a id="loginButton" data-role="button" data-inline="true" data-theme="b" href="#">Accedi</a>
				</div>
				<div class="rTextAligned">
					<label>Non sei registrato?</label>
					<a id="registrationLink" href="registration" rel="external">Clicca qui</a>
				</div>
			</div>
		</div>
	</body>
</html>
