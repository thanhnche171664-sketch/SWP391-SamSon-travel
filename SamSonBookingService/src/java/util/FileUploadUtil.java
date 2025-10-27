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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Utility class for handling file uploads
 * Provides methods for validating and saving uploaded files
 * 
 * @author SamSon Travel Team
 */
public class FileUploadUtil {
    
    private static final Logger LOGGER = Logger.getLogger(FileUploadUtil.class.getName());
    
    // Allowed file types for avatar upload
    private static final String[] ALLOWED_TYPES = {"image/jpeg", "image/jpg", "image/png", "image/gif"};
    private static final String[] ALLOWED_EXTENSIONS = {".jpg", ".jpeg", ".png", ".gif"};
    
    // File size limits (5MB in bytes)
    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024;
    
    /**
     * Validate uploaded image file
     * 
     * @param filePart The uploaded file part
     * @return Validation result message (null if valid)
     */
    public static String validateImageFile(Part filePart) {
        if (filePart == null || filePart.getSize() == 0) {
            return "Không có file được chọn";
        }
        
        // Check file size
        if (filePart.getSize() > MAX_FILE_SIZE) {
            return "File quá lớn. Kích thước tối đa là 5MB";
        }
        
        // Check content type
        String contentType = filePart.getContentType();
        boolean isValidType = false;
        for (String type : ALLOWED_TYPES) {
            if (type.equalsIgnoreCase(contentType)) {
                isValidType = true;
                break;
            }
        }
        
        if (!isValidType) {
            return "Định dạng file không được hỗ trợ. Chỉ chấp nhận JPG, PNG, GIF";
        }
        
        // Check file extension
        String fileName = getFileName(filePart);
        if (fileName != null && fileName.contains(".")) {
            String extension = fileName.substring(fileName.lastIndexOf(".")).toLowerCase();
            boolean isValidExtension = false;
            for (String ext : ALLOWED_EXTENSIONS) {
                if (ext.equals(extension)) {
                    isValidExtension = true;
                    break;
                }
            }
            
            if (!isValidExtension) {
                return "Phần mở rộng file không hợp lệ. Chỉ chấp nhận .jpg, .jpeg, .png, .gif";
            }
        }
        
        LOGGER.info("File validation successful for: " + fileName);
        return null; // Valid file
    }
    
    /**
     * Save uploaded file to disk
     * 
     * @param filePart The uploaded file part
     * @param uploadPath Directory path to save file
     * @param fileName Desired file name
     * @return true if save successful, false otherwise
     */
    public static boolean saveFile(Part filePart, String uploadPath, String fileName) {
        if (filePart == null || uploadPath == null || fileName == null) {
            LOGGER.warning("Invalid parameters for saveFile");
            return false;
        }
        
        try {
            // Create upload directory if not exists
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
                LOGGER.info("Created upload directory: " + uploadPath);
            }
            
            // Create file path
            String filePath = uploadPath + File.separator + fileName;
            File file = new File(filePath);
            
            // Save file
            try (InputStream inputStream = filePart.getInputStream();
                 FileOutputStream outputStream = new FileOutputStream(file)) {
                
                byte[] buffer = new byte[8192];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
                
                LOGGER.info("File saved successfully: " + filePath);
                return true;
            }
            
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error saving file: " + fileName, e);
            return false;
        }
    }
    
    /**
     * Delete a file from disk
     * 
     * @param filePath Path to file to delete
     * @return true if delete successful, false otherwise
     */
    public static boolean deleteFile(String filePath) {
        if (filePath == null || filePath.trim().isEmpty()) {
            LOGGER.warning("Invalid file path for deletion");
            return false;
        }
        
        try {
            File file = new File(filePath);
            if (file.exists() && file.isFile()) {
                boolean deleted = file.delete();
                if (deleted) {
                    LOGGER.info("File deleted successfully: " + filePath);
                } else {
                    LOGGER.warning("Failed to delete file: " + filePath);
                }
                return deleted;
            } else {
                LOGGER.warning("File not found: " + filePath);
                return false;
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error deleting file: " + filePath, e);
            return false;
        }
    }
    
    /**
     * Get file name from Part
     * 
     * @param part File part
     * @return File name or null if not found
     */
    public static String getFileName(Part part) {
        String contentDisposition = part.getHeader("content-disposition");
        String[] tokens = contentDisposition.split(";");
        for (String token : tokens) {
            if (token.trim().startsWith("filename")) {
                String fileName = token.substring(token.indexOf("=") + 1);
                // Remove quotes
                if (fileName.startsWith("\"") && fileName.endsWith("\"")) {
                    fileName = fileName.substring(1, fileName.length() - 1);
                }
                return fileName;
            }
        }
        return null;
    }
    
    /**
     * Generate unique file name
     * Format: user_{userId}_{timestamp}.{extension}
     * 
     * @param userId User ID
     * @param originalFileName Original file name to extract extension
     * @return Generated file name
     */
    public static String generateFileName(int userId, String originalFileName) {
        String extension = "";
        if (originalFileName != null && originalFileName.contains(".")) {
            extension = originalFileName.substring(originalFileName.lastIndexOf("."));
        }
        long timestamp = System.currentTimeMillis();
        return "user_" + userId + "_" + timestamp + extension;
    }
    
    /**
     * Get file extension from file name
     * 
     * @param fileName File name
     * @return Extension including dot (e.g., ".jpg")
     */
    public static String getFileExtension(String fileName) {
        if (fileName == null || !fileName.contains(".")) {
            return "";
        }
        return fileName.substring(fileName.lastIndexOf("."));
    }
    
    /**
     * Check if directory exists, create if not
     * 
     * @param dirPath Directory path
     * @return true if directory exists or created successfully
     */
    public static boolean ensureDirectoryExists(String dirPath) {
        try {
            File dir = new File(dirPath);
            if (!dir.exists()) {
                return dir.mkdirs();
            }
            return true;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error creating directory: " + dirPath, e);
            return false;
        }
    }
}

