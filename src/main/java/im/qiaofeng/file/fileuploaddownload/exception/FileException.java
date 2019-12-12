package im.qiaofeng.file.fileuploaddownload.exception;


import im.qiaofeng.file.fileuploaddownload.utils.CodeMsgDTO;

/**
 * 外部公共接口异常类
 *
 * @author hubilie
 */
public class FileException extends BaseException {

    private CodeMsgDTO codeMsgDTO;

    public FileException(CodeMsgDTO codeMsgDTO) {
        super(codeMsgDTO);
        this.codeMsgDTO = codeMsgDTO;
    }

    public FileException(Throwable cause) {
        super(cause);
    }

    public FileException(String message, Throwable cause) {
        super(message, cause);
    }

}