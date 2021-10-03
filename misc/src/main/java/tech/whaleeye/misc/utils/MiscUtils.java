package tech.whaleeye.misc.utils;

import net.coobird.thumbnailator.Thumbnails;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.util.ByteSource;
import org.springframework.web.multipart.MultipartFile;
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
    private final static String FILE_UPLOAD_PATH = System.getenv("SUSTechStoreUpload");


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


    /**
     * Compress and rename one picture.
     *
     * @param picture  the picture that needs to be processed
     * @param fileType specifies the type of this picture, such as avatar, good description pictures etc.
     * @return the filename of the processed picture
     */
    public static String processPicture(MultipartFile picture, FileType fileType) throws IOException {
        String name = UUID.randomUUID().toString().replaceAll("-", "");
        BufferedImage image = ImageIO.read(picture.getInputStream());
        if (image == null || (fileType == FileType.AVATAR && image.getWidth() != image.getHeight())) {
            throw new InvalidValueException();
        }

        // Compress and rename
        float scaleFactor = (float) fileType.heightLimit / image.getHeight();
        float qualityFactor = picture.getSize() > fileType.fileSizeLimit ? (float) fileType.fileSizeLimit / picture.getSize() : 1f;
        Thumbnails.Builder<? extends InputStream> builder = Thumbnails.of(picture.getInputStream()).outputFormat("png").scale(scaleFactor).outputQuality(qualityFactor);

//        // Watermark for description pictures
//        if (fileType == FileType.DESC_PIC) {
//            BufferedImage waterMark = handleTextWaterMark(nickName);
//            builder = builder.watermark(Positions.BOTTOM_RIGHT, waterMark, 0.8f);
//        }

        File parentDir = new File(FILE_UPLOAD_PATH, fileType.relFilePath);
        if (!parentDir.exists()) {
            if (!parentDir.mkdir()) {
                throw new IOException();
            }
        }

        builder.toFile(new File(parentDir, name));

        return name.concat(".").concat("png");
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

    public enum FileType {
        AVATAR(200 * 1024, 400, "avatar"), DESC_PIC(2 * 1024 * 1024, 1080, "desc");
        // Max file size (Bytes)
        long fileSizeLimit;
        long heightLimit;
        String relFilePath;

        FileType(long fileSizeLimit, long heightLimit, String relFilePath) {
            this.fileSizeLimit = fileSizeLimit;
            this.heightLimit = heightLimit;
            this.relFilePath = relFilePath;
        }
    }
}
