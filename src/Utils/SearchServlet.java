package Utils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.*;
import java.sql.*;
import java.util.*;

@WebServlet("/search")
public class SearchServlet extends HttpServlet {
    private static final String DB_PATH = "src/data/database/words.db";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String query = request.getParameter("word");

        if (query == null || query.isEmpty()) {
            request.setAttribute("results", new ArrayList<>());
            request.getRequestDispatcher("/index.jsp").forward(request, response);
            return;
        }

        List<Result> results = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:" + DB_PATH)) {
            String sql = "SELECT filename, frequency FROM word_frequency WHERE word = ? ORDER BY frequency DESC";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, query.toLowerCase());
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        String filename = rs.getString("filename");
                        int frequency = rs.getInt("frequency");
                        results.add(new Result(filename, frequency));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Устанавливаем атрибуты для JSP
        request.setAttribute("results", results);
        request.setAttribute("query", query);
        request.getRequestDispatcher("/index.jsp").forward(request, response);
    }

    public static class Result {
        public String filename;
        public int frequency;

        public Result(String filename, int frequency) {
            this.filename = filename;
            this.frequency = frequency;
        }
    }
}
