/**
 * Main JavaScript file for SamSon Travel Homepage
 * Handles core functionality, animations, and user interactions
 */

// Global variables
let isScrolling = false;
let scrollTimeout;
let currentHeroSlide = 0;
let heroSlideInterval;

// DOM elements
const elements = {
    header: document.getElementById('header'),
    navLinks: document.querySelectorAll('.nav-link'),
    mobileMenuToggle: document.getElementById('mobileMenuToggle'),
    nav: document.getElementById('nav'),
    heroSlider: document.getElementById('heroSlider'),
    backToTop: document.getElementById('backToTop'),
    bookingForm: document.getElementById('bookingForm'),
    newsletterForm: document.getElementById('newsletterForm'),
    testimonialsSlider: document.getElementById('testimonialsSlider'),
    testimonialsContainer: document.getElementById('testimonialsContainer'),
    testimonialPrev: document.getElementById('testimonialPrev'),
    testimonialNext: document.getElementById('testimonialNext'),
    toastContainer: document.getElementById('toastContainer')
};

// Initialize homepage
function initializeHomepage() {
    initializeScrollEffects();
    initializeHeroSlider();
    initializeMobileMenu();
    initializeBookingForm();
    initializeNewsletterForm();
    initializeTestimonialsSlider();
    initializeBackToTop();
    initializeAnimations();
    initializeLazyLoading();
    initializeParallax();
    initializeAccessibility();
    
    console.log('Homepage initialized successfully');
}

// Scroll effects
function initializeScrollEffects() {
    let lastScrollTop = 0;
    
    window.addEventListener('scroll', () => {
        if (!isScrolling) {
            window.requestAnimationFrame(() => {
                handleScroll();
                lastScrollTop = window.pageYOffset;
                isScrolling = false;
            });
            isScrolling = true;
        }
    });
}

function handleScroll() {
    const scrollTop = window.pageYOffset;
    
    // Header scroll effect
    if (scrollTop > 100) {
        elements.header.classList.add('scrolled');
    } else {
        elements.header.classList.remove('scrolled');
    }
    
    // Back to top button
    if (scrollTop > 500) {
        elements.backToTop.classList.add('visible');
    } else {
        elements.backToTop.classList.remove('visible');
    }
    
    // Active navigation link
    updateActiveNavLink();
    
    // Parallax effects
    updateParallaxElements(scrollTop);
}

// Hero slider
function initializeHeroSlider() {
    if (!elements.heroSlider) return;
    
    const slides = elements.heroSlider.querySelectorAll('.hero-slide');
    if (slides.length <= 1) return;
    
    // Auto-advance slides
    heroSlideInterval = setInterval(() => {
        nextHeroSlide();
    }, 5000);
    
    // Pause on hover
    elements.heroSlider.addEventListener('mouseenter', () => {
        clearInterval(heroSlideInterval);
    });
    
    elements.heroSlider.addEventListener('mouseleave', () => {
        heroSlideInterval = setInterval(() => {
            nextHeroSlide();
        }, 5000);
    });
}

function nextHeroSlide() {
    const slides = elements.heroSlider.querySelectorAll('.hero-slide');
    const totalSlides = slides.length;
    
    slides[currentHeroSlide].classList.remove('active');
    currentHeroSlide = (currentHeroSlide + 1) % totalSlides;
    slides[currentHeroSlide].classList.add('active');
}

// Accessibility features
function initializeAccessibility() {
    initializeKeyboardNavigation();
    initializeARIALiveRegions();
    initializeFocusManagement();
    initializeScreenReaderSupport();
}

function initializeKeyboardNavigation() {
    // Enhanced keyboard navigation for custom elements
    document.addEventListener('keydown', (e) => {
        // Escape key to close modals/menus
        if (e.key === 'Escape') {
            closeAllModals();
        }
        
        // Arrow keys for slider navigation
        if (e.key === 'ArrowLeft') {
            navigateSlider('prev');
        } else if (e.key === 'ArrowRight') {
            navigateSlider('next');
        }
        
        // Tab navigation enhancement
        if (e.key === 'Tab') {
            enhanceTabNavigation(e);
        }
        
        // Enter/Space for custom buttons
        if ((e.key === 'Enter' || e.key === ' ') && e.target.classList.contains('custom-button')) {
            e.preventDefault();
            e.target.click();
        }
    });
}

function initializeARIALiveRegions() {
    // Create live region for dynamic content announcements
    if (!document.getElementById('live-region')) {
        const liveRegion = document.createElement('div');
        liveRegion.id = 'live-region';
        liveRegion.setAttribute('aria-live', 'polite');
        liveRegion.setAttribute('aria-atomic', 'true');
        liveRegion.className = 'sr-only';
        document.body.appendChild(liveRegion);
    }
}

function initializeFocusManagement() {
    // Manage focus for modal dialogs and dynamic content
    const focusableElements = 'button, [href], input, select, textarea, [tabindex]:not([tabindex="-1"])';
    
    function trapFocus(element) {
        const focusableContent = element.querySelectorAll(focusableElements);
        const firstFocusableElement = focusableContent[0];
        const lastFocusableElement = focusableContent[focusableContent.length - 1];
        
        element.addEventListener('keydown', (e) => {
            if (e.key === 'Tab') {
                if (e.shiftKey) {
                    if (document.activeElement === firstFocusableElement) {
                        lastFocusableElement.focus();
                        e.preventDefault();
                    }
                } else {
                    if (document.activeElement === lastFocusableElement) {
                        firstFocusableElement.focus();
                        e.preventDefault();
                    }
                }
            }
        });
    }
    
    // Apply focus trap to modals
    document.querySelectorAll('.modal, .dropdown-menu').forEach(trapFocus);
}

function initializeScreenReaderSupport() {
    // Announce page changes to screen readers
    function announceToScreenReader(message) {
        const liveRegion = document.getElementById('live-region');
        if (liveRegion) {
            liveRegion.textContent = message;
            setTimeout(() => {
                liveRegion.textContent = '';
            }, 1000);
        }
    }
    
    // Announce navigation changes
    document.querySelectorAll('.nav-link').forEach(link => {
        link.addEventListener('click', () => {
            const sectionName = link.textContent.trim();
            announceToScreenReader(`Navigating to ${sectionName} section`);
        });
    });
    
    // Announce form submissions
    document.querySelectorAll('form').forEach(form => {
        form.addEventListener('submit', () => {
            announceToScreenReader('Form submitted successfully');
        });
    });
}

// Enhanced mobile menu with accessibility
function initializeMobileMenu() {
    if (!elements.mobileMenuToggle || !elements.nav) return;
    
    elements.mobileMenuToggle.addEventListener('click', () => {
        const isExpanded = elements.nav.classList.contains('mobile-open');
        elements.nav.classList.toggle('mobile-open');
        elements.mobileMenuToggle.classList.toggle('active');
        elements.mobileMenuToggle.setAttribute('aria-expanded', !isExpanded);
        document.body.classList.toggle('menu-open');
        
        // Announce menu state change
        const message = !isExpanded ? 'Mobile menu opened' : 'Mobile menu closed';
        announceToScreenReader(message);
        
        // Focus management
        if (!isExpanded) {
            const firstLink = elements.nav.querySelector('.nav-link');
            if (firstLink) {
                firstLink.focus();
            }
        }
    });
    
    // Close menu when clicking on links
    elements.navLinks.forEach(link => {
        link.addEventListener('click', () => {
            elements.nav.classList.remove('mobile-open');
            elements.mobileMenuToggle.classList.remove('active');
            elements.mobileMenuToggle.setAttribute('aria-expanded', 'false');
            document.body.classList.remove('menu-open');
        });
    });
    
    // Close menu when clicking outside
    document.addEventListener('click', (e) => {
        if (!elements.nav.contains(e.target) && !elements.mobileMenuToggle.contains(e.target)) {
            elements.nav.classList.remove('mobile-open');
            elements.mobileMenuToggle.classList.remove('active');
            elements.mobileMenuToggle.setAttribute('aria-expanded', 'false');
            document.body.classList.remove('menu-open');
        }
    });
}

// Enhanced hero slider with accessibility
function initializeHeroSlider() {
    if (!elements.heroSlider) return;
    
    const slides = elements.heroSlider.querySelectorAll('.hero-slide');
    if (slides.length <= 1) return;
    
    // Add ARIA attributes
    slides.forEach((slide, index) => {
        slide.setAttribute('aria-hidden', index !== 0 ? 'true' : 'false');
        slide.setAttribute('aria-label', `Hero image ${index + 1} of ${slides.length}`);
    });
    
    // Auto-advance slides
    heroSlideInterval = setInterval(() => {
        nextHeroSlide();
    }, 5000);
    
    // Pause on hover
    elements.heroSlider.addEventListener('mouseenter', () => {
        clearInterval(heroSlideInterval);
    });
    
    elements.heroSlider.addEventListener('mouseleave', () => {
        heroSlideInterval = setInterval(() => {
            nextHeroSlide();
        }, 5000);
    });
}

function nextHeroSlide() {
    const slides = elements.heroSlider.querySelectorAll('.hero-slide');
    const totalSlides = slides.length;
    
    slides[currentHeroSlide].classList.remove('active');
    slides[currentHeroSlide].setAttribute('aria-hidden', 'true');
    
    currentHeroSlide = (currentHeroSlide + 1) % totalSlides;
    
    slides[currentHeroSlide].classList.add('active');
    slides[currentHeroSlide].setAttribute('aria-hidden', 'false');
    
    // Announce slide change to screen readers
    announceToScreenReader(`Hero image ${currentHeroSlide + 1} of ${totalSlides}`);
}

// Booking form
function initializeBookingForm() {
    if (!elements.bookingForm) return;
    
    elements.bookingForm.addEventListener('submit', (e) => {
        e.preventDefault();
        
        const formData = new FormData(elements.bookingForm);
        const bookingData = {
            destination: formData.get('destination'),
            departureDate: formData.get('departureDate'),
            guests: formData.get('guests')
        };
        
        // Validate form
        if (!bookingData.destination || !bookingData.departureDate) {
            showToast('Vui lòng điền đầy đủ thông tin', 'error');
            return;
        }
        
        // Simulate booking search
        showToast('Đang tìm kiếm tours phù hợp...', 'info');
        
        setTimeout(() => {
            // Redirect to tours page with search parameters
            const searchParams = new URLSearchParams(bookingData);
            window.location.href = `${window.location.origin}${window.location.pathname.replace('/home', '')}/tours?${searchParams}`;
        }, 1500);
    });
}

// Newsletter form
function initializeNewsletterForm() {
    if (!elements.newsletterForm) return;
    
    elements.newsletterForm.addEventListener('submit', (e) => {
        e.preventDefault();
        
        const email = elements.newsletterForm.querySelector('.newsletter-input').value;
        
        if (!isValidEmail(email)) {
            showToast('Vui lòng nhập email hợp lệ', 'error');
            return;
        }
        
        // Simulate newsletter subscription
        showToast('Đang đăng ký nhận tin...', 'info');
        
        setTimeout(() => {
            showToast('Đăng ký thành công! Cảm ơn bạn đã quan tâm.', 'success');
            elements.newsletterForm.reset();
        }, 1500);
    });
}

// Testimonials slider
function initializeTestimonialsSlider() {
    if (!elements.testimonialsSlider || !elements.testimonialsContainer) return;
    
    const slides = elements.testimonialsContainer.querySelectorAll('.testimonial-slide');
    if (slides.length <= 1) return;
    
    let currentSlide = 0;
    const slideWidth = slides[0].offsetWidth;
    const totalSlides = slides.length;
    
    // Previous button
    if (elements.testimonialPrev) {
        elements.testimonialPrev.addEventListener('click', () => {
            currentSlide = (currentSlide - 1 + totalSlides) % totalSlides;
            updateTestimonialsSlider();
        });
    }
    
    // Next button
    if (elements.testimonialNext) {
        elements.testimonialNext.addEventListener('click', () => {
            currentSlide = (currentSlide + 1) % totalSlides;
            updateTestimonialsSlider();
        });
    }
    
    function updateTestimonialsSlider() {
        const translateX = -currentSlide * slideWidth;
        elements.testimonialsContainer.style.transform = `translateX(${translateX}px)`;
    }
    
    // Auto-advance testimonials
    setInterval(() => {
        currentSlide = (currentSlide + 1) % totalSlides;
        updateTestimonialsSlider();
    }, 6000);
}

// Back to top button
function initializeBackToTop() {
    if (!elements.backToTop) return;
    
    elements.backToTop.addEventListener('click', () => {
        window.scrollTo({
            top: 0,
            behavior: 'smooth'
        });
    });
}

// Animations
function initializeAnimations() {
    // Intersection Observer for scroll animations
    const observerOptions = {
        threshold: 0.1,
        rootMargin: '0px 0px -50px 0px'
    };
    
    const observer = new IntersectionObserver((entries) => {
        entries.forEach(entry => {
            if (entry.isIntersecting) {
                entry.target.classList.add('animate-visible');
            }
        });
    }, observerOptions);
    
    // Observe elements with animation classes
    const animatedElements = document.querySelectorAll('.animate-fade-in, .animate-slide-up, .animate-slide-left, .animate-slide-right, .animate-scale-in, .animate-count-up');
    animatedElements.forEach(el => observer.observe(el));
}

// Lazy loading
function initializeLazyLoading() {
    const images = document.querySelectorAll('img[loading="lazy"]');
    
    if ('IntersectionObserver' in window) {
        const imageObserver = new IntersectionObserver((entries) => {
            entries.forEach(entry => {
                if (entry.isIntersecting) {
                    const img = entry.target;
                    img.src = img.dataset.src || img.src;
                    img.classList.remove('lazy');
                    imageObserver.unobserve(img);
                }
            });
        });
        
        images.forEach(img => imageObserver.observe(img));
    }
}

// Parallax effects
function initializeParallax() {
    // Add parallax elements
    const parallaxElements = document.querySelectorAll('.hero-section, .destinations-section');
    parallaxElements.forEach(el => {
        el.setAttribute('data-parallax', 'true');
    });
}

function updateParallaxElements(scrollTop) {
    const parallaxElements = document.querySelectorAll('[data-parallax="true"]');
    
    parallaxElements.forEach(el => {
        const speed = el.dataset.speed || 0.5;
        const yPos = -(scrollTop * speed);
        el.style.transform = `translateY(${yPos}px)`;
    });
}

// Utility functions
function isValidEmail(email) {
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return emailRegex.test(email);
}

function showToast(message, type = 'info') {
    if (!elements.toastContainer) return;
    
    const toast = document.createElement('div');
    toast.className = `toast toast-${type}`;
    toast.innerHTML = `
        <div class="toast-content">
            <i class="fas fa-${getToastIcon(type)}"></i>
            <span>${message}</span>
        </div>
        <button class="toast-close" onclick="this.parentElement.remove()">
            <i class="fas fa-times"></i>
        </button>
    `;
    
    elements.toastContainer.appendChild(toast);
    
    // Auto remove after 5 seconds
    setTimeout(() => {
        if (toast.parentElement) {
            toast.remove();
        }
    }, 5000);
}

function getToastIcon(type) {
    const icons = {
        success: 'check-circle',
        error: 'exclamation-circle',
        warning: 'exclamation-triangle',
        info: 'info-circle'
    };
    return icons[type] || 'info-circle';
}

function updateActiveNavLink() {
    const sections = document.querySelectorAll('section[id]');
    const scrollPos = window.pageYOffset + 100;
    
    sections.forEach(section => {
        const sectionTop = section.offsetTop;
        const sectionHeight = section.offsetHeight;
        const sectionId = section.getAttribute('id');
        
        if (scrollPos >= sectionTop && scrollPos < sectionTop + sectionHeight) {
            elements.navLinks.forEach(link => {
                link.classList.remove('active');
                if (link.getAttribute('href') === `#${sectionId}`) {
                    link.classList.add('active');
                }
            });
        }
    });
}

// Tour and hotel detail functions
function viewTourDetails(tourId) {
    showToast('Đang tải thông tin tour...', 'info');
    setTimeout(() => {
        window.location.href = `${window.location.origin}${window.location.pathname.replace('/home', '')}/tour-details?id=${tourId}`;
    }, 500);
}

function viewHotelDetails(hotelId) {
    showToast('Đang tải thông tin khách sạn...', 'info');
    setTimeout(() => {
        window.location.href = `${window.location.origin}${window.location.pathname.replace('/home', '')}/hotel-details?id=${hotelId}`;
    }, 500);
}

function viewDestination(destinationName) {
    showToast(`Đang tải thông tin ${destinationName}...`, 'info');
    setTimeout(() => {
        window.location.href = `${window.location.origin}${window.location.pathname.replace('/home', '')}/destinations?name=${encodeURIComponent(destinationName)}`;
    }, 500);
}

// Admin features
function showAdminFeatures() {
    // Add admin-specific functionality
    console.log('Admin features enabled');
    
    // Add admin panel link to navigation
    const nav = document.querySelector('.nav-list');
    if (nav) {
        const adminLink = document.createElement('li');
        adminLink.className = 'nav-item';
        adminLink.innerHTML = '<a href="/admin/dashboard" class="nav-link admin-link"><i class="fas fa-cog"></i> Admin</a>';
        nav.appendChild(adminLink);
    }
}

// Smooth scrolling for anchor links
document.addEventListener('DOMContentLoaded', function() {
    const anchorLinks = document.querySelectorAll('a[href^="#"]');
    
    anchorLinks.forEach(link => {
        link.addEventListener('click', (e) => {
            e.preventDefault();
            
            const targetId = link.getAttribute('href').substring(1);
            const targetElement = document.getElementById(targetId);
            
            if (targetElement) {
                const headerHeight = elements.header ? elements.header.offsetHeight : 0;
                const targetPosition = targetElement.offsetTop - headerHeight;
                
                window.scrollTo({
                    top: targetPosition,
                    behavior: 'smooth'
                });
            }
        });
    });
});

// Performance optimization
function debounce(func, wait) {
    let timeout;
    return function executedFunction(...args) {
        const later = () => {
            clearTimeout(timeout);
            func(...args);
        };
        clearTimeout(timeout);
        timeout = setTimeout(later, wait);
    };
}

// Export functions for global access
window.SamSonTravel = {
    showToast,
    viewTourDetails,
    viewHotelDetails,
    viewDestination,
    initializeHomepage
};
