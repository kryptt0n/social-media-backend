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

            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucket)
                    .key(s3Key)
                    .build();
            s3Client.putObject(putObjectRequest, RequestBody.fromBytes(imageData));

            Media media = new Media();
            media.setSourceId(payload.getSourceId());
            media.setS3Key(s3Key);
            media.setProvider(payload.getProvider());
            mediaRepository.save(media);
    }

    private String generateS3Key(String mediaId, Provider provider) {
        return "media/" + provider + "/" + mediaId + "/" + UUID.randomUUID();
    }

    public Optional<Media> findBySourceIdAndProvider(String sourceId, Provider provider){
        return mediaRepository.findBySourceIdAndProvider(sourceId, provider);
    }
}
