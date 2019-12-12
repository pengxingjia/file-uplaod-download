package im.qiaofeng.file.fileuploaddownload.exception;


import im.qiaofeng.file.fileuploaddownload.utils.CodeMsgDTO;

/**
 * 参数异常类
 *
 * @author qiaofeng
 */
public class ParamException extends BaseException {
    private CodeMsgDTO codeMsgDTO;

    public CodeMsgDTO getCodeMsgDTO() {
        return codeMsgDTO;
    }

    public ParamException(CodeMsgDTO codeMsgDTO){
        super(codeMsgDTO);
        this.codeMsgDTO = codeMsgDTO;
    }
}