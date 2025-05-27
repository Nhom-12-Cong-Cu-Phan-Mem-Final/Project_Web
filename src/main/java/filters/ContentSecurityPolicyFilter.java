package filters;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.UUID;


public class ContentSecurityPolicyFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpServletRequest httpRequest = (HttpServletRequest) request;

        // Tạo nonce và gán vào request để dùng trong JSP
        String nonce = UUID.randomUUID().toString();
        httpRequest.setAttribute("cspNonce", nonce);

        // Thiết lập CSP Header
        httpResponse.setHeader("Content-Security-Policy",
                "default-src 'self'; " +
                "script-src 'self' 'nonce-" + nonce + "' https://accounts.google.com https://code.jquery.com https://cdn.jsdelivr.net https://cdnjs.cloudflare.com https://stackpath.bootstrapcdn.com; " +
                "style-src 'self' 'nonce-" + nonce + "' https://cdn.jsdelivr.net https://cdnjs.cloudflare.com https://stackpath.bootstrapcdn.com; " +
                "font-src 'self' https://cdn.jsdelivr.net data:; " +
                "img-src 'self' data: https://cdn-icons-png.flaticon.com; " +
                "media-src 'self' https://cdn-icons-mp4.flaticon.com; " +
                "frame-ancestors 'none'; " +
                "object-src 'none';"
        );

        // Các Header bảo mật bổ sung
        httpResponse.setHeader("X-Content-Type-Options", "nosniff");
        httpResponse.setHeader("X-Frame-Options", "DENY");
        httpResponse.setHeader("X-XSS-Protection", "1; mode=block");
        httpResponse.setHeader("Referrer-Policy", "no-referrer");
        httpResponse.setHeader("Permissions-Policy", "camera=(), microphone=(), geolocation=()");
        httpResponse.setHeader("Strict-Transport-Security", "max-age=31536000; includeSubDomains; preload");

        chain.doFilter(request, response);
    }
}
