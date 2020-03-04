package fun.lain.bilibiu.upload;

import com.google.api.services.drive.Drive;


/**
 * 谷歌服务封装
 */
public interface GoogleDrive {
    /**
     * 获取谷歌云服务
     * @return
     */
    Drive getDriveService();

    String createFolder(Drive driveService,String folderName,String parent);



}
