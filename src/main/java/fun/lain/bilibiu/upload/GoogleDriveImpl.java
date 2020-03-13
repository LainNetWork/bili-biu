package fun.lain.bilibiu.upload;

import com.google.api.services.drive.Drive;

import java.util.List;

public class GoogleDriveImpl implements GoogleDrive{
    @Override
    public Drive getDriveService() {

        return null;
    }

    @Override
    public String createFolder(Drive driveService, String folderName, String parentId) {
        return null;
    }

    @Override
    public String createFolder(Drive driveServie, List<String> paths) {
        return null;
    }
}
