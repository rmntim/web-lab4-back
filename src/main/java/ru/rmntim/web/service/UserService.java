package ru.rmntim.web.service;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import lombok.extern.slf4j.Slf4j;
import ru.rmntim.web.auth.PasswordHasher;
import ru.rmntim.web.dao.UserDAO;
import ru.rmntim.web.dto.UserInfoDTO;
import ru.rmntim.web.exceptions.UserNotFoundException;

@Stateless
@Slf4j
public class UserService {
    @EJB
    private UserDAO userDAO;

    public UserInfoDTO getUserInfo(Long userId) throws UserNotFoundException {
        return userDAO.getUserInfo(userId);
    }

    public UserInfoDTO updateUserInfo(Long userId, UserInfoDTO userInfo) throws UserNotFoundException {
        return userDAO.updateUserInfo(userId, userInfo);
    }

    public void deleteUser(Long userId) throws UserNotFoundException {
        userDAO.deleteUser(userId);
    }

    public void updatePassword(Long userId, String currentPassword, String newPassword) throws UserNotFoundException {
        var user = userDAO.findById(userId).orElseThrow(() -> new UserNotFoundException("User not found"));

        if (!PasswordHasher.checkPassword(currentPassword.toCharArray(), user.getPassword())) {
            throw new UserNotFoundException("Wrong password");
        }

        var newPasswordHash = PasswordHasher.hashPassword(newPassword.toCharArray());
        userDAO.updatePassword(user, newPasswordHash);
    }
}
