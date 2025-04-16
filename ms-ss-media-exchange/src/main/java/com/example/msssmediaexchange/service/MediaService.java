package com.example.msssmediaexchange.service;

import com.example.msssmediaexchange.dto.MediaPayload;
import com.example.msssmediaexchange.dto.Provider;
import com.example.msssmediaexchange.entity.Media;
import com.example.msssmediaexchange.repository.MediaRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Object;

import java.util.Base64;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MediaService {
    private final S3Client s3Client;
    private final MediaRepository mediaRepository;
    private static final Logger logger = LoggerFactory.getLogger(MediaService.class);

    @Value("${aws.s3.bucket}")
    private String bucket;

//    @KafkaListener(topics = "${kafka.topic.media}", groupId = "${spring.kafka.consumer.group-id}")
    @KafkaListener(
            topics = "${kafka.topic.media}",
            groupId = "${spring.kafka.consumer.group-id}",
            autoStartup = "${kafka.listener.enabled:true}"
    )
    public void processMedia(MediaPayload payload) {
        if (payload == null) {
            System.out.println("⚠️ Received null payload");
            return;
        }

        System.out.println("🎯 Kafka listener triggered!");
        System.out.println("📥 Kafka listener: Received MediaPayload: " + payload);

        if (payload.getBase64Image() == null || payload.getBase64Image().isEmpty()) {
            System.out.println("⚠️ Skipping empty media payload for sourceId: " + payload.getSourceId());
            return;
        }

        try {
            System.out.println("🔑 Trying to decode image");
            byte[] imageData = Base64.getDecoder().decode(payload.getBase64Image());

            String providerString = payload.getProvider();
            if (providerString == null || providerString.isBlank()) {
                throw new IllegalArgumentException("Missing provider type in payload");
            }

            Provider provider;
            if (providerString.equalsIgnoreCase("POST")) {
                provider = Provider.POST;
            } else {
                throw new IllegalArgumentException("Unsupported provider type: " + providerString);
            }
            //Provider provider = Provider.valueOf(providerString.toUpperCase());

            String s3Key = generateS3Key(payload.getSourceId().toString(), provider);

            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucket)
                    .key(s3Key)
                    .build();
            s3Client.putObject(putObjectRequest, RequestBody.fromBytes(imageData));

            System.out.println("✅ Uploaded to S3 with key: " + s3Key);

            Media media = new Media();
            media.setSourceId(payload.getSourceId().toString());
            media.setS3Key(s3Key);
            media.setProvider(provider);

            mediaRepository.save(media);
            System.out.println("✅ Saved media entry to database: " + media);
        } catch (Exception e) {
            System.out.println("❌ Error processing media payload for sourceId=" + payload.getSourceId() + ": " + e.getMessage());
            logger.error("An error occurred while processing media payload", e);
        }
    }

    private String generateS3Key(String mediaId, Provider provider) {
        return "media/" + provider + "/" + mediaId + "/" + UUID.randomUUID();
    }

    public String findBySourceIdAndProvider(String sourceId, Provider provider){

        Optional<Media> mediaOpt = mediaRepository.findBySourceIdAndProvider(sourceId, provider);
        if (mediaOpt.isEmpty()) {
            throw new NoSuchElementException("Media not found for sourceId: " + sourceId + " and provider: " + provider);
        }

        String s3Key = mediaOpt.get().getS3Key();

        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucket) // Replace with your bucket name
                .key(s3Key)
                .build();

        s3Client.getObject(getObjectRequest);

        try {
            ResponseBytes<GetObjectResponse> responseBytes = s3Client.getObjectAsBytes(getObjectRequest);
            byte[] data = responseBytes.asByteArray();
            return Base64.getEncoder().encodeToString(data);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
