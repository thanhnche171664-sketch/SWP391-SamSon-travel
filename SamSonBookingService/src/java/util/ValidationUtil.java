/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util;

import java.util.regex.Pattern;
import java.util.logging.Logger;

/**
 * Utility class for input validation and sanitization
 * Provides methods for validating user inputs and preventing XSS attacks
 * 
 * @author SamSon Travel Team
 */
public class ValidationUtil {
    
    private static final Logger LOGGER = Logger.getLogger(ValidationUtil.class.getName());
    
    // Regex patterns
    private static final String EMAIL_PATTERN = 
        "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
    
    private static final String PHONE_PATTERN = 
        "^0[0-9]{9,10}$"; // Vietnamese phone: 10-11 digits starting with 0
    
    private static final String NAME_PATTERN = 
        "^[\\p{L}\\p{M}\\s'-]{2,50}$"; // 2-50 letters/unicode characters, spaces, apostrophes, hyphens
    
    // Compiled patterns
    private static final Pattern EMAIL_REGEX = Pattern.compile(EMAIL_PATTERN);
    private static final Pattern PHONE_REGEX = Pattern.compile(PHONE_PATTERN);
    private static final Pattern NAME_REGEX = Pattern.compile(NAME_PATTERN);
    
    /**
     * Validate email format
     * 
     * @param email Email to validate
     * @return true if email format is valid
     */
    public static boolean isValidEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        return EMAIL_REGEX.matcher(email.trim()).matches();
    }
    
    /**
     * Validate Vietnamese phone number format
     * Format: 10-11 digits starting with 0
     * 
     * @param phone Phone number to validate
     * @return true if phone format is valid
     */
    public static boolean isValidPhone(String phone) {
        if (phone == null || phone.trim().isEmpty()) {
            return false;
        }
        
        String cleanedPhone = phone.trim().replaceAll("[\\s\\-\\(\\)]", "");
        return PHONE_REGEX.matcher(cleanedPhone).matches();
    }
    
    /**
     * Validate name format
     * Name should be 2-50 characters, containing only letters and spaces
     * 
     * @param name Name to validate
     * @return true if name format is valid
     */
    public static boolean isValidName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return false;
        }
        
        String trimmedName = name.trim();
        
        // Check length
        if (trimmedName.length() < 2 || trimmedName.length() > 50) {
            return false;
        }
        
        // Simple validation: allow letters, spaces, apostrophes, hyphens
        // Must contain at least one letter
        boolean hasLetter = trimmedName.matches(".*[\\p{L}\\p{M}].*");
        boolean onlyValidChars = trimmedName.matches("^[\\p{L}\\p{M}\\p{N}\\s'-]+$");
        
        return hasLetter && onlyValidChars;
    }
    
    /**
     * Sanitize input to prevent XSS attacks
     * Removes or escapes potentially dangerous characters
     * 
     * @param input Input string to sanitize
     * @return Sanitized string
     */
    public static String sanitizeInput(String input) {
        if (input == null) {
            return "";
        }
        
        // Remove HTML tags
        String sanitized = input.replaceAll("<", "&lt;")
                                 .replaceAll(">", "&gt;")
                                 .replaceAll("\"", "&quot;")
                                 .replaceAll("'", "&#x27;")
                                 .replaceAll("/", "&#x2F;");
        
        LOGGER.info("Input sanitized");
        return sanitized;
    }
    
    /**
     * Validate password strength
     * Requirements: 8+ characters, uppercase, lowercase, digit, special char
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
        boolean hasSpecialChar = password.chars().anyMatch(ch -> 
            "!@#$%^&*()_+-=[]{}|;:,.<>?".indexOf(ch) >= 0
        );
        
        return hasUpperCase && hasLowerCase && hasDigit && hasSpecialChar;
    }
    
    /**
     * Get password strength feedback
     * 
     * @param password Password to check
     * @return Strength feedback message
     */
    public static String getPasswordStrength(String password) {
        if (password == null || password.isEmpty()) {
            return "Mật khẩu không được để trống";
        }
        
        if (password.length() < 8) {
            return "Mật khẩu phải có ít nhất 8 ký tự";
        }
        
        boolean hasUpperCase = password.chars().anyMatch(Character::isUpperCase);
        boolean hasLowerCase = password.chars().anyMatch(Character::isLowerCase);
        boolean hasDigit = password.chars().anyMatch(Character::isDigit);
        boolean hasSpecialChar = password.chars().anyMatch(ch -> 
            "!@#$%^&*()_+-=[]{}|;:,.<>?".indexOf(ch) >= 0
        );
        
        StringBuilder feedback = new StringBuilder();
        
        if (!hasUpperCase) {
            feedback.append("Mật khẩu phải có ít nhất một chữ hoa. ");
        }
        if (!hasLowerCase) {
            feedback.append("Mật khẩu phải có ít nhất một chữ thường. ");
        }
        if (!hasDigit) {
            feedback.append("Mật khẩu phải có ít nhất một chữ số. ");
        }
        if (!hasSpecialChar) {
            feedback.append("Mật khẩu phải có ít nhất một ký tự đặc biệt (!@#$%^&*()_+-=[]{}|;:,.<>?). ");
        }
        
        return feedback.length() > 0 ? feedback.toString().trim() : "Mật khẩu hợp lệ";
    }
    
    /**
     * Validate address
     * 
     * @param address Address to validate
     * @return true if address is valid
     */
    public static boolean isValidAddress(String address) {
        if (address == null || address.trim().isEmpty()) {
            return false;
        }
        
        String trimmedAddress = address.trim();
        return trimmedAddress.length() >= 5 && trimmedAddress.length() <= 255;
    }
    
    /**
     * Validate gender
     * 
     * @param gender Gender to validate
     * @return true if gender is valid
     */
    public static boolean isValidGender(String gender) {
        if (gender == null) {
            return false;
        }
        
        return gender.equalsIgnoreCase("male") || 
               gender.equalsIgnoreCase("female") || 
               gender.equalsIgnoreCase("other");
    }
    
    /**
     * Truncate string to maximum length
     * 
     * @param input Input string
     * @param maxLength Maximum length
     * @return Truncated string
     */
    public static String truncate(String input, int maxLength) {
        if (input == null) {
            return "";
        }
        
        if (input.length() <= maxLength) {
            return input;
        }
        
        return input.substring(0, maxLength) + "...";
    }
    
    /**
     * Validate file extension
     * 
     * @param fileName File name
     * @param allowedExtensions Array of allowed extensions
     * @return true if extension is allowed
     */
    public static boolean isValidFileExtension(String fileName, String[] allowedExtensions) {
        if (fileName == null || !fileName.contains(".")) {
            return false;
        }
        
        String extension = fileName.substring(fileName.lastIndexOf(".")).toLowerCase();
        for (String ext : allowedExtensions) {
            if (ext.toLowerCase().equals(extension)) {
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * Format phone number for display
     * 
     * @param phone Phone number
     * @return Formatted phone number
     */
    public static String formatPhone(String phone) {
        if (phone == null || phone.trim().isEmpty()) {
            return "";
        }
        
        String cleaned = phone.trim().replaceAll("[\\s\\-\\(\\)]", "");
        if (cleaned.length() == 10) {
            return cleaned.substring(0, 4) + " " + cleaned.substring(4, 7) + " " + cleaned.substring(7);
        }
        
        return cleaned;
    }
}

