package servlets;

import beans.TaiKhoan;
import dao.CVDAO;
import dao.CongViecDAO;
import dao.HoSoDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import utils.AuthUtil;

import java.io.IOException;
import java.io.PrintWriter;

public class UngTuyenServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public UngTuyenServlet() {
        super();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (!AuthUtil.authorizeRole(request, response, "UngVien")) return;

        HttpSession session = request.getSession(false);
        TaiKhoan taiKhoan = (TaiKhoan) session.getAttribute("account");

        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        try {
            
            String idCVStr = request.getParameter("idCV");
            String idCongViecStr = request.getParameter("idCongViec");

            if (idCVStr == null || idCongViecStr == null) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.write("{\"success\": false, \"message\": \"Thiếu tham số.\"}");
                return;
            }

            int idCV = Integer.parseInt(idCVStr);
            int idCongViec = Integer.parseInt(idCongViecStr);

            if (!CVDAO.isCVBelongsToUser(idCV, taiKhoan.getId())) {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                out.write("{\"success\": false, \"message\": \"CV không thuộc về bạn.\"}");
                return;
            }

            boolean isSuccessful = HoSoDAO.AddHoSo(idCV, idCongViec);

            if (isSuccessful) {
                CongViecDAO.updateLuotNop(idCongViec);
            }

            String newToken = (String) request.getSession().getAttribute("csrfToken");

            out.write("{\"success\": " + isSuccessful + ", \"newToken\": \"" + newToken + "\"}");

        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.write("{\"success\": false, \"message\": \"ID không hợp lệ.\"}");
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.write("{\"success\": false, \"message\": \"Lỗi hệ thống.\"}");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED, "Chỉ hỗ trợ POST.");
    }
}
