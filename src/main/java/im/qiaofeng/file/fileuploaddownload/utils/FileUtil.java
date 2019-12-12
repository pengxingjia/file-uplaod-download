package im.qiaofeng.file.fileuploaddownload.utils;

import im.qiaofeng.file.fileuploaddownload.constants.ErrorCodeConstants;
import im.qiaofeng.file.fileuploaddownload.exception.FileException;
import java.io.File;
import java.io.FileNotFoundException;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ResourceUtils;

/**
 * 路径相关工具类
 * @author qiaofeng
 */
@Slf4j
public class FileUtil {


    /**
     * 获取静态文件物理地址
     */
    public static String getRealPath(HttpServletRequest request) {
        String realPath = request.getSession().getServletContext().getRealPath("/") + "/static/";
        realPath = realPath.replaceAll("\\\\", "/");
        return realPath;
    }

    /**
     * 获取static下的文件对象
     * @param fileUrl 文件的相对地址
     * @return 文件对象
     */
    public static File getFilePath(String fileUrl){
        try {
            return ResourceUtils.getFile(fileUrl);
        } catch (FileNotFoundException e) {
            log.error("构建文件对象失败");
            throw new FileException(ErrorCodeConstants.FILE_TRANSFER_EXCEPTION);
        }
    }
}