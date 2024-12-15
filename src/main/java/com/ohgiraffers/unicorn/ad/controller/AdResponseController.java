package com.ohgiraffers.unicorn.ad.controller;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.ohgiraffers.unicorn.ad.repository.AdRepository;
import com.ohgiraffers.unicorn.ad.service.AdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/ad/generated")
public class AdResponseController {

//    @Autowired
//    private AmazonS3Client amazonS3Client;
//
//    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Autowired
    private AdService adService;

    private final Storage storage = StorageOptions.getDefaultInstance().getService();

    @PostMapping("/preview")
    public ResponseEntity<String> handleAdImageUpload(
            @RequestParam("adId") Long adId,
            @RequestParam("previewImage") MultipartFile previewImage) {
        try {
            String firebaseUrl = uploadFileToFirebase(adId, previewImage, "preview");
            adService.updatePreviewUrl(adId, firebaseUrl);
            return ResponseEntity.ok(firebaseUrl);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to upload image to Firebase: " + e.getMessage());
        }
    }

    @PostMapping("/video")
    public ResponseEntity<String> handleAdVideoUpload(
            @RequestParam("adId") Long adId,
            @RequestParam("adVideo") MultipartFile adVideo) {
        try {
            String firebaseUrl = uploadFileToFirebase(adId, adVideo, "video");
            adService.updateAdVideoUrl(adId, firebaseUrl);
            return ResponseEntity.ok(firebaseUrl);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to upload video to Firebase: " + e.getMessage());
        }
    }

    private String uploadFileToFirebase(Long adId, MultipartFile file, String type) throws IOException {
        String folderName = "ad/" + adId;
        String fileName = folderName + "/" + type + "_" + file.getOriginalFilename();

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

//    @PostMapping("/preview")
//    public ResponseEntity<String> handleAdImageUpload(
//            @RequestParam("adId") Long adId,
//            @RequestParam("previewImage") MultipartFile previewImage) {
//        try {
//            String s3Url = uploadFileToS3(adId, previewImage, "preview");
//            adService.updatePreviewUrl(adId, s3Url);
//            return ResponseEntity.ok(s3Url);
//        } catch (IOException e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body("Failed to upload image to S3: " + e.getMessage());
//        }
//    }
//
//    @PostMapping("/video")
//    public ResponseEntity<String> handleAdVideoUpload(
//            @RequestParam("adId") Long adId,
//            @RequestParam("adVideo") MultipartFile adVideo) {
//        try {
//            String s3Url = uploadFileToS3(adId, adVideo, "video");
//            adService.updateAdVideoUrl(adId, s3Url);
//            return ResponseEntity.ok(s3Url);
//        } catch (IOException e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body("Failed to upload video to S3: " + e.getMessage());
//        }
//    }
//
//
//    private String uploadFileToS3(Long adId, MultipartFile file, String type) throws IOException {
//        String folderName = "ad/" + adId + "/";
//        String fileName = folderName + "/" + type + "_" + file.getOriginalFilename();
//        String fileUrl = "https://" + bucket + ".s3.ap-northeast-2.amazonaws.com/" + fileName;
//
//        ObjectMetadata metadata = new ObjectMetadata();
//        metadata.setContentType(file.getContentType());
//        metadata.setContentLength(file.getSize());
//
//        // S3에 파일 업로드
//        amazonS3Client.putObject(bucket, fileName, file.getInputStream(), metadata);
//
//        return fileUrl;
//    }


}
