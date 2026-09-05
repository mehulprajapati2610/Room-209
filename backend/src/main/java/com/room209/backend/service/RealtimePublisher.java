package com.room209.backend.service;

import com.room209.backend.dto.RealtimeEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class RealtimePublisher {

    private static final Logger logger = LoggerFactory.getLogger(RealtimePublisher.class);
    private final SimpMessagingTemplate messagingTemplate;

    public RealtimePublisher(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void publishPresenceUpdate(Long roomId, Object payload) {
        String destination = "/topic/room." + roomId + ".presence";
        logger.info("Publishing presence event to destination: {}", destination);
        messagingTemplate.convertAndSend(destination, new RealtimeEvent("PRESENCE", "UPDATED", payload));
    }

    public void publishFeedEvent(Long roomId, String action, Object payload) {
        String destination = "/topic/room." + roomId + ".feed";
        logger.info("Publishing feed event ({}) to destination: {}", action, destination);
        messagingTemplate.convertAndSend(destination, new RealtimeEvent("FEED", action, payload));
    }

    public void publishPlanEvent(Long roomId, String action, Object payload) {
        String destination = "/topic/room." + roomId + ".plans";
        logger.info("Publishing plan event ({}) to destination: {}", action, destination);
        messagingTemplate.convertAndSend(destination, new RealtimeEvent("PLAN", action, payload));
    }

    public void publishChoreEvent(Long roomId, String action, Object payload) {
        String destination = "/topic/room." + roomId + ".chores";
        logger.info("Publishing chore event ({}) to destination: {}", action, destination);
        messagingTemplate.convertAndSend(destination, new RealtimeEvent("CHORE", action, payload));
    }

    public void publishPollEvent(Long roomId, String action, Object payload) {
        String destination = "/topic/room." + roomId + ".polls";
        logger.info("Publishing poll event ({}) to destination: {}", action, destination);
        messagingTemplate.convertAndSend(destination, new RealtimeEvent("POLL", action, payload));
    }
}
