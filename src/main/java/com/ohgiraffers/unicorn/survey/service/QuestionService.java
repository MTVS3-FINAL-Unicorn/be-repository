package com.ohgiraffers.unicorn.survey.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
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
            List<String> options = uploadImagesAndGenerateOptions(images);
            question.setOptions(options);
        }
        return questionRepository.save(question);
    }

    private List<String> uploadImagesAndGenerateOptions(List<MultipartFile> images) throws IOException {
        List<String> options = new ArrayList<>();
        for (int i = 0; i < images.size(); i++) {
            String option = "Option " + (i + 1);
            String imageUrl = uploadFileToS3(images.get(i), "question-options");
            options.add(option + ": " + imageUrl);
        }
        return options;
    }

    private String uploadFileToS3(MultipartFile file, String folderName) throws IOException {
        String fileName = folderName + "/" + file.getOriginalFilename();
        String fileUrl = "https://" + bucket + ".s3.ap-northeast-2.amazonaws.com/" + fileName;
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(file.getContentType());
        metadata.setContentLength(file.getSize());
        amazonS3Client.putObject(bucket, fileName, file.getInputStream(), metadata);
        return fileUrl;
    }

    public Question getQuestion(Long questionId) {
        return questionRepository.findById(questionId).orElse(null);
    }
}
