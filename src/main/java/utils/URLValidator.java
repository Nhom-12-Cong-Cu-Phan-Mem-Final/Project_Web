package utils;

import java.net.InetAddress;
import java.net.URI;
import java.net.URL;

public class URLValidator {

    public static boolean isSafeURL(String urlString) {
        try {
            // Parse URL
            URI uri = new URI(urlString);
            String host = uri.getHost();
            if (host == null) return false;

            // Lấy địa chỉ IP của host
            InetAddress address = InetAddress.getByName(host);

            // Kiểm tra nếu là địa chỉ nội bộ
            if (address.isAnyLocalAddress()           // 0.0.0.0
                    || address.isLoopbackAddress()    // 127.0.0.1
                    || address.isLinkLocalAddress()   // 169.254.x.x
                    || address.isSiteLocalAddress())  // 10.x.x.x, 192.168.x.x, 172.16.x.x - 172.31.x.x
            {
                return false; // Không an toàn
            }

            // Nếu không phải IP nội bộ
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false; // Nếu có lỗi khi phân tích URL, coi là không an toàn
        }
    }
}
