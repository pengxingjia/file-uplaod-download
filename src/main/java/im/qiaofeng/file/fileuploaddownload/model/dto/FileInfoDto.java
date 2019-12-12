package im.qiaofeng.file.fileuploaddownload.model.dto;

import lombok.Data;

/**
 * 文件传输类
 *
 * @author hubilie
 */
@Data
public class FileInfoDto {

    private String fileId;
    private String fileName;
    private Long fileSize;
    private String fileUrl;
    private String fileType;
    private String uploaderId;
    private String userName;//上传者姓名
    private Long uploadTime;//上传时间

}
