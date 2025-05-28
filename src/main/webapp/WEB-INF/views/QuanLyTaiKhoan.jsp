<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<%
    String nonce = (String) request.getAttribute("cspNonce");
%>
<!DOCTYPE html>
<html lang="vi">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Quản lý tài khoản</title>
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
	crossorigin="anonymous" nonce="<%= nonce %>"></script>

<script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"
	integrity="sha384-DfXdz2htPH0lsSSs5nCTpuj/zy4C+OGpamoFVy38MVBnE+IbbVYUew+OrCXaRkfj"
	crossorigin="anonymous" nonce="<%= nonce %>"></script>

<script
	src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.9.3/dist/umd/popper.min.js"
	integrity="sha384-eMNCOe7tC1doHpGoWe/6oMVemdAVTMs2xqW4mwXrXsW0L84Iytr2wi5v2QjrP/xp"
	crossorigin="anonymous" nonce="<%= nonce %>"></script>

<script
	src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.min.js"
	integrity="sha384-0pUGZvbkm6XF6gxjEnlmuGrJXVbNuzT9qBBavbLwCsOGabYfZo0T0to5eqruptLy"
	crossorigin="anonymous" nonce="<%= nonce %>"></script>
	
<meta name="csrf-token" content="${csrfToken}">

</head>
<body class="bg-light-grey">

	<jsp:include page="fragments/topNavAcc.jsp"></jsp:include>

	<div class="d-flex mt-5">
		<jsp:include page="fragments/sidebar_UngVien.jsp" />

		<div class="container mt-5">
			<h2 class="text-center">Quản Lý Tài Khoản</h2>

			<!-- Phần trên: Thông tin tài khoản -->
			<div class="card mb-4">
				<div class="card-header">
					<strong>Thông tin tài khoản</strong>
				</div>
				<div class="card-body">
					<form>

						<!-- Lấy thông tin tài khoản -->
						<c:set var="taiKhoan" value="${requestScope.taiKhoan}" />
						<c:set var="uv" value="${requestScope.uv}" />

						<input type="hidden" id="id" value="${taiKhoan.id}" />

						<div class="row mb-3">
							<!-- Username -->
							<div class="col-6">
								<label for="username" class="form-label">Username</label> <input
									type="text" class="form-control" id="username"
									value="${taiKhoan.username}" readonly />
							</div>
							<!-- Password -->
							<div class="col-6">
								<label for="password" class="form-label">Password</label> <input
									type="password" class="form-control" id="password"
									value="************" readonly />
							</div>
						</div>
					</form>

					<!-- Nút Toggle để thêm phần Đổi Mật Khẩu -->
					<button type="button" id="toggleChangePassword"
						class="btn btn-secondary mb-4">Đổi mật khẩu</button>

					<!-- Phần giao diện Đổi Mật Khẩu -->
					<div id="changePasswordForm" class="mt-3 hidden">
						<div class="card">
							<div class="card-header">Thay đổi mật khẩu</div>
							<div class="card-body">
								<form id="changePasswordFormId">
									<input type="hidden" name="csrfToken"
										value="<c:out value='${csrfToken}'/>">
									<div class="mb-3">
										<label for="oldPassword" class="form-label">Mật khẩu
											cũ</label> <input type="password" id="oldPassword" name="oldPassword"
											class="form-control" placeholder="Nhập mật khẩu cũ" required maxlength="32"/>
									</div>
									<div class="mb-3">
										<label for="newPassword" class="form-label">Mật khẩu
											mới</label> <input type="password" id="newPassword"
											name="newPassword" class="form-control"
											placeholder="Nhập mật khẩu mới" required maxlength="32"/>
									</div>
									<div class="mb-3">
										<label for="confirmPassword" class="form-label">Xác
											nhận mật khẩu mới</label> <input type="password" id="confirmPassword"
											name="confirmPassword" class="form-control"
											placeholder="Nhập lại mật khẩu mới" required maxlength="32"/>
									</div>
									<button type="submit" class="btn btn-primary">Lưu thay
										đổi</button>
								</form>
							</div>
						</div>
					</div>
				</div>
			</div>

			<!-- Phần dưới: Thông tin cá nhân -->
			<div class="card">
				<div class="card-header">
					<strong>Thông tin cá nhân</strong>
				</div>
				<div class="card-body">

					<form>
						<div class="mb-3 row">
							<!-- Avatar -->
							<div class="col-md-6">
								<div
									class="position-relative d-flex justify-content-center align-items-center">
									<!-- Avatar -->
									<img id="avatarPreview" src="${uv.avatar}" alt="Avatar"
     									class="rounded-circle border avatar-preview">


									<!-- Hidden file input -->
									<input type="file" id="avatarUpload" class="d-none"
										accept="image/*" >

									<!-- Button container -->
									<div class="position-absolute d-flex align-items-center avatar-button-position">

										<!-- Upload button -->
										<button type="button"
											class="btn btn-outline-primary btn-sm me-1"
											data-bs-toggle="modal" data-bs-target="#addAvatarImageModal">
											<i class="bi bi-upload"></i>
										</button>
									</div>
								</div>
							</div>

							<div class="col-md-6">
								<!-- Họ tên -->
								<div>
									<label for="fullname" class="form-label">Họ tên</label> <input
										type="text" id="fullname" name="fullname" class="form-control"
										required value="${uv.fullName}" maxlength="50"/>
								</div>

								<!-- Giới tính -->
								<div class="col-md-3">
									<label for="gender" class="form-label">Giới tính:</label> <select
										id="gender" name="gender" class="form-control" required>
										<option value="" ${empty uv.gender ? "selected" : ""}>Chọn
											giới tính</option>
										<option value="Nam" ${uv.gender == "Nam" ? "selected" : ""}>Nam</option>
										<option value="Nữ" ${uv.gender == "Nữ" ? "selected" : ""}>Nữ</option>
										<option value="Khác" ${uv.gender == "Khác" ? "selected" : ""}>Khác</option>
									</select>
								</div>

								<!-- Ngày sinh -->
								<div class="col-md-3">
									<label for="dob" class="form-label">Ngày sinh:</label> <input
										type="date" id="dob" name="dob" class="form-control" required
										value="${uv.dob}" />
								</div>
							</div>

						</div>

						<div class="mb-3 row">
							<!-- Số điện thoại -->
							<div class="col-md-6">
								<label for="phone" class="form-label">Số điện thoại:</label> <input
									type="tel" id="phone" name="phone" pattern="[0-9]{10}"
									class="form-control" placeholder="Nhập số điện thoại" required
									value="${uv.phone}"  maxlength="10"/>
							</div>
							<!-- Email -->
							<div class="col-md-6">
								<label for="email" class="form-label">Email:</label> <input
									type="email" id="email" name="email" class="form-control"
									placeholder="Nhập email" readonly value="${uv.email}"/>
							</div>
						</div>

						<div class="mb-3 row">
							<!-- Tỉnh thành -->
							<div class="col-md-6">
								<label for="location" class="form-label">Tỉnh thành:</label> <input
									type="text" id="location" name="location" class="form-control"
									placeholder="Nhập tỉnh thành" required value="${uv.location}" maxlength="50"/>
							</div>
							<!-- Địa chỉ -->
							<div class="col-md-6">
								<label for="address" class="form-label">Địa chỉ:</label> <input
									type="text" id="address" name="address" class="form-control"
									placeholder="Nhập địa chỉ" required value="${uv.address}" maxlength="100"/>
							</div>
						</div>

						<!-- Giới thiệu -->
						<div class="mb-3">
							<label for="introduction" class="form-label">Giới thiệu:</label>
							<textarea id="introduction" name="introduction"
								class="form-control" placeholder="Giới thiệu bản thân" rows="4"  maxlength="500"
								required>${uv.introduction}</textarea>
						</div>
						<div class="form-group text-end">
							<button type="submit" class="btn btn-primary"
								id="saveAllCandidateChanges">Lưu Thay Đổi</button>
						</div>
					</form>

				</div>
			</div>
		</div>
	</div>
	<jsp:include page="modals/AddImageModals.jsp"></jsp:include>
	<script src="js/QuanLyUngVien.js" nonce="<%= nonce %>"></script>
	<script src="js/QuanLyMatKhau.js" nonce="<%= nonce %>"></script>
	<script nonce="<%= nonce %>">
		document.addEventListener("DOMContentLoaded", function () {
		    const avatarInput = document.getElementById("avatarUpload");
		    if (avatarInput) {
		        avatarInput.addEventListener("change", previewAvatar);
		    }
		});
	</script>

</body>
</html>
