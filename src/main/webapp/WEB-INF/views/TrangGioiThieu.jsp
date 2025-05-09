<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Giới Thiệu</title>
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap-icons/font/bootstrap-icons.css"
	rel="stylesheet">
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
	src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.9.3/dist/umd/popper.min.js">
</script> 
<script
	src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js">	
</script>
 <!-- Bootstrap Slider CSS -->
<link href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-slider/10.6.2/css/bootstrap-slider.min.css" rel="stylesheet">
<!-- Bootstrap Slider JS -->
<script src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-slider/10.6.2/bootstrap-slider.min.js"></script>
<!-- Liên kết CSS của noUiSlider -->
<link href="https://cdnjs.cloudflare.com/ajax/libs/noUiSlider/15.6.0/nouislider.min.css" rel="stylesheet">

<!-- Liên kết JavaScript của noUiSlider -->
<script src="https://cdnjs.cloudflare.com/ajax/libs/noUiSlider/15.6.0/nouislider.min.js"></script>

<link href="assets/css/style.css" rel="stylesheet">
</head>
<body class="bg-light-grey">
	<!-- Header Navigation -->
	<c:choose>
		<c:when test="${not empty sessionScope.id}">
			<!-- Đã đăng nhập -->
			<jsp:include page="fragments/topNavAcc.jsp"></jsp:include>
		</c:when>
		<c:otherwise>
			<!-- Chưa đăng nhập -->
			<jsp:include page="fragments/topNav.jsp"></jsp:include>
		</c:otherwise>
	</c:choose>
	<!-- Main -->
	<div class="container mt-5">
		<jsp:include page="fragments/frg_Banner.jsp" />
		<div class="bg-white rounded p-3 shadow-sm mt-4 mb-4"
			style="background-color: rgba(255, 255, 255, 0.5);">
			<jsp:include page="fragments/frg_TimKiemVaLoc.jsp" />
			<jsp:include page="fragments/frg_DeXuat.jsp" />
		</div>

		<!-- Section các tin tuyển dụng -->
		<h4 class="col-12 mb-4">Công việc đang tuyển</h4>
		<jsp:include page="fragments/frg_CongViec.jsp" />

	</div>
	<!-- Footer -->
	<jsp:include page="fragments/footer.jsp" />
	<script src="js/PhanTrangGioiThieu.js"></script>
	<script src="js/LocSlider.js"></script>
</body>
</html>