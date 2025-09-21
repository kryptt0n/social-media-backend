package com.example.msssmediaexchange.service;

import com.example.msssmediaexchange.dto.MediaPayload;
import com.example.msssmediaexchange.dto.MediaResponse;
import com.example.msssmediaexchange.dto.Provider;
import com.example.msssmediaexchange.entity.Media;
import com.example.msssmediaexchange.repository.MediaRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;

import java.time.Duration;
import java.util.Base64;
import java.util.Optional;
import java.util.UUID;

@Service
public class MediaService {
    private final S3Client s3Client;
    private final MediaRepository mediaRepository;
    private final S3Presigner presigner;

    public MediaService(@Qualifier("minio") S3Client s3Client, MediaRepository mediaRepository, S3Presigner presigner) {
        this.s3Client = s3Client;
        this.mediaRepository = mediaRepository;
        this.presigner = presigner;
    }

    @Value("${s3.bucket-name}")
    private String bucket;

    @KafkaListener(topics = "${kafka.topic.media}", groupId = "${spring.kafka.consumer.group-id}")
    public void processMedia(MediaPayload payload) {
        if (payload.getBase64Image() == null || payload.getBase64Image().isEmpty()) {
            return;
        }

        byte[] imageData = Base64.getDecoder().decode(payload.getBase64Image());
        String sourceId = payload.getSourceId();
        Provider provider = payload.getProvider();

        Optional<Media> existingMedia = mediaRepository.findBySourceIdAndProvider(sourceId, provider);

        String s3Key;
        if (existingMedia.isPresent()) {
            // Delete the old image from S3
            DeleteObjectRequest deleteRequest = DeleteObjectRequest.builder()
                    .bucket(bucket)
                    .key(existingMedia.get().getS3Key())
                    .build();
            s3Client.deleteObject(deleteRequest);

            // Generate a new S3 key
            s3Key = generateS3Key(sourceId, provider);

            // Update the existing Media entity
            Media media = existingMedia.get();
            media.setS3Key(s3Key);
            mediaRepository.save(media);

        } else {
            // Create a new Media entity and generate an S3 key
            s3Key = generateS3Key(sourceId, provider);
            Media media = new Media();
            media.setSourceId(sourceId);
            media.setS3Key(s3Key);
            media.setProvider(provider);
            mediaRepository.save(media);
        }

        // Upload the new image to S3
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucket)
                .key(s3Key)
                .build();
        s3Client.putObject(putObjectRequest, RequestBody.fromBytes(imageData));
    }

    private String generateS3Key(String mediaId, Provider provider) {
        return "media/" + provider + "/" + mediaId + "/" + UUID.randomUUID();
    }

    public Optional<MediaResponse> findBySourceIdAndProvider(String sourceId, Provider provider) {
        Optional<Media> mediaOptional = mediaRepository.findBySourceIdAndProvider(sourceId, provider);
        if (mediaOptional.isPresent()) {
            Media media = mediaOptional.get();
            MediaResponse mediaResponse = new MediaResponse();
            mediaResponse.setId(media.getId());
            mediaResponse.setProvider(media.getProvider());
            mediaResponse.setS3Key(media.getS3Key());
            mediaResponse.setSourceId(media.getSourceId());
            mediaResponse.setUrl(presignedGetUrl(media.getS3Key(), Duration.ofHours(1)));
            return Optional.of(mediaResponse);
        } else
            return Optional.empty();
    }

    @Transactional
    public void deleteMediaBySourceIdAndProvider(String sourceId, Provider provider) {
        Optional<Media> existingMedia = mediaRepository.findBySourceIdAndProvider(sourceId, provider);

        if (existingMedia.isPresent()) {
            DeleteObjectRequest deleteRequest = DeleteObjectRequest.builder()
                    .bucket(bucket)
                    .key(existingMedia.get().getS3Key())
                    .build();
            s3Client.deleteObject(deleteRequest);

            mediaRepository.deleteMediaBySourceIdAndProvider(sourceId, provider);
        }
    }

    public String presignedGetUrl(String key, Duration ttl) {
        var getReq = GetObjectRequest.builder().bucket(bucket).key(key).build();
        var presign = GetObjectPresignRequest.builder()
                .getObjectRequest(getReq).signatureDuration(ttl).build();
        return presigner.presignGetObject(presign).url().toString();
    }
}
