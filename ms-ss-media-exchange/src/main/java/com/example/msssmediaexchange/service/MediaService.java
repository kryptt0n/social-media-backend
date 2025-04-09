package com.example.msssmediaexchange.service;

import com.example.msssmediaexchange.dto.MediaPayload;
import com.example.msssmediaexchange.dto.Provider;
import com.example.msssmediaexchange.entity.Media;
import com.example.msssmediaexchange.repository.MediaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.util.Base64;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MediaService {
    private final S3Client s3Client;
    private final MediaRepository mediaRepository;

    @Value("${aws.s3.bucket}")
    private String bucket;

    @KafkaListener(topics = "${kafka.topic.media}", groupId = "${spring.kafka.consumer.group-id}")
    public void processMedia(MediaPayload payload) {

            if (payload.getBase64Image() == null || payload.getBase64Image().isEmpty()) {
                return;
            }

            byte[] imageData = Base64.getDecoder().decode(payload.getBase64Image());
            String s3Key = generateS3Key(payload.getSourceId(), payload.getProvider());
            String contentType = determineContentType(imageData);

            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucket)
                    .key(s3Key)
                    .contentType(contentType)
                    .build();
            s3Client.putObject(putObjectRequest, RequestBody.fromBytes(imageData));

            Media media = new Media();
            media.setId(payload.getSourceId());
            media.setS3Key(s3Key);
            media.setContentType(contentType);
            media.setProvider(payload.getProvider());
            mediaRepository.save(media);
    }

    private String generateS3Key(Long mediaId, Provider provider) {
        return "media/" + provider + "/" + mediaId + "/" + UUID.randomUUID();
    }

    private String determineContentType(byte[] imageData) {
        if (imageData.length > 2 && imageData[0] == (byte) 0xFF && imageData[1] == (byte) 0xD8) {
            return "image/jpeg";
        } else if (imageData.length > 8 && imageData[0] == (byte) 0x89 && imageData[1] == (byte) 0x50 && imageData[2] == (byte) 0x4E && imageData[3] == (byte) 0x47) {
            return "image/png";
        }
        return "application/octet-stream";
    }

    public Optional<Media> findBySourceIdAndProvider(Long sourceId, Provider provider){
        return mediaRepository.findBySourceIdAndProvider(sourceId, provider);
    }
}
