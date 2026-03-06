package com.example.msssmediaexchange.service;

import com.example.msssmediaexchange.dto.MediaPayload;
import com.example.msssmediaexchange.dto.MediaResponse;
import com.example.msssmediaexchange.dto.Provider;
import com.example.msssmediaexchange.entity.Media;
import com.example.msssmediaexchange.repository.MediaRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.time.Duration;
import java.util.Base64;
import java.util.Optional;
import java.util.UUID;

@Service
public class MediaService {
    private static final Logger log = LoggerFactory.getLogger(MediaService.class);
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

        switch (payload.getType()) {
            case BASE64 -> processBase64Image(payload);
            case URL -> processUrlImage(payload);
            default -> {
                return;
            }
        }
    }

    private void processBase64Image(MediaPayload payload) {
        if (payload.getImage() == null || payload.getImage().isEmpty()) {
            return;
        }

        byte[] imageData = Base64.getDecoder().decode(payload.getImage());
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

    private void processUrlImage(MediaPayload payload) {
        payload.setImage(downloadImage(payload.getImage()));
        processBase64Image(payload);
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

    private String downloadImage(String imageUrlString) {
        try {
            URL url = new URL(imageUrlString);

            try (InputStream is = url.openStream()) {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int read = 0;
                while ((read = is.read(buffer, 0, buffer.length)) != -1) {
                    baos.write(buffer, 0, read);
                }
                baos.flush();

                byte[] imageBytes = baos.toByteArray();

                return Base64.getEncoder().encodeToString(imageBytes);
            }

        } catch (IOException e) {
            log.warn("Error downloading or converting image: {}", e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}
