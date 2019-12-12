package im.qiaofeng.file.fileuploaddownload.utils;

import im.qiaofeng.file.fileuploaddownload.constants.ErrorCodeConstants;
import im.qiaofeng.file.fileuploaddownload.exception.ParamException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.codec.binary.Base64;

/**
 * 字符串处理公共类
 *
 * @author qiaofeng
 */
@UtilityClass
@Slf4j
public class StringUtil {

    /**
     * 判断null和空字符串
     */
    public static boolean isNotEmpty(String str) {
        return !(str == null || "".equals(str));
    }

    public static boolean isEmpty(String str) {
        return !(str != null && !"".equals(str.trim()));
    }


    /**
     * 判断字符串是否相等
     */
    public static boolean equals(String str1, String str2) {
        return str1.equals(str2);
    }

    /**
     * 判断数组是否为空数组 为空返回true
     */
    public static Boolean isEmptyList(List list) {
        if (list != null) {
            if (!list.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断数组是否为空数组 为空返回true
     */
    public static Boolean isNotEmptyList(List list) {
        if (list != null) {
            if (!list.isEmpty()) {
                return true;
            }
        }
        return false;
    }

    //将数组转为集合
    public static <T> List<T> arrayToCollection(T[] array){
        List<T> list = new ArrayList<>();
        if (array != null && array.length >0) {
            list = Arrays.asList(array);
        }
        return list;
    }

    /**
     * 将字符串数组转为int数组
     * @param str 字符串数组 [1,2,3]
     * @return z
     */
    public static Integer[] stringToIn(String str){
        str = str.replace("[","").replace("]","");
        String[] strArr = str.split(","); //然后使用split方法将字符串拆解到字符串数组中
        Integer[] intArr = new Integer[strArr.length]; //定义一个长度与上述的字符串数组长度相通的整型数组
        if (StringUtil.isEmpty(str)){
            return new Integer[0];
        }
        for(int a=0;a<strArr.length;a++){
            intArr[a] = Integer.valueOf(strArr[a]); //然后遍历字符串数组，使用包装类Integer的valueOf方法将字符串转为整型
        }
        return intArr;
    }

    /**
     * 编码
     */
    public static String encodeStr(String plainText) {
        if (plainText == null){
            return "";
        }
        byte[] b = plainText.getBytes();
        Base64 base64 = new Base64();
        b = base64.encode(b);
        return new String(b);
    }

    /**
     * 解码
     */
    public static String decodeStr(String encodeStr) {
        if (encodeStr == null){
            return "";
        }
        byte[] b = encodeStr.getBytes();
        Base64 base64 = new Base64();
        b = base64.decode(b);
        return new String(b);
    }

    /**
     * 检查某参数是否为空
     * @param paramList 需要检查的参数
     */
    public static void checkParamIsNull(String... paramList) {
        for (String param : paramList){
            if (StringUtil.isEmpty(param)) {
                log.info("参数异常");
                throw new ParamException(ErrorCodeConstants.PARAM_ERROR);
            }
        }
    }

    /**
     * 检查某参数是否为空
     * @param paramList 需要检查的参数
     */
    public static void checkIntegerParamIsNull(Object... paramList) {
        for (Object param : paramList){
            if (param == null) {
                log.info("参数异常");
                throw new ParamException(ErrorCodeConstants.PARAM_ERROR);
            }
        }
    }


}
