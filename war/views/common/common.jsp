<%@page language="java" pageEncoding="UTF-8" contentType="text/html;charset=utf-8"%>

<%
	response.setHeader("Cache-Control","no-cache");
	response.setHeader("Pragma","no-cache");
	response.setHeader("Expires","0");
%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<c:set var="contextPath" value="${pageContext['request'].contextPath}"/>
<c:set var="r" value="${pageContext.request}" />
<c:set var="language" value="${not empty param.language ? param.language : not empty language ? language : pageContext.request.locale}" scope="session" />

<fmt:setLocale value="${language}" />
<fmt:setBundle basename="it.halfone.coffix.bundle.MessagesBundle"/>

<script>
	var contextPath = '${contextPath}';
</script>