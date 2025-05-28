<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%
    String nonce = (String) request.getAttribute("cspNonce");
%>

<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Đăng tin</title>
<link rel="stylesheet"
	href="https://cdn.jsdelivr.net/npm/bootstrap-icons/font/bootstrap-icons.css"
	integrity="sha384-Bk5cbLkZQ5raZ0+H2/+VbfYx3WpvxvQK4zqXZr7sYODuaX7bKXoSOnipQxkaS8sv"
	crossorigin="anonymous">
<link href="assets/css/style.css" rel="stylesheet">
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
	rel="stylesheet"
	integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH"
	crossorigin="anonymous">
<script
	src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"
	integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz"
	crossorigin="anonymous"></script>
</head>
<body class="bg-light-grey">
	<script nonce="<%= nonce %>">
		<c:if test="${not empty message}">
		alert("${message}");
		</c:if>
	</script>
	
	<c:if test="${not empty sessionScope.flashMessage}">
		<script>alert('${sessionScope.flashMessage}');</script>
		<c:remove var="flashMessage" scope="session" />
	</c:if>
	<jsp:include page="fragments/topNavAcc.jsp"></jsp:include>

	<div class="d-flex mt-5">
		<!-- Sidebar -->
		<jsp:include page="fragments/sidebar_CongTy.jsp" />
		<!-- Main content -->
		<div class="container mt-4 bg-light py-3 px-3 shadow rounded ">
			<h2 class="text-center mb-4">Đăng Công Việc</h2>
			<form action="DangCongViecServlet" method="POST">
				<input type="hidden" name="csrfToken"
					value="<c:out value='${csrfToken}'/>">
				<div class="row">
					<div class="col-md-6">
						<div class="mb-3">
							<label for="ten" class="form-label">Tên Công Việc</label> <input
								type="text" class="form-control" id="ten" name="ten"
								placeholder="Nhập tên công việc" maxlength="100">
						</div>
						<div class="mb-3">
							<label for="diaDiem" class="form-label">Địa Điểm</label> <input
								type="text" class="form-control" id="diaDiem" name="diaDiem"
								placeholder="Nhập địa điểm công việc" maxlength="100">
						</div>
						<div class="mb-3">
							<label for="luong" class="form-label">Lương</label> <input
								type="number" class="form-control" id="luong" name="luong"
								placeholder="Nhập mức lương" step="0.01" min="0">
						</div>
						<div class="mb-3">
							<label for="namKinhNghiem" class="form-label">Kinh Nghiệm
								(Năm)</label> <input type="number" class="form-control"
								id="namKinhNghiem" name="namKinhNghiem" min="0"
								placeholder="Nhập số năm kinh nghiệm tối thiểu">
						</div>
						<div class="mb-3">
							<label for="linhVuc" class="form-label">Lĩnh Vực</label> <input
								type="text" class="form-control" id="linhVuc" name="linhVuc"
								placeholder="Nhập lĩnh vực công việc" maxlength="100">
						</div>
						<div class="mb-3">
							<label for="thoiGianHetHan" class="form-label">Thời Gian
								Hết Hạn</label> <input type="datetime-local" class="form-control"
								id="thoiGianHetHan" name="thoiGianHetHan">
						</div>
					</div>
					<div class="col-md-6">
						<div class="mb-3">
							<label for="moTa" class="form-label">Mô Tả Công Việc</label>
							<textarea class="form-control" id="moTa" name="moTa" rows="5"
								placeholder="Nhập mô tả công việc" maxlength="1000"></textarea>
						</div>
						<div class="mb-3">
							<label for="yeuCau" class="form-label">Yêu Cầu</label>
							<textarea class="form-control" id="yeuCau" name="yeuCau" rows="5"
								placeholder="Nhập yêu cầu công việc" maxlength="1000"></textarea>
						</div>
						<div class="mb-3">
							<label for="quyenLoi" class="form-label">Quyền Lợi</label>
							<textarea class="form-control" id="quyenLoi" name="quyenLoi"
								rows="4" placeholder="Nhập quyền lợi của công việc" maxlength="100"></textarea>
						</div>
					</div>
				</div>
				<div class="text-end">
					<button type="submit" class="btn bg-coral text-light">Đăng
						Công Việc</button>
				</div>
			</form>

		</div>
	</div>
	<script src="js/kiemTraSoAm.js"></script>
</body>
</html>
