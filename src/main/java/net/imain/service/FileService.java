package net.imain.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * @author: uncle
 * @apdateTime: 2017-11-21 17:11
 */
public interface FileService {
    /**
     * Upload file
     *
     * @param file
     * @param imgPath The file on the server path
     * @param localPath The file on the local path
     * @return file name
     */
    String upload(MultipartFile file, String imgPath, String localPath);
}
