package com.learning.springboot.provider;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.OSSObject;
import com.learning.springboot.exception.CustomizeErrorCode;
import com.learning.springboot.exception.CustomizeException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.io.InputStream;
import java.util.Date;

@Component
public class OSSProvider {

    @Value("${aliyun.oss.endpoint}")
    private String endpoint;

    @Value("${aliyun.accesskey}")
    private String accessKeyId;

    @Value("${aliyun.accesskey.secret}")
    private String accessKeySecret;

    @Value("${aliyun.oss.bucketname}")
    private String bucketName;

    public String upload(InputStream fileStream, String fileName){

        OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);

        String[] filePath = fileName.split("\\.");

        String generatedName;

        if(filePath.length > 1) {
            generatedName = System.currentTimeMillis() + "." + filePath[filePath.length-1];
        }else {
            return null;
        }

        try{

            ossClient.putObject(bucketName, generatedName, fileStream);

        } catch (OSSException oe) {
            oe.printStackTrace();
            throw new CustomizeException(CustomizeErrorCode.FILE_FAILED_UPLOAD);
        } catch (ClientException ce) {
            ce.printStackTrace();
            throw new CustomizeException(CustomizeErrorCode.FILE_FAILED_UPLOAD);
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomizeException(CustomizeErrorCode.FILE_FAILED_UPLOAD);
        } finally {
            ossClient.shutdown();
        }
        return generatedName;
    }

    public String getUrl(String generatedName){
        OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);

        try{
            Date expiration = new Date(new Date().getTime() + 3600*10000);
            String url = ossClient.generatePresignedUrl(bucketName, generatedName, expiration).toString();
            return url;
        } catch (OSSException oe) {
            oe.printStackTrace();
        } catch (ClientException ce) {
            ce.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ossClient.shutdown();
        }
        return null;
    }

}
