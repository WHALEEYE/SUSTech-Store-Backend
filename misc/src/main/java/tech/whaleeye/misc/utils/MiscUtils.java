package tech.whaleeye.misc.utils;

import net.coobird.thumbnailator.Thumbnails;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.util.ByteSource;
import org.springframework.web.multipart.MultipartFile;
import tech.whaleeye.misc.constants.UploadFileType;
import tech.whaleeye.misc.constants.Values;
import tech.whaleeye.misc.exceptions.InvalidValueException;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

public class MiscUtils {

//    public static void main(String[] args) {
//        String a = generateSalt(8);
//        System.out.println(a);
//        String password = "What1sth1s";
//        System.out.println(new Md5Hash(password, getSaltFromHex(a), 1024));
//    }

    private static final double EARTH_RADIUS = 6371393; // 平均半径,单位：m；不是赤道半径。赤道为6378左右

    /**
     * Get the user ID of the current user. The user's ID can only be got after the user has logged in.
     *
     * @return the ID of the current user
     */
    public static Integer currentUserId() {
        return (Integer) SecurityUtils.getSubject().getPrincipal();
    }

    /**
     * Generate a random salt of a specified length.
     * Note that the return string is a hex string corresponds with the bytes,
     * which means if it generates a salt with n bytes, it will return a hex string with length 2n.
     *
     * @param len the number of bytes in the salt
     * @return the hex string form of the salt
     */
    public static String generateSalt(int len) {
        SecureRandomNumberGenerator secureRandom = new SecureRandomNumberGenerator();
        return secureRandom.nextBytes(len).toHex();
    }

    /**
     * Transform the salt in hex string form to {@link ByteSource} object. Note that the hex string must have an even length.
     *
     * @param hexSalt the hex string
     * @return a {@link ByteSource} object that can be used by shiro
     */
    public static ByteSource getSaltFromHex(String hexSalt) {
        int hexLen = hexSalt.length();
        byte[] saltByte = new byte[hexLen / 2];
        for (int i = 0; i < hexLen; i += 2) {
            saltByte[i / 2] = (byte) Integer.parseInt(hexSalt.substring(i, i + 2), 16);
        }
        return ByteSource.Util.bytes(saltByte);
    }

//    /**
//     * Generate a watermark of one user's nickname.
//     *
//     * @param nickName the nickname of one user
//     * @return a watermark in the form of one {@link BufferedImage} object
//     */
//    private static BufferedImage handleTextWaterMark(String nickName) {
//
//        BufferedImage image = new BufferedImage(20, 20, BufferedImage.TYPE_INT_RGB);
//        Graphics2D g = image.createGraphics();
//        image = g.getDeviceConfiguration().createCompatibleImage(20, 20, Transparency.TRANSLUCENT);
//
//        g = image.createGraphics();
//        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
//        g.setColor(Color.red);
//
//        if (StringUtils.isNotBlank(nickName)) {
//            g.drawString("@" + nickName, 5, 0);
//        }
//
//        g.dispose();
//        return image;
//    }

    /**
     * Compress and rename one picture.
     *
     * @param picture  the picture that needs to be processed
     * @param fileType specifies the type of this picture, such as avatar, good description pictures etc.
     * @return the file path of the processed picture
     */
    public static String processPicture(MultipartFile picture, UploadFileType fileType) throws IOException {
        String name = UUID.randomUUID().toString().replaceAll("-", "");
        BufferedImage image = ImageIO.read(picture.getInputStream());
        if (image == null || (fileType == UploadFileType.AVATAR && image.getWidth() != image.getHeight())) {
            throw new InvalidValueException();
        }

        // Compress and rename
        float scaleFactor = (float) fileType.getHeightLimit() / image.getHeight();
        float qualityFactor = picture.getSize() > fileType.getFileSizeLimit() ? (float) fileType.getFileSizeLimit() / picture.getSize() : 1f;
        Thumbnails.Builder<? extends InputStream> builder = Thumbnails.of(picture.getInputStream()).outputFormat("png").scale(scaleFactor).outputQuality(qualityFactor);

//        // Watermark for description pictures
//        if (fileType == FileType.DESC_PIC) {
//            BufferedImage waterMark = handleTextWaterMark(nickName);
//            builder = builder.watermark(Positions.BOTTOM_RIGHT, waterMark, 0.8f);
//        }

        File parentDir = new File(Values.FILE_UPLOAD_PATH, fileType.getRelFilePath());
        if (!parentDir.exists()) {
            if (!parentDir.mkdir()) {
                throw new IOException();
            }
        }

        builder.toFile(new File(parentDir, name));

        return name.concat(".png");
    }

    /**
     * Get the distance between two coordinates.
     *
     * @return the distance in the unit of meter
     */
    public static double getDistance(String latitude1, String longitude1, String latitude2, String longitude2) {
        double lat1 = Double.parseDouble(latitude1) * Math.PI / 180;
        double lng1 = Double.parseDouble(longitude1) * Math.PI / 180;
        double lat2 = Double.parseDouble(latitude2) * Math.PI / 180;
        double lng2 = Double.parseDouble(longitude2) * Math.PI / 180;
        double hSinX = Math.sin((lng1 - lng2) * 0.5);
        double hSinY = Math.sin((lat1 - lat2) * 0.5);
        double h = hSinY * hSinY + (Math.cos(lat1) * Math.cos(lat2) * hSinX * hSinX);
        return 2 * Math.atan2(Math.sqrt(h), Math.sqrt(1 - h)) * 6367000;
    }

}
