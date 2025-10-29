/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util;

import jakarta.servlet.http.Part;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

/**
 * Utility class for handling file uploads with validation and security
 * 
 * @author SamSon Travel Team
 */
public class FileUploadUtil {
    
    private static final Logger LOGGER = Logger.getLogger(FileUploadUtil.class.getName());
    
    // Allowed image MIME types
    private static final List<String> ALLOWED_IMAGE_TYPES = Arrays.asList(
        "image/jpeg",
        "image/jpg", 
        "image/png",
        "image/gif",
        "image/webp"
    );
    
    // Allowed image file extensions
    private static final List<String> ALLOWED_IMAGE_EXTENSIONS = Arrays.asList(
        ".jpg", ".jpeg", ".png", ".gif", ".webp"
    );
    
    // Maximum file size (5MB)
    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024;
    
    /**
     * Validate uploaded image file
     * @param filePart The uploaded file part
     * @return Error message if validation fails, null if valid
     */
    public static String validateImageFile(Part filePart) {
        if (filePart == null) {
            return "Không có file được tải lên";
        }
        
        // Check file size
        if (filePart.getSize() == 0) {
            return "File không được để trống";
        }
        
        if (filePart.getSize() > MAX_FILE_SIZE) {
            return "Kích thước file không được vượt quá 5MB";
        }
        
        // Check content type
        String contentType = filePart.getContentType();
        if (contentType == null || !ALLOWED_IMAGE_TYPES.contains(contentType.toLowerCase())) {
            return "Chỉ được phép tải lên file ảnh (JPG, PNG, GIF, WebP)";
        }
        
        // Check file extension
        String fileName = getFileName(filePart);
        if (fileName == null || fileName.isEmpty()) {
            return "Tên file không hợp lệ";
        }
        
        String extension = getFileExtension(fileName);
        if (extension == null || !ALLOWED_IMAGE_EXTENSIONS.contains(extension.toLowerCase())) {
            return "Định dạng file không được hỗ trợ";
        }
        
        return null; // Valid file
    }
    
    /**
     * Ensure directory exists, create if not
     * @param directoryPath Path to directory
     * @return true if directory exists or was created successfully
     */
    public static boolean ensureDirectoryExists(String directoryPath) {
        try {
            Path path = Paths.get(directoryPath);
            if (!Files.exists(path)) {
                Files.createDirectories(path);
                LOGGER.info("Created directory: " + directoryPath);
            }
            return true;
        } catch (IOException e) {
            LOGGER.severe("Failed to create directory: " + directoryPath + " - " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Get filename from Part
     * @param part The file part
     * @return Filename or null if not found
     */
    public static String getFileName(Part part) {
        String contentDisposition = part.getHeader("content-disposition");
        if (contentDisposition == null) {
            return null;
        }
        
        String[] tokens = contentDisposition.split(";");
        for (String token : tokens) {
            if (token.trim().startsWith("filename")) {
                return token.substring(token.indexOf("=") + 2, token.length() - 1);
            }
        }
        return null;
    }
    
    /**
     * Generate unique filename for user avatar
     * @param userId User ID
     * @param originalFileName Original filename
     * @return Generated unique filename
     */
    public static String generateFileName(int userId, String originalFileName) {
        String extension = getFileExtension(originalFileName);
        String uniqueId = UUID.randomUUID().toString().substring(0, 8);
        return "user_" + userId + "_" + uniqueId + extension;
    }
    
    /**
     * Get file extension from filename
     * @param fileName Filename
     * @return File extension with dot (e.g., ".jpg") or null
     */
    private static String getFileExtension(String fileName) {
        if (fileName == null || fileName.isEmpty()) {
            return null;
        }
        
        int lastDotIndex = fileName.lastIndexOf('.');
        if (lastDotIndex == -1 || lastDotIndex == fileName.length() - 1) {
            return null;
        }
        
        return fileName.substring(lastDotIndex);
    }
    
    /**
     * Save uploaded file to specified path
     * @param filePart The uploaded file part
     * @param uploadPath Directory path to save file
     * @param fileName Filename to save as
     * @return true if saved successfully, false otherwise
     */
    public static boolean saveFile(Part filePart, String uploadPath, String fileName) {
        try (InputStream inputStream = filePart.getInputStream()) {
            File file = new File(uploadPath, fileName);
            
            try (FileOutputStream outputStream = new FileOutputStream(file)) {
                byte[] buffer = new byte[8192];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
                outputStream.flush();
            }
            
            LOGGER.info("File saved successfully: " + file.getAbsolutePath());
            return true;
            
        } catch (IOException e) {
            LOGGER.severe("Failed to save file: " + fileName + " - " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Delete file from filesystem
     * @param filePath Path to file to delete
     * @return true if deleted successfully or file doesn't exist, false otherwise
     */
    public static boolean deleteFile(String filePath) {
        try {
            Path path = Paths.get(filePath);
            if (Files.exists(path)) {
                Files.delete(path);
                LOGGER.info("File deleted successfully: " + filePath);
            }
            return true;
        } catch (IOException e) {
            LOGGER.severe("Failed to delete file: " + filePath + " - " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Check if file exists
     * @param filePath Path to file
     * @return true if file exists, false otherwise
     */
    public static boolean fileExists(String filePath) {
        return Files.exists(Paths.get(filePath));
    }
    
    /**
     * Get file size in bytes
     * @param filePath Path to file
     * @return File size in bytes, -1 if error
     */
    public static long getFileSize(String filePath) {
        try {
            return Files.size(Paths.get(filePath));
        } catch (IOException e) {
            LOGGER.severe("Failed to get file size: " + filePath + " - " + e.getMessage());
            return -1;
        }
    }
}
