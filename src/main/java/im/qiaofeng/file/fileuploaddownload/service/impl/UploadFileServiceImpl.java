package im.qiaofeng.file.fileuploaddownload.service.impl;

import im.qiaofeng.file.fileuploaddownload.constants.ErrorCodeConstants;
import im.qiaofeng.file.fileuploaddownload.exception.FileException;
import im.qiaofeng.file.fileuploaddownload.model.dto.FileInfoDto;
import im.qiaofeng.file.fileuploaddownload.model.po.FileInfo;
import im.qiaofeng.file.fileuploaddownload.service.UploadFileService;
import im.qiaofeng.file.fileuploaddownload.utils.FileUtil;
import im.qiaofeng.file.fileuploaddownload.utils.UuidUtils;
import java.io.File;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件接口实现
 *
 * @author qiaofeng
 */
@Slf4j
@Service
public class UploadFileServiceImpl implements UploadFileService {

    @Override
    public FileInfoDto fileUpload(HttpServletRequest request, MultipartFile file) {
        String fileName = UuidUtils.getUuid();
        String originalName = file.getOriginalFilename();
        String extName = "";
        if (originalName != null && originalName.contains(".")) {
            extName = originalName.substring(originalName.lastIndexOf(".") + 1);
            fileName = fileName + "." + extName;
        }
        //构建空文件对象
        File newFile = FileUtil.getFilePath(fileName);

        //将文件写入到空文件对象中
        try {
            file.transferTo(newFile);
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new FileException(ErrorCodeConstants.FILE_TRANSFER_EXCEPTION);
        }
        //保存文件关联关系
        FileInfo fileInfo = initInfoFileRel(request, file, fileName, extName);
        return this.getFileInfoDto(fileInfo);
    }

    /**
     * 封装fileInfo对象
     */
    private FileInfoDto getFileInfoDto(FileInfo fileInfo) {
        FileInfoDto fileInfoDto = new FileInfoDto();
        fileInfoDto.setFileId(fileInfo.getFileId());
        fileInfoDto.setFileName(fileInfo.getOriginalName());
        fileInfoDto.setFileSize(fileInfo.getFileSize());
        fileInfoDto.setFileType(fileInfo.getExtName());
        fileInfoDto.setFileUrl(fileInfo.getUrl());
        fileInfoDto.setUploadTime(fileInfo.getGmtModify());
        return fileInfoDto;
    }

    /**
     * 封装TaskFileRel对象
     */
    private FileInfo initInfoFileRel(HttpServletRequest request, MultipartFile file, String fileName, String extName) {
        FileInfo fileInfo = new FileInfo();
        fileInfo.setFileId(UuidUtils.getUuid());
        fileInfo.setFileSize(file.getSize());
        fileInfo.setGmtCreate(System.currentTimeMillis());
        fileInfo.setGmtModify(System.currentTimeMillis());
        fileInfo.setExtName(extName);
        fileInfo.setName(fileName);
        fileInfo.setOriginalName(file.getOriginalFilename());
        fileInfo.setPath(FileUtil.getRealPath(request) + fileName);
        fileInfo.setUrl("/static/" + fileName);

        //保存文件信息
        //fileInfoMapper.insertSelective(infoFileRel);
        return fileInfo;
    }
}