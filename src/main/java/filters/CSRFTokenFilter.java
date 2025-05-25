package filters;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

public class CSRFTokenFilter implements Filter {

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;

		String uri = req.getRequestURI();

		// Bỏ qua tài nguyên tĩnh (nếu có)
		if (uri.endsWith(".css") || uri.endsWith(".js") || uri.contains("resources")) {
			chain.doFilter(request, response);
			return;
		}

		if ("GET".equalsIgnoreCase(req.getMethod())) {
			String csrfToken = (String) req.getSession().getAttribute("csrfToken");
			if (csrfToken == null) {
				csrfToken = UUID.randomUUID().toString();
				req.getSession().setAttribute("csrfToken", csrfToken);
			}
			req.setAttribute("csrfToken", csrfToken);
		}

		if ("POST".equalsIgnoreCase(req.getMethod())) {
			String tokenSession = (String) req.getSession().getAttribute("csrfToken");

			// Ưu tiên lấy token từ header
			String tokenRequest = req.getHeader("X-CSRF-TOKEN");

			// Nếu không có trong header, lấy từ body/form field
			if (tokenRequest == null || tokenRequest.isEmpty()) {
				tokenRequest = req.getParameter("csrfToken");
			}

			if (tokenSession == null || tokenRequest == null || !tokenSession.equals(tokenRequest)) {
				res.sendError(HttpServletResponse.SC_FORBIDDEN, "CSRF token không hợp lệ hoặc đã hết hạn.");
				return;
			}

			// Token đúng thì xoá token cũ và sinh token mới
			req.getSession().removeAttribute("csrfToken");
			String csrfToken = UUID.randomUUID().toString();
			req.getSession().setAttribute("csrfToken", csrfToken);
		}

		chain.doFilter(request, response);
	}

	@Override
	public void init(FilterConfig filterConfig) {}

	@Override
	public void destroy() {}
}
