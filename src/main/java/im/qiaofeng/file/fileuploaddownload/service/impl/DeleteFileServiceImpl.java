package im.qiaofeng.file.fileuploaddownload.service.impl;

import im.qiaofeng.file.fileuploaddownload.service.DeleteFileService;
import im.qiaofeng.file.fileuploaddownload.utils.FileUtil;
import java.io.File;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 删除文件实现
 *
 * @author qiaofeng
 */
@Slf4j
@Service
public class DeleteFileServiceImpl implements DeleteFileService {

    @Override
    public void fileDelete(String url) {
        File file = FileUtil.getFilePath(url);
        if (file.exists()){
            if (file.delete()){
                log.error("文件删除失败");
            }
        }
    }
}