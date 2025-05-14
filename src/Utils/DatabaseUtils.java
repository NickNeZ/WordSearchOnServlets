package Utils;

import Utils.WordUtils;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class DatabaseUtils {
    private static final String DB_URL = "jdbc:sqlite:src/data/database/words.db";

    public DatabaseUtils() {
        createTableIfNotExists();
    }

    public static class WordRecord {
        private String filename;
        private String word;
        private int frequency;

        public WordRecord(String filename, String word, int frequency) {
            this.filename = filename;
            this.word = word;
            this.frequency = frequency;
        }

        // Getters and toString method
        public String getFilename() {
            return filename;
        }

        public String getWord() {
            return word;
        }

        public int getFrequency() {
            return frequency;
        }

        @Override
        public String toString() {
            return "WordRecord{filename='" + filename + "', word='" + word + "', frequency=" + frequency + '}';
        }
    }

    public static Connection connect() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }

    public static List<WordRecord> queryByWord(String word) {
        List<WordRecord> results = new ArrayList<>();
        String sql = "SELECT filename, word, frequency FROM word_frequency WHERE word = ? ORDER BY frequency DESC";

        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, word);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String filename = rs.getString("filename");
                String foundWord = rs.getString("word");
                int frequency = rs.getInt("frequency");

                results.add(new WordRecord(foundWord, filename, frequency));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return results;
    }

    private void createTableIfNotExists() {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {

            // Создание таблицы
            String createTableSQL =
                "CREATE TABLE IF NOT EXISTS word_frequency (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT,"+
                    "filename TEXT NOT NULL," +
                    "word TEXT NOT NULL," +
                    "frequency INTEGER NOT NULL" +
                ");"
                ;
            stmt.execute(createTableSQL);

            // Создание индекса по слову
            String createIndexSQL = "CREATE INDEX IF NOT EXISTS idx_word ON word_frequency(word);";
            stmt.execute(createIndexSQL);

        } catch (SQLException e) {
            System.err.println("Ошибка при создании таблицы: " + e.getMessage());
        }
    }

    public void insertFrequencies(String filename, Map<String, Integer> frequencies) {
        String insertSQL = "INSERT INTO word_frequency(filename, word, frequency) VALUES (?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(insertSQL)) {

            conn.setAutoCommit(false);

            for (Map.Entry<String, Integer> entry : frequencies.entrySet()) {
                pstmt.setString(1, filename);
                pstmt.setString(2, entry.getKey());
                pstmt.setInt(3, entry.getValue());
                pstmt.addBatch();
            }

            pstmt.executeBatch();
            conn.commit();

        } catch (SQLException e) {
            System.err.println("Ошибка при вставке данных: " + e.getMessage());
        }
    }
}
