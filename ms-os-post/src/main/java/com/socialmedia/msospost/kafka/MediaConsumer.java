//package com.socialmedia.msospost.kafka;
//
//import com.socialmedia.msospost.dto.MediaPayload;
//import org.springframework.kafka.annotation.KafkaListener;
//import org.springframework.stereotype.Component;
//
//@Component
//public class MediaConsumer {
//
//    @KafkaListener(
//        topics = "${kafka.topic.media}",
//        groupId = "post-media",
//        containerFactory = "mediaKafkaListenerContainerFactory"
//    )
//    public void onMediaReady(MediaPayload media) {
//        System.out.println("📥 Received media update: " + media);
//        // todo Handle image enrichment if needed
//        /*
//          Enrich the post with this media upon consuming it
//          Store the image temporarily
//          send a follow-up event to the frontend once media is available
//        */
//    }
//
//}
