package im.qiaofeng.file.fileuploaddownload.model.po;

import java.io.Serializable;
import lombok.Data;

@Data
public class FileInfo implements Serializable {

    private Long gmtCreate;

    private Long gmtModify;

    /**
     * 后缀名
     */
    private String extName;

    /**
     * 文件id
     */
    private String fileId;

    /**
     * 文件长度
     */
    private Long fileSize;

    /**
     * 生产的uuid+后缀
     */
    private String name;

    /**
     * 整个文件的名字，例如：  表扬信.pdf
     */
    private String originalName;

    /**
     * 文件存储的绝对路径
     */
    private String path;

    /**
     * 文件的相对路径，例如：/static/508022f632b94112b5317372410f3cbd
     */
    private String url;

    private static final long serialVersionUID = 1L;

}