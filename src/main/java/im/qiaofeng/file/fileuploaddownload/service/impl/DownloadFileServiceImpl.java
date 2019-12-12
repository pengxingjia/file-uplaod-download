package im.qiaofeng.file.fileuploaddownload.service.impl;

import im.qiaofeng.file.fileuploaddownload.constants.ErrorCodeConstants;
import im.qiaofeng.file.fileuploaddownload.exception.FileException;
import im.qiaofeng.file.fileuploaddownload.service.DownloadFileService;
import im.qiaofeng.file.fileuploaddownload.utils.FileUtil;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 下载文件实现类
 *
 * @author qiaofeng
 */
@Slf4j
@Service
public class DownloadFileServiceImpl implements DownloadFileService {

    //音视频格式，不区分大小写
    private static List<String> extNameVideoList;
    private static List<String> extNameAudioList;

    private static long fileMaxSize = 2147483648L;

    private void initExtNameMap(){
        if (extNameVideoList == null) {
            extNameVideoList = new ArrayList<>();
            extNameVideoList.add("mp4");
            extNameVideoList.add("mov");
        }
        if (extNameAudioList == null){
            extNameAudioList = new ArrayList<>();
            extNameAudioList.add("mp3");
        }
    }

    @Override
    public void download(String fid, HttpServletRequest request, HttpServletResponse response) {
        //根据fid查询出文件信息，文件信息包含的文件地址、文件名、文件后缀
        String url = "";
        String originalName = "";
        String extName = "";
        //构建文件对象
        File file = FileUtil.getFilePath(url);
        try {
            sendVideo(request, response, file, originalName, extName);
        } catch (IOException e) {
            log.error("文件下载失败");
            throw new FileException(ErrorCodeConstants.FILE_DOWNLOAD_EXCEPTION);
        }
    }

    //苹果代表分片下载，其他代表一次性下载文件
    public void sendVideo(HttpServletRequest request, HttpServletResponse response, File file, String originalName, String extName) throws IOException {
        //判断后缀是否是音频/视频类
        checkExtNameSetHealer(response, extName);
        response.addHeader("Content-Disposition", "attachment;filename=\"" + new String(originalName.getBytes(StandardCharsets.UTF_8), "iso8859-1") + "\"");
        response.setHeader("ETag", originalName);
        response.setHeader("Last-Modified", new Date().toString());
        response.setHeader("Accept-Ranges", "bytes");

        //设置文件为只读模式
        RandomAccessFile randomFile = new RandomAccessFile(file, "r");
        long contentLength = randomFile.length();
        if (contentLength > fileMaxSize){
            log.error("文件太大，最大支持2g");
            throw new RuntimeException("文件太大");
        }
        response.setHeader("Content-length", "" + contentLength);

        //分片下载时的请求头Range
        String range = request.getHeader("Range");
        try {
            if (range != null && range.startsWith("bytes=")) {
                //分段读取文件流
                pieceDownFileInfo(range, response, randomFile);
            } else {
                //一次性读取所有文件流
                readAllAtOnce(response, randomFile);
            }
        }catch (IOException io){
            io.getStackTrace();
        }
    }

    /**
     * 判断后缀是否是音频/视频类设置响应的响应头
     *
     * @param extName 后缀名
     */
    private void checkExtNameSetHealer(HttpServletResponse response, String extName) {
        initExtNameMap();
        String lowExtUser = extName.toLowerCase();

        if (extNameVideoList.contains(lowExtUser)){
            response.setContentType("video/mp4");
            response.setHeader("Content-Type", "video/mp4");
        }else if (extNameAudioList.contains(lowExtUser)){
            response.setContentType("audio/mp3");
            response.setHeader("Content-Type", "audio/mp3");
        }
    }


    /**
     * 一次性输出所有文件流
     *
     * @param response 响应对象
     * @param randomFile 只读文件对象
     */
    private void readAllAtOnce(HttpServletResponse response, RandomAccessFile randomFile) throws IOException {
        ServletOutputStream out = response.getOutputStream();

        //缓存字节数组
        byte[] buffer = new byte[1024];
        int len = randomFile.read(buffer);
        while (len > 0) {
            out.write(buffer, 0, len);
            len = randomFile.read(buffer);
            out.flush();
        }
        randomFile.close();
        out.close();
    }

    /**
     * 分片下载--bytes=0-一次性获取-》兼容
     *
     * @param range 表示每次需要获取的字节数
     * @param response http响应对象
     * @param randomFile 只读文件对象
     */
    private void pieceDownFileInfo(String range, HttpServletResponse response, RandomAccessFile randomFile) throws IOException {
        long contentLength = randomFile.length();

        int start = 0;
        int end = 0;
        //是分片下载 safari浏览器（后面都叫做苹果）预请求传的是bytes=0-1， 其他（android，后面都叫做其他）格式为bytes=0-
        String[] values = range.split("=")[1].split("-");
        start = Integer.parseInt(values[0]);
        if (values.length > 1) {
            //分片
            end = Integer.parseInt(values[1]);
        } else {
            end = Math.toIntExact(contentLength);
        }
        int requestSizeNum = end - start + 1;

        //设置分片下载的请求头
        responseSetHeaderContent(range, contentLength, response);
        //输出分片的文件流
        outFileStream(start, requestSizeNum, randomFile, response);
    }

    /**
     * 设置分片下载的请求头
     *
     * @param range range请求头信息
     * @param contentLength 文件的总字节数
     * @param response 响应数据对象
     */
    private void responseSetHeaderContent(String range, long contentLength, HttpServletResponse response) {
        //第一次请求只返回content length来让客户端请求多次实际数据，其他没有Range请求头的设置下面这种，一次性获取所有文件流
        //苹果以后的多次以断点续传的方式来返回视频数据，分段第一次预请求状态码是200，后面分段下载状态码是206
        //后面的多次请求Range: bytes=0-1364341  表示这一段获取的字节数
        response.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT);//206

        long requestStart = 0;
        long requestEnd = 0;
        String[] ranges = range.split("=");
        if (ranges.length > 1) {
            String[] rangeDatas = ranges[1].split("-");
            requestStart = Integer.parseInt(rangeDatas[0]);
            if (rangeDatas.length > 1) {
                requestEnd = Integer.parseInt(rangeDatas[1]);
            }
        }
        long length;
        if (requestEnd > 0) {
            //分片
            length = requestEnd - requestStart + 1;
            response.setHeader("Content-length", "" + length);
            response.setHeader("Content-Range", "bytes " + requestStart + "-" + requestEnd + "/" + contentLength);
        } else {
            //一次性返回完
            length = contentLength - requestStart;
            response.setHeader("Content-length", "" + length);
            response.setHeader("Content-Range", "bytes " + requestStart + "-" + (contentLength - 1) + "/" + contentLength);
        }
    }

    /**
     * 输出分片的文件流
     *
     * @param start 开始字节位置
     * @param needSize 本次请求的数量
     * @param randomFile 只读文件对象
     * @param response 响应对象
     */
    private void outFileStream(int start, int needSize, RandomAccessFile randomFile, HttpServletResponse response) throws IOException {
        ServletOutputStream out = response.getOutputStream();
        //缓存字节数组
        byte[] buffer = new byte[1024];
        //设置读取文件流的起始位置
        randomFile.seek(start);

        while (needSize > 0) {
            //这里的len是实际读取到的字节数，有可能长度和buffer一样长，有可能只有一些
            int len = randomFile.read(buffer);
            if (needSize < buffer.length) {
                out.write(buffer, 0, needSize);
            } else {
                out.write(buffer, 0, len);
                if (len < buffer.length) {
                    break;
                }
            }
            out.flush();
            needSize -= buffer.length;
        }
        randomFile.close();
        out.close();
    }


}