package org.spring.backendspring.s3;

import java.io.IOException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AwsS3Service {
    
    // AWS S3 SDK v1의 클라이언트 객체
    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;
    private String dir = "/test";
    private String defaultUrl = "https://버켓.s3.ap-northeast-2.amazonaws.com/";

    // S3 파일 업로드
    public String uploadFile(MultipartFile file) throws IOException {
        
        String bucketDir = bucketName + dir;
        String dirUrl = defaultUrl + dir + "/";
        String fileName = generateFileName(file);

        System.out.println(dirUrl + " 파일 dirUrl");
        System.out.println(fileName + "파일 이름");

        amazonS3Client.putObject(bucketDir, fileName, file.getInputStream(), getObjectMetadata(file));
        return dirUrl + fileName;
    }

    // 파일 이름 생성
    private String generateFileName(MultipartFile file) {
        return UUID.randomUUID().toString() + "-" + file.getOriginalFilename();
    }

    // s3 파일 삭제
    public void deleteFile(String fileUrl) {
        String fileName = fileUrl.substring(fileUrl.indexOf(".com/") + 6);
        amazonS3Client.deleteObject(new DeleteObjectRequest(bucketName, fileName));
        System.out.println(bucketName);
    }

    // 업로드 시 파일의 MIME 타입, 크기 정보를 함께 전달
    private ObjectMetadata getObjectMetadata(MultipartFile file) {
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(file.getContentType());
        objectMetadata.setContentLength(file.getSize());
        return objectMetadata;
    }
}
