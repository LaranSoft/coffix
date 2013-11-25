<%@page language="java" pageEncoding="UTF-8" contentType="text/html;charset=utf-8"%>

<%
	response.setHeader("Cache-Control","no-cache");
	response.setHeader("Pragma","no-cache");
	response.setHeader("Expires","0");
	String userAgent = request.getHeader("User-Agent").toLowerCase();
	boolean isMobile = (Boolean)request.getAttribute("mobile");
%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<c:set var="contextPath" value="${pageContext['request'].contextPath}"/>
<c:set var="r" value="${pageContext.request}" />
<c:set var="language" value="${not empty param.language ? param.language : not empty language ? language : pageContext.request.locale}" scope="session" />
<c:set var="isMobile" scope="request"><%= isMobile %></c:set>
<c:set var="cssPath">desktop</c:set>
<c:if test="${isMobile}">
	<c:set var="cssPath">mobile</c:set>
</c:if>

<fmt:setLocale value="${language}" />
<fmt:setBundle basename="it.halfone.coffix.bundle.MessagesBundle"/>