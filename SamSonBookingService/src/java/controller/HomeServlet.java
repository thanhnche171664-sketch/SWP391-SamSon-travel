/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import dao.*;
import entity.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * HomeServlet - Main homepage controller
 * Fetches and prepares all data needed for the world-class homepage
 * Handles role-based content and dynamic data loading
 * 
 * @author SamSon Travel Team
 */
@WebServlet(name = "HomeServlet", urlPatterns = {"/home"})
public class HomeServlet extends HttpServlet {
    
    private static final Logger LOGGER = Logger.getLogger(HomeServlet.class.getName());
    
    // DAO instances
    private final HotelDAO hotelDAO = new HotelDAO();
    private final TourMediaDAO tourMediaDAO = new TourMediaDAO();
    private final DiscountDAO discountDAO = new DiscountDAO();
    private final ServiceCategoryDAO serviceCategoryDAO = new ServiceCategoryDAO();
    private final ImageDAO imageDAO = new ImageDAO();
    
    /**
     * Handles GET requests to the homepage
     * Fetches all necessary data and forwards to home.jsp
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        try {
            // Get user session and role
            HttpSession session = request.getSession(false);
            User currentUser = null;
            String userRole = "GUEST";
            
            if (session != null && session.getAttribute("user") != null) {
                currentUser = (User) session.getAttribute("user");
                userRole = getUserRoleName(currentUser.getRoleId());
            }
            
            // Fetch homepage data
            HomepageData homepageData = fetchHomepageData();
            
            // Set request attributes
            request.setAttribute("currentUser", currentUser);
            request.setAttribute("userRole", userRole);
            request.setAttribute("homepageData", homepageData);
            
            // Set page title and meta information
            request.setAttribute("pageTitle", "SamSon Travel - Khám Phá Vẻ Đẹp Sầm Sơn");
            request.setAttribute("pageDescription", "Trải nghiệm du lịch tuyệt vời tại Sầm Sơn với các dịch vụ đa dạng, khách sạn cao cấp và dịch vụ chuyên nghiệp");
            request.setAttribute("pageKeywords", "du lịch sầm sơn, khách sạn sầm sơn, nghỉ dưỡng biển");
            
            // Forward to home.jsp
            request.getRequestDispatcher("/home.jsp").forward(request, response);
            
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error in HomeServlet", e);
            request.setAttribute("errorMessage", "Có lỗi xảy ra khi tải trang chủ. Vui lòng thử lại sau.");
            request.getRequestDispatcher("/error/500.jsp").forward(request, response);
        }
    }
    
    /**
     * Handles POST requests (for search, newsletter, etc.)
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String action = request.getParameter("action");
        
        if ("search".equals(action)) {
            handleSearch(request, response);
        } else if ("newsletter".equals(action)) {
            handleNewsletter(request, response);
        } else {
            // Default to GET handling
            doGet(request, response);
        }
    }
    
    /**
     * Fetch all data needed for homepage
     * @return HomepageData object containing all necessary data
     */
    private HomepageData fetchHomepageData() {
        HomepageData data = new HomepageData();
        
        try {
            // Fetch featured hotels (top 3)
            List<Hotel> hotels = hotelDAO.getFeaturedHotels();
            if (hotels == null || hotels.isEmpty()) {
                hotels = createSampleHotels();
            }
            
            // Load ảnh cho mỗi hotel - ưu tiên image_url từ bảng Hotels, sau đó mới load từ bảng Images
            Map<Integer, String> hotelImages = new HashMap<>();
            for (Hotel hotel : hotels) {
                String imageUrl = null;
                
                // Ưu tiên 1: Kiểm tra image_url đã có trong Hotel object (từ bảng Hotels)
                if (hotel.getImageUrl() != null && !hotel.getImageUrl().trim().isEmpty()) {
                    imageUrl = hotel.getImageUrl();
                    System.out.println("✓ Hotel ID " + hotel.getId() + " - Using image_url from Hotels table: " + imageUrl);
                } else {
                    // Ưu tiên 2: Load từ bảng Images nếu không có trong Hotels table
                    System.out.println("Hotel ID " + hotel.getId() + " - No image_url in Hotels table, checking Images table...");
                    
                    try {
                        Image primaryImage = imageDAO.getPrimaryImage("hotel", hotel.getId());
                        if (primaryImage != null) {
                            imageUrl = primaryImage.getImageUrl();
                            System.out.println("✓ Found PRIMARY image from Images table: " + imageUrl);
                        } else {
                            Image firstImage = imageDAO.getFirstImage("hotel", hotel.getId());
                            if (firstImage != null) {
                                imageUrl = firstImage.getImageUrl();
                                System.out.println("✓ Found FIRST image from Images table: " + imageUrl);
                            } else {
                                // Fallback
                                imageUrl = "uploads/hotel_image/hotel_" + hotel.getId() + ".jpg";
                                System.out.println("✗ No images found, fallback to: " + imageUrl);
                            }
                        }
                    } catch (Exception e) {
                        System.err.println("✗ ERROR getting images: " + e.getMessage());
                        imageUrl = "uploads/hotel_image/hotel_" + hotel.getId() + ".jpg";
                    }
                }
                
                // Đảm bảo imageUrl được set vào Hotel object
                hotel.setImageUrl(imageUrl);
                hotelImages.put(hotel.getId(), imageUrl);
                
                // Debug: Log hotel image URLs
                LOGGER.info("Hotel ID: " + hotel.getId() + ", Name: " + hotel.getName() + 
                           ", ImageUrl: " + imageUrl);
            }
            
            data.setFeaturedHotels(hotels);
            data.setHotelImages(hotelImages);
            
            // Fetch hero section images
            List<TourMedia> heroImages = tourMediaDAO.getHeroImages();
            if (heroImages == null || heroImages.isEmpty()) {
                heroImages = createSampleHeroImages();
            }
            data.setHeroImages(heroImages);
            
            // Fetch active discounts
            data.setActiveDiscounts(discountDAO.getActiveDiscounts());
            
            // Fetch service categories
            data.setServiceCategories(serviceCategoryDAO.getAllCategories());
            
            // Fetch tour gallery images
            data.setTourImages(tourMediaDAO.getTourImages());
            
            // Fetch destination images
            data.setDestinationImages(tourMediaDAO.getDestinationImages());
            
            // Calculate statistics
            data.setTotalHotels(hotels.size());
            
            // Ensure statistics are not zero
            if (data.getTotalHotels() == 0) data.setTotalHotels(2);
            
            LOGGER.info("Homepage data fetched successfully");
            
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error fetching homepage data", e);
            // Fallback to sample data
            data = createSampleHomepageData();
        }
        
        return data;
    }
    
    /**
     * Create sample hotels for fallback
     */
    private List<Hotel> createSampleHotels() {
        List<Hotel> hotels = new ArrayList<>();
        
        Hotel hotel1 = new Hotel();
        hotel1.setId(1);
        hotel1.setName("FLC Sầm Sơn Resort");
        hotel1.setAddress("Sầm Sơn, Thanh Hóa");
        hotel1.setRating(4.8);
        hotel1.setAmenities("WiFi,Pool,Restaurant,Spa");
        hotel1.setImageUrl("uploads/hotel_image/hotel_1.jpg");
        hotels.add(hotel1);
        
        Hotel hotel2 = new Hotel();
        hotel2.setId(2);
        hotel2.setName("Sunworld Sầm Sơn");
        hotel2.setAddress("Sầm Sơn, Thanh Hóa");
        hotel2.setRating(4.5);
        hotel2.setAmenities("WiFi,Pool,Restaurant");
        hotel2.setImageUrl("uploads/hotel_image/hotel_2.jpg");
        hotels.add(hotel2);
        
        return hotels;
    }
    
    /**
     * Create sample hero images for fallback
     */
    private List<TourMedia> createSampleHeroImages() {
        List<TourMedia> images = new ArrayList<>();
        
        TourMedia image1 = new TourMedia();
        image1.setMediaId(1);
        image1.setFileUrl("heroSection/bai-bien-sam-son-1-1024x682.webp");
        image1.setTitle("Bãi biển Sầm Sơn");
        image1.setDescription("Bãi biển đẹp nhất tại Sầm Sơn");
        images.add(image1);
        
        TourMedia image2 = new TourMedia();
        image2.setMediaId(2);
        image2.setFileUrl("heroSection/du-an-flc-sam-son-canh-quan-xanh.jpg");
        image2.setTitle("FLC Sầm Sơn");
        image2.setDescription("Resort cao cấp với view biển tuyệt đẹp");
        images.add(image2);
        
        return images;
    }
    
    /**
     * Create complete sample homepage data for fallback
     */
    private HomepageData createSampleHomepageData() {
        HomepageData data = new HomepageData();
        List<Hotel> sampleHotels = createSampleHotels();
        data.setFeaturedHotels(sampleHotels);
        
        // Create hotel images map for sample hotels
        Map<Integer, String> hotelImages = new HashMap<>();
        for (Hotel hotel : sampleHotels) {
            if (hotel.getImageUrl() != null && !hotel.getImageUrl().isEmpty()) {
                hotelImages.put(hotel.getId(), hotel.getImageUrl());
            } else {
                hotelImages.put(hotel.getId(), "uploads/hotels/default.jpg");
            }
        }
        data.setHotelImages(hotelImages);
        
        data.setHeroImages(createSampleHeroImages());
        data.setTotalHotels(sampleHotels.size());
        return data;
    }
    
    /**
     * Handle search functionality
     */
    private void handleSearch(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String searchQuery = request.getParameter("searchQuery");
        String searchType = request.getParameter("searchType");
        
        if (searchQuery != null && !searchQuery.trim().isEmpty()) {
            // Perform search based on type
            if ("hotels".equals(searchType)) {
                List<Hotel> searchResults = hotelDAO.searchHotels(searchQuery);
                request.setAttribute("searchResults", searchResults);
                request.setAttribute("searchType", "hotels");
            }
            
            request.setAttribute("searchQuery", searchQuery);
        }
        
        // Forward back to homepage with search results
        doGet(request, response);
    }
    
    /**
     * Handle newsletter subscription
     */
    private void handleNewsletter(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String email = request.getParameter("email");
        
        if (email != null && !email.trim().isEmpty()) {
            // TODO: Implement newsletter subscription logic
            request.setAttribute("newsletterMessage", "Cảm ơn bạn đã đăng ký nhận tin! Chúng tôi sẽ gửi thông tin mới nhất về khuyến mãi.");
        } else {
            request.setAttribute("newsletterError", "Vui lòng nhập địa chỉ email hợp lệ.");
        }
        
        // Forward back to homepage
        doGet(request, response);
    }
    
    /**
     * Get user role name by role ID
     * @param roleId Role ID
     * @return Role name
     */
    private String getUserRoleName(int roleId) {
        switch (roleId) {
            case 1: return "ADMINISTRATOR";
            case 2: return "SERVICE_MANAGER";
            case 3: return "HOTEL_MANAGER";
            case 4: return "CUSTOMER";
            case 5: return "FRONT_OFFICE";
            default: return "GUEST";
        }
    }
    
    /**
     * Inner class to hold all homepage data
     */
    public static class HomepageData {
        private List<Hotel> featuredHotels;
        private Map<Integer, String> hotelImages;
        private List<TourMedia> heroImages;
        private List<Discount> activeDiscounts;
        private List<ServiceCategory> serviceCategories;
        private List<TourMedia> tourImages;
        private List<TourMedia> destinationImages;
        private int totalHotels;
        
        // Getters and Setters
        public List<Hotel> getFeaturedHotels() { return featuredHotels; }
        public void setFeaturedHotels(List<Hotel> featuredHotels) { this.featuredHotels = featuredHotels; }
        
        public Map<Integer, String> getHotelImages() { return hotelImages; }
        public void setHotelImages(Map<Integer, String> hotelImages) { this.hotelImages = hotelImages; }
        
        public List<TourMedia> getHeroImages() { return heroImages; }
        public void setHeroImages(List<TourMedia> heroImages) { this.heroImages = heroImages; }
        
        public List<Discount> getActiveDiscounts() { return activeDiscounts; }
        public void setActiveDiscounts(List<Discount> activeDiscounts) { this.activeDiscounts = activeDiscounts; }
        
        public List<ServiceCategory> getServiceCategories() { return serviceCategories; }
        public void setServiceCategories(List<ServiceCategory> serviceCategories) { this.serviceCategories = serviceCategories; }
        
        public List<TourMedia> getTourImages() { return tourImages; }
        public void setTourImages(List<TourMedia> tourImages) { this.tourImages = tourImages; }
        
        public List<TourMedia> getDestinationImages() { return destinationImages; }
        public void setDestinationImages(List<TourMedia> destinationImages) { this.destinationImages = destinationImages; }
        
        public int getTotalHotels() { return totalHotels; }
        public void setTotalHotels(int totalHotels) { this.totalHotels = totalHotels; }
    }
}
