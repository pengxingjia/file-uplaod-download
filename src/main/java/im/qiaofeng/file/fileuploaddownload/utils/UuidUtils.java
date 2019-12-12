package im.qiaofeng.file.fileuploaddownload.utils;

import java.util.UUID;

/**
 * uuid
 *
 * @author qiaofeng
 */
public class UuidUtils {

    public UuidUtils() {
    }

    /** @deprecated */
    public static String getUUID() {
        return getUpperUUID();
    }

    public static String getUpperUUID() {
        String uuid = UUID.randomUUID().toString().toUpperCase();
        return uuid.replaceAll("-", "");
    }

    public static String getLowerUUID() {
        String uuid = UUID.randomUUID().toString().toLowerCase();
        return uuid.replaceAll("-", "");
    }


    /**
     * 获取32位随机字符
     */
    public static String getUuid() {
        return getLowerUUID().substring(0, 32);
    }

}