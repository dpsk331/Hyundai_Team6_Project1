<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8" />
	<meta name="viewport" content="width=device-width, initial-scale=1.0" />
	<title>SpringFramework</title>
	<!-- 부트스트랩 CSS CDN -->
	<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.1/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-F3w7mX95PdgyTmZZMECAngseQB83DfGTowi0iMjiWaeVhAn4FJkqJByhZMI3AhiU" crossorigin="anonymous">
	<!-- jquery CDN -->
	<script src="https://code.jquery.com/jquery-3.6.0.min.js" integrity="sha256-/xUj+3OJU5yExlq6GSYGSHk7tPXikynS7ogEvDej/m4=" crossorigin="anonymous"></script>
	<!-- 부트스트랩 JS CDN -->
	<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.1/dist/js/bootstrap.bundle.min.js" integrity="sha384-/bQdsTh/da6pkI1MST/rWKFNjaCP5gBSY4sEBT38Q/9RBh9AH40zEOg7Hlq2THRZ" crossorigin="anonymous"></script>
	<!-- 폰트 -->
	<style>
    @font-face {
      font-family: 'NanumBarunpen';
      src: url('https://cdn.jsdelivr.net/gh/projectnoonnu/noonfonts_two@1.0/NanumBarunpen.woff') format('woff');
      font-weight: normal;
      font-style: normal;
  	}
  	
  	body {
  	  font-family: 'NanumBarunpen';
  	}
  	
  	a {
  		cursor: pointer;
  	}
  	
  	.pop {
  		transition: 0.1s;
  		cursor: pointer;
  	}
  	.pop:active {
	  transform: scale(0.1);
	  opacity: 0.5;
  	}
  	
  </style>
</head>
<body>


	<!-- 네비게이션 바 -->
	<div class="d-flex justify-content-center" style="margin: 5vh;">
	<nav class="d-flex justify-content-between align-items-center py-3 mb-5 col-8">
		<div class="ms-5">	
			<a class="fs-3 text-decoration-none text-dark" href="${pageContext.request.contextPath}/">| THE | HANDSOME |</a>
		</div>
		<div class="me-5">
			<ul class="list-inline m-0 fs-4">
				<li class="d-flex justify-content-between">
				<sec:authorize access="isAnonymous()">
					<a href="${pageContext.request.contextPath}/member/loginForm" class="text-decoration-none text-dark">Login</a>
					<a href="${pageContext.request.contextPath}/member/loginForm" class="text-decoration-none text-dark ms-3">👜</a>			
				</sec:authorize>
				</li>
				
				<li class="d-flex justify-content-between">
				<sec:authorize access="isAuthenticated()">
					<form method="post" action="${pageContext.request.contextPath}/logout">
						<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
						<a href="${pageContext.request.contextPath}/logout" class="text-decoration-none text-dark">Logout</a>
					</form>
					<a href="${pageContext.request.contextPath}/order/shoppingbag" class="text-decoration-none text-dark ms-3">👜(0)</a>
				</sec:authorize>			
				</li>
			</ul>
		</div>
	</nav>
	</div>
	
</body>	
</html>