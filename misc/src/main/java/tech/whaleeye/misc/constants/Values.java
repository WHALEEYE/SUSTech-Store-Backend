package tech.whaleeye.misc.constants;

public class Values {

    // Verification Code Values
    // expire time in minutes
    public static final int V_CODE_EXPIRE_TIME_MINUTES = 5;

    // Json Web Token Values
    // secret of background system (private key)
    public static final String JWT_BACK_SECRET = "_=|Hn^0(nprgJPw=";
    // secret of front system (private key)
    public static final String JWT_FRONT_SECRET = "/5FHqQ*~u3Z,5tNg";

    // HTTP header
    public static final String JWT_AUTH_HEADER = "Authorize";
    // expire time (5 minutes)
    public static final long JWT_EXPIRE_TIME_SECOND = 20 * 60 * 1000;

    // File Upload Values
    // path to store uploaded files
    public final static String FILE_UPLOAD_PATH = System.getenv("SUSTechStoreUpload");

    // Used in order service
    public final static String CENTER_LATITUDE = "22.60593";
    public final static String CENTER_LONGITUDE = "114.006689";
    public final static int MAX_DISTANCE = 1100;

}
