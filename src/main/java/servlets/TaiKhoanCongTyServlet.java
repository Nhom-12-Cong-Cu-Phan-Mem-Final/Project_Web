package servlets;

import jakarta.servlet.ServletContext;
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
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Set;

import org.eclipse.core.runtime.Platform;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import beans.CongTy;
import beans.CongViec;
import beans.TaiKhoan;
import dao.CongTyDAO;
import dao.CongViecDAO;
import dao.TaiKhoanDAO;
import filters.HTMLSanitizer;
import filters.LengthFilter;

/**
 * Servlet implementation class TaiKhoanCongTy
 */
public class TaiKhoanCongTyServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LoggerFactory.getLogger(TaiKhoanCongTyServlet.class);
    /**
     * @see HttpServlet#HttpServlet()
     */
    public TaiKhoanCongTyServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		if (!AuthUtil.authorizeRole(request, response, "CongTy")) return;
		
		TaiKhoan taiKhoan = (TaiKhoan) request.getSession(false).getAttribute("account");

	    try {
	        CongTy congTy = CongTyDAO.GetCongTyById(taiKhoan.getId());
	        List<String> images = CongTyDAO.getHinhAnhHoatDong(taiKhoan.getId());

	        request.setAttribute("tk", taiKhoan);
	        request.setAttribute("congTy", congTy);
	        request.setAttribute("images", images);

	        request.getRequestDispatcher("/WEB-INF/views/TaiKhoanCongTy.jsp").forward(request, response);
	    } catch (Exception e) {
	        e.printStackTrace();
	        response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
	    }
   }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//doGet(request, response);
		if (!AuthUtil.authorizeRole(request, response, "CongTy")) return;
		
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
            // Lấy các trường thông tin từ JSON
            String tenCongTy = json.getString("tenCongTy");
            tenCongTy = HTMLSanitizer.sanitizeInput(tenCongTy);
            String sdt = json.getString("sdt");
            sdt = HTMLSanitizer.sanitizeInput(sdt);
            String tinhThanh = json.getString("tinhThanh");
            tinhThanh = HTMLSanitizer.sanitizeInput(tinhThanh);
            String diaChi = json.getString("diaChi");
            diaChi = HTMLSanitizer.sanitizeInput(diaChi);
            String maSoThue = json.getString("maSoThue");
            maSoThue = HTMLSanitizer.sanitizeInput(maSoThue);
            String linhVuc = json.getString("linhVuc");
            linhVuc = HTMLSanitizer.sanitizeInput(linhVuc);
            String quyMoNhanSu = json.getString("quyMoNhanSu");
            quyMoNhanSu = HTMLSanitizer.sanitizeInput(quyMoNhanSu);
            String url = json.getString("url");
            url = HTMLSanitizer.sanitizeInput(url);
            String gioiThieu = json.getString("gioiThieu");
            gioiThieu = HTMLSanitizer.sanitizeInput(gioiThieu);
            
            if (
            	    LengthFilter.isTooLong(tenCongTy, 100) ||
            	    LengthFilter.isTooLong(sdt, 15) ||
            	    LengthFilter.isTooLong(tinhThanh, 100) ||
            	    LengthFilter.isTooLong(diaChi, 200) ||
            	    LengthFilter.isTooLong(maSoThue, 20) ||
            	    LengthFilter.isTooLong(linhVuc, 100) ||
            	    LengthFilter.isTooLong(quyMoNhanSu, 50) ||
            	    LengthFilter.isTooLong(url, 200) ||
            	    LengthFilter.isTooLong(gioiThieu, 2000)
            	) {
            		logger.warn("Invalid input length!");
            	    response.setStatus(HttpServletResponse.SC_BAD_REQUEST); // 400
            	    response.getWriter().write("Một số trường vượt quá độ dài cho phép!");
            	    return;
            	}

            
            boolean isUpdateAvatar = true;
            boolean isUpdateActivities = true;
            boolean isUpdateBackGround = true;
            // Kiểm tra và lấy avatarSource và avatarFileName
            String avatarSource = json.optString("avatarSource", null);  // Nếu không có sẽ trả về null
            String avatarFileName = json.optString("avatarFileName", null); // Nếu không có sẽ trả về null

            // Kiểm tra null cho avatarSource và avatarFileName
            if (avatarSource == null || avatarFileName == null) {
                isUpdateAvatar = false;
            }
            
            // Kiểm tra và lấy mảng imageSources và fileNames
            JSONArray imageSources = json.optJSONArray("imageSources");
            JSONArray fileNames = json.optJSONArray("fileNames");

            // Kiểm tra xem mảng imageSources và fileNames có null không
            if (imageSources == null || fileNames == null || imageSources.length() != fileNames.length()) {
                isUpdateActivities = false;
            }
            
            String backGroundSource = json.optString("backGroundSource", null);
            String backGroundFileName = json.optString("backGroundFileName", null);
            
            if (backGroundSource == null || backGroundFileName == null) {
				isUpdateBackGround = false;
			}
            if (isUpdateActivities) {
                // Đường dẫn thư mục trong project
                String absolutePath = "D:\\Project\\Project_Web\\src\\main\\webapp\\assets\\images\\act";

                // Đường dẫn thư mục nơi ứng dụng được deploy
                String deployedPath = request.getServletContext().getRealPath("/assets/images/act");

                // Kiểm tra và tạo thư mục lưu trữ nếu chưa tồn tại
                File dir = new File(absolutePath);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                File deployedDir = new File(deployedPath);
                if (!deployedDir.exists()) {
                    deployedDir.mkdirs();
                }

                // Lưu ảnh vào cả 2 thư mục
                for (int i = 0; i < imageSources.length(); i++) {
                    String imageSrc = imageSources.getString(i); // Dữ liệu Base64 của ảnh
                    String fileName = fileNames.getString(i);    // Tên file ảnh

                    // Loại bỏ tiền tố base64 nếu có
                    if (imageSrc != null && imageSrc.startsWith("data:image")) {
                        imageSrc = imageSrc.split(",")[1];  // Cắt bỏ phần tiền tố "data:image..."
                    }

                    try {
                        // Giải mã dữ liệu Base64 thành byte[]
                        byte[] decodedImage = Base64.getDecoder().decode(imageSrc);

                        // Lưu vào thư mục project
                        File imageFile = new File(absolutePath, fileName);
                        try (FileOutputStream fos = new FileOutputStream(imageFile)) {
                            fos.write(decodedImage);
                        }

                        // Lưu vào thư mục deploy
                        File deployedImageFile = new File(deployedPath, fileName);
                        try (FileOutputStream fos = new FileOutputStream(deployedImageFile)) {
                            fos.write(decodedImage);
                        }

                        // Lưu thông tin vào DB
                        CongTyDAO.addHinhAnhHoatDong(id, "assets/images/act/" + fileName);
                        System.out.println("Đã lưu ảnh: " + imageFile.getAbsolutePath());
                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                        response.getWriter().write("Dữ liệu Base64 không hợp lệ.");
                        return;
                    } catch (IOException e) {
                        e.printStackTrace();
                        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                        response.getWriter().write("Lỗi khi lưu ảnh.");
                        return;
                    }
                }
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
                    response.getWriter().write("Dữ liệu Base64 của avatar không hợp lệ.");
                    return;
                } catch (IOException e) {
                    e.printStackTrace();
                    response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    response.getWriter().write("Lỗi khi lưu avatar.");
                    return;
                }
            }



            CongTy ct = new CongTy();
            ct.setIdCT(id);
            ct.setTenCongTy(tenCongTy);
            ct.setSdt(sdt);
            ct.setTinhThanh(tinhThanh);
            ct.setDiaChi(diaChi);
            ct.setMaSoThue(maSoThue);
            ct.setLinhVuc(linhVuc);
            ct.setQuyMoNhanSu(quyMoNhanSu);
            ct.setUrl(url);
            ct.setGioiThieu(gioiThieu);
            if (isUpdateAvatar) {
            	 ct.setLogo("assets/images/avatar/" + avatarFileName);
			}
            else {
				ct.setLogo(null);
			}
            CongTyDAO.updateThongtinCongTy(ct);
            logger.info("Company id {} updated profile: TenCongTy={}, SDT={}, TinhThanh={}, DiaChi={}, MaSoThue={}, LinhVuc={}, QuyMoNhanSu={}"
            		+ ", URL = {}", 
            	    ct.getIdCT(), 
            	    ct.getTenCongTy(), 
            	    ct.getSdt(), 
            	    ct.getTinhThanh(), 
            	    ct.getDiaChi(), 
            	    ct.getMaSoThue(), 
            	    ct.getLinhVuc(), 
            	    ct.getQuyMoNhanSu(),
            	    ct.getUrl()
            	);
            session.setAttribute("name", ct.getTenCongTy());
            
            if (isUpdateBackGround) {
				
				// Đường dẫn thư mục avatar trong project
                String backGroundPath = "D:\\Project\\Project_Web\\src\\main\\webapp\\assets\\images\\background";

                // Đường dẫn thư mục avatar nơi ứng dụng được deploy
                String deployedBackGroundPath = request.getServletContext().getRealPath("/assets/images/background");

                // Kiểm tra và tạo thư mục lưu trữ nếu chưa tồn tại
                File avatarDir = new File(backGroundPath);
                if (!avatarDir.exists()) {
                    avatarDir.mkdirs();
                }
                File deployedBackGroundDir = new File(deployedBackGroundPath);
                if (!deployedBackGroundDir.exists()) {
                    deployedBackGroundDir.mkdirs();
                }

                // Loại bỏ tiền tố base64 nếu có
                if (backGroundSource != null && backGroundSource.startsWith("data:image")) {
                	backGroundSource = backGroundSource.split(",")[1];  // Cắt bỏ phần tiền tố "data:image..."
                }

                try {
                    // Giải mã dữ liệu Base64 của avatar
                    byte[] decodedBackGround = Base64.getDecoder().decode(backGroundSource);

                    // Lưu vào thư mục project
                    File backGroundFile = new File(backGroundPath, backGroundFileName);
                    try (FileOutputStream fos = new FileOutputStream(backGroundFile)) {
                        fos.write(decodedBackGround);
                    }

                    // Lưu vào thư mục deploy
                    File deployedBackGroundFile = new File(deployedBackGroundPath, backGroundFileName);
                    try (FileOutputStream fos = new FileOutputStream(deployedBackGroundFile)) {
                        fos.write(decodedBackGround);
                    }
                    CongTyDAO.updateBackGround("assets/images/background/" + backGroundFileName, id);
                    //CSRFTokenManager.generateToken(request);
                    System.out.println("Đã lưu background: " + backGroundFile.getAbsolutePath());
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    response.getWriter().write("Dữ liệu Base64 của background không hợp lệ.");
                    return;
                } catch (IOException e) {
                    e.printStackTrace();
                    response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    response.getWriter().write("Lỗi khi lưu background.");
                    return;
                }
			}
            
            String newToken = (String) request.getSession().getAttribute("csrfToken");

            JSONObject jsonResponse = new JSONObject();
            jsonResponse.put("status", "success");
            jsonResponse.put("newToken", newToken); 
            //jsonResponse.put("imagePath", relativePath + "/" + fileName); // �u?ng d?n ?nh tr? v?
            
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(jsonResponse.toString());
        } catch (Exception e) {
            e.printStackTrace();

            JSONObject jsonResponse = new JSONObject();
            jsonResponse.put("status", "error");
            jsonResponse.put("message", "Lỗi khi xử lý ảnh!");

            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(jsonResponse.toString());
        }
	}

}
