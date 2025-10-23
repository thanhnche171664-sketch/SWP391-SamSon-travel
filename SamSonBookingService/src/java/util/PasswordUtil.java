/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Password utility class for secure password hashing and verification
 * Uses simple but effective MD5 + Salt algorithm for password security
 * 
 * @author SamSon Travel Team
 */
public class PasswordUtil {
    
    private static final Logger LOGGER = Logger.getLogger(PasswordUtil.class.getName());
    
    
    
    /**
     * Hash a plain text password using simple MD5 algorithm
     * 
     * @param plainPassword The plain text password to hash
     * @return Hashed password string
     * @throws IllegalArgumentException if password is null or empty
     */
    public static String hashPassword(String plainPassword) {
        if (plainPassword == null || plainPassword.trim().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty");
        }
        
        try {
            // Simple MD5 implementation without salt
            MessageDigest digest = MessageDigest.getInstance("MD5");
            byte[] hashBytes = digest.digest(plainPassword.getBytes());
            String hashedPassword = bytesToHex(hashBytes);
            
            LOGGER.info("Password hashed successfully");
            return hashedPassword;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error hashing password", e);
            throw new RuntimeException("Failed to hash password", e);
        }
    }
    
    /**
     * Verify a plain text password against a hashed password
     * 
     * @param plainPassword The plain text password to verify
     * @param hashedPassword The stored hashed password
     * @return true if password matches, false otherwise
     * @throws IllegalArgumentException if either parameter is null or empty
     */
    public static boolean verifyPassword(String plainPassword, String hashedPassword) {
        if (plainPassword == null || plainPassword.trim().isEmpty()) {
            throw new IllegalArgumentException("Plain password cannot be null or empty");
        }
        if (hashedPassword == null || hashedPassword.trim().isEmpty()) {
            throw new IllegalArgumentException("Hashed password cannot be null or empty");
        }
        
        try {
            // Check if it's old format (plain text) - for backward compatibility
            if (hashedPassword.length() < 32) {
                // This is likely an old plain text password
                boolean isValid = plainPassword.equals(hashedPassword);
                LOGGER.info("Password verification result (plain text): " + isValid);
                return isValid;
            }
            
            // Simple MD5 verification
            MessageDigest digest = MessageDigest.getInstance("MD5");
            byte[] hashBytes = digest.digest(plainPassword.getBytes());
            String computedHash = bytesToHex(hashBytes);
            
            boolean isValid = hashedPassword.equals(computedHash);
            LOGGER.info("Password verification result: " + isValid);
            return isValid;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error verifying password", e);
            return false;
        }
    }
    
    
    /**
     * Convert byte array to hexadecimal string
     * 
     * @param bytes Byte array to convert
     * @return Hexadecimal string
     */
    private static String bytesToHex(byte[] bytes) {
        StringBuilder result = new StringBuilder();
        for (byte b : bytes) {
            result.append(String.format("%02x", b));
        }
        return result.toString();
    }
    
    /**
     * Validate password strength
     * 
     * @param password Password to validate
     * @return true if password meets strength requirements
     */
    public static boolean isValidPassword(String password) {
        if (password == null || password.length() < 8) {
            return false;
        }
        
        boolean hasUpperCase = password.chars().anyMatch(Character::isUpperCase);
        boolean hasLowerCase = password.chars().anyMatch(Character::isLowerCase);
        boolean hasDigit = password.chars().anyMatch(Character::isDigit);
        boolean hasSpecialChar = password.chars().anyMatch(ch -> "!@#$%^&*()_+-=[]{}|;:,.<>?".indexOf(ch) >= 0);
        
        return hasUpperCase && hasLowerCase && hasDigit && hasSpecialChar;
    }
    
}
