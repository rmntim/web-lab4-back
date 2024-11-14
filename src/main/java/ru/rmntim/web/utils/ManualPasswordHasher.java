package ru.rmntim.web.utils;

import at.favre.lib.crypto.bcrypt.BCrypt;

public class ManualPasswordHasher {

    public static void main(String[] args) {
        String password = "example";
        int cost = 12;
        String hashedPassword = BCrypt.withDefaults().hashToString(cost, password.toCharArray());
        System.out.println("Hashed Password: " + hashedPassword);

        // Now use the hashedPassword to insert your admin user into the database
        // This would typically involve a SQL INSERT statement
        // For example: INSERT INTO users (username, password, role) VALUES ('admin', hashedPassword, 'ADMIN');
    }
}
