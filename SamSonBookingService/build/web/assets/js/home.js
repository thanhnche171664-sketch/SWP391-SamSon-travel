/**
 * Homepage-specific JavaScript file for SamSon Travel
 * Handles homepage-specific functionality and interactions
 */

// Homepage-specific variables
let homepageData = {};
let currentSection = 'home';
let isPageLoaded = false;

// Initialize homepage-specific functionality
function initializeHomepage() {
    loadHomepageData();
    setupSectionNavigation();
    setupInteractiveElements();
    setupFormHandlers();
    setupImageGalleries();
    setupScrollEffects();
    setupKeyboardNavigation();
    
    // Hide loading overlay
    hideLoadingOverlay();
    
    isPageLoaded = true;
    console.log('Homepage-specific functionality initialized');
}

// Hide loading overlay
function hideLoadingOverlay() {
    const loadingOverlay = document.getElementById('loadingOverlay');
    if (loadingOverlay) {
        setTimeout(() => {
            loadingOverlay.style.opacity = '0';
            setTimeout(() => {
                loadingOverlay.style.display = 'none';
            }, 300);
        }, 500);
    }
}

// Load homepage data
function loadHomepageData() {
    // This would typically fetch data from the server
    // For now, we'll use the data passed from the JSP
    homepageData = {
        heroImages: document.querySelectorAll('.hero-slide').length,
        tours: document.querySelectorAll('.tour-card').length,
        hotels: document.querySelectorAll('.hotel-card').length,
        testimonials: document.querySelectorAll('.testimonial-slide').length
    };
}

// Section navigation
function setupSectionNavigation() {
    const sections = document.querySelectorAll('section[id]');
    const navLinks = document.querySelectorAll('.nav-link');
    
    // Smooth scrolling for navigation links
    navLinks.forEach(link => {
        link.addEventListener('click', (e) => {
            e.preventDefault();
            
            const targetId = link.getAttribute('href').substring(1);
            const targetSection = document.getElementById(targetId);
            
            if (targetSection) {
                scrollToSection(targetSection);
                updateActiveNavLink(targetId);
            }
        });
    });
    
    // Update active link on scroll
    window.addEventListener('scroll', debounce(() => {
        updateActiveNavOnScroll();
    }, 100));
}

function scrollToSection(section) {
    const headerHeight = document.querySelector('.header').offsetHeight;
    const targetPosition = section.offsetTop - headerHeight;
    
    window.scrollTo({
        top: targetPosition,
        behavior: 'smooth'
    });
}

function updateActiveNavLink(activeId) {
    const navLinks = document.querySelectorAll('.nav-link');
    
    navLinks.forEach(link => {
        link.classList.remove('active');
        if (link.getAttribute('href') === `#${activeId}`) {
            link.classList.add('active');
        }
    });
}

function updateActiveNavOnScroll() {
    const sections = document.querySelectorAll('section[id]');
    const scrollPos = window.pageYOffset + 200;
    
    sections.forEach(section => {
        const sectionTop = section.offsetTop;
        const sectionHeight = section.offsetHeight;
        const sectionId = section.getAttribute('id');
        
        if (scrollPos >= sectionTop && scrollPos < sectionTop + sectionHeight) {
            updateActiveNavLink(sectionId);
            currentSection = sectionId;
        }
    });
}

// Interactive elements
function setupInteractiveElements() {
    setupTourCards();
    setupHotelCards();
    setupServiceCards();
    setupDestinationCards();
    setupTestimonialCards();
}

function setupTourCards() {
    const tourCards = document.querySelectorAll('.tour-card');
    
    tourCards.forEach(card => {
        // Add hover effects
        card.addEventListener('mouseenter', () => {
            card.classList.add('card-hovered');
            animateTourCard(card, true);
        });
        
        card.addEventListener('mouseleave', () => {
            card.classList.remove('card-hovered');
            animateTourCard(card, false);
        });
        
        // Add click handler
        const viewButton = card.querySelector('.tour-card-button');
        if (viewButton) {
            viewButton.addEventListener('click', (e) => {
                e.stopPropagation();
                const tourId = card.dataset.tourId;
                if (tourId) {
                    viewTourDetails(tourId);
                }
            });
        }
    });
}

function setupHotelCards() {
    const hotelCards = document.querySelectorAll('.hotel-card');
    
    hotelCards.forEach(card => {
        card.addEventListener('mouseenter', () => {
            card.classList.add('card-hovered');
            animateHotelCard(card, true);
        });
        
        card.addEventListener('mouseleave', () => {
            card.classList.remove('card-hovered');
            animateHotelCard(card, false);
        });
        
        const viewButton = card.querySelector('.btn');
        if (viewButton) {
            viewButton.addEventListener('click', (e) => {
                e.stopPropagation();
                const hotelId = card.dataset.hotelId;
                if (hotelId) {
                    viewHotelDetails(hotelId);
                }
            });
        }
    });
}

function setupServiceCards() {
    const serviceCards = document.querySelectorAll('.service-card');
    
    serviceCards.forEach(card => {
        card.addEventListener('mouseenter', () => {
            animateServiceCard(card, true);
        });
        
        card.addEventListener('mouseleave', () => {
            animateServiceCard(card, false);
        });
    });
}

function setupDestinationCards() {
    const destinationCards = document.querySelectorAll('.destination-card');
    
    destinationCards.forEach(card => {
        card.addEventListener('mouseenter', () => {
            animateDestinationCard(card, true);
        });
        
        card.addEventListener('mouseleave', () => {
            animateDestinationCard(card, false);
        });
        
        const exploreButton = card.querySelector('.btn');
        if (exploreButton) {
            exploreButton.addEventListener('click', (e) => {
                e.stopPropagation();
                const destinationName = card.dataset.destination;
                if (destinationName) {
                    viewDestination(destinationName);
                }
            });
        }
    });
}

function setupTestimonialCards() {
    const testimonialCards = document.querySelectorAll('.testimonial-card');
    
    testimonialCards.forEach(card => {
        card.addEventListener('mouseenter', () => {
            card.classList.add('testimonial-hovered');
        });
        
        card.addEventListener('mouseleave', () => {
            card.classList.remove('testimonial-hovered');
        });
    });
}

// Form handlers
function setupFormHandlers() {
    setupBookingForm();
    setupNewsletterForm();
    setupContactForm();
}

function setupBookingForm() {
    const bookingForm = document.getElementById('bookingForm');
    if (!bookingForm) return;
    
    const destinationSelect = bookingForm.querySelector('select[name="destination"]');
    const dateInput = bookingForm.querySelector('input[name="departureDate"]');
    const guestsSelect = bookingForm.querySelector('select[name="guests"]');
    
    // Set minimum date to today
    if (dateInput) {
        const today = new Date().toISOString().split('T')[0];
        dateInput.setAttribute('min', today);
    }
    
    // Form validation
    bookingForm.addEventListener('submit', (e) => {
        e.preventDefault();
        
        if (validateBookingForm(bookingForm)) {
            processBookingForm(bookingForm);
        }
    });
    
    // Real-time validation
    [destinationSelect, dateInput, guestsSelect].forEach(input => {
        if (input) {
            input.addEventListener('change', () => {
                validateBookingForm(bookingForm);
            });
        }
    });
}

function setupNewsletterForm() {
    const newsletterForm = document.getElementById('newsletterForm');
    if (!newsletterForm) return;
    
    const emailInput = newsletterForm.querySelector('.newsletter-input');
    
    newsletterForm.addEventListener('submit', (e) => {
        e.preventDefault();
        
        if (validateNewsletterForm(newsletterForm)) {
            processNewsletterForm(newsletterForm);
        }
    });
    
    // Email validation
    if (emailInput) {
        emailInput.addEventListener('input', () => {
            validateEmailInput(emailInput);
        });
    }
}

function setupContactForm() {
    // This would be for any contact forms on the homepage
    const contactForms = document.querySelectorAll('.contact-form');
    
    contactForms.forEach(form => {
        form.addEventListener('submit', (e) => {
            e.preventDefault();
            processContactForm(form);
        });
    });
}

// Image galleries
function setupImageGalleries() {
    setupHeroSlider();
    setupDestinationGallery();
    setupHotelGallery();
}

function setupHeroSlider() {
    const heroSlider = document.getElementById('heroSlider');
    if (!heroSlider) return;
    
    const slides = heroSlider.querySelectorAll('.hero-slide');
    if (slides.length <= 1) return;
    
    let currentSlide = 0;
    let slideInterval;
    
    // Auto-advance slides
    function startSlideShow() {
        slideInterval = setInterval(() => {
            nextSlide();
        }, 5000);
    }
    
    function nextSlide() {
        slides[currentSlide].classList.remove('active');
        currentSlide = (currentSlide + 1) % slides.length;
        slides[currentSlide].classList.add('active');
    }
    
    function prevSlide() {
        slides[currentSlide].classList.remove('active');
        currentSlide = (currentSlide - 1 + slides.length) % slides.length;
        slides[currentSlide].classList.add('active');
    }
    
    // Pause on hover
    heroSlider.addEventListener('mouseenter', () => {
        clearInterval(slideInterval);
    });
    
    heroSlider.addEventListener('mouseleave', () => {
        startSlideShow();
    });
    
    // Touch/swipe support
    let startX = 0;
    let endX = 0;
    
    heroSlider.addEventListener('touchstart', (e) => {
        startX = e.touches[0].clientX;
    });
    
    heroSlider.addEventListener('touchend', (e) => {
        endX = e.changedTouches[0].clientX;
        handleSwipe();
    });
    
    function handleSwipe() {
        const threshold = 50;
        const diff = startX - endX;
        
        if (Math.abs(diff) > threshold) {
            if (diff > 0) {
                nextSlide();
            } else {
                prevSlide();
            }
        }
    }
    
    startSlideShow();
}

function setupDestinationGallery() {
    const destinationCards = document.querySelectorAll('.destination-card');
    
    destinationCards.forEach(card => {
        const image = card.querySelector('img');
        if (image) {
            image.addEventListener('load', () => {
                card.classList.add('image-loaded');
            });
        }
    });
}

function setupHotelGallery() {
    const hotelCards = document.querySelectorAll('.hotel-card');
    
    hotelCards.forEach(card => {
        const image = card.querySelector('img');
        if (image) {
            image.addEventListener('load', () => {
                card.classList.add('image-loaded');
            });
        }
    });
}

// Scroll effects
function setupScrollEffects() {
    setupParallaxScrolling();
    setupScrollAnimations();
    setupStickyElements();
}

function setupParallaxScrolling() {
    const parallaxElements = document.querySelectorAll('[data-parallax]');
    
    if (parallaxElements.length === 0) return;
    
    let ticking = false;
    
    function updateParallax() {
        const scrollTop = window.pageYOffset;
        
        parallaxElements.forEach(element => {
            const speed = parseFloat(element.dataset.speed) || 0.5;
            const yPos = -(scrollTop * speed);
            
            element.style.transform = `translateY(${yPos}px)`;
        });
        
        ticking = false;
    }
    
    function requestTick() {
        if (!ticking) {
            requestAnimationFrame(updateParallax);
            ticking = true;
        }
    }
    
    window.addEventListener('scroll', requestTick);
}

function setupScrollAnimations() {
    const animatedElements = document.querySelectorAll('.animate-on-scroll');
    
    const observer = new IntersectionObserver((entries) => {
        entries.forEach(entry => {
            if (entry.isIntersecting) {
                entry.target.classList.add('animate-visible');
            }
        });
    }, {
        threshold: 0.1,
        rootMargin: '0px 0px -50px 0px'
    });
    
    animatedElements.forEach(el => observer.observe(el));
}

function setupStickyElements() {
    const stickyElements = document.querySelectorAll('[data-sticky]');
    
    stickyElements.forEach(element => {
        const offset = element.dataset.stickyOffset || 0;
        
        window.addEventListener('scroll', () => {
            const scrollTop = window.pageYOffset;
            const elementTop = element.offsetTop;
            
            if (scrollTop >= elementTop - offset) {
                element.classList.add('sticky');
            } else {
                element.classList.remove('sticky');
            }
        });
    });
}

// Keyboard navigation
function setupKeyboardNavigation() {
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
    });
}

function closeAllModals() {
    const modals = document.querySelectorAll('.modal, .dropdown-menu');
    modals.forEach(modal => {
        modal.classList.remove('active', 'show');
    });
}

function navigateSlider(direction) {
    const activeSlider = document.querySelector('.slider-active');
    if (!activeSlider) return;
    
    const prevButton = activeSlider.querySelector('.slider-prev');
    const nextButton = activeSlider.querySelector('.slider-next');
    
    if (direction === 'prev' && prevButton) {
        prevButton.click();
    } else if (direction === 'next' && nextButton) {
        nextButton.click();
    }
}

function enhanceTabNavigation(e) {
    const focusableElements = document.querySelectorAll(
        'a[href], button, input, select, textarea, [tabindex]:not([tabindex="-1"])'
    );
    
    const firstElement = focusableElements[0];
    const lastElement = focusableElements[focusableElements.length - 1];
    
    if (e.shiftKey && document.activeElement === firstElement) {
        e.preventDefault();
        lastElement.focus();
    } else if (!e.shiftKey && document.activeElement === lastElement) {
        e.preventDefault();
        firstElement.focus();
    }
}

// Animation functions
function animateTourCard(card, isHovered) {
    const image = card.querySelector('.tour-card-image img');
    const content = card.querySelector('.tour-card-content');
    
    if (isHovered) {
        if (image) {
            image.style.transform = 'scale(1.1)';
        }
        if (content) {
            content.style.transform = 'translateY(-5px)';
        }
    } else {
        if (image) {
            image.style.transform = 'scale(1)';
        }
        if (content) {
            content.style.transform = 'translateY(0)';
        }
    }
}

function animateHotelCard(card, isHovered) {
    const image = card.querySelector('.hotel-card-image img');
    const content = card.querySelector('.hotel-card-content');
    
    if (isHovered) {
        if (image) {
            image.style.transform = 'scale(1.05)';
        }
        if (content) {
            content.style.transform = 'translateY(-3px)';
        }
    } else {
        if (image) {
            image.style.transform = 'scale(1)';
        }
        if (content) {
            content.style.transform = 'translateY(0)';
        }
    }
}

function animateServiceCard(card, isHovered) {
    const icon = card.querySelector('.service-icon');
    
    if (isHovered) {
        if (icon) {
            icon.style.transform = 'scale(1.2) rotate(5deg)';
        }
    } else {
        if (icon) {
            icon.style.transform = 'scale(1) rotate(0deg)';
        }
    }
}

function animateDestinationCard(card, isHovered) {
    const overlay = card.querySelector('.destination-overlay');
    
    if (isHovered) {
        if (overlay) {
            overlay.style.opacity = '1';
        }
    } else {
        if (overlay) {
            overlay.style.opacity = '0';
        }
    }
}

// Validation functions
function validateBookingForm(form) {
    const destination = form.querySelector('select[name="destination"]').value;
    const date = form.querySelector('input[name="departureDate"]').value;
    const guests = form.querySelector('select[name="guests"]').value;
    
    let isValid = true;
    
    if (!destination) {
        showFieldError(form.querySelector('select[name="destination"]'), 'Vui lòng chọn điểm đến');
        isValid = false;
    }
    
    if (!date) {
        showFieldError(form.querySelector('input[name="departureDate"]'), 'Vui lòng chọn ngày khởi hành');
        isValid = false;
    }
    
    if (!guests) {
        showFieldError(form.querySelector('select[name="guests"]'), 'Vui lòng chọn số người');
        isValid = false;
    }
    
    return isValid;
}

function validateNewsletterForm(form) {
    const emailInput = form.querySelector('.newsletter-input');
    const email = emailInput.value.trim();
    
    if (!email) {
        showFieldError(emailInput, 'Vui lòng nhập email');
        return false;
    }
    
    if (!isValidEmail(email)) {
        showFieldError(emailInput, 'Email không hợp lệ');
        return false;
    }
    
    return true;
}

function validateEmailInput(input) {
    const email = input.value.trim();
    
    if (email && !isValidEmail(email)) {
        input.classList.add('error');
    } else {
        input.classList.remove('error');
    }
}

function showFieldError(field, message) {
    field.classList.add('error');
    
    // Remove existing error message
    const existingError = field.parentNode.querySelector('.field-error');
    if (existingError) {
        existingError.remove();
    }
    
    // Add new error message
    const errorDiv = document.createElement('div');
    errorDiv.className = 'field-error';
    errorDiv.textContent = message;
    field.parentNode.appendChild(errorDiv);
    
    // Remove error on input
    field.addEventListener('input', () => {
        field.classList.remove('error');
        errorDiv.remove();
    }, { once: true });
}

// Processing functions
function processBookingForm(form) {
    const formData = new FormData(form);
    const bookingData = {
        destination: formData.get('destination'),
        departureDate: formData.get('departureDate'),
        guests: formData.get('guests')
    };
    
    showToast('Đang tìm kiếm tours phù hợp...', 'info');
    
    // Simulate API call
    setTimeout(() => {
        const searchParams = new URLSearchParams(bookingData);
        window.location.href = `/tours?${searchParams}`;
    }, 1500);
}

function processNewsletterForm(form) {
    const email = form.querySelector('.newsletter-input').value.trim();
    
    showToast('Đang đăng ký nhận tin...', 'info');
    
    // Simulate API call
    setTimeout(() => {
        showToast('Đăng ký thành công! Cảm ơn bạn đã quan tâm.', 'success');
        form.reset();
    }, 1500);
}

function processContactForm(form) {
    const formData = new FormData(form);
    
    showToast('Đang gửi tin nhắn...', 'info');
    
    // Simulate API call
    setTimeout(() => {
        showToast('Tin nhắn đã được gửi thành công!', 'success');
        form.reset();
    }, 1500);
}

// Utility functions
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

function isValidEmail(email) {
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return emailRegex.test(email);
}

// Export functions
window.HomepageUtils = {
    initializeHomepage,
    viewTourDetails,
    viewHotelDetails,
    viewDestination,
    validateBookingForm,
    validateNewsletterForm,
    processBookingForm,
    processNewsletterForm
};
