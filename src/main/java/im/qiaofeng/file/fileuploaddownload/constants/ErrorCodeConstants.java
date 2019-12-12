package im.qiaofeng.file.fileuploaddownload.constants;


import im.qiaofeng.file.fileuploaddownload.utils.CodeMsgDTO;
import java.util.HashMap;
import java.util.Map;

/**
 * 异常信息常量
 *
 * @author qiaofeng
 */
public final class ErrorCodeConstants {

    /**
     * 错误码对象集合
     */
    public static final Map<Long, CodeMsgDTO> codeDtoMap = new HashMap<Long, CodeMsgDTO>();


    /**
     * 未捕获异常
     */
    public static final CodeMsgDTO SYSTEM_ERROR = CodeMsgDTO.create(-1L, "系统异常");

    /**
     * 通用型异常
     */
    public static final CodeMsgDTO PARAM_ERROR = CodeMsgDTO.create(1000L, "参数错误");

    public static final CodeMsgDTO FILE_TRANSFER_EXCEPTION = CodeMsgDTO.create(4011L, "文件传输异常");
    public static final CodeMsgDTO INFO_IS_NOT_EXIST = CodeMsgDTO.create(4012L, "信息不存在");
    public static final CodeMsgDTO USER_IS_NOT_PERMISSION = CodeMsgDTO.create(4013L, "用户无权限");
    public static final CodeMsgDTO FILE_IS_NOT_EXIST = CodeMsgDTO.create(4014L, "文件不存在");
    public static final CodeMsgDTO FILE_DOWNLOAD_EXCEPTION = CodeMsgDTO.create(4015L, "文件下载异常");
    public static final CodeMsgDTO NOT_FILE_UPLOADER = CodeMsgDTO.create(4016L, "非文件上传者");

}
