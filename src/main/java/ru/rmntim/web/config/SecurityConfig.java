package ru.rmntim.web.config;

import ru.rmntim.web.exceptions.ConfigurationException;

import javax.naming.InitialContext;
import javax.naming.NamingException;

public class SecurityConfig {
    public static String getJwtSecretKey() throws ConfigurationException {
        try {
            InitialContext initialContext = new InitialContext();
            String key = (String) initialContext.lookup("java:global/env/jwt/SecretKey");
            if (key == null) {
                throw new ConfigurationException("JWT secret key not found in JNDI.");
            }
            return key;
        } catch (NamingException e) {
            throw new ConfigurationException("Error looking up JWT secret key.");
        }
    }
}

