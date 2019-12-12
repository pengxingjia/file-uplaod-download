package im.qiaofeng.file.fileuploaddownload.exception;


import im.qiaofeng.file.fileuploaddownload.utils.CodeMsgDTO;

/**
 * 通用异常类
 *
 * @author hubilie
 */
public class UtilException extends BaseException {

    private CodeMsgDTO codeMsgDTO;

    public UtilException(CodeMsgDTO codeMsgDTO) {
        super(codeMsgDTO);
        this.codeMsgDTO = codeMsgDTO;
    }

    public UtilException(Throwable cause) {
        super(cause);
    }

    public UtilException(String message, Throwable cause) {
        super(message, cause);
    }

}