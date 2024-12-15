package com.ohgiraffers.unicorn.ad.service;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.ohgiraffers.unicorn.ad.client.AdClient;
import com.ohgiraffers.unicorn.ad.dto.AdRequestDTO;
import com.ohgiraffers.unicorn.ad.entity.Ad;
import com.ohgiraffers.unicorn.ad.repository.AdRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class AdService {

    private final Logger logger = LoggerFactory.getLogger(AdService.class);

    @Autowired
    private AdRepository adRepository;

//    @Autowired
//    private AmazonS3Client amazonS3Client;
//
//    @Value("${cloud.aws.s3.bucket}")
//    private String bucket;

    @Autowired
    private AdClient adClient;

    private final Storage storage = StorageOptions.getDefaultInstance().getService();

    public Ad createAdImmediately(Long corpId, String description) {
        Ad ad = new Ad(corpId, description);
        ad.setPreviewUrl("default");
        ad.setAdVideoUrl("default");
        Ad savedAd = adRepository.save(ad);
        logger.info("Ad created immediately with adId: {}", savedAd.getAdId());
        return savedAd;
    }

    @Async
    public void updatePreviewAndVideo(Long adId, String description, String fileUrl) {
        try {
            AdRequestDTO requestDTO = new AdRequestDTO(description, adId, fileUrl);
            String adVideoUrl = adClient.generateVideoAd(requestDTO);
            updateAdVideoUrl(adId, adVideoUrl);
        } catch (Exception e) {
            logger.error("광고 영상 생성 중 오류 발생: {}", e.getMessage());
            updateAdVideoUrl(adId, "default");
        }
    }

    public void updateFileUrl(Long adId, String fileUrl) {
        Ad ad = adRepository.findByAdId(adId)
                .orElseThrow(() -> new IllegalArgumentException("Ad not found"));
        ad.setFileUrl(fileUrl);
        adRepository.save(ad);
    }

    public void updatePreviewUrl(Long adId, String previewUrl) {
        Ad ad = adRepository.findByAdId(adId)
                .orElseThrow(() -> new IllegalArgumentException("Ad not found"));
        ad.setPreviewUrl(previewUrl);
        adRepository.save(ad);
    }

    public void updateAdVideoUrl(Long adId, String adVideoUrl) {
        Ad ad = adRepository.findByAdId(adId)
                .orElseThrow(() -> new IllegalArgumentException("Ad not found"));
        ad.setAdVideoUrl(adVideoUrl);
        adRepository.save(ad);
    }

    public String uploadFileToFirebase(MultipartFile file, String folderName) throws IOException {
        String fileName = folderName + "/originalImage_" + file.getOriginalFilename();

        // 업로드할 파일의 메타데이터 정의
        BlobInfo blobInfo = BlobInfo.newBuilder("unicorn-4b430.firebasestorage.app", fileName)
                .setContentType(file.getContentType())
                .build();

        // Firebase 스토리지에 파일 업로드
        storage.create(blobInfo, file.getBytes());

        System.out.println("File uploaded to Firebase: " + fileName);

        // Firebase Storage 파일 URL 반환
        return "https://firebasestorage.googleapis.com/v0/b/unicorn-4b430.firebasestorage.app/o/"
                + fileName.replace("/", "%2F")
                + "?alt=media";
    }

    public byte[] downloadFileFromFirebase(String folderName, String fileName) throws IOException {
        Blob blob = storage.get("unicorn-4b430.firebasestorage.app", folderName + "/" + fileName);
        return blob.getContent();
    }

    public void deleteFileFromFirebase(String folderName, String fileName) {
        storage.delete("unicorn-4b430.firebasestorage.app", folderName + "/" + fileName);
        logger.info("File deleted from Firebase: {}/{}", folderName, fileName);
    }

//    public String uploadImageToS3(MultipartFile file, String folderName) throws IOException {
//        String fileName = folderName + "/originalImage_" + file.getOriginalFilename();
//        String fileUrl = "https://" + bucket + ".s3.ap-northeast-2.amazonaws.com/" + fileName;
//
//        ObjectMetadata metadata = new ObjectMetadata();
//        metadata.setContentType(file.getContentType());
//        metadata.setContentLength(file.getSize());
//
//        amazonS3Client.putObject(bucket, fileName, file.getInputStream(), metadata);
//        logger.info("File uploaded to S3: {}", fileUrl);
//
//        return fileUrl;
//    }

    public Ad findByAdId(Long adId) {
        return adRepository.findByAdId(adId)
                .orElseThrow(() -> new IllegalArgumentException("요청하신 광고를 찾을 수 없습니다."));
    }

    // 특정 기업의 모든 광고 조회
    public List<Ad> findAllAdsByCorpId(Long corpId) {
        return adRepository.findAllByCorpId(corpId);
    }

}
