package fun.lain.bilibiu;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.googleapis.media.MediaHttpUploader;
import com.google.api.client.googleapis.media.MediaHttpUploaderProgressListener;
import com.google.api.client.http.FileContent;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;


import javax.activation.FileTypeMap;
import java.io.*;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class GoogleTest {
    private static final String APPLICATION_NAME = "bili-biu";
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final String TOKENS_DIRECTORY_PATH = "tokens";

    /**
     * Global instance of the scopes required by this quickstart.
     * If modifying these scopes, delete your previously saved tokens/ folder.
     */
    private static final List<String> SCOPES = Collections.singletonList(DriveScopes.DRIVE);
    private static final String CREDENTIALS_FILE_PATH = "/bili-biu-lain.json";


    public static void main(String... args) throws IOException, GeneralSecurityException {
        // Build a new authorized API client service.
        InputStream in = GoogleTest.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
//        GoogleCredentials credential = ServiceAccountCredentials.fromStream(in);
//        credential = credential.createScoped(SCOPES);

        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        Drive service = new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, GoogleCredential.fromStream(in).createScoped(SCOPES))
                .setApplicationName(APPLICATION_NAME)
//                .setHttpRequestInitializer(request -> {
//                    request.setConnectTimeout(0);
//                    request.setReadTimeout(0);
////                    request.setIOExceptionHandler((request1, supportsRetry) -> {
////                        System.out.println(request.getUrl());
////                        return true;
////                    });
//                })
                .build();

        // Print the names and IDs for up to 10 files.
//        FileList result = service.files().list()
//                .setPageSize(10)
//                .setFields("nextPageToken, files(id, name)")
//                .execute();
        FileList result = service.files().list()
                .setQ("mimeType = 'application/vnd.google-apps.folder' and name='bili-back'")
//                .setPageSize(10)
//                .setFields("nextPageToken, files(id, name)")
                .execute();
        List<File> files = result.getFiles();
        if (files == null || files.isEmpty()) {
            System.out.println("No files found.");
        } else {
            System.out.println("Files:");
            for (File file : files) {
                System.out.printf("%s (%s)\n", file.getName(), file.getId());

            }
        }
        File file = new File();
        file.setName("Windows.iso");

        file.setParents(Arrays.asList("14SZbxWR0Y8jf8QhsT2thCKsq6cU9FRIC"));

        java.io.File filePath = new java.io.File("D:/Windows.iso");
        FileContent fileContent = new FileContent("",filePath);

        Drive.Files.Create str = service.files().create(file,fileContent).setFields("id");


        MediaHttpUploader uploader = str.getMediaHttpUploader();
        uploader.setProgressListener(new UploadListener());
        uploader.setDirectUploadEnabled(false);


        try {
            str.execute();
        }catch (Exception e){
            System.out.println("上传过程异常！开始重试："+e.getMessage());

        }
        System.out.println("FileId:"+file.getId());

        System.out.println(service.files().get("14SZbxWR0Y8jf8QhsT2thCKsq6cU9FRIC"));
    }


}
class UploadListener implements MediaHttpUploaderProgressListener {

    @Override
    public void progressChanged(MediaHttpUploader uploader) throws IOException {
        switch (uploader.getUploadState()) {
            case INITIATION_STARTED:
                System.out.println("Initiation Started");
                break;
            case INITIATION_COMPLETE:
                System.out.println("Initiation Completed");
                break;
            case MEDIA_IN_PROGRESS:
                System.out.println("Upload in progress");
                System.out.println(uploader.getChunkSize());
                System.out.println("Upload percentage: " + uploader.getProgress());
                break;
            case MEDIA_COMPLETE:
                System.out.println("Upload Completed!");
                break;
        }
    }
}
