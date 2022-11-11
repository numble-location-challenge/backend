package com.example.backend.service;

public interface SocialingService {
    void addParticipants(Long userId, Long socialId);

    void deleteParticipants(Long userId, Long socialId);

    void kickOutParticipants(Long userId, Long socialId, Long droppedUserId);
}
