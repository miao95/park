<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib prefix="spr" uri="http://www.springframework.org/tags"%>
<html>
<head>
<%@ include file="./backStatic.jsp"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<title>登录-<spr:message code="login_head_cn" /></title>
<script type="text/javascript">
	function checkNull() {
		var username = $("#username").val();
		var password = $("#password").val();
		if (username == "" || password == "") {
			$.messager.alert('信息', '用户名密码不能为空!', 'info');
			return false;
		} else {
			return true;
		}
	}
</script>

<style type="text/css">
body {
	margin: 0px;
}

html,body {
	height: 100%;
	width: 100%;
}

.background-gradient {
	height: 100%;
	background-image: -webkit-linear-gradient(top, #7dc1e1, #f2f9f6);
	background-image: -moz-linear-gradient(top, #7dc1e1, #f2f9f6);
	filter: progid:DXImageTransform.Microsoft.gradient(StartColorStr="#7dc1e1",
		EndColorStr="#f2f9f6");
	background-image: -ms-linear-gradient(top, #7dc1e1, #f2f9f6);
	background-image: linear-gradient(top, #7dc1e1, #f2f9f6);
}

.box-title {
	background: -webkit-linear-gradient(top, #5BC3F4, #C9E6EF);
	-webkit-background-clip: text;
	color: transparent;
	font-family: 微软雅黑, Microsoft Yahei, Verdana, Tahoma;
}
.box-subtitle {
	background: -webkit-linear-gradient(bottom, #5BC3F4, #C9E6EF);
	-webkit-background-clip: text;
	color: transparent;
	font-family: 微软雅黑, Microsoft Yahei, Verdana, Tahoma;
}

.logo {
	padding-top: 100px;
	height: 80px;
	width: 450px;
}

.pageContent {
	box-shadow: 1px 3px 8px rgba(0, 0, 0, 0.2);
	background: #fafafa;
	padding: 30px;
	width: 450px;
	height: 260px;
}

.login-form {
	border-top: 1px solid #D3D3D3;
	padding: 20px;
}

.login-form label {
	color: #333333;
	font-size: 14px;
	font-weight: bold;
}

.login-form input[type="text"],.login-form input[type="password"] {
	border: 1px solid #CCCCCC;
	border-radius: 5px;
	box-shadow: 0 1px 1px rgba(0, 0, 0, 0.1) inset;
	color: #666666;
	font-size: 15px;
	height: 24px;
	width: 200px;
	font-size: 15px
}

.color-btn {
	border-radius: 4px;
	border-style: solid;
	border-width: 1px;
	border-color: #F89406 #F89406 #AD6704;
	cursor: pointer;
	padding-left: 16px;
	padding-right: 16px;
	padding-top: 6px;
	padding-bottom: 22px;
	background-color: #FBB450;
	box-shadow: 0 1px 0 rgba(255, 255, 255, 0.2) inset, 0 1px 1px
		rgba(0, 0, 0, 0.05);
	text-shadow: 0 -1px 0 rgba(0, 0, 0, 0.25);
	text-align: center;
	color: #fafafa;
	font-size: 14px;
	font-weight: bold;
}

.color-btn:hover,.color-btn:focus {
	color: #333333;
	text-decoration: none;
	background-color: #F89406;
	-webkit-transition: background-position 0.1s linear;
	-moz-transition: background-position 0.1s linear;
	-o-transition: background-position 0.1s linear;
	transition: background-position 0.1s linear;
}
</style>
</head>
<body>
	<div align="center" class="background-gradient">
		<div class="logo"></div>
		<div class="pageContent">
			<h1 class="box-title">惠智区块链智能停车后台管理系统</h1>
			<form action="./admin/j_spring_security_check" method="post"
				class="login-form">
				<sec:csrfInput />
				<c:if test="${ not empty param.error and param.error == 0}">用户名或密码错误！
				</c:if>
				<c:if
					test="${ not empty param.error and param.error > 0 and param.error < 5}">登录失败，今天还能尝试<c:out
						value="${5 - param.error}" />次！
				</c:if>
				<c:if test="${ not empty param.error and param.error >= 5}">登录失败次数过多，账户已被锁定！请次日五点后再登录！
				</c:if>
				<c:if test="${ not empty param.pwdWeek}">密码格式不符合规范
				</c:if>
				<table>
					<tr>
						<td><label for="username">用户名：</label></td>
						<td><input type="text" id="username" name="j_username"
							<c:out value="${SPRING_SECURITY_LAST_USERNAME}"/> /></td>
					</tr>
					<tr>
						<td><label for="password">密码：</label></td>
						<td><input type="password" id="password" name="j_password"
							autocomplete="off" /></td>
					</tr>
					<tr>
						<td colspan="2" style="text-align: right;"><input id="submit"
							type="submit" value="登录" class="color-btn"
							onclick="return checkNull();" />
					</tr>
				</table>
			</form>
		</div>
		<%@ include file="footer.jsp"%>
	</div>
</body>
</html>