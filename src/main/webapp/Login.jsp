<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<style>
:root {
	--first-color: #3d405b; /* Màu xanh đậm chiếm nhiều nhất */
	--second-color: #ff8160; /* Màu cam chiếm vừa */
	--third-color: #f4f1de; /* Màu sáng chiếm ít nhất */
	--background-color: #f0f0f0; /* Màu nền xám trắng */
}

body {
	font-family: "Arial", sans-serif;
	background-color: var(--background-color);
	display: flex;
	justify-content: center;
	align-items: center;
	height: 100vh;
	margin: 0;
	background-image: linear-gradient(to bottom right, rgba(61, 64, 91, 0.8),
		var(--second-color)); /* Thay đổi để có nhiều màu xanh hơn */
	color: var(--third-color);
}

.login-container {
	background-color: white;
	padding: 40px;
	border-radius: 10px;
	box-shadow: 0 4px 20px rgba(0, 0, 0, 0.2);
	width: 300px;
	text-align: center;
}

h2 {
	margin-bottom: 20px;
	color: var(--first-color);
}

.form-group {
	margin-bottom: 5px; /* Giữ khoảng cách giữa các ô nhập liệu */
	display: flex;
	flex-direction: column;
	text-align: left; /* Align labels to the left */
}

.form-group label {
	display: block;
	margin-bottom: 5px;
	font-weight: bold;
	color: var(--first-color);
}

.form-group input {
	padding: 10px;
	border: 1px solid #ced4da;
	border-radius: 5px;
	transition: border-color 0.3s;
}

.form-group input:focus {
	border-color: var(--first-color);
	outline: none;
}

.form-group button {
	width: 100%;
	padding: 10px;
	background-color: var(--second-color);
	color: white;
	border: none;
	border-radius: 5px;
	font-weight: bold;
	cursor: pointer;
	transition: background-color 0.3s;
	margin-top: 2px;
	/* Giảm thêm khoảng cách giữa ô mật khẩu và nút Đăng Nhập */
}

.form-group button:hover {
	background-color: #e56a4e; /* Màu tối hơn của nút khi hover */
}

.form-footer {
	margin-top: 15px;
	font-size: 0.9em;
	color: var(--first-color);
}

.form-footer a {
	color: var(--second-color);
	text-decoration: none;
}

.form-footer a:hover {
	text-decoration: underline;
}
#loginGoogle{
	width: 300px;
    height: 34.93px;
    border-radius: 4px;
    background: #ffff;
}
#loginGoogle:hover {
    cursor: pointer !important;
    background: #ccc !important;
}

</style>
<script src="https://accounts.google.com/gsi/client" async defer></script>
<title>Đăng Nhập</title>
</head>
<body onload="checkLoginMessage()">
	<div class="login-container">
		<h2>Đăng Nhập</h2>
		<form action="LoginServlet" method="POST">
			<div class="form-group">
				<label for="username">Tên đăng nhập:</label> <input type="text"
					id="username" name="username" placeholder="Nhập tên đăng nhập"
					required />
			</div>
			<div class="form-group">
				<label for="password">Mật khẩu:</label> <input type="password"
					id="password" name="password" placeholder="Nhập mật khẩu" required />
			</div>
			<div class="form-group">
				<button type="submit">Đăng Nhập</button>
			</div>
		</form>

		<!-- Google Sign-In Button -->
		<!-- <div id="g_id_onload" data-client_id="YOUR_CLIENT_ID.apps.googleusercontent.com">
		</div>
		
		<div class="g_id_signin"
		     data-type="standard"
		     data-size="large"
		     data-theme="outline"
		     data-text="sign_in_with"
		     data-shape="rectangular"
		     data-logo_alignment="left"
		     data-onsuccess="handleCredentialResponse"
		     style="cursor: pointer;" >
		</div> -->
		<!-- Nút của bạn sẽ nằm trên nút Google -->
		<button id = "loginGoogle" class="custom-button" onclick="redirectToGoogleLogin()">
		    Đăng nhập với Google
		</button>
		

		<div class="form-footer">
			<p>
				Chưa có tài khoản? <a href="Signup.jsp">Đăng ký tài khoản</a>
			</p>
		</div>
	</div>


	<script type="text/javascript">
	// Hàm kiểm tra trạng thái đăng nhập của người dùng bằng facebook
	function checkLoginState() {
	    FB.getLoginStatus(function(response) {
	        if (response.status === 'connected') {
	            // Gửi token ID tới server để xác minh
	            var accessToken = response.authResponse.accessToken;
	            window.location.href = "facebook_login?access_token=" + accessToken;
	        } else {
	            console.log("User cancelled login or did not fully authorize.");
	        }
	    });
	}
	</script>
	<script>
        // Hàm kiểm tra tham số lỗi và hiển thị thông báo
        function checkLoginMessage() {
            const urlParams = new URLSearchParams(window.location.search);
            if (urlParams.get('error') === '1') {
                alert("Login failed! Please check your username and password.");
                // Xóa tham số 'error' khỏi URL
                removeErrorParam();
            }
            if (urlParams.get('success') === '1') {
    			alert("Succefully sign up! You can log in now!")
    			removeSuccessParam();
    		}
        }
        // Hàm xóa tham số 'error' khỏi URL
        function removeErrorParam() {
            const url = new URL(window.location.href);
            url.searchParams.delete('error'); // Xóa tham số 'error'
            window.history.replaceState({}, document.title, url); // Cập nhật URL mà không tải lại trang
        }
        function removeSuccessParam() {
            const url = new URL(window.location.href);
            url.searchParams.delete('success'); // Xóa tham số 'error'
            window.history.replaceState({}, document.title, url); // Cập nhật URL mà không tải lại trang
        }
    </script>
    <script>
    // Hàm lấy tham số từ URL
    function getUrlParameter(name) {
        const urlParams = new URLSearchParams(window.location.search);
        return urlParams.get(name); // Trả về giá trị của tham số hoặc null nếu không có tham số
    }

    // Hàm kiểm tra tham số 'role' và truyền vào URL của LoginGoogleServlet
    function redirectToGoogleLogin() {
        // Lấy giá trị của 'role' từ URL
        const role = getUrlParameter('role');
        
        // Nếu có tham số 'role', thêm nó vào URL
        if (role) {
            window.location.href = 'LoginGoogleServlet?role=' + role;
        } else {
            // Nếu không có tham số 'role', chỉ điều hướng đến LoginGoogleServlet mà không có tham số
            window.location.href = 'LoginGoogleServlet';
        }
    }
	</script>
    <script>
    function handleCredentialResponse(response) {
        // Chỉ để giữ nút hoạt động mà không làm gì.
        console.log('Đăng nhập không kích hoạt: ', response);
    }
</script>
    
</body>
</html>