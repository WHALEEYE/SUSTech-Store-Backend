package tech.whaleeye.misc.constants;

public class Values {

    // Verification Code Values
    // expire time in minutes
    public static final int V_CODE_EXPIRE_TIME_MINUTES = 5;

    // Json Web Token Values
    // secret (private key)
    public static final String JWT_SECRET = "_=|Hn^0(nprgJPw=";
    // HTTP header
    public static final String JWT_AUTH_HEADER = "JWT-Token";
    // expire time (5 minutes)
    public static final long JWT_EXPIRE_TIME_SECOND = 5 * 60 * 1000;

    // File Upload Values
    // path to store uploaded files
    public final static String FILE_UPLOAD_PATH = System.getenv("SUSTechStoreUpload");

}
