package com.ohgiraffers.unicorn.report.controller;

import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.google.cloud.storage.BlobInfo;
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

//    @Autowired
//    private AmazonS3Client amazonS3Client;
//
//    @Value("${cloud.aws.s3.bucket}")
//    private String bucket;

    private final Storage storage = StorageOptions.getDefaultInstance().getService();

    @PostMapping("/wordcloud")
    public ResponseEntity<String> handleWordcloudImageUpload(
            @RequestParam("meetingId") Long meetingId,
            @RequestParam("wordcloudFile") MultipartFile file) {

        try {
            String firebaseUrl = uploadFileToFirebase(meetingId, file);
            return ResponseEntity.ok(firebaseUrl);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to upload image to Firebase: " + e.getMessage());
        }
    }

//    @PostMapping("/wordcloud")
//    public ResponseEntity<String> handleWordcloudImageUpload(
//            @RequestParam("meetingId") Long meetingId,
//            @RequestParam("wordcloudFile") MultipartFile file
//            ) {
//
//        try {
//            // Upload the file to S3 and return the S3 URL
//            String s3Url = uploadFileToFirebase(meetingId, file);
//            return ResponseEntity.ok(s3Url);
//        } catch (IOException e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body("Failed to upload image to S3: " + e.getMessage());
//        }
//    }
//
//    private String uploadFileToS3(Long meetingId, MultipartFile file) throws IOException {
//        String folderName = "meeting/wordclouds/" + meetingId;
//        String fileName = folderName + "/" + file.getOriginalFilename();
//        String fileUrl = "https://" + bucket + ".s3.ap-northeast-2.amazonaws.com/" + fileName;
//
//        // Set metadata
//        ObjectMetadata metadata = new ObjectMetadata();
//        metadata.setContentType(file.getContentType());
//        metadata.setContentLength(file.getSize());
//
//        // Upload file to S3
//        amazonS3Client.putObject(bucket, fileName, file.getInputStream(), metadata);
//
//        return fileUrl;
//    }

    private String uploadFileToFirebase(Long meetingId, MultipartFile file) throws IOException {
        String folderName = "report/" + meetingId + "/";
        String fileName = folderName + file.getOriginalFilename();

        // 업로드할 파일의 메타데이터 정의
        BlobInfo blobInfo = BlobInfo.newBuilder("unicorn-4b430.firebasestorage.app", fileName)
                .setContentType(file.getContentType())
                .build();

        // Firebase 스토리지에 파일 업로드
        storage.create(blobInfo, file.getBytes());

        System.out.println("File uploaded to Firebase: " + fileName);

        // Firebase URL 반환
        return "https://firebasestorage.googleapis.com/v0/b/unicorn-4b430.firebasestorage.app/o/"
                + fileName.replace("/", "%2F")
                + "?alt=media";
    }

}
