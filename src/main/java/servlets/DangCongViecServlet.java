package servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import services.Message;
import utils.AuthUtil;
import utils.CSRFTokenManager;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;

import org.eclipse.tags.shaded.org.apache.xpath.operations.And;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mysql.cj.Session;

import beans.CongViec;
import beans.TaiKhoan;
import dao.CongViecDAO;
import filters.HTMLSanitizer;
import filters.LengthFilter;

/**
 * Servlet implementation class DangCongViecServlet
 */
/* @WebServlet("/Project_Web/DangCongViecServlet") */
public class DangCongViecServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = LoggerFactory.getLogger(DangCongViecServlet.class);
	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public DangCongViecServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		if (!AuthUtil.authorizeRole(request, response, "CongTy")) return;
				
		request.getRequestDispatcher("/WEB-INF/views/DangCongViec.jsp").forward(request, response);	
		
		}
		

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		if (!AuthUtil.authorizeRole(request, response, "CongTy")) return;
		
		HttpSession session = request.getSession(false);
		
		TaiKhoan taiKhoan = (TaiKhoan) session.getAttribute("account");
		
		int idCT= taiKhoan.getId();
		CongViec congViec = new CongViec();

		// Kiểm tra các tham số request để đảm bảo không rỗng
		String ten = request.getParameter("ten");
		ten = HTMLSanitizer.sanitizeInput(ten);
		String diaDiem = request.getParameter("diaDiem");
		diaDiem = HTMLSanitizer.sanitizeInput(diaDiem);
		String luongStr = request.getParameter("luong");
		luongStr = HTMLSanitizer.sanitizeInput(luongStr);
		String namKinhNghiemStr = request.getParameter("namKinhNghiem");
		namKinhNghiemStr = HTMLSanitizer.sanitizeInput(namKinhNghiemStr);
		String linhVuc = request.getParameter("linhVuc");
		linhVuc = HTMLSanitizer.sanitizeInput(linhVuc);
		String thoiGianHetHanStr = request.getParameter("thoiGianHetHan"); // yyyy-MM-ddTHH:mm
		thoiGianHetHanStr = HTMLSanitizer.sanitizeInput(thoiGianHetHanStr);
		String moTa = request.getParameter("moTa");
		moTa = HTMLSanitizer.sanitizeInput(moTa);
		String yeuCau = request.getParameter("yeuCau");
		yeuCau = HTMLSanitizer.sanitizeInput(yeuCau);
		String quyenLoi = request.getParameter("quyenLoi");
		quyenLoi = HTMLSanitizer.sanitizeInput(quyenLoi);
		
		try {
			// Kiểm tra độ dài các trường
			if (
			    LengthFilter.isTooLong(ten, 100) ||
			    LengthFilter.isTooLong(diaDiem, 200) ||
			    LengthFilter.isTooLong(luongStr, 20) ||
			    LengthFilter.isTooLong(namKinhNghiemStr, 2) ||
			    LengthFilter.isTooLong(linhVuc, 100) ||
			    LengthFilter.isTooLong(thoiGianHetHanStr, 20) ||
			    LengthFilter.isTooLong(moTa, 1000) ||
			    LengthFilter.isTooLong(yeuCau, 1000) ||
			    LengthFilter.isTooLong(quyenLoi, 1000)
			) {
				LOGGER.info("Input type length is invalid");
			    session.setAttribute("flashMessage", "Input type length is invalid");
			    response.sendRedirect(request.getContextPath() + "/DangCongViecServlet");
			    return;
			}

			// Kiểm tra giá trị đầu vào
			if (ten != null && !ten.isEmpty() && diaDiem != null && !diaDiem.isEmpty() && luongStr != null
					&& !luongStr.isEmpty() && namKinhNghiemStr != null && !namKinhNghiemStr.isEmpty()
					&& thoiGianHetHanStr != null && !thoiGianHetHanStr.isEmpty()) {

				// Gán giá trị cho đối tượng congViec
				congViec.setIdCT(idCT);
				congViec.setTen(ten);
				congViec.setDiaDiem(diaDiem);
				congViec.setLuong(Double.parseDouble(luongStr));
				congViec.setNamKinhNghiem(Integer.parseInt(namKinhNghiemStr));
				congViec.setLinhVuc(linhVuc);

				// Chuyển đổi thoiGianHetHanStr từ định dạng datetime-local
				String formattedDateTime = thoiGianHetHanStr.replace("T", " ") + ":00"; // yyyy-MM-dd HH:mm:ss
				congViec.setThoiGianHetHan(Timestamp.valueOf(formattedDateTime)); // Chuyển thành Timestamp

				congViec.setMoTa(moTa);
				congViec.setYeuCau(yeuCau);
				congViec.setQuyenLoi(quyenLoi);

				// Thêm công việc mới vào cơ sở dữ liệu
				if (CongViecDAO.AddCongViecMoi(congViec)) {
					LOGGER.info("Add new job with id = {} by CongTy with id = {}", congViec.getIdCongViec(), congViec.getIdCT());
					session.setAttribute("flashMessage", "Đăng công việc thành công!");
					response.sendRedirect(request.getContextPath() + "/QuanLyTinDangServlet");
	            } else {
					LOGGER.warn("Error occured when adding new job with id = {} by CongTy with id = {}", congViec.getIdCongViec(), congViec.getIdCT());
	            	
	            	session.setAttribute("flashMessage", "Có lỗi xảy ra vui lòng thử lại");
	            	response.sendRedirect(request.getContextPath() + "/DangCongViecServlet");
	            }
	        } else {
				LOGGER.warn("Invalid data type for CongViec with id = {}", congViec.getIdCongViec());
	        	session.setAttribute("flashMessage", "Dữ liệu không hợp lệ");
	        	response.sendRedirect(request.getContextPath() + "/DangCongViecServlet");
	        }
		} catch (Exception e) {
			session.setAttribute("flashMessage", "Đã xảy ra lỗi!");
			response.sendRedirect(request.getContextPath() + "/DangCongViecServlet");
			System.out.print(e.getMessage());
	       }
	}
	
}
