package ru.rmntim.web.dao;

import ru.rmntim.web.dto.UserInfoDTO;
import ru.rmntim.web.entity.UserEntity;
import ru.rmntim.web.exceptions.ServerException;
import ru.rmntim.web.exceptions.UserNotFoundException;

import java.util.Optional;

public interface UserDAO {
    Optional<UserEntity> findByUsername(String username);

    Optional<UserEntity> findById(Long userId);

    UserEntity createUser(UserEntity user) throws ServerException;

    void startNewSession(Long userId) throws UserNotFoundException;

    void endSession(Long userId) throws UserNotFoundException;

    void updateLastActivity(Long userId);

    Optional<UserEntity> findByEmail(String email);

    UserInfoDTO getUserInfo(Long userId) throws UserNotFoundException;

    UserInfoDTO updateUserInfo(Long userId, UserInfoDTO userInfo) throws UserNotFoundException;

    void deleteUser(Long userId) throws UserNotFoundException;
}
