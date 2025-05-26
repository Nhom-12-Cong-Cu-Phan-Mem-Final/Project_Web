package utils;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class SessionConfigListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        // Đặt timeout là 5 phút
        sce.getServletContext().setSessionTimeout(5);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        // Không cần xử lý khi shutdown
    }
}
