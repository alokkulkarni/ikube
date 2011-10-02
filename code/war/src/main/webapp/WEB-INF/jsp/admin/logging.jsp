<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!--
	This page needs to be re-done. It can get the tail end of the log file, the 
	actions that have been performed by the server and the rules and results of the 
	rules for the action as well as the exceptions that were thrown by the indexing 
	process or the searching for that matter.  
 -->
<table class="table-content" width="100%">
	<tr>
		<td class="top-content">
			<span class="top-content-header">Logging</span>
			<span class="date" style="float: right;"><script type="text/javascript">writeDate();</script></span>
		</td>
	</tr>
	
	<tr>
		<th class="td-content"><a href="javascript:location.reload(true)">Refresh this page</a></th>
	</tr>
	<tr>
		<th class="td-content">Server - ${server.address} - log tail</th>
	</tr>
	<tr>
		<td class="td-content">
			<textarea rows="30" cols="65" class="td-content">${server.logTail}</textarea>
		</td>
	</tr>
	
	<tr>
		<td class="bottom-content">Servers' logging details brought to you by Ikube : </td>
	</tr>
</table>