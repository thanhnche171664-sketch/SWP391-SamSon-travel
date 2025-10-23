/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util;

import java.security.SecureRandom;
import java.util.UUID;
import java.util.logging.Logger;

/**
 * Token generator utility class for creating secure verification tokens
 * Uses UUID and secure random generation for token security
 * 
 * @author SamSon Travel Team
 */
public class TokenGenerator {
    
    private static final Logger LOGGER = Logger.getLogger(TokenGenerator.class.getName());
    
    // Secure random generator for additional entropy
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();
    
    // Token length constants
    private static final int DEFAULT_TOKEN_LENGTH = 32;
    private static final int MAX_TOKEN_LENGTH = 64;
    
    // Character sets for token generation
    private static final String UPPERCASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String LOWERCASE = "abcdefghijklmnopqrstuvwxyz";
    private static final String DIGITS = "0123456789";
    private static final String SPECIAL_CHARS = "-_";
    private static final String ALL_CHARS = UPPERCASE + LOWERCASE + DIGITS + SPECIAL_CHARS;
    
    /**
     * Generate a unique verification token using UUID
     * 
     * @return Unique token string
     */
    public static String generateToken() {
        try {
            // Generate UUID and remove hyphens for cleaner token
            String uuid = UUID.randomUUID().toString().replace("-", "");
            LOGGER.info("Generated UUID-based token successfully");
            return uuid;
        } catch (Exception e) {
            LOGGER.severe("Error generating UUID token: " + e.getMessage());
            // Fallback to custom token generation
            return generateCustomToken(DEFAULT_TOKEN_LENGTH);
        }
    }
    
    /**
     * Generate a custom token with specified length
     * 
     * @param length Length of the token to generate
     * @return Custom token string
     */
    public static String generateCustomToken(int length) {
        if (length <= 0) {
            length = DEFAULT_TOKEN_LENGTH;
        }
        if (length > MAX_TOKEN_LENGTH) {
            length = MAX_TOKEN_LENGTH;
        }
        
        try {
            StringBuilder token = new StringBuilder(length);
            
            // Ensure token starts with a letter (not digit or special char)
            token.append(UPPERCASE.charAt(SECURE_RANDOM.nextInt(UPPERCASE.length())));
            
            // Fill the rest with random characters
            for (int i = 1; i < length; i++) {
                token.append(ALL_CHARS.charAt(SECURE_RANDOM.nextInt(ALL_CHARS.length())));
            }
            
            LOGGER.info("Generated custom token with length: " + length);
            return token.toString();
        } catch (Exception e) {
            LOGGER.severe("Error generating custom token: " + e.getMessage());
            // Ultimate fallback
            return generateSimpleToken(length);
        }
    }
    
    /**
     * Generate a simple alphanumeric token (fallback method)
     * 
     * @param length Length of the token
     * @return Simple token string
     */
    private static String generateSimpleToken(int length) {
        StringBuilder token = new StringBuilder();
        String chars = UPPERCASE + LOWERCASE + DIGITS;
        
        for (int i = 0; i < length; i++) {
            token.append(chars.charAt(SECURE_RANDOM.nextInt(chars.length())));
        }
        
        return token.toString();
    }
    
    /**
     * Generate a token with timestamp for additional uniqueness
     * 
     * @return Token with timestamp suffix
     */
    public static String generateTimestampedToken() {
        try {
            String baseToken = generateToken();
            long timestamp = System.currentTimeMillis();
            String timestampHex = Long.toHexString(timestamp);
            
            String token = baseToken + timestampHex;
            LOGGER.info("Generated timestamped token successfully");
            return token;
        } catch (Exception e) {
            LOGGER.severe("Error generating timestamped token: " + e.getMessage());
            return generateToken(); // Fallback to regular token
        }
    }
    
    /**
     * Generate a short token for quick verification (e.g., 6-digit code)
     * 
     * @param length Length of the short token (typically 4-8)
     * @return Short numeric token
     */
    public static String generateShortToken(int length) {
        if (length <= 0) {
            length = 6; // Default to 6 digits
        }
        if (length > 10) {
            length = 10; // Max 10 digits
        }
        
        StringBuilder token = new StringBuilder();
        
        // Ensure first digit is not 0
        token.append(SECURE_RANDOM.nextInt(9) + 1);
        
        // Fill the rest with random digits
        for (int i = 1; i < length; i++) {
            token.append(SECURE_RANDOM.nextInt(10));
        }
        
        LOGGER.info("Generated short token with length: " + length);
        return token.toString();
    }
    
    /**
     * Generate a token with specific prefix
     * 
     * @param prefix Prefix to add to the token
     * @param length Total length including prefix
     * @return Token with prefix
     */
    public static String generateTokenWithPrefix(String prefix, int length) {
        if (prefix == null) {
            prefix = "";
        }
        
        int remainingLength = length - prefix.length();
        if (remainingLength <= 0) {
            return prefix;
        }
        
        String suffix = generateCustomToken(remainingLength);
        String token = prefix + suffix;
        
        LOGGER.info("Generated token with prefix: " + prefix);
        return token;
    }
    
    /**
     * Validate token format (basic validation)
     * 
     * @param token Token to validate
     * @return true if token format is valid
     */
    public static boolean isValidTokenFormat(String token) {
        if (token == null || token.trim().isEmpty()) {
            return false;
        }
        
        // Check if token contains only valid characters
        return token.matches("^[A-Za-z0-9\\-_]+$");
    }
    
    /**
     * Generate multiple unique tokens
     * 
     * @param count Number of tokens to generate
     * @return Array of unique tokens
     */
    public static String[] generateMultipleTokens(int count) {
        if (count <= 0) {
            count = 1;
        }
        if (count > 100) {
            count = 100; // Limit to prevent memory issues
        }
        
        String[] tokens = new String[count];
        for (int i = 0; i < count; i++) {
            tokens[i] = generateToken();
        }
        
        LOGGER.info("Generated " + count + " tokens successfully");
        return tokens;
    }
}
