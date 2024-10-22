package ru.rmntim.web.beans;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import ru.rmntim.web.models.User;
import ru.rmntim.web.repositories.UserRepository;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@Slf4j
@Stateless
public class UserBean {
    @Inject
    private UserRepository userRepository;

    public String createUser() {
        try {
            var user = new User();
            var pwHash = MessageDigest.getInstance("SHA-256").digest("test".getBytes(StandardCharsets.UTF_8));
            user.setUsername("test");
            user.setPasswordHashB64(Base64.getEncoder().encodeToString(pwHash));
            userRepository.create(user);
            return "User created";
        } catch (NoSuchAlgorithmException e) {
            log.error("Error while creating user", e);
            return "Error";
        }
    }
}
