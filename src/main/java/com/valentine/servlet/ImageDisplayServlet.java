package com.valentine.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class ImageDisplayServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        String dbURL = "jdbc:mysql://localhost:3306/imgur_clone";
        String dbUser = "root";
        String dbPassword = "root710";

        try (Connection conn = DriverManager.getConnection(dbURL, dbUser, dbPassword);
             Statement stmt = conn.createStatement()) {
            String sql = "SELECT filename FROM images";
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                String filename = rs.getString("filename");

                out.println("<h3>" + filename + "</h3>");
                out.println("<img src='ImageServeServlet?filename=" + filename + "' width='200' height='200'><br>");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
