package net.imain.util;

import net.imain.common.Constants;
import net.imain.common.HandlerCheck;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

/**
 * ftp server
 *
 * @author: uncle
 * @apdateTime: 2017-11-21 17:42
 */
public class FTPUtil {

    private static Logger logger = LoggerFactory.getLogger(FTPUtil.class);

    private static String ftpIp = PropertiesUtil.getProperties(Constants.Ftp.FTP_IP_KEY);

    private static String ftpUser = PropertiesUtil.getProperties(Constants.Ftp.FTP_USER_KEY);

    private static String ftpPass = PropertiesUtil.getProperties(Constants.Ftp.FTP_PASS);

    private static String filePath = PropertiesUtil.getProperties(Constants.Ftp.FTP_FILEPATH);

    private String ip;

    private Integer port;

    private String password;

    private String user;

    private FTPClient ftpClient;

    public FTPUtil(String ip, Integer port, String password, String user) {
        this.ip = ip;
        this.port = port;
        this.password = password;
        this.user = user;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public FTPClient getFtpClient() {
        return ftpClient;
    }

    public void setFtpClient(FTPClient ftpClient) {
        this.ftpClient = ftpClient;
    }

    /**
     * connect ftp
     *
     * @param fileList Files that need to be upload
     * @param imgPath File storage path
     * @return true is success, false is error
     * @throws IOException
     */
    public static boolean uploadFile(List<File> fileList, String imgPath) throws IOException {
        FTPUtil ftpUtil = new FTPUtil(ftpIp, Constants.Ftp.PORT, ftpUser, ftpPass);
        // uploading
        return ftpUtil.uploadFile(filePath, fileList, imgPath);
    }

    /**
     * file upload
     *
     * @param remotePath
     * @param fileList
     * @return true is success, false is error.
     */
    private boolean uploadFile(String remotePath, List<File> fileList, String imgPath) throws IOException {
        boolean success = true;
        FileInputStream fis = null;
        if (connectServer(ip, port, user, password)) {
            try {
                // Move to the FTP server directory
                if (!ftpClient.changeWorkingDirectory(remotePath + imgPath)) {
                    String[] path = imgPath.split("/");
                    for (String dir : path) {
                        if (StringUtils.isNotBlank(dir)) {
                            remotePath += "/" + dir;
                            if (!ftpClient.changeWorkingDirectory(remotePath)) {
                                if (!ftpClient.makeDirectory(remotePath)) {
                                    success = false;
                                    logger.error("changeWorkingDirectory exception");
                                    return success;
                                }
                                ftpClient.changeWorkingDirectory(remotePath);
                            }
                        }
                    }
                }
                // create directory
                // ↓ set buffer：1024 | encode：utf-8 | file type：binary | open the local passive mode
                ftpClient.setBufferSize(1024);
                ftpClient.setControlEncoding("UTF-8");
                ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
                ftpClient.enterLocalActiveMode();
                // ↑ set end...
                // Formal upload
                for (File file : fileList) {
                    fis = new FileInputStream(file);
                    ftpClient.storeFile(file.getName(), fis);
                }
            } catch (IOException e) {
                success = false;
                logger.error("upload exception: {}", e);
            } finally {
                if (HandlerCheck.ObjectIsEmpty(fis))
                    fis.close();
                ftpClient.disconnect();
            }
        }
        return success;
    }

    /**
     * Determine if can connect
     *
     * @param ip
     * @param user
     * @param pass
     * @return true is success, false is error.
     */
    private boolean connectServer(String ip, Integer port, String user, String pass) {
        boolean isSuccess = true;
        ftpClient = new FTPClient();
        try {
            ftpClient.connect(ip, port);
            isSuccess = ftpClient.login(user, pass);
        } catch (IOException e) {
            logger.error("connect exception: {}", e);
        }
        return isSuccess;
    }
}
