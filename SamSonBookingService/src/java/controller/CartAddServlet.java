/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

package controller;

import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author User
 */
@WebServlet(name="CartAddServlet", urlPatterns={"/user-cart"})
public class CartAddServlet extends HttpServlet {
   @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException, ServletException {

        String serviceIdRaw = req.getParameter("serviceId");
        String quantityRaw = req.getParameter("quantity");

        int serviceId = 0;
        int quantity = 1;

        try {
            serviceId = Integer.parseInt(serviceIdRaw);
        } catch (NumberFormatException e) {
            resp.sendRedirect(req.getContextPath() + "/user-wellness");
            return;
        }

        try {
            quantity = Integer.parseInt(quantityRaw);
        } catch (NumberFormatException e) {
            quantity = 1; 
        }

        HttpSession session = req.getSession();
        Map<Integer, Integer> cart = (Map<Integer, Integer>) session.getAttribute("cart");
        if (cart == null) {
            cart = new HashMap<>();
        }

        int oldQty = cart.getOrDefault(serviceId, 0);
        cart.put(serviceId, oldQty + quantity);

        session.setAttribute("cart", cart);
        resp.sendRedirect(req.getContextPath() + "/user-wellness");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        resp.sendRedirect(req.getContextPath() + "/user-wellness");
    }
} 