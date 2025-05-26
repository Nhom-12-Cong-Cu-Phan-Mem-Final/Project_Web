package filters;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;

public class SessionSecurityFilter implements Filter {

    public void init(FilterConfig filterConfig) {}

    public void destroy() {}

    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        HttpSession session = request.getSession(false);

        // Chỉ kiểm tra khi đã đăng nhập
        if (session != null && session.getAttribute("account") != null) {
            String storedIP = (String) session.getAttribute("ip_address");
            String storedUA = (String) session.getAttribute("user_agent");

            String currentIP = request.getRemoteAddr();
            String currentUA = request.getHeader("User-Agent");

            // Nếu khác IP hoặc User-Agent → nghi ngờ chiếm phiên
            if (!currentIP.equals(storedIP) || !currentUA.equals(storedUA)) {
                session.invalidate();
                response.sendRedirect(request.getContextPath() + "/Login.jsp?error=1");
                return;
            }
        }

        chain.doFilter(request, response);
    }
}
