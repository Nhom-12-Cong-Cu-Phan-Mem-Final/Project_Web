package utils;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class PreventRedirect {
	public static void PreventRedirect(String userUrl)
	{
		HttpURLConnection conn;
		try {
			conn = (HttpURLConnection) new URL(userUrl).openConnection();
			conn.setInstanceFollowRedirects(false); // Vô hiệu hóa redirect
			int responseCode = conn.getResponseCode();
			if (responseCode == HttpURLConnection.HTTP_MOVED_TEMP
			    || responseCode == HttpURLConnection.HTTP_MOVED_PERM
			    || responseCode == HttpURLConnection.HTTP_SEE_OTHER) {
			    // Từ chối tiếp tục
			    throw new SecurityException("Redirects not allowed");
			}

		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
