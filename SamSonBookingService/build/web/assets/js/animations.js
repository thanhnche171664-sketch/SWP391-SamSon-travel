/**
 * Animations JavaScript file for SamSon Travel Homepage
 * Handles advanced animations, transitions, and micro-interactions
 */

// Animation configuration
const animationConfig = {
    duration: 300,
    easing: 'cubic-bezier(0.4, 0, 0.2, 1)',
    staggerDelay: 100
};

// Initialize animations
function initializeAnimations() {
    setupScrollAnimations();
    setupHoverAnimations();
    setupClickAnimations();
    setupFormAnimations();
    setupCounterAnimations();
    setupParallaxAnimations();
}

// Scroll animations
function setupScrollAnimations() {
    const animatedElements = document.querySelectorAll('[data-animate]');
    
    const observer = new IntersectionObserver((entries) => {
        entries.forEach(entry => {
            if (entry.isIntersecting) {
                const element = entry.target;
                const animationType = element.dataset.animate;
                const delay = element.dataset.delay || 0;
                
                setTimeout(() => {
                    element.classList.add('animate-visible');
                    triggerAnimation(element, animationType);
                }, delay);
                
                observer.unobserve(element);
            }
        });
    }, {
        threshold: 0.1,
        rootMargin: '0px 0px -50px 0px'
    });
    
    animatedElements.forEach(el => observer.observe(el));
}

// Hover animations
function setupHoverAnimations() {
    // Card hover effects
    const cards = document.querySelectorAll('.tour-card, .hotel-card, .service-card, .destination-card');
    
    cards.forEach(card => {
        card.addEventListener('mouseenter', () => {
            card.classList.add('hovered');
            animateCardHover(card, true);
        });
        
        card.addEventListener('mouseleave', () => {
            card.classList.remove('hovered');
            animateCardHover(card, false);
        });
    });
    
    // Button hover effects
    const buttons = document.querySelectorAll('.btn');
    
    buttons.forEach(button => {
        button.addEventListener('mouseenter', () => {
            animateButtonHover(button, true);
        });
        
        button.addEventListener('mouseleave', () => {
            animateButtonHover(button, false);
        });
    });
}

// Click animations
function setupClickAnimations() {
    // Ripple effect for buttons
    const rippleButtons = document.querySelectorAll('.btn, .tour-card-button, .hotel-card-button');
    
    rippleButtons.forEach(button => {
        button.addEventListener('click', (e) => {
            createRippleEffect(e, button);
        });
    });
    
    // Card click animations
    const clickableCards = document.querySelectorAll('.tour-card, .hotel-card');
    
    clickableCards.forEach(card => {
        card.addEventListener('click', (e) => {
            if (!e.target.closest('.btn')) {
                animateCardClick(card);
            }
        });
    });
}

// Form animations
function setupFormAnimations() {
    const formInputs = document.querySelectorAll('.booking-form-input, .booking-form-select, .newsletter-input');
    
    formInputs.forEach(input => {
        // Focus animations
        input.addEventListener('focus', () => {
            animateInputFocus(input, true);
        });
        
        input.addEventListener('blur', () => {
            animateInputFocus(input, false);
        });
        
        // Input validation animations
        input.addEventListener('input', () => {
            validateInput(input);
        });
    });
}

// Counter animations
function setupCounterAnimations() {
    const counters = document.querySelectorAll('.statistic-number');
    
    const counterObserver = new IntersectionObserver((entries) => {
        entries.forEach(entry => {
            if (entry.isIntersecting) {
                const counter = entry.target;
                const target = parseInt(counter.dataset.target);
                animateCounter(counter, target);
                counterObserver.unobserve(counter);
            }
        });
    }, { threshold: 0.5 });
    
    counters.forEach(counter => counterObserver.observe(counter));
}

// Parallax animations
function setupParallaxAnimations() {
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

// Animation functions
function triggerAnimation(element, animationType) {
    switch (animationType) {
        case 'fadeIn':
            element.style.opacity = '0';
            element.style.transform = 'translateY(20px)';
            animate(element, {
                opacity: 1,
                transform: 'translateY(0)'
            }, animationConfig.duration);
            break;
            
        case 'slideUp':
            element.style.opacity = '0';
            element.style.transform = 'translateY(50px)';
            animate(element, {
                opacity: 1,
                transform: 'translateY(0)'
            }, animationConfig.duration);
            break;
            
        case 'slideLeft':
            element.style.opacity = '0';
            element.style.transform = 'translateX(-50px)';
            animate(element, {
                opacity: 1,
                transform: 'translateX(0)'
            }, animationConfig.duration);
            break;
            
        case 'slideRight':
            element.style.opacity = '0';
            element.style.transform = 'translateX(50px)';
            animate(element, {
                opacity: 1,
                transform: 'translateX(0)'
            }, animationConfig.duration);
            break;
            
        case 'scaleIn':
            element.style.opacity = '0';
            element.style.transform = 'scale(0.8)';
            animate(element, {
                opacity: 1,
                transform: 'scale(1)'
            }, animationConfig.duration);
            break;
            
        case 'rotateIn':
            element.style.opacity = '0';
            element.style.transform = 'rotate(-10deg) scale(0.8)';
            animate(element, {
                opacity: 1,
                transform: 'rotate(0deg) scale(1)'
            }, animationConfig.duration);
            break;
    }
}

function animateCardHover(card, isHovered) {
    const image = card.querySelector('.tour-card-image img, .hotel-card-image img, .destination-image img');
    const content = card.querySelector('.tour-card-content, .hotel-card-content');
    
    if (isHovered) {
        if (image) {
            animate(image, {
                transform: 'scale(1.1)'
            }, animationConfig.duration);
        }
        
        if (content) {
            animate(content, {
                transform: 'translateY(-5px)'
            }, animationConfig.duration);
        }
        
        card.style.boxShadow = '0 20px 40px rgba(0,0,0,0.1)';
    } else {
        if (image) {
            animate(image, {
                transform: 'scale(1)'
            }, animationConfig.duration);
        }
        
        if (content) {
            animate(content, {
                transform: 'translateY(0)'
            }, animationConfig.duration);
        }
        
        card.style.boxShadow = '';
    }
}

function animateButtonHover(button, isHovered) {
    if (isHovered) {
        animate(button, {
            transform: 'translateY(-2px)',
            boxShadow: '0 8px 25px rgba(0,0,0,0.15)'
        }, animationConfig.duration);
    } else {
        animate(button, {
            transform: 'translateY(0)',
            boxShadow: ''
        }, animationConfig.duration);
    }
}

function createRippleEffect(event, element) {
    const ripple = document.createElement('span');
    const rect = element.getBoundingClientRect();
    const size = Math.max(rect.width, rect.height);
    const x = event.clientX - rect.left - size / 2;
    const y = event.clientY - rect.top - size / 2;
    
    ripple.style.cssText = `
        position: absolute;
        width: ${size}px;
        height: ${size}px;
        left: ${x}px;
        top: ${y}px;
        background: rgba(255,255,255,0.3);
        border-radius: 50%;
        transform: scale(0);
        animation: ripple 0.6s linear;
        pointer-events: none;
    `;
    
    element.style.position = 'relative';
    element.style.overflow = 'hidden';
    element.appendChild(ripple);
    
    setTimeout(() => {
        ripple.remove();
    }, 600);
}

function animateCardClick(card) {
    card.style.transform = 'scale(0.95)';
    
    setTimeout(() => {
        animate(card, {
            transform: 'scale(1)'
        }, 150);
    }, 100);
}

function animateInputFocus(input, isFocused) {
    const label = input.previousElementSibling;
    
    if (isFocused) {
        if (label) {
            animate(label, {
                transform: 'translateY(-20px) scale(0.9)',
                color: '#007bff'
            }, animationConfig.duration);
        }
        
        input.style.borderColor = '#007bff';
        input.style.boxShadow = '0 0 0 3px rgba(0,123,255,0.1)';
    } else {
        if (label && !input.value) {
            animate(label, {
                transform: 'translateY(0) scale(1)',
                color: '#666'
            }, animationConfig.duration);
        }
        
        input.style.borderColor = '';
        input.style.boxShadow = '';
    }
}

function validateInput(input) {
    const isValid = input.checkValidity();
    
    if (input.value) {
        if (isValid) {
            input.style.borderColor = '#28a745';
            input.style.boxShadow = '0 0 0 3px rgba(40,167,69,0.1)';
        } else {
            input.style.borderColor = '#dc3545';
            input.style.boxShadow = '0 0 0 3px rgba(220,53,69,0.1)';
        }
    } else {
        input.style.borderColor = '';
        input.style.boxShadow = '';
    }
}

function animateCounter(element, target) {
    let current = 0;
    const increment = target / 100;
    const duration = 2000;
    const stepTime = duration / 100;
    
    const timer = setInterval(() => {
        current += increment;
        
        if (current >= target) {
            current = target;
            clearInterval(timer);
        }
        
        element.textContent = Math.floor(current).toLocaleString();
    }, stepTime);
}

// Generic animation function
function animate(element, properties, duration) {
    const startTime = performance.now();
    const startValues = {};
    
    // Get initial values
    Object.keys(properties).forEach(prop => {
        startValues[prop] = getComputedStyle(element)[prop];
    });
    
    function updateAnimation(currentTime) {
        const elapsed = currentTime - startTime;
        const progress = Math.min(elapsed / duration, 1);
        
        // Apply easing
        const easedProgress = easeInOutCubic(progress);
        
        // Update properties
        Object.keys(properties).forEach(prop => {
            const startValue = parseFloat(startValues[prop]) || 0;
            const endValue = parseFloat(properties[prop]) || 0;
            const currentValue = startValue + (endValue - startValue) * easedProgress;
            
            if (prop === 'opacity') {
                element.style.opacity = currentValue;
            } else if (prop === 'transform') {
                element.style.transform = properties[prop];
            } else {
                element.style[prop] = currentValue + (prop.includes('px') ? 'px' : '');
            }
        });
        
        if (progress < 1) {
            requestAnimationFrame(updateAnimation);
        }
    }
    
    requestAnimationFrame(updateAnimation);
}

// Easing functions
function easeInOutCubic(t) {
    return t < 0.5 ? 4 * t * t * t : (t - 1) * (2 * t - 2) * (2 * t - 2) + 1;
}

function easeOutBounce(t) {
    if (t < 1 / 2.75) {
        return 7.5625 * t * t;
    } else if (t < 2 / 2.75) {
        return 7.5625 * (t -= 1.5 / 2.75) * t + 0.75;
    } else if (t < 2.5 / 2.75) {
        return 7.5625 * (t -= 2.25 / 2.75) * t + 0.9375;
    } else {
        return 7.5625 * (t -= 2.625 / 2.75) * t + 0.984375;
    }
}

// Stagger animations for lists
function staggerAnimation(elements, animationType, delay = 100) {
    elements.forEach((element, index) => {
        setTimeout(() => {
            triggerAnimation(element, animationType);
        }, index * delay);
    });
}

// Loading animations
function showLoadingAnimation(element) {
    element.style.opacity = '0.5';
    element.style.pointerEvents = 'none';
    
    const spinner = document.createElement('div');
    spinner.className = 'loading-spinner';
    spinner.innerHTML = '<div class="spinner"></div>';
    
    element.appendChild(spinner);
    
    return spinner;
}

function hideLoadingAnimation(element, spinner) {
    element.style.opacity = '';
    element.style.pointerEvents = '';
    
    if (spinner) {
        spinner.remove();
    }
}

// Export functions
window.AnimationUtils = {
    triggerAnimation,
    animateCardHover,
    animateButtonHover,
    createRippleEffect,
    animateCounter,
    staggerAnimation,
    showLoadingAnimation,
    hideLoadingAnimation,
    initializeAnimations
};
