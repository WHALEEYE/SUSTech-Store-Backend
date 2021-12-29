package tech.whaleeye.misc.utils;

import com.alibaba.fastjson.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

public class BaiduMapUtils {

    private static final String AK = System.getenv("BaiduMapAK");
    private static final String SK = System.getenv("BaiduMapSK");

//    public static void main(String[] args) {
//        try {
//            JSONObject rst = revGeoCoding("22.615485", "114.008661", CoordinateType.BAIDU_LL);
//            System.out.println(rst);
////            System.out.println("latitude-->" + ((JSONObject) ((JSONArray) rst.get("result")).get(0)).get("y"));
////            System.out.println("longitude-->" + ((JSONObject) ((JSONArray) rst.get("result")).get(0)).get("x"));
//        } catch (MalformedURLException | UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//    }

    /**
     * Transform the coordinate from one type to another type.
     *
     * @param latitude          The source latitude.
     * @param longitude         The source longitude.
     * @param srcCoordinateType The type of source coordinate.
     * @param dstCoordinateType The type of destination coordinate.
     * @return A {@link JSONObject} containing the return information from the API.
     */
    public static JSONObject geoConv(String latitude, String longitude, SrcCoordinateType srcCoordinateType, DstCoordinateType dstCoordinateType) throws MalformedURLException, UnsupportedEncodingException {
        Map<String, String> paramsMap = new LinkedHashMap<>();
        paramsMap.put("coords", longitude + "," + latitude);
        paramsMap.put("from", srcCoordinateType.code + "");
        paramsMap.put("to", dstCoordinateType.code + "");
        paramsMap.put("output", "json");
        URL url = generateURL(paramsMap, Prefix.GEO_CONV);
        return requestAPI(url);
    }

    /**
     * Get the latitude and longitude of the address.
     *
     * @param address The address that need to be queried.
     * @param city    The city of the queried address.
     *                If there are more than one place that corresponds to the address, can use city to filter the results.
     *                Can be null.
     * @return A {@link JSONObject} containing the return information from the API.
     */
    public static JSONObject geoCoding(String address, String city, CoordinateType coordinateType) throws MalformedURLException, UnsupportedEncodingException {
        Map<String, String> paramsMap = new LinkedHashMap<>();
        paramsMap.put("address", address);
        paramsMap.put("output", "json");
        if (city != null) {
            paramsMap.put("city", city);
        }
        paramsMap.put("ret_coordtype", coordinateType.code);
        URL url = generateURL(paramsMap, Prefix.GEO_CODING);
        return requestAPI(url);
    }

    /**
     * Get the location name of the given coordinate.
     *
     * @param latitude  The latitude.
     * @param longitude The longitude.
     * @return A {@link JSONObject} containing the return information from the API.
     */
    public static JSONObject revGeoCoding(String latitude, String longitude, CoordinateType coordinateType) throws MalformedURLException, UnsupportedEncodingException {
        Map<String, String> paramsMap = new LinkedHashMap<>();
        paramsMap.put("location", latitude + "," + longitude);
        paramsMap.put("output", "json");
        paramsMap.put("coordtype", coordinateType.code);
        URL url = generateURL(paramsMap, Prefix.REVERSE_GEO_CODING);
        return requestAPI(url);
    }

    /**
     * Request the API of Baidu Map and return a {@link JSONObject} corresponds to the answer of the API.
     *
     * @param url The request URL.
     * @return The answer from the API.
     */
    private static JSONObject requestAPI(URL url) {
        StringBuilder buffer = new StringBuilder();
        try {
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestMethod("GET");
            conn.connect();
            InputStream is = conn.getInputStream();
            InputStreamReader isr = new InputStreamReader(is, StandardCharsets.UTF_8);
            BufferedReader br = new BufferedReader(isr);
            String line;
            while ((line = br.readLine()) != null) {
                buffer.append(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return JSONObject.parseObject(buffer.toString());
    }

    /**
     * Generate a {@link URL} object from the parameters.
     *
     * @param paramsMap A {@link LinkedHashMap} storing the key and value of the parameters.
     * @param urlPrefix A {@link Prefix} object representing the part of URL that between domain and the parameters.
     * @return The generated {@link URL} object.
     */
    private static URL generateURL(Map<String, String> paramsMap, Prefix urlPrefix) throws UnsupportedEncodingException, MalformedURLException {
        paramsMap.put("ak", AK);
        String paramsStr = toQueryString(paramsMap);
        String wholeStr = urlPrefix.content + paramsStr + SK;
        String tempStr = URLEncoder.encode(wholeStr, "UTF-8");
        String sn = MD5(tempStr);
        return new URL("https://api.map.baidu.com" + urlPrefix.content + paramsStr + "&sn=" + sn);
    }

    /**
     * Encode all the value in the map with UTF-8 coding, joint the results and return.
     *
     * @param data A {@link Map} whose value need to be encoded.
     * @return A {@link String} that is jointed by the results.
     */
    private static String toQueryString(Map<?, ?> data) throws UnsupportedEncodingException {
        StringBuilder queryString = new StringBuilder();
        for (Entry<?, ?> pair : data.entrySet()) {
            queryString.append(pair.getKey()).append("=");
            queryString.append(URLEncoder.encode((String) pair.getValue(), "UTF-8")).append("&");
        }
        if (queryString.length() > 0) {
            queryString.deleteCharAt(queryString.length() - 1);
        }
        return queryString.toString();
    }

    /**
     * An MD5 calculate algorithm from StackOverflow.
     * Uses methods in {@link java.security.MessageDigest} and converts the result from byte array to HEX String.
     *
     * @param md5 A {@link String} that needs to be encrypted.
     * @return An encrypted {@link String}.
     */
    private static String MD5(String md5) {
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
            byte[] array = md.digest(md5.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : array) {
                sb.append(Integer.toHexString((b & 0xFF) | 0x100), 1, 3);
            }
            return sb.toString();
        } catch (java.security.NoSuchAlgorithmException ignored) {
        }
        return null;
    }

    private enum Prefix {
        IP_LOCATE("/location/ip/?"), GEO_CODING("/geocoding/v3/?"), REVERSE_GEO_CODING("/reverse_geocoding/v3/?"), GEO_CONV("/geoconv/v1/?");

        final String content;

        Prefix(String content) {
            this.content = content;
        }
    }

    public enum CoordinateType {
        GCJ_LL("gcj02ll"), BAIDU_MC("bd09mc"), BAIDU_LL("bd09ll");

        final String code;

        CoordinateType(String code) {
            this.code = code;
        }
    }

    public enum SrcCoordinateType {
        WGS(1), SOUGOU(2), GCJ_LL(3), GCJ_MC(4), BAIDU_LL(5), BAIDU_MC(6), TUBA(7), MAP_51(8);

        final int code;

        SrcCoordinateType(int code) {
            this.code = code;
        }
    }

    public enum DstCoordinateType {
        GCJ_LL(3), BAIDU_LL(5), BAIDU_MC(6);

        final int code;

        DstCoordinateType(int code) {
            this.code = code;
        }
    }
}