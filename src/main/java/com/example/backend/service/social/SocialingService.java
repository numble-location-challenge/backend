package com.example.backend.service.social;

public interface SocialingService {
    void addParticipants(String userId, Long socialId);

    void deleteParticipants(String userId, Long socialId);

    void kickOutParticipants(String userId, Long socialId, Long droppedUserId);
}
