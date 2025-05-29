package servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.AuthUtil;
import utils.CSRFTokenManager;

import java.io.IOException;
import java.io.PrintWriter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dao.TaiKhoanDAO;
import filters.HTMLSanitizer;

public class ChangePasswordServlet extends HttpServlet {
	private static final Logger LOGGER = LoggerFactory.getLogger(ChangePasswordServlet.class);
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	
    	if (!AuthUtil.isAuthenticated(request, response))
    	{
    		return;
    	}
    	// Lấy các tham số từ form
    	
        String oldPassword = request.getParameter("oldPassword");
        String newPassword = request.getParameter("newPassword");
        String username = request.getParameter("username"); 
        
        oldPassword = HTMLSanitizer.sanitizeInput(oldPassword);
        newPassword = HTMLSanitizer.sanitizeInput(newPassword);
        username = HTMLSanitizer.sanitizeInput(username);
        // Gọi phương thức trong TaiKhoanDAO để cập nhật mật khẩu
        TaiKhoanDAO taiKhoanDAO = new TaiKhoanDAO();

        // Kiểm tra mật khẩu cũ và cập nhật mật khẩu mới
        boolean isUpdated = taiKhoanDAO.updateTaiKhoan(username, oldPassword, newPassword);

        // Trả về kết quả cho client (Dưới đây là một ví dụ trả về JSON)
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        if (isUpdated) {
        	//CSRFTokenManager.generateToken(request);
        	String newToken = (String) request.getSession().getAttribute("csrfToken");
        	LOGGER.info("Username = {} has changed password", username);
        	response.getWriter().write("{\"status\":\"success\", \"message\":\"Mật khẩu đã được cập nhật\", \"newToken\":\"" + newToken + "\"}");
        } else {
        	response.getWriter().write("{\"status\":\"error\", \"message\":\"Có lỗi xảy ra khi thực hiện thao tác!\"}");
        }
    }
}
