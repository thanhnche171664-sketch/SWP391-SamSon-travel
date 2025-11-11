/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util;

import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import jakarta.mail.*;
import jakarta.mail.internet.*;

/**
 * Email utility class for sending verification emails
 * Uses Gmail SMTP for email delivery
 * 
 * @author SamSon Travel Team
 */
public class EmailUtil {
    
    private static final Logger LOGGER = Logger.getLogger(EmailUtil.class.getName());
    
    // Gmail SMTP configuration
    private static final String SMTP_HOST = "smtp.gmail.com";
    private static final String SMTP_PORT = "587";
    private static final String SMTP_USERNAME = "longrpk200313@gmail.com";
    private static final String SMTP_PASSWORD = "xjrt ypar fydp xuow"; // Replace with actual app password
    
    // Email templates
    private static final String FROM_NAME = "SamSon Travel";
    private static final String FROM_EMAIL = SMTP_USERNAME;
    
    /**
     * Send verification email to user
     * 
     * @param toEmail Recipient email address
     * @param token Verification token
     * @return true if email sent successfully, false otherwise
     */
    public static boolean sendVerificationEmail(String toEmail, String token) {
        if (toEmail == null || toEmail.trim().isEmpty()) {
            LOGGER.warning("Recipient email is null or empty");
            return false;
        }
        
        if (token == null || token.trim().isEmpty()) {
            LOGGER.warning("Verification token is null or empty");
            return false;
        }
        
        try {
            // Create verification link
            String verificationLink = "http://localhost:9999/SWP/verify-email?token=" + token;
            
            // Email subject and content
            String subject = "Xác nhận tài khoản - SamSon Travel";
            String htmlContent = createVerificationEmailContent(toEmail, verificationLink);
            
            // Send email
            boolean sent = sendEmail(toEmail, subject, htmlContent, true);
            
            if (sent) {
                LOGGER.info("Verification email sent successfully to: " + toEmail);
            } else {
                LOGGER.warning("Failed to send verification email to: " + toEmail);
            }
            
            return sent;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error sending verification email to: " + toEmail, e);
            return false;
        }
    }
    
    /**
     * Send password reset email
     * 
     * @param toEmail Recipient email address
     * @param token Reset token
     * @return true if email sent successfully, false otherwise
     */
    public static boolean sendPasswordResetEmail(String toEmail, String token) {
        if (toEmail == null || toEmail.trim().isEmpty()) {
            LOGGER.warning("Recipient email is null or empty");
            return false;
        }
        
        if (token == null || token.trim().isEmpty()) {
            LOGGER.warning("Reset token is null or empty");
            return false;
        }
        
        try {
            // Create reset link (legacy default). Prefer sendPasswordResetEmailWithLink overload.
            String resetLink = "http://localhost:9999/SWP/reset-password?token=" + token;
            
            String subject = "Đặt lại mật khẩu - SamSon Travel";
            String htmlContent = createPasswordResetEmailContent(toEmail, resetLink);
            
            // Send email
            boolean sent = sendEmail(toEmail, subject, htmlContent, true);
            
            if (sent) {
                LOGGER.info("Password reset email sent successfully to: " + toEmail);
            } else {
                LOGGER.warning("Failed to send password reset email to: " + toEmail);
            }
            
            return sent;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error sending password reset email to: " + toEmail, e);
            return false;
        }
    }
    
    /**
     * Send password reset email using a fully-formed reset link.
     * Prefer this overload in controllers to support dynamic base URL.
     *
     * @param toEmail Recipient email address
     * @param fullResetLink Fully-qualified reset link
     * @return true if email sent successfully, false otherwise
     */
    public static boolean sendPasswordResetEmailWithLink(String toEmail, String fullResetLink) {
        if (toEmail == null || toEmail.trim().isEmpty()) {
            LOGGER.warning("Recipient email is null or empty");
            return false;
        }
        if (fullResetLink == null || fullResetLink.trim().isEmpty()) {
            LOGGER.warning("Reset link is null or empty");
            return false;
        }
        try {
            String subject = "Đặt lại mật khẩu - SamSon Travel";
            String htmlContent = createPasswordResetEmailContent(toEmail, fullResetLink);
            boolean sent = sendEmail(toEmail, subject, htmlContent, true);
            if (sent) {
                LOGGER.info("Password reset email (with link) sent successfully to: " + toEmail);
            } else {
                LOGGER.warning("Failed to send password reset email (with link) to: " + toEmail);
            }
            return sent;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error sending password reset email (with link) to: " + toEmail, e);
            return false;
        }
    }
    
    /**
     * Send welcome email after successful verification
     * 
     * @param toEmail Recipient email address
     * @param userName User's name
     * @return true if email sent successfully, false otherwise
     */
    public static boolean sendWelcomeEmail(String toEmail, String userName) {
        if (toEmail == null || toEmail.trim().isEmpty()) {
            LOGGER.warning("Recipient email is null or empty");
            return false;
        }
        
        try {
            String subject = "Chào mừng đến với SamSon Travel!";
            String htmlContent = createWelcomeEmailContent(userName);
            
            boolean sent = sendEmail(toEmail, subject, htmlContent, true);
            
            if (sent) {
                LOGGER.info("Welcome email sent successfully to: " + toEmail);
            } else {
                LOGGER.warning("Failed to send welcome email to: " + toEmail);
            }
            
            return sent;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error sending welcome email to: " + toEmail, e);
            return false;
        }
    }
    
    /**
     * Core method to send email
     * 
     * @param toEmail Recipient email
     * @param subject Email subject
     * @param content Email content
     * @param isHtml Whether content is HTML
     * @return true if sent successfully
     */
    private static boolean sendEmail(String toEmail, String subject, String content, boolean isHtml) {
        try {
            // Configure SMTP properties
            Properties props = new Properties();
            props.put("mail.smtp.host", SMTP_HOST);
            props.put("mail.smtp.port", SMTP_PORT);
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.starttls.required", "true");
            props.put("mail.smtp.ssl.trust", SMTP_HOST);
            
            // Create session with authentication
            Session session = Session.getInstance(props, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(SMTP_USERNAME, SMTP_PASSWORD);
                }
            });
            
            // Create message
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(FROM_EMAIL, FROM_NAME));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject(subject);
            
            if (isHtml) {
                message.setContent(content, "text/html; charset=utf-8");
            } else {
                message.setText(content);
            }
            
            // Send message
            Transport.send(message);
            return true;
            
        } catch (MessagingException e) {
            LOGGER.log(Level.SEVERE, "Messaging error sending email to: " + toEmail, e);
            return false;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "General error sending email to: " + toEmail, e);
            return false;
        }
    }
    
    /**
     * Create HTML content for verification email
     */
    private static String createVerificationEmailContent(String email, String verificationLink) {
        return String.format("""
            <!DOCTYPE html>
            <html>
            <head>
                <meta charset="UTF-8">
                <title>Xác nhận tài khoản</title>
                <style>
                    body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; }
                    .container { max-width: 600px; margin: 0 auto; padding: 20px; }
                    .header { background: linear-gradient(135deg, ##667eea 0%%, ##764ba2 100%%); color: white; padding: 30px; text-align: center; border-radius: 10px 10px 0 0; }
                    .content { background: #f9f9f9; padding: 30px; border-radius: 0 0 10px 10px; }
                    .button { display: inline-block; background: ##667eea; color: white; padding: 15px 30px; text-decoration: none; border-radius: 5px; margin: 20px 0; }
                    .footer { text-align: center; margin-top: 30px; color: #666; font-size: 14px; }
                </style>
            </head>
            <body>
                <div class="container">
                    <div class="header">
                        <h1>🏖️ SamSon Travel</h1>
                        <p>Xác nhận tài khoản của bạn</p>
                    </div>
                    <div class="content">
                        <h2>Xin chào!</h2>
                        <p>Cảm ơn bạn đã đăng ký tài khoản tại <strong>SamSon Travel</strong>.</p>
                        <p>Để hoàn tất quá trình đăng ký, vui lòng nhấn vào nút bên dưới để xác nhận địa chỉ email của bạn:</p>
                        <p style="text-align: center;">
                            <a href="%s" class="button">Xác nhận Email</a>
                        </p>
                        <p>Hoặc copy và paste link này vào trình duyệt:</p>
                        <p style="word-break: break-all; background: #eee; padding: 10px; border-radius: 5px;">%s</p>
                        <p><strong>Lưu ý:</strong> Link này sẽ hết hạn sau 24 giờ.</p>
                        <p>Nếu bạn không đăng ký tài khoản này, vui lòng bỏ qua email này.</p>
                    </div>
                    <div class="footer">
                        <p>© 2024 SamSon Travel. Tất cả quyền được bảo lưu.</p>
                        <p>Email này được gửi tự động, vui lòng không trả lời.</p>
                    </div>
                </div>
            </body>
            </html>
            """, verificationLink, verificationLink);
    }
    
    /**
     * Create HTML content for password reset email
     */
    private static String createPasswordResetEmailContent(String email, String resetLink) {
        return String.format("""
            <!DOCTYPE html>
            <html>
            <head>
                <meta charset="UTF-8">
                <title>Đặt lại mật khẩu</title>
                <style>
                    body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; }
                    .container { max-width: 600px; margin: 0 auto; padding: 20px; }
                    .header { background: linear-gradient(135deg, ##ff6b6b 0%%, ##ee5a24 100%%); color: white; padding: 30px; text-align: center; border-radius: 10px 10px 0 0; }
                    .content { background: #f9f9f9; padding: 30px; border-radius: 0 0 10px 10px; }
                    .button { display: inline-block; background: ##ff6b6b; color: white; padding: 15px 30px; text-decoration: none; border-radius: 5px; margin: 20px 0; }
                    .footer { text-align: center; margin-top: 30px; color: #666; font-size: 14px; }
                </style>
            </head>
            <body>
                <div class="container">
                    <div class="header">
                        <h1>🔒 SamSon Travel</h1>
                        <p>Đặt lại mật khẩu</p>
                    </div>
                    <div class="content">
                        <h2>Yêu cầu đặt lại mật khẩu</h2>
                        <p>Chúng tôi nhận được yêu cầu đặt lại mật khẩu cho tài khoản của bạn.</p>
                        <p>Nhấn vào nút bên dưới để tạo mật khẩu mới:</p>
                        <p style="text-align: center;">
                            <a href="%s" class="button">Đặt lại mật khẩu</a>
                        </p>
                        <p>Hoặc copy và paste link này vào trình duyệt:</p>
                        <p style="word-break: break-all; background: #eee; padding: 10px; border-radius: 5px;">%s</p>
                        <p><strong>Lưu ý:</strong> Link này sẽ hết hạn sau 1 giờ.</p>
                        <p>Nếu bạn không yêu cầu đặt lại mật khẩu, vui lòng bỏ qua email này.</p>
                    </div>
                    <div class="footer">
                        <p>© 2024 SamSon Travel. Tất cả quyền được bảo lưu.</p>
                        <p>Email này được gửi tự động, vui lòng không trả lời.</p>
                    </div>
                </div>
            </body>
            </html>
            """, resetLink, resetLink);
    }
    
    /**
     * Create HTML content for welcome email
     */
    private static String createWelcomeEmailContent(String userName) {
        return String.format("""
            <!DOCTYPE html>
            <html>
            <head>
                <meta charset="UTF-8">
                <title>Chào mừng đến với SamSon Travel</title>
                <style>
                    body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; }
                    .container { max-width: 600px; margin: 0 auto; padding: 20px; }
                    .header { background: linear-gradient(135deg, ##00b894 0%%, ##00a085 100%%); color: white; padding: 30px; text-align: center; border-radius: 10px 10px 0 0; }
                    .content { background: #f9f9f9; padding: 30px; border-radius: 0 0 10px 10px; }
                    .button { display: inline-block; background: ##00b894; color: white; padding: 15px 30px; text-decoration: none; border-radius: 5px; margin: 20px 0; }
                    .footer { text-align: center; margin-top: 30px; color: #666; font-size: 14px; }
                </style>
            </head>
            <body>
                <div class="container">
                    <div class="header">
                        <h1>🎉 SamSon Travel</h1>
                        <p>Chào mừng bạn!</p>
                    </div>
                    <div class="content">
                        <h2>Xin chào %s!</h2>
                        <p>Chúc mừng! Tài khoản của bạn đã được xác nhận thành công.</p>
                        <p>Bây giờ bạn có thể:</p>
                        <ul>
                            <li>Đặt tour du lịch</li>
                            <li>Quản lý thông tin cá nhân</li>
                            <li>Theo dõi lịch sử đặt tour</li>
                            <li>Nhận thông báo về các tour mới</li>
                        </ul>
                        <p style="text-align: center;">
                            <a href="http://localhost:9999/SWP/login" class="button">Đăng nhập ngay</a>
                        </p>
                        <p>Cảm ơn bạn đã tin tưởng và lựa chọn <strong>SamSon Travel</strong>!</p>
                    </div>
                    <div class="footer">
                        <p>© 2024 SamSon Travel. Tất cả quyền được bảo lưu.</p>
                        <p>Email này được gửi tự động, vui lòng không trả lời.</p>
                    </div>
                </div>
            </body>
            </html>
            """, userName != null ? userName : "Bạn");
    }
    
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
        
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return email.matches(emailRegex);
    }
}
