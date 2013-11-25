<%@include file="common/common.jsp"%>

<html>
	<head>
		<%@include file="common/common_head.jsp"%>
	</head>
	
	<body>
		<%@include file="common/common_body.jsp"%>
		<script>
			$(function(){
				loadPage('homePage');
			});
		</script>
		
		<div id="main-container">
			<div id="main-content-wrapper">
				<div id="main-content">
					<div id="blockMask" class="noDisplay"></div>
					<div id="pageContainer">
					</div>
				</div>
			</div>
		</div>
	</body>
</html>
