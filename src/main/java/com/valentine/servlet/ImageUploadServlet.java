package com.valentine.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@MultipartConfig
public class ImageUploadServlet extends HttpServlet {

    private static final String UPLOAD_DIR = "uploads"; // Web-accessible folder

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Get application directory
        String uploadFilePath = request.getServletContext().getRealPath("") + File.separator + UPLOAD_DIR;

        // Create directory if it doesn't exist
        File uploadDir = new File(uploadFilePath);
        if (!uploadDir.exists()) {
            uploadDir.mkdir();
        }

        // Get file part from the request
        Part filePart = request.getPart("file");
        String fileName = getFileName(filePart);
        String filePath = uploadFilePath + File.separator + fileName;

        // Write the file to disk
        filePart.write(filePath);

        // Save the image metadata in the database
        saveImageMetadata(fileName, filePath);

        // Pass the image filename to the JSP
        request.setAttribute("filename", fileName);

        // Forward to the JSP page to display the uploaded image
        request.getRequestDispatcher("/imageDisplay.jsp").forward(request, response);
    }

    private String getFileName(Part part) {
        String contentDisposition = part.getHeader("content-disposition");
        for (String content : contentDisposition.split(";")) {
            if (content.trim().startsWith("filename")) {
                return content.substring(content.indexOf('=') + 1).trim().replace("\"", "");
            }
        }
        return null;
    }

    private void saveImageMetadata(String fileName, String filePath) {
        String dbURL = "jdbc:mysql://localhost:3306/imgur_clone";
        String dbUser = "root";
        String dbPassword = "root710";

        String sql = "INSERT INTO images (filename, filepath) VALUES (?, ?)";

        try (Connection conn = DriverManager.getConnection(dbURL, dbUser, dbPassword);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, fileName);
            stmt.setString(2, filePath);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
