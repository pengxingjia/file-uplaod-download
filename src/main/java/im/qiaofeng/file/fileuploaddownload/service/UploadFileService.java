package im.qiaofeng.file.fileuploaddownload.service;

import im.qiaofeng.file.fileuploaddownload.model.dto.FileInfoDto;
import javax.servlet.http.HttpServletRequest;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件接口
 * @author qiaofeng
 */
public interface UploadFileService {

    FileInfoDto fileUpload(HttpServletRequest request, MultipartFile file);
}