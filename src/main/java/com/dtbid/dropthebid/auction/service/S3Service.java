package com.dtbid.dropthebid.auction.service;

import java.io.File;
import java.io.IOException;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class S3Service {

  private final AmazonS3 s3Client;
  
  @Value("${aws.bucketname}")
  private String bucketName;
  
  public String uploadImage(MultipartFile file) throws IOException {
    String originalFileName = file.getOriginalFilename();
    
    String fileExtension = "";
    String savedFileName = "";
    if (originalFileName != null && originalFileName.contains(".")) {
      fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
    }
    UUID uuid = UUID.randomUUID();
    savedFileName = uuid.toString() + fileExtension;
    
    String uploadPath = "auction/";
    //파일경로: 업로드폴더 + uuid.확장자
    String filePath = uploadPath + savedFileName;
    
    ObjectMetadata metadata = new ObjectMetadata();
    metadata.setContentLength(file.getSize());
    metadata.setContentType(file.getContentType());
    
    s3Client.putObject(new PutObjectRequest(bucketName, filePath, file.getInputStream(), metadata));
    
    return s3Client.getUrl(bucketName, filePath).toString();
}
  public void deleteImage(String fileName) {
    s3Client.deleteObject(new DeleteObjectRequest(bucketName, fileName));
}

}
