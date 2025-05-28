package servlets;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import utils.AuthUtil;
import utils.CSRFTokenManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.List;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mysql.cj.Session;

import beans.CV;
import beans.CongTy;
import beans.TaiKhoan;
import beans.UngVien;
import dao.CVDAO;
import dao.CongTyDAO;
import dao.TaiKhoanDAO;
import dao.UngVienDAO;
import filters.HTMLSanitizer;
import filters.LengthFilter;

public class QuanLyTaiKhoanServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LoggerFactory.getLogger(QuanLyTaiKhoanServlet.class);
    public QuanLyTaiKhoanServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	if (!AuthUtil.authorizeRole(request, response, "UngVien")) return;

        TaiKhoan taiKhoan = (TaiKhoan) request.getSession(false).getAttribute("account");
        
        try {
	        UngVien uv = UngVienDAO.getUngVienById(taiKhoan.getId());

	        request.setAttribute("taiKhoan", taiKhoan);
	        request.setAttribute("uv", uv);
	        
	        request.getRequestDispatcher("/WEB-INF/views/QuanLyTaiKhoan.jsp").forward(request, response);
	    } catch (Exception e) {
	        e.printStackTrace();
	        response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
	    }
        
    }


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    // Lấy thông tin từ form
		if (!AuthUtil.authorizeRole(request, response, "UngVien")) return;
		
		HttpSession session = request.getSession(false);

        TaiKhoan taiKhoan = (TaiKhoan) session.getAttribute("account");
	    int id = taiKhoan.getId();
	    
	    BufferedReader reader = request.getReader();
	    StringBuilder jsonString = new StringBuilder();
	    String line;
	    while ((line = reader.readLine()) != null) {
	        jsonString.append(line);
	    }

	    try {
	        JSONObject json = new JSONObject(jsonString.toString());
	        // Lấy dữ liệu từ JSON
	        String fullname = json.getString("fullname");
	        fullname = HTMLSanitizer.sanitizeInput(fullname);
	        String gender = json.getString("gender");
	        gender = HTMLSanitizer.sanitizeInput(gender);
	        String dobString = json.getString("dob");
	        dobString = HTMLSanitizer.sanitizeInput(dobString);
	        String phone = json.getString("phone");
	        phone = HTMLSanitizer.sanitizeInput(phone);
	        String location = json.getString("location");
	        location = HTMLSanitizer.sanitizeInput(location);
	        String address = json.getString("address");
	        address = HTMLSanitizer.sanitizeInput(address);
	        String introduction = json.getString("introduction");
	        introduction = HTMLSanitizer.sanitizeInput(introduction);
	        
	        if (LengthFilter.isTooLong(fullname, 100)) {
	            logger.warn("Full name is too long!");
	            response.setStatus(HttpServletResponse.SC_BAD_REQUEST); // 400
	            response.getWriter().write("Full name exceeds maximum length!");
	            return;
	        }
	        
	        if (LengthFilter.isTooLong(gender, 20)) {
	            logger.warn("Gender is too long!");
	            response.setStatus(HttpServletResponse.SC_BAD_REQUEST); // 400
	            response.getWriter().write("Gender exceeds maximum length!");
	            return;
	        }
	        
	        if (LengthFilter.isTooLong(dobString, 10)) { // Assuming dob format is yyyy-mm-dd
	            logger.warn("Date of birth is too long!");
	            response.setStatus(HttpServletResponse.SC_BAD_REQUEST); // 400
	            response.getWriter().write("Date of birth exceeds maximum length!");
	            return;
	        }
	        
	        if (LengthFilter.isTooLong(phone, 10)) {
	            logger.warn("Phone number is too long!");
	            response.setStatus(HttpServletResponse.SC_BAD_REQUEST); // 400
	            response.getWriter().write("Phone number exceeds maximum length!");
	            return;
	        }
	        
	        if (LengthFilter.isTooLong(location, 100)) {
	            logger.warn("Location is too long!");
	            response.setStatus(HttpServletResponse.SC_BAD_REQUEST); // 400
	            response.getWriter().write("Location exceeds maximum length!");
	            return;
	        }
	        
	        if (LengthFilter.isTooLong(address, 200)) {
	            logger.warn("Address is too long!");
	            response.setStatus(HttpServletResponse.SC_BAD_REQUEST); // 400
	            response.getWriter().write("Address exceeds maximum length!");
	            return;
	        }
	        
	        if (LengthFilter.isTooLong(introduction, 2000)) {
	            logger.warn("Introduction is too long!");
	            response.setStatus(HttpServletResponse.SC_BAD_REQUEST); // 400
	            response.getWriter().write("Introduction exceeds maximum length!");
	            return;
	        }
	        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	        Date dob = null;

	        try {
	            java.util.Date tempDoB = sdf.parse(dobString);
	            dob = new Date(tempDoB.getTime()); // Chuyển chuỗi thành Date
	            System.out.println(dob);
	        } catch (ParseException e) {
	            e.printStackTrace();
	            // Xử lý lỗi nếu chuỗi ngày không hợp lệ
	        }

	        boolean isUpdateAvatar = true;
	        // Kiểm tra và lấy avatarSource và avatarFileName
	        String avatarSource = json.optString("avatarSource", null);  // Nếu không có sẽ trả về null
	        String avatarFileName = json.optString("avatarFileName", null); // Nếu không có sẽ trả về null

	        // Kiểm tra null cho avatarSource và avatarFileName
	        if (avatarSource == null || avatarFileName == null) {
	            isUpdateAvatar = false;
	        }
	        // Kiểm tra và lưu ảnh avatar nếu có cập nhật avatar
	        if (isUpdateAvatar) {
	            // Đường dẫn thư mục avatar trong project
	            String avatarPath = "D:\\Project\\Project_Web\\src\\main\\webapp\\assets\\images\\avatar";
	            // Đường dẫn thư mục avatar nơi ứng dụng được deploy
	            String deployedAvatarPath = request.getServletContext().getRealPath("/assets/images/avatar");

	            // Kiểm tra và tạo thư mục lưu trữ nếu chưa tồn tại
	            File avatarDir = new File(avatarPath);
	            if (!avatarDir.exists()) {
	                avatarDir.mkdirs();
	            }
	            File deployedAvatarDir = new File(deployedAvatarPath);
	            if (!deployedAvatarDir.exists()) {
	                deployedAvatarDir.mkdirs();
	            }

	            // Loại bỏ tiền tố base64 nếu có
	            if (avatarSource != null && avatarSource.startsWith("data:image")) {
	                avatarSource = avatarSource.split(",")[1];  // Cắt bỏ phần tiền tố "data:image..."
	            }

	            try {
	                // Giải mã dữ liệu Base64 của avatar
	                byte[] decodedAvatar = Base64.getDecoder().decode(avatarSource);

	                // Lưu vào thư mục project
	                File avatarFile = new File(avatarPath, avatarFileName);
	                try (FileOutputStream fos = new FileOutputStream(avatarFile)) {
	                    fos.write(decodedAvatar);
	                }

	                // Lưu vào thư mục deploy
	                File deployedAvatarFile = new File(deployedAvatarPath, avatarFileName);
	                try (FileOutputStream fos = new FileOutputStream(deployedAvatarFile)) {
	                    fos.write(decodedAvatar);
	                }

	                System.out.println("Đã lưu avatar: " + avatarFile.getAbsolutePath());
	            } catch (IllegalArgumentException e) {
	                e.printStackTrace();
	                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
	                JSONObject errorResponse = new JSONObject();
	                errorResponse.put("success", false);
	                errorResponse.put("message", "Dữ liệu Base64 của avatar không hợp lệ.");
	                response.setContentType("application/json");
	                response.getWriter().write(errorResponse.toString());
	                return;
	            } catch (IOException e) {
	                e.printStackTrace();
	                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
	                JSONObject errorResponse = new JSONObject();
	                errorResponse.put("success", false);
	                errorResponse.put("message", "Lỗi khi lưu avatar.");
	                response.setContentType("application/json");
	                response.getWriter().write(errorResponse.toString());
	                return;
	            }
	        }

	        // Tạo đối tượng UserAccount để lưu thông tin người dùng
	        UngVien userAccount = new UngVien();
	        userAccount.setFullName(fullname);
	        userAccount.setGender(gender);
	        userAccount.setDob(dob);
	        userAccount.setPhone(phone);
	        userAccount.setLocation(location);
	        userAccount.setAddress(address);
	        userAccount.setIntroduction(introduction);
	        userAccount.setIdUV(id);
	        if (avatarFileName != null) {
	        	userAccount.setAvatar("assets/images/avatar/" + avatarFileName);
			}
	        else {
	        	userAccount.setAvatar(null);
			}
	        try {
	            UngVienDAO.updateUngVien(userAccount);
	            logger.info("User id {} updated profile: fullname={}, gender={}, dob={}, phone={}, location={}, address={}, avatar={}", 
	            	    userAccount.getIdUV(), 
	            	    userAccount.getFullName(), 
	            	    userAccount.getGender(), 
	            	    userAccount.getDob(), 
	            	    userAccount.getPhone(), 
	            	    userAccount.getLocation(), 
	            	    userAccount.getAddress(), 
	            	    userAccount.getAvatar()
	            	);

	            // Nếu cập nhật thành công, trả về phản hồi JSON
	            //CSRFTokenManager.generateToken(request);
	            JSONObject successResponse = new JSONObject();
	            successResponse.put("success", true);
	            successResponse.put("message", "Cập nhật thông tin thành công.");
	            
	            String newToken = (String) request.getSession().getAttribute("csrfToken");
	            successResponse.put("newToken", newToken);
	            
	            session.setAttribute("name", userAccount.getFullName());
	            response.setContentType("application/json");
	            response.getWriter().write(successResponse.toString());
	        } catch (SQLException e) {
	            e.printStackTrace();
	            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
	            JSONObject errorResponse = new JSONObject();
	            errorResponse.put("success", false);
	            errorResponse.put("message", "Lỗi khi cập nhật thông tin người dùng.");
	            response.setContentType("application/json");
	            response.getWriter().write(errorResponse.toString());
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
	        JSONObject errorResponse = new JSONObject();
	        errorResponse.put("success", false);
	        errorResponse.put("message", "Lỗi xử lý yêu cầu.");
	        response.setContentType("application/json");
	        response.getWriter().write(errorResponse.toString());
	    }
	    //doGet(request, response);
	}
	

}
