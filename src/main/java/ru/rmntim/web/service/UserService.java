package ru.rmntim.web.service;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import lombok.extern.slf4j.Slf4j;
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
}
