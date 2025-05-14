package Utils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.*;
import java.nio.file.*;

@WebServlet("/download")
public class DownloadServlet extends HttpServlet {
    private static final String FILE_DIR = "src/data/textfiles";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String filename = request.getParameter("file");
        if (filename == null || filename.trim().isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Файл не указан.");
            return;
        }

        Path path = Paths.get(FILE_DIR, filename);
        if (!Files.exists(path)) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Файл не найден.");
            return;
        }

        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment;filename=" + filename);

        try (OutputStream out = response.getOutputStream()) {
            Files.copy(path, out);
        }
    }
}
