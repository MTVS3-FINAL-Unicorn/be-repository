package com.ohgiraffers.unicorn.survey.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.ohgiraffers.unicorn.survey.entity.Question;
import com.ohgiraffers.unicorn.survey.entity.QuestionType;
import com.ohgiraffers.unicorn.survey.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class QuestionService {

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    private final Storage storage = StorageOptions.getDefaultInstance().getService();

    public Question createQuestion(Long meetingId, String content, QuestionType type) {

        Question question = new Question();

        question.setMeetingId(meetingId);
        question.setContent(content);
        question.setType(type);

        return questionRepository.save(question);
    }

    public Question createPreferenceQuestion(Long meetingId, String content, QuestionType type, List<MultipartFile> images) throws IOException {
        Question question = new Question(meetingId, content, type);
        if (type == QuestionType.PREFERENCE && images != null && !images.isEmpty()) {
            List<String> options = uploadImagesAndGenerateOptions(images, meetingId);
            question.setOptions(options);
        }
        return questionRepository.save(question);
    }

    private List<String> uploadImagesAndGenerateOptions(List<MultipartFile> images, Long meetingId) throws IOException {
        List<String> options = new ArrayList<>();
        for (int i = 0; i < images.size(); i++) {
            String option = "Option " + (i + 1);
            String imageUrl = uploadFileToFirebase(images.get(i), "meeting" + meetingId + "question-options");
            options.add(option + ": " + imageUrl);
        }
        return options;
    }

//    private String uploadFileToS3(MultipartFile file, String folderName) throws IOException {
//        String fileName = folderName + "/" + file.getOriginalFilename();
//        String fileUrl = "https://" + bucket + ".s3.ap-northeast-2.amazonaws.com/" + fileName;
//        ObjectMetadata metadata = new ObjectMetadata();
//        metadata.setContentType(file.getContentType());
//        metadata.setContentLength(file.getSize());
//        amazonS3Client.putObject(bucket, fileName, file.getInputStream(), metadata);
//        return fileUrl;
//    }

    public String uploadFileToFirebase(MultipartFile file, String folderName) throws IOException {
        String fileName = folderName + file.getOriginalFilename();

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

    public Question getQuestion(Long questionId) {
        return questionRepository.findById(questionId).orElse(null);
    }
}
