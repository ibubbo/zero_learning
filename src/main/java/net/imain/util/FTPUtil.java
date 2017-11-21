package net.imain.util;

import net.imain.common.Const;
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

    private static String ftpIp = PropertiesUtil.getProperties(Const.Ftp.FTP_IP_KEY);

    private static String ftpUser = PropertiesUtil.getProperties(Const.Ftp.FTP_USER_KEY);

    private static String ftpPass = PropertiesUtil.getProperties(Const.Ftp.FTP_PASS);

    private static String filePath = PropertiesUtil.getProperties(Const.Ftp.FTP_FILEPATH);

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
     * connection ftp
     */
    public static boolean uploadFile(List<File> fileList, String newPath) throws IOException {
        FTPUtil ftpUtil = new FTPUtil(ftpIp, Const.Ftp.PORT, ftpUser, ftpPass);
        // uploading
        boolean flag = ftpUtil.uploadFile(filePath, fileList, newPath);
        return flag;
    }

    /**
     * file upload
     *
     * @param remotePath
     * @param fileList
     * @return true is success, false is error.
     */
    private boolean uploadFile(String remotePath, List<File> fileList, String newPath) throws IOException {
        boolean success = true;
        FileInputStream fis = null;
        if (connectServer(ip, port, user, password)) { // 判断连接是否有效
            try {
                // Move to the FTP server directory 切换到ftp的文件路径上
                File fileDir = new File("/2020/20/20/");
                if (!fileDir.exists()) {
                    // give writable permissions
                    fileDir.setWritable(true);
                    fileDir.mkdirs();
                }
                ftpClient.changeWorkingDirectory(remotePath);
                // create directory
                // 设置缓冲区：1024 | 编码：utf-8 | 文件类型：二进制 | 打开本地的被动模式
                ftpClient.setBufferSize(1024);
                ftpClient.setControlEncoding("UTF-8");
                ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
                ftpClient.enterLocalActiveMode();
                // Formal upload
                for (File file : fileList) {
                    fis = new FileInputStream(file);
                    ftpClient.storeFile(file.getName(), fis);
                }
            } catch (IOException e) {
                success = false;
                logger.error("upload exception: {}", e);
            } finally {
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
