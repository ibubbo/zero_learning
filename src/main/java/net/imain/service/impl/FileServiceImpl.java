package net.imain.service.impl;

import com.google.common.collect.Lists;
import net.imain.service.FileService;
import net.imain.util.FTPUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * @author: uncle
 * @apdateTime: 2017-11-21 17:11
 */
@Service
public class FileServiceImpl implements FileService {

    private static Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);

    @Override
    public String upload(MultipartFile file, String path) {
        // 1.拼接文件名
        String fileName = file.getOriginalFilename();
        String suffix = fileName.substring(fileName.lastIndexOf("."));
        // rename file name after upload
        String uploadFileName = UUID.randomUUID() + suffix;
        logger.info("upload start file, file name upload: {}, file name after upload: {}, upload path: {}",
                fileName, uploadFileName, path);

        // 2.创建文件 path = /2017/11/21
//        File fileDir = new File(path);
//        // create directory
//        if (!fileDir.exists()) {
//            // give writable permissions
//            fileDir.setWritable(true);
//            fileDir.mkdirs();
//        }
        // create file: path + uploadFileName, 3.此时路径 + 文件已经准备就绪
        File targetFile = new File(path, uploadFileName);

        try {
            // copy file
            file.transferTo(targetFile);
            // TODO upload files to ftp server 4.上传
            FTPUtil.uploadFile(Lists.newArrayList(targetFile), path);
            // TODO When done, delete the files under the upload folder， 删除
            targetFile.delete();
        } catch (IOException e) {
            logger.error("file upload exception: {}", e);
            return null;
        }
        return targetFile.getName();
    }
}
