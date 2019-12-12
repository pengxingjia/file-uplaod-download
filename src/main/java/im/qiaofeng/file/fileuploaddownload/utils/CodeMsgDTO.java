package im.qiaofeng.file.fileuploaddownload.utils;

import im.qiaofeng.file.fileuploaddownload.constants.ErrorCodeConstants;
import java.io.Serializable;
import lombok.Data;

/**
 * 错误码对象
 *
 * @author qiaofeng
 */
@Data
public class CodeMsgDTO implements Serializable {

    private Long code;
    private String err;

    public CodeMsgDTO() {
    }

    public CodeMsgDTO(Long code, String err) {
        this.code = code;
        this.err = err;
    }

    public static CodeMsgDTO create(Long code, String err) {
        CodeMsgDTO codeMsgDTO = new CodeMsgDTO(code, err);
        ErrorCodeConstants.codeDtoMap.put(code, codeMsgDTO);
        return codeMsgDTO;
    }


}