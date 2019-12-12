package im.qiaofeng.file.fileuploaddownload.exception;

import im.qiaofeng.file.fileuploaddownload.utils.CodeMsgDTO;
import lombok.Data;

/**
 * 自定义异常处理类
 *
 * @author qiaofeng
 */
@Data
public class BaseException extends RuntimeException {

    private CodeMsgDTO codeMsgDTO;

    BaseException(CodeMsgDTO codeMsgDTO) {
        super(codeMsgDTO.getErr());
        this.codeMsgDTO = codeMsgDTO;
    }

    BaseException(Throwable cause) {
        super(cause);
    }

    BaseException(String message, Throwable cause) {
        super(message, cause);
    }


}