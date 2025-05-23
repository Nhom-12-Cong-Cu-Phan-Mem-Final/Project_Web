<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import="java.util.List"%>
<%@ page import="beans.CV"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>CV Form</title>
<!-- Bootstrap CSS -->
<link rel="stylesheet"
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css"
	integrity="sha384-T3c6CoIi6uLrA9TneNEoa7RxnatzjcDSCmG1MXxSR1GAsXEV/Dwwykc2MPK8M2HN"
	crossorigin="anonymous">
<link rel="stylesheet"
	href="https://cdn.jsdelivr.net/npm/bootstrap-icons/font/bootstrap-icons.css"
	integrity="sha384-Bk5cbLkZQ5raZ0+H2/+VbfYx3WpvxvQK4zqXZr7sYODuaX7bKXoSOnipQxkaS8sv"
	crossorigin="anonymous">

</head>

<style>
/* Định dạng nút tròn */
.circle-rating {
	display: flex;
	gap: 10px;
}

.hide-button {
	display: none; /* Ẩn các nút */
}

.circle {
	width: 20px;
	height: 20px;
	border-radius: 50%;
	background-color: #ddd; /* Màu xám nhạt cho nút chưa chọn */
	cursor: pointer;
	transition: background-color 0.3s ease;
}

.circle.active {
	background-color: #28a745; /* Màu xanh lá khi được chọn */
}

.circle:hover {
	background-color: #6c757d; /* Màu xám đậm khi hover */
}

.skill-row .form-control {
	max-width: 200px; /* Đặt độ rộng tối đa cho ô nhập tên kỹ năng */
}
</style>
<body class="bg-light">
	<jsp:include page="/WEB-INF/views/fragments/frg_ViewCV.jsp" />
	<!-- Bootstrap JS -->
	<script
		src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
	<script src="${pageContext.request.contextPath}/js/ThongTinCV.js"></script>
</body>
</html>
