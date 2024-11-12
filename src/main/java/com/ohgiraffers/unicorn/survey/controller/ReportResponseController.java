package com.ohgiraffers.unicorn.survey.controller;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/report/whole")
public class ReportResponseController {

    @Autowired
    private AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @PostMapping("/wordcloud")
    public ResponseEntity<String> handleWordcloudImageUpload(
            @RequestParam("wordcloudFile") MultipartFile file,
            @RequestParam("meetingId") String meetingId) {
        try {
            // Upload the file to S3 and return the S3 URL
            String s3Url = uploadFileToS3(file, meetingId);
            return ResponseEntity.ok(s3Url);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to upload image to S3: " + e.getMessage());
        }
    }

    private String uploadFileToS3(MultipartFile file, String meetingId) throws IOException {
        String folderName = "meeting/wordclouds/" + meetingId;
        String fileName = folderName + "/" + file.getOriginalFilename();
        String fileUrl = "https://" + bucket + ".s3.ap-northeast-2.amazonaws.com/" + fileName;

        // Set metadata
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(file.getContentType());
        metadata.setContentLength(file.getSize());

        // Upload file to S3
        amazonS3Client.putObject(bucket, fileName, file.getInputStream(), metadata);

        return fileUrl;
    }

}
