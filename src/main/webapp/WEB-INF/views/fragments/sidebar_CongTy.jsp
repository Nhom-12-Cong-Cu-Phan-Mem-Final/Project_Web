<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<style>
/* Sidebar styles */
.sidebar {
	height: 100vh;
	width: 250px;
	position: fixed;
	top: 0;
	left: -250px; /* Ẩn sidebar ngoài màn hình */
	background-color: #343a40; /* Màu nền */
	transition: all 0.3s ease-in-out;
	z-index: 1050; /* Đảm bảo hiển thị trên các thành phần khác */
}

.sidebar.active {
	left: 0; /* Hiển thị sidebar */
}

.sidebar .nav-link {
	color: #343a40;
	display: flex;
	align-items: center;
	gap: 20px;
	font-size: 16px; /* Cỡ chữ cho từng liên kết */
	font-weight: bold;
	text-decoration: none;
	padding: 10px 20px;
}

.sidebar .nav-link:hover {
	background-color: #495057; /* Hiệu ứng hover */
}

/* Nút toggle nằm bên trong sidebar */
.btn-toggle {
	position: absolute;
	top: 70px;
	right: -40px; /* Dịch ra khỏi sidebar để nằm sát mép phải */
	background-color: #343a40;
	color: #fff;
	border: none;
	border-radius: 50%;
	width: 35px;
	height: 35px;
	display: flex;
	align-items: center;
	justify-content: center;
	z-index: 1100; /* Hiển thị trên sidebar */
	cursor: pointer;
	box-shadow: 0 2px 4px rgba(0, 0, 0, 0.2);
}

.btn-toggle:hover {
	background-color: #495057;
}
</style>

<div id="sidebar" class="sidebar d-flex flex-column bg-white shadow">
	<button id="btnToggle" class="btn-toggle">☰</button>

	<a href="DangCongViec.jsp" class="nav-link hover-coral"> <i
		class="fs-4 fw-bold bi bi-clipboard-plus"></i> <span>Đăng tin
			tuyển dụng</span>
	</a> <a href="QuanLyTinDangServlet" class="nav-link hover-coral"> <i
		class="fs-4 fw-bold bi bi-file-earmark-post"></i> <span>Quản lý
			tin đã đăng</span>
	</a> <a href="HoSoUngTuyenServlet" class="nav-link hover-coral"> <i
		class="fs-4 fw-bold bi bi-briefcase"></i> <span>Hồ sơ ứng tuyển</span>
	</a> <a href="GoogleCalendarEventsServlet" class="nav-link hover-coral">
		<i class="fs-4 fw-bold bi bi-calendar-check"></i> <span>Lịch
			hẹn</span>
	</a> <a href="TaiKhoanCongTyServlet" class="nav-link hover-coral"> <i
		class=" fs-4 fw-bold bi bi-person-gear"></i> <span>Quản lý tài
			khoản</span>
	</a>

</div>

<script>
	const sidebar = document.getElementById('sidebar');
	const toggleButton = document.getElementById('toggleSidebar');
	
	document.getElementById('btnToggle').addEventListener('click', function() {
	        const sidebar = document.getElementById('sidebar');
	        sidebar.classList.toggle('active');
	    });
</script>
