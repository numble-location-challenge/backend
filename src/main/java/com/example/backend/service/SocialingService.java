package com.example.backend.service;

public interface SocialingService {
    void addApplyUser(Long userId, Long socialId);

    void deleteUserApplied(Long userId, Long socialId);

    void dropSocialUser(Long userId, Long socialId, Long droppedUserId);
}
