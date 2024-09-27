<%@page contentType="text/html" pageEncoding="UTF-8"%>
<html>
    <head>
        <title>Image Uploaded</title>
    </head>
    <body>
        <h2>Image Uploaded Successfully!</h2>

        <img src="ImageServeServlet?filename=${filename}" alt="Uploaded Image" width="400" height="400"><br><br>

        <form action="displayImage">
            <input type="submit" value="View All Uploaded Images">
        </form>
    </body>
</html>
