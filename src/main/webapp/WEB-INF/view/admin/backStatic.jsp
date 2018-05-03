<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="spr" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page import="java.util.ResourceBundle"%>
<sec:csrfMetaTags />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name=”renderer” content=”webkit|ie-comp|ie-stand” />  
<%
	String path = request.getContextPath();
	/* //部署的时候打开
	 ResourceBundle res = ResourceBundle.getBundle("system");
	String basePath = request.getScheme() + "://"
			+ res.getString("domainName") + ":"
			+ request.getServerPort() + path + "/";  */
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";   
%>
<base href="<%=basePath%>" />
<link rel="shortcut icon" href="./img/settLogo23.png" />
<link href="./jquery-easyui-1.5.3/themes/metro/easyui.css"
	rel="stylesheet" type="text/css" />
<link href="./jquery-easyui-1.5.3/themes/icon.css" rel="stylesheet"
	type="text/css" />
<link href="./css/admin.css" rel="stylesheet" type="text/css" />
<link href="./font-awesome-4.7.0/css/font-awesome.min.css"
	rel="stylesheet" type="text/css" />
<script src="./jquery-easyui-1.5.3/jquery.min.js" type="text/javascript"></script>
<script src="./jquery-easyui-1.5.3/jquery.easyui.min.js"
	type="text/javascript"></script>
<script src="./jquery-easyui-1.5.3/locale/easyui-lang-zh_CN.js"
	type="text/javascript"></script>
<script src="./jquery-easyui-1.5.3/jquery.cookie.js"
	type="text/javascript"></script>
<script src="./js/common.js" type="text/javascript"></script>
<script src="./js/easyui.common.js" type="text/javascript"></script>

<script>
	$(function() {
		var token = $("meta[name='_csrf']").attr("content");
		var header = $("meta[name='_csrf_header']").attr("content");
		$(document).ajaxSend(function(e, xhr, options) {
			xhr.setRequestHeader(header, token);
		});
	});
</script>

