package fun.lain.bilibiu.upload;

import com.google.api.services.drive.Drive;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.InputStream;
import java.util.List;

/**
 * 谷歌服务封装
 */
public interface GoogleDrive {


    /**
     * 获取谷歌云服务控制器
     * @return
     */
    Drive getDriveService();
    /**
     * 创建文件夹
     * @param folderName
     * @param parentId
     * @return 创建的文件夹Id
     */
    String createFolder(Drive driveService,String folderName,String parentId);

    /**
     * 通过路径创建文件夹
     * @param driveServie
     * @param paths
     * @return
     */
    String createFolder(Drive driveServie, List<String> paths);

}
