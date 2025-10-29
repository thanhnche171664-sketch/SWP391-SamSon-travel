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
import java.util.List;
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
    private final TourDAO tourDAO = new TourDAO();
    private final HotelDAO hotelDAO = new HotelDAO();
    private final TourMediaDAO tourMediaDAO = new TourMediaDAO();
    private final TestimonialDAO testimonialDAO = new TestimonialDAO();
    private final DiscountDAO discountDAO = new DiscountDAO();
    private final ServiceCategoryDAO serviceCategoryDAO = new ServiceCategoryDAO();
    
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
            request.setAttribute("pageDescription", "Trải nghiệm du lịch tuyệt vời tại Sầm Sơn với các tour đa dạng, khách sạn cao cấp và dịch vụ chuyên nghiệp");
            request.setAttribute("pageKeywords", "du lịch sầm sơn, tour sầm sơn, khách sạn sầm sơn, nghỉ dưỡng biển");
            
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
            // Fetch featured tours (top 6)
            List<Tour> tours = tourDAO.getFeaturedTours();
            if (tours == null || tours.isEmpty()) {
                tours = createSampleTours();
            }
            data.setFeaturedTours(tours);
            
            // Fetch featured hotels (top 3)
            List<Hotel> hotels = hotelDAO.getFeaturedHotels();
            if (hotels == null || hotels.isEmpty()) {
                hotels = createSampleHotels();
            }
            data.setFeaturedHotels(hotels);
            
            // Fetch hero section images
            List<TourMedia> heroImages = tourMediaDAO.getHeroImages();
            if (heroImages == null || heroImages.isEmpty()) {
                heroImages = createSampleHeroImages();
            }
            data.setHeroImages(heroImages);
            
            // Fetch featured testimonials (top 6 with rating >= 4)
            List<Testimonial> testimonials = testimonialDAO.getFeaturedTestimonials();
            if (testimonials == null || testimonials.isEmpty()) {
                testimonials = createSampleTestimonials();
            }
            data.setFeaturedTestimonials(testimonials);
            
            // Fetch active discounts
            data.setActiveDiscounts(discountDAO.getActiveDiscounts());
            
            // Fetch service categories
            data.setServiceCategories(serviceCategoryDAO.getAllCategories());
            
            // Fetch tour gallery images
            data.setTourImages(tourMediaDAO.getTourImages());
            
            // Fetch destination images
            data.setDestinationImages(tourMediaDAO.getDestinationImages());
            
            // Calculate statistics
            data.setTotalTours(tours.size());
            data.setTotalHotels(hotels.size());
            data.setTotalTestimonials(testimonials.size());
            
            // Ensure statistics are not zero
            if (data.getTotalTours() == 0) data.setTotalTours(3);
            if (data.getTotalHotels() == 0) data.setTotalHotels(2);
            if (data.getTotalTestimonials() == 0) data.setTotalTestimonials(2);
            
            LOGGER.info("Homepage data fetched successfully");
            
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error fetching homepage data", e);
            // Fallback to sample data
            data = createSampleHomepageData();
        }
        
        return data;
    }
    
    /**
     * Create sample tours for fallback
     */
    private List<Tour> createSampleTours() {
        List<Tour> tours = new ArrayList<>();
        
        Tour tour1 = new Tour();
        tour1.setTourId(1);
        tour1.setTourName("Tour Sầm Sơn 2 Ngày 1 Đêm");
        tour1.setDescription("Khám phá vẻ đẹp của bãi biển Sầm Sơn với tour trọn gói 2 ngày 1 đêm");
        tour1.setDurationDays(2);
        tour1.setDurationNights(1);
        tour1.setBasePrice(1500000);
        tour1.setFeaturedImage("hero/samson-beach-1.jpg");
        tours.add(tour1);
        
        Tour tour2 = new Tour();
        tour2.setTourId(2);
        tour2.setTourName("Tour Đảo Hòn Mê");
        tour2.setDescription("Trải nghiệm thiên nhiên hoang sơ tại đảo Hòn Mê");
        tour2.setDurationDays(1);
        tour2.setDurationNights(0);
        tour2.setBasePrice(800000);
        tour2.setFeaturedImage("hero/hon-me-island.jpg");
        tours.add(tour2);
        
        Tour tour3 = new Tour();
        tour3.setTourId(3);
        tour3.setTourName("Tour Chùa Độc Cước");
        tour3.setDescription("Tham quan chùa Độc Cước - di tích lịch sử nổi tiếng");
        tour3.setDurationDays(1);
        tour3.setDurationNights(0);
        tour3.setBasePrice(500000);
        tour3.setFeaturedImage("hero/doc-cuoc-temple.jpg");
        tours.add(tour3);
        
        return tours;
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
        hotels.add(hotel1);
        
        Hotel hotel2 = new Hotel();
        hotel2.setId(2);
        hotel2.setName("Sunworld Sầm Sơn");
        hotel2.setAddress("Sầm Sơn, Thanh Hóa");
        hotel2.setRating(4.5);
        hotel2.setAmenities("WiFi,Pool,Restaurant");
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
     * Create sample testimonials for fallback
     */
    private List<Testimonial> createSampleTestimonials() {
        List<Testimonial> testimonials = new ArrayList<>();
        
        Testimonial testimonial1 = new Testimonial();
        testimonial1.setTestimonialId(1);
        testimonial1.setCustomerName("Nguyễn Văn A");
        testimonial1.setReviewText("Tour rất tuyệt vời, hướng dẫn viên nhiệt tình và chuyên nghiệp");
        testimonial1.setRating(5);
        testimonial1.setCustomerAvatar("default-avatar.jpg");
        testimonials.add(testimonial1);
        
        Testimonial testimonial2 = new Testimonial();
        testimonial2.setTestimonialId(2);
        testimonial2.setCustomerName("Trần Thị B");
        testimonial2.setReviewText("Khách sạn đẹp, dịch vụ tốt, sẽ quay lại lần sau");
        testimonial2.setRating(5);
        testimonial2.setCustomerAvatar("default-avatar.jpg");
        testimonials.add(testimonial2);
        
        return testimonials;
    }
    
    /**
     * Create complete sample homepage data for fallback
     */
    private HomepageData createSampleHomepageData() {
        HomepageData data = new HomepageData();
        data.setFeaturedTours(createSampleTours());
        data.setFeaturedHotels(createSampleHotels());
        data.setHeroImages(createSampleHeroImages());
        data.setFeaturedTestimonials(createSampleTestimonials());
        data.setTotalTours(3);
        data.setTotalHotels(2);
        data.setTotalTestimonials(2);
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
            if ("tours".equals(searchType)) {
                List<Tour> searchResults = tourDAO.searchTours(searchQuery);
                request.setAttribute("searchResults", searchResults);
                request.setAttribute("searchType", "tours");
            } else if ("hotels".equals(searchType)) {
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
            request.setAttribute("newsletterMessage", "Cảm ơn bạn đã đăng ký nhận tin! Chúng tôi sẽ gửi thông tin mới nhất về tour và khuyến mãi.");
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
        private List<Tour> featuredTours;
        private List<Hotel> featuredHotels;
        private List<TourMedia> heroImages;
        private List<Testimonial> featuredTestimonials;
        private List<Discount> activeDiscounts;
        private List<ServiceCategory> serviceCategories;
        private List<TourMedia> tourImages;
        private List<TourMedia> destinationImages;
        private int totalTours;
        private int totalHotels;
        private int totalTestimonials;
        
        // Getters and Setters
        public List<Tour> getFeaturedTours() { return featuredTours; }
        public void setFeaturedTours(List<Tour> featuredTours) { this.featuredTours = featuredTours; }
        
        public List<Hotel> getFeaturedHotels() { return featuredHotels; }
        public void setFeaturedHotels(List<Hotel> featuredHotels) { this.featuredHotels = featuredHotels; }
        
        public List<TourMedia> getHeroImages() { return heroImages; }
        public void setHeroImages(List<TourMedia> heroImages) { this.heroImages = heroImages; }
        
        public List<Testimonial> getFeaturedTestimonials() { return featuredTestimonials; }
        public void setFeaturedTestimonials(List<Testimonial> featuredTestimonials) { this.featuredTestimonials = featuredTestimonials; }
        
        public List<Discount> getActiveDiscounts() { return activeDiscounts; }
        public void setActiveDiscounts(List<Discount> activeDiscounts) { this.activeDiscounts = activeDiscounts; }
        
        public List<ServiceCategory> getServiceCategories() { return serviceCategories; }
        public void setServiceCategories(List<ServiceCategory> serviceCategories) { this.serviceCategories = serviceCategories; }
        
        public List<TourMedia> getTourImages() { return tourImages; }
        public void setTourImages(List<TourMedia> tourImages) { this.tourImages = tourImages; }
        
        public List<TourMedia> getDestinationImages() { return destinationImages; }
        public void setDestinationImages(List<TourMedia> destinationImages) { this.destinationImages = destinationImages; }
        
        public int getTotalTours() { return totalTours; }
        public void setTotalTours(int totalTours) { this.totalTours = totalTours; }
        
        public int getTotalHotels() { return totalHotels; }
        public void setTotalHotels(int totalHotels) { this.totalHotels = totalHotels; }
        
        public int getTotalTestimonials() { return totalTestimonials; }
        public void setTotalTestimonials(int totalTestimonials) { this.totalTestimonials = totalTestimonials; }
    }
}
