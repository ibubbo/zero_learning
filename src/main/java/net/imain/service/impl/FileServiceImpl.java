package net.imain.service.impl;

import com.google.common.collect.Lists;
import net.imain.service.FileService;
import net.imain.util.FTPUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * @author: uncle
 * @apdateTime: 2017-11-21 17:11
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class FileServiceImpl implements FileService {

    private static Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);

    @Override
    public String upload(MultipartFile file, String imgPath, String localPath) {
        // 1.Splicing file name
        String fileName = file.getOriginalFilename();
        String suffix = fileName.substring(fileName.lastIndexOf("."));
        // rename file name after upload
        String uploadFileName = UUID.randomUUID() + suffix;
        logger.info("upload start file, file name upload: {}, file name after upload: {}, upload path: {}",
                fileName, uploadFileName, localPath);

        // 2.create local folder
        File fileDir = new File(localPath);
        if (!fileDir.exists()) {
            // give writable permissions
            fileDir.setWritable(true);
            fileDir.mkdirs();
        }
        // 3.create local file: path + uploadFileName
        File targetFile = new File(localPath, uploadFileName);
        try {
            // copy file, file -> targetFile(file shadow)
            file.transferTo(targetFile);
            FTPUtil.uploadFile(Lists.newArrayList(targetFile), imgPath);
            // remove local file
            targetFile.delete();
        } catch (IOException e) {
            logger.error("file upload exception: {}", e);
            return null;
        }
        return targetFile.getName();
    }
}
