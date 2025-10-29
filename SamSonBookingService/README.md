# SamSon Travel Booking Service

## 📋 Tổng quan dự án

SamSon Travel Booking Service là một hệ thống đặt tour du lịch tại Sầm Sơn được phát triển bằng Jakarta Servlet, JSP và SQL Server. Dự án được thiết kế theo kiến trúc MVC với các tính năng đầy đủ cho việc quản lý tour, khách sạn, booking và user management.

## 🏗️ Kiến trúc hệ thống

### Backend
- **Framework**: Jakarta Servlet 6.0, JSP với JSTL
- **Database**: SQL Server với JDBC
- **Authentication**: Session-based với role-based access control
- **File Upload**: Multipart file handling với validation

### Frontend
- **HTML5**: Semantic markup với ARIA labels
- **CSS3**: Modern CSS với Grid, Flexbox, Custom Properties
- **JavaScript**: Vanilla ES6+ với progressive enhancement
- **Responsive**: Mobile-first design với breakpoints tối ưu

## 📁 Cấu trúc dự án

```
SamSonBookingService/
├── src/java/
│   ├── controller/          # Servlet controllers
│   ├── dao/                 # Data Access Objects
│   ├── entity/              # Entity classes
│   ├── filter/              # Servlet filters
│   └── util/                # Utility classes
├── web/
│   ├── assets/              # Static assets (CSS, JS, images)
│   ├── uploads/             # User uploaded files
│   ├── error/               # Error pages
│   └── WEB-INF/
│       └── web.xml          # Web application configuration
├── lib/                     # External JAR libraries
└── datamoi.sql             # Database schema and sample data
```

## 🚀 Tính năng chính

### 1. User Management
- ✅ Đăng ký/Đăng nhập với email verification
- ✅ Profile management với avatar upload
- ✅ Password reset với token-based security
- ✅ Role-based access control (5 roles)

### 2. Tour Management
- ✅ CRUD operations cho tours
- ✅ Tour schedules và availability
- ✅ Tour itineraries chi tiết
- ✅ Tour packages với pricing

### 3. Hotel Management
- ✅ Hotel information và amenities
- ✅ Room management
- ✅ Rating và review system

### 4. Booking System
- ✅ Tour booking với multiple services
- ✅ Payment integration
- ✅ Booking confirmation và management

### 5. Homepage Features
- ✅ Dynamic hero slider với tour images
- ✅ Featured tours và hotels
- ✅ Testimonials carousel
- ✅ Service categories showcase
- ✅ Newsletter subscription
- ✅ Responsive design với animations

## 🔐 Security Features

- **SQL Injection Prevention**: PreparedStatement cho tất cả queries
- **XSS Protection**: Input sanitization với ValidationUtil
- **File Upload Security**: File type validation và size limits
- **Session Security**: HttpOnly cookies và timeout management
- **Password Security**: BCrypt hashing với strength validation

## 📊 Database Schema

### Core Tables
- `Users` - User accounts và profiles
- `Roles` - User roles và permissions
- `Tours` - Tour information
- `Tour_Schedules` - Tour availability
- `Tour_Itineraries` - Day-by-day activities
- `Tour_Packages` - Package configurations
- `Hotels` - Hotel information
- `Bookings` - Booking records
- `Discounts` - Promotional offers

## 🛠️ Setup và Installation

### Prerequisites
- Java 17+
- Apache Tomcat 10+
- SQL Server 2019+
- Maven hoặc Ant build tool

### Database Setup
1. Tạo database `booking_travel` trong SQL Server
2. Import schema từ `datamoi.sql`
3. Cập nhật connection string trong `DBContext.java`

### Application Setup
1. Clone repository
2. Cấu hình database connection
3. Deploy WAR file lên Tomcat
4. Access application tại `http://localhost:8080/SamSonBookingService`

## 📚 API Endpoints

### Authentication
- `POST /login` - User login
- `POST /register` - User registration
- `GET /logout` - User logout
- `GET /verify-email` - Email verification

### User Management
- `GET /profile` - View user profile
- `POST /update-profile` - Update profile
- `POST /change-password` - Change password
- `POST /upload-avatar` - Upload avatar

### Homepage
- `GET /home` - Homepage với dynamic content

## 🎨 Frontend Features

### Responsive Design
- Mobile: 320px - 767px
- Tablet: 768px - 1023px
- Desktop: 1024px - 1439px
- Large Desktop: 1440px+

### Accessibility (WCAG 2.1 AA)
- Semantic HTML5 elements
- ARIA labels và roles
- Keyboard navigation support
- Focus indicators
- Color contrast ratios 4.5:1+

### Performance Optimizations
- Lazy loading images
- CSS/JS minification
- Browser caching headers
- Optimized animations (60fps)

## 🔧 Development Guidelines

### Code Standards
- **Naming**: camelCase cho variables/methods, PascalCase cho classes
- **Documentation**: JavaDoc cho tất cả public methods
- **Error Handling**: Comprehensive try-catch với logging
- **Security**: Input validation và sanitization

### Best Practices
- Use PreparedStatement cho database queries
- Implement proper exception handling
- Follow MVC pattern strictly
- Use connection pooling
- Validate all user inputs

## 📈 Performance Metrics

- **Page Load Time**: < 2 seconds
- **Lighthouse Score**: 90+ cho tất cả metrics
- **Database Response**: < 100ms cho simple queries
- **File Upload**: Support up to 5MB với validation

## 🐛 Troubleshooting

### Common Issues
1. **Database Connection**: Kiểm tra SQL Server service và connection string
2. **File Upload**: Verify multipart configuration trong web.xml
3. **Session Issues**: Check session timeout và cookie settings
4. **CSS/JS Loading**: Verify asset paths và MIME type mappings

### Logging
- Application logs: `java.util.logging`
- Database logs: SQL Server logs
- Web server logs: Tomcat logs

## 🤝 Contributing

1. Fork repository
2. Create feature branch
3. Follow coding standards
4. Add comprehensive tests
5. Submit pull request

## 📄 License

This project is licensed under the MIT License - see the LICENSE file for details.

## 👥 Team

**SamSon Travel Team**
- Backend Development: Jakarta Servlet, JSP, SQL Server
- Frontend Development: HTML5, CSS3, JavaScript
- Database Design: SQL Server schema optimization
- UI/UX Design: Responsive design với accessibility

## 📞 Support

- Email: info@samsontravel.com
- Phone: +84 123 456 789
- Address: 123 Đường Trần Phú, Sầm Sơn, Thanh Hóa

---

**SamSon Travel Booking Service** - Professional travel booking solution for Sam Son tourism.
