//package controller;
//
//import org.junit.Before;
//import org.junit.Test;
//
//import static org.mockito.Mockito.*;
//import static org.junit.Assert.*;
//
//import jakarta.servlet.RequestDispatcher;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//
//public class WellnessAddServletTest {
//
//    private WellnessAddServlet servlet;          // đổi tên nếu class bạn khác
//    private HttpServletRequest req;
//    private HttpServletResponse resp;
//    private RequestDispatcher rd;
//
//    @Before
//    public void setUp() {
//        servlet = new WellnessAddServlet();      // đổi tên nếu class bạn khác
//        req = mock(HttpServletRequest.class);
//        resp = mock(HttpServletResponse.class);
//        rd  = mock(RequestDispatcher.class);
//    }
//
//    // --------- HAPPY PATH: dữ liệu hợp lệ -> redirect về list ----------
//    @Test
//    public void testDoPost_Success_Redirect() throws Exception {
//        when(req.getParameter("serviceName")).thenReturn("Yoga Deluxe");
//        when(req.getParameter("description")).thenReturn("Morning class");
//        when(req.getParameter("basePrice")).thenReturn("250000");
//        when(req.getParameter("durationMinutes")).thenReturn("60");
//        when(req.getParameter("capacity")).thenReturn("10");
//        when(req.getParameter("operatingHours")).thenReturn("08:00-20:00");
//        when(req.getParameter("status")).thenReturn("ACTIVE");
//
//        servlet.doPost(req, resp);
//
//        // nếu code bạn dùng sendRedirect sau khi thêm thành công:
//        verify(resp, atLeastOnce()).sendRedirect(contains("/wellness"));
//    }
//
//    // --------- LỖI: thiếu tên dịch vụ -> forward về trang add ----------
//    @Test
//    public void testDoPost_MissingName_ForwardToAddJsp() throws Exception {
//        when(req.getParameter("serviceName")).thenReturn("   "); // rỗng
//        when(req.getParameter("description")).thenReturn("desc");
//        when(req.getParameter("basePrice")).thenReturn("250000");
//        when(req.getParameter("durationMinutes")).thenReturn("60");
//        when(req.getParameter("capacity")).thenReturn("10");
//        when(req.getParameter("operatingHours")).thenReturn("08:00-20:00");
//        when(req.getParameter("status")).thenReturn("ACTIVE");
//
//        // Đổi đúng tên file JSP mà servlet của bạn forward khi lỗi
//        when(req.getRequestDispatcher("wellness_add.jsp")).thenReturn(rd);
//
//        servlet.doPost(req, resp);
//
//        // Có set attribute lỗi và forward về trang add
//        verify(req, atLeastOnce()).setAttribute(eq("errorServiceName"), anyString());
//        verify(rd, times(1)).forward(eq(req), eq(resp));
//        // không redirect trong nhánh lỗi
//        verify(resp, never()).sendRedirect(anyString());
//    }
//}
