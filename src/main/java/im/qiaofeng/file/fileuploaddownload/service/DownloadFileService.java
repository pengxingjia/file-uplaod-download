package im.qiaofeng.file.fileuploaddownload.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 下载文件接口
 *
 * @author qiaofeng
 */
public interface DownloadFileService {

    /**
     * 文件下载--支持分片下载
     * @param fid 文件id
     * @param request 请求对象
     * @param response 响应对象
     */
    void download(String fid, HttpServletRequest request, HttpServletResponse response);
}