package fun.lain.bilibiu;

import fun.lain.bilibiu.collection.service.SaveService;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.client.RestTemplate;
import sun.text.resources.iw.FormatData_iw_IL;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;

@SpringBootTest
public class SaveServiceTest {
    @Autowired
    RestTemplate restTemplate;
    @Value("${lain.user-agent}")
    String userAgent;

    String url = "http://upos-hz-mirrorhw.acgvideo.com/upgcxcode/26/10/128991026/128991026_nb2-1-64.flv?e=ig8euxZM2rNcNbU37WdVhoMBnwUVhwdEto8g5X10ugNcXBlqNxHxNEVE5XREto8KqJZHUa6m5J0SqE85tZvEuENvNo8g2ENvNo8i8o859r1qXg8xNEVE5XREto8GuFGv2U7SuxI72X6fTr859r1qXg8gNEVE5XREto8z5JZC2X2gkX5L5F1eTX1jkXlsTXHeux_f2o859IB_&uipk=5&nbs=1&deadline=1573929527&gen=playurl&os=hw&oi=3072798049&trid=346eb76f41b64147bb5c409f719ca762u&platform=pc&upsig=8ee3065a0fc7e7d0e277ac3997c66cd6&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,platform&mid=1631252";
    @Test
    public void test0(){
        System.out.println(new Date(1573906200));
    }
    @Test
    public void test(){

        try (FileOutputStream outputStream = new FileOutputStream("D:/output.flv")){
            URL downUrl = new URL(url);

            URLConnection connection = downUrl.openConnection();

            connection.setRequestProperty(HttpHeaders.REFERER,"https://www.bilibili.com/video/av75408133");
            connection.setRequestProperty(HttpHeaders.RANGE,"bytes=0-");

            InputStream inputStream = connection.getInputStream();
            byte[] buffer = new byte[1024*1024*4];//
            while (true){
                int len =inputStream.read(buffer);
                if(len == -1){
                    break;
                }
                outputStream.write(buffer,0,len);
            }
            outputStream.write(buffer);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
//        System.setProperty("http.proxyHost", "127.0.0.1");
//        System.setProperty("https.proxyHost", "127.0.0.1");
//        System.setProperty("http.proxyPort", "8888");
//        System.setProperty("https.proxyPort", "8888");
//        //-DproxySet=true -DproxyHost=127.0.0.1 -DproxyPort=8888 -Djavax.net.ssl.trustStore=C:\Program Files\Java\jdk1.8.0_231\bin\FiddlerRoot.cer -Djavax.net.ssl.trustStorePassword=Lain2434
//
//        HttpHeaders httpHeaders = new HttpHeaders();
//        int i = 1024*4;
//        httpHeaders.set(HttpHeaders.RANGE,"bytes=0-");
//        httpHeaders.set(HttpHeaders.USER_AGENT,userAgent);
//        httpHeaders.set(HttpHeaders.ORIGIN,"https://www.bilibili.com");
//        httpHeaders.set(HttpHeaders.REFERER,"https://www.bilibili.com/video/av75408133");
//        httpHeaders.set(HttpHeaders.HOST,"cn-jlcc3-cu-v-05.acgvideo.com");
//        httpHeaders.set("Sec-Fetch-Mode","cors");
//        httpHeaders.set("Sec-Fetch-Site","cross-site");
//        httpHeaders.set(HttpHeaders.ACCEPT,"*/*");
//        httpHeaders.set(HttpHeaders.ACCEPT_RANGES,"gzip, deflate, br");
//
//        httpHeaders.set("Access-Control-Request-Headers","range");
//
//        HttpEntity entity = new HttpEntity(null,httpHeaders);
//        ResponseEntity<byte[]> entity1 = restTemplate.exchange(url, HttpMethod.OPTIONS,entity,byte[].class);
//        ;
//        try {
//            FileUtils.writeByteArrayToFile(new File("D:/1.flv"),entity1.getBody());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }
}
