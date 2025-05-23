package servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import utils.AuthUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;

import beans.HoSo;
import beans.TaiKhoan;
import dao.CongViecDAO;
import dao.HoSoDAO;

/**
 * Servlet implementation class CongViecDaUngTuyenServlet
 */
public class CongViecDaUngTuyenServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CongViecDaUngTuyenServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		if (!AuthUtil.authorizeRole(request, response, "UngVien")) return;
		
		HttpSession session = request.getSession(false);
		
		TaiKhoan taiKhoan = (TaiKhoan) session.getAttribute("account");
		int idUV= taiKhoan.getId();
		
		List<HoSo> hoSos = new ArrayList<>();
		
		try {
	        hoSos = HoSoDAO.GetListHoSoByIdUV(idUV);
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
		
		List<String> linhVucs = new ArrayList<>();
		try {
			linhVucs = CongViecDAO.getListLinhVuc();
		} catch (Exception e) {
			e.printStackTrace();
		}	  
		request.setAttribute("linhVucs", linhVucs);
		String linhVuc = request.getParameter("linhVuc");
	    String trangThai = request.getParameter("trangThai");
	    String ten = request.getParameter("searchText");  // Lấy giá trị tìm kiếm theo tên công việc
		String thoiGian = request.getParameter("thoiGian");
		hoSos = HoSo.LocTheoTenCongViec(ten, hoSos);
		hoSos = HoSo.LocTheoLinhVuc(linhVuc, hoSos);
		hoSos = HoSo.LocTheoThoiGian(thoiGian, hoSos);
		hoSos = HoSo.LocTheoTrangThai(trangThai, hoSos);
		
		int pageSize = 20;
	    int page = 1;  // Mặc định là trang đầu tiên

	    // Lấy tham số 'page' từ yêu cầu
	    String pageStr = request.getParameter("page");
	    if (pageStr != null && !pageStr.isEmpty()) {
	        try {
	            page = Integer.parseInt(pageStr);
	        } catch (NumberFormatException e) {
	            page = 1;  // Nếu tham số không hợp lệ, sử dụng trang đầu tiên
	        }
	    }

	    int totalHoSos = hoSos.size();
	    int totalPages = (int) Math.ceil((double) totalHoSos / pageSize);

	    int startIndex = (page - 1) * pageSize;
	    int endIndex = Math.min(startIndex + pageSize, totalHoSos);
	    List<HoSo> pagedHoSos = hoSos.subList(startIndex, endIndex);

	    // Nếu không phải AJAX, bạn có thể chuyển hướng sang JSP
	    if (!"true".equals(request.getParameter("ajax"))) {
	        request.getRequestDispatcher("/WEB-INF/views/CongViecDaUngTuyen.jsp").forward(request, response);
	    }
	    else {
	    	 response.setContentType("application/json");
	 	    response.setCharacterEncoding("UTF-8");

	 	    Map<String, Object> responseData = new HashMap<>();
	 	    responseData.put("hoSos", pagedHoSos);
	 	    responseData.put("totalPages", totalPages);
	 	    responseData.put("currentPage", page);

	 	    String jsonResponse = new Gson().toJson(responseData);
	 	    System.out.println(jsonResponse); 
	 	    response.getWriter().write(jsonResponse);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
