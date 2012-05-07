<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags" %>

<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<html>
	<head>
		<meta http-equiv="Expires" content="-1">
		<meta http-equiv="Pragma" content="no-cache">
		<meta http-equiv="Content-Type" content="text/html;charset=UTF-8" />
		
		<title><tiles:insertAttribute name="title" /></title>
		
		<meta name="Keywords" content="Michael Couck, Ikube, Enterprise Search, Web Site Search, Database Search, High Volume" />
		<meta name="Description" content="Ikube Enterprise Search." />
		
		<link rel="shortcut icon" href="<c:url value="/images/icon.ico"/>" />
		<link href="<c:url value="/style/style-white.css"/>" rel="stylesheet" type="text/css" media="screen" />
		<link href="<c:url value="/style/prettyprint/prettify.css"/>" rel="stylesheet" type="text/css" media="screen" />
		<link href="<c:url value="/js/jquery.autocomplete.css"/>" rel="stylesheet" type="text/css" media="screen" />
		
		<script type="text/javascript" src="<c:url value="/js/ikube.js" />"></script>
		<script type="text/javascript" src="<c:url value="/style/prettyprint/prettify.js" />"></script>
		
		<script src="<c:url value="/js/jquery-1.7.1.js"/>" type="text/javascript"></script>
		<script src="<c:url value="/js/jquery.autocomplete.js"/>" type="text/javascript"></script>
		<script src="<c:url value="/js/jquery.autocomplete.min.js"/>" type="text/javascript"></script>
		<script src="<c:url value="/js/jquery.autocomplete.pack.js"/>" type="text/javascript"></script>
		
		<script src="http://www.google-analytics.com/ga.js" type="text/javascript"></script>
		<script src="http://maps.google.com/maps/api/js?sensor=false" type="text/javascript"></script>
	</head>

	<body onload="JavaScript:timedRefresh(10000);">
		<script type="text/javascript">
			try {
				var pageTracker = _gat._getTracker("UA-13044914-4");
				pageTracker._trackPageview();
			} catch(err) {
				document.write('<!-- ' + err + ' -->');
			}
		</script>
		<table>
			<tr>
				<td colspan="3">
					<tiles:insertAttribute name="header" />
				</td>
			</tr>
			<tr id="content">
				<td width="250">
					<tiles:insertAttribute name="menu" />
				</td>
				<td>&nbsp;</td>
				<td width="100%">
					<tiles:insertAttribute name="content" />
				</td>
			</tr>
			<tr>
				<td colspan="3">
					<tiles:insertAttribute name="footer" />
				</td>
			</tr>
		</table>
	</body>
</html>