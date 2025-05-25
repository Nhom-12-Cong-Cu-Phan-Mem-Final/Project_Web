<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
    String nonce = (String) request.getAttribute("cspNonce");
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Công việc yêu thích</title>
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<link rel="stylesheet"
	href="https://cdn.jsdelivr.net/npm/bootstrap-icons/font/bootstrap-icons.css"
	integrity="sha384-Bk5cbLkZQ5raZ0+H2/+VbfYx3WpvxvQK4zqXZr7sYODuaX7bKXoSOnipQxkaS8sv"
	crossorigin="anonymous">
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
	rel="stylesheet"
	integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH"
	crossorigin="anonymous">
<script
	src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"
	integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz"
	crossorigin="anonymous"></script>
<script
	src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.9.3/dist/umd/popper.min.js"
	integrity="sha384-eMNCOe7tC1doHpGoWe/6oMVemdAVTMs2xqW4mwXrXsW0L84Iytr2wi5v2QjrP/xp"
	crossorigin="anonymous"></script>
<script
	src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.min.js"
	integrity="sha384-0pUGZvbkm6XF6gxjEnlmuGrJXVbNuzT9qBBavbLwCsOGabYfZo0T0to5eqruptLy"
	crossorigin="anonymous"></script>
<script src="https://code.jquery.com/jquery-3.6.0.min.js"
	integrity="sha384-vtXRMe3mGCbOeY7l30aIg8H9p3GdeSe4IFlP6G8JMa7o7lXvnz3GFKzPxzJdPfGK"
	crossorigin="anonymous"></script>
<!-- Liên kết CSS của noUiSlider -->
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/noUiSlider/15.6.0/nouislider.min.css"
	integrity="sha384-PSZaVsyG9jDu8hFaSJev5s/9poIJlX7cuxSGdqCgXRHpo2DzIaZAyCd2rG/DJJmV"
	crossorigin="anonymous">
<!-- Liên kết JavaScript của noUiSlider -->
<script
	src="https://cdnjs.cloudflare.com/ajax/libs/noUiSlider/15.6.0/nouislider.min.js"
	integrity="sha384-KiWE02DmrpKdHbQq8GswlJMAWML1i8bcHHKVrFiRIV6MFi8sLmFIbDMh4vQtX1Cd"
	crossorigin="anonymous"></script>
<link href="assets/css/style.css" rel="stylesheet">
</head>
<body class="bg-light-grey">
	<jsp:include page="fragments/topNavAcc.jsp"></jsp:include>
	<div class="d-flex mt-5">
		<!-- Sidebar -->
		<jsp:include page="fragments/sidebar_UngVien.jsp" />
		<!-- Main content -->
		<div class="container mt-3">

			<div class="bg-white rounded p-3 shadow-sm mt-4 mb-4 bg-transparent-white"
				>
				<h2 class="text-center mb-4">Công Việc Yêu Thích</h2>
				<jsp:include page="fragments/frg_TimKiemVaLoc.jsp" />
			</div>

			<h4 class="col-12 mb-4">Công việc bạn đã thích</h4>
			<jsp:include page="fragments/frg_CongViec.jsp" />

		</div>
	</div>
	<script src="js/PhanTrangCongViecYeuThich.js"></script>
	<script src="js/LocSlider.js"></script>
</body>
</html>
