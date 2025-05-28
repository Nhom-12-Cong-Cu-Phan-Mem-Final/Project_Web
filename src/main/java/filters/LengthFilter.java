package filters;

public class LengthFilter {
	public static boolean isTooLong(String value, int maxLength) {
	    return value != null && value.length() > maxLength;
	}
}
