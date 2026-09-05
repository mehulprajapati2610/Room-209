package com.room209.backend.service;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.io.InputStream;

@Service
public class FCMService {

    private static final Logger logger = LoggerFactory.getLogger(FCMService.class);

    @Value("${firebase.enabled:false}")
    private boolean firebaseEnabled;

    @Value("${firebase.credentials-path:firebase-service-account.json}")
    private String credentialsPath;

    private final ResourceLoader resourceLoader;
    private boolean initialized = false;

    public FCMService(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @PostConstruct
    public void initialize() {
        if (!firebaseEnabled) {
            logger.info("Firebase Cloud Messaging is disabled in configuration.");
            return;
        }

        try {
            Resource resource = resourceLoader.getResource("classpath:" + credentialsPath);
            if (!resource.exists()) {
                logger.warn("Firebase credentials file '{}' not found. Push notifications will be mocked.", credentialsPath);
                return;
            }

            try (InputStream serviceAccount = resource.getInputStream()) {
                FirebaseOptions options = FirebaseOptions.builder()
                        .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                        .build();

                if (FirebaseApp.getApps().isEmpty()) {
                    FirebaseApp.initializeApp(options);
                    logger.info("Firebase Cloud Messaging initialized successfully.");
                }
                initialized = true;
            }
        } catch (Exception e) {
            logger.warn("Failed to initialize Firebase: {}. Push notifications will be logged only.", e.getMessage());
        }
    }

    public void sendPushNotification(String token, String title, String body) {
        if (token == null || token.trim().isEmpty()) {
            return;
        }

        if (!initialized) {
            logger.info("[MOCK PUSH] To: {} | Title: {} | Body: {}", token, title, body);
            return;
        }

        try {
            Message message = Message.builder()
                    .setToken(token)
                    .setNotification(Notification.builder()
                            .setTitle(title)
                            .setBody(body)
                            .build())
                    .build();

            String response = FirebaseMessaging.getInstance().send(message);
            logger.info("Successfully sent FCM message: {}", response);
        } catch (Exception e) {
            logger.error("Failed to send FCM message to token {}: {}", token, e.getMessage());
        }
    }
}
