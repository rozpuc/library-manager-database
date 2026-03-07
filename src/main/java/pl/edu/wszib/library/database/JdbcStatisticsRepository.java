package pl.edu.wszib.library.database;

import pl.edu.wszib.library.config.DatabaseConfig;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JdbcStatisticsRepository implements StatisticsRepository {

    @Override
    public int getTotalBooks() {
        String sql = "SELECT COUNT(*) FROM books";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            System.err.println("Błąd podczas pobierania statystyk: " + e.getMessage());
        }
        return 0;
    }

    @Override
    public int getBorrowedBooksCount() {
        String sql = "SELECT COUNT(*) FROM books WHERE available = FALSE";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            System.err.println("Błąd podczas pobierania statystyk: " + e.getMessage());
        }
        return 0;
    }

    @Override
    public List<String> getMostPopularBooks(int limit) {
        List<String> result = new ArrayList<>();
        String sql = """
            SELECT b.title, b.author, COUNT(br.id) AS borrow_count
            FROM books b
            JOIN borrowings br ON b.id = br.book_id
            GROUP BY b.id, b.title, b.author
            ORDER BY borrow_count DESC
            LIMIT ?
        """;
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, limit);
            try (ResultSet rs = ps.executeQuery()) {
                int rank = 1;
                while (rs.next()) {
                    result.add(String.format("%d. \"%s\" - %s (%d wyp.)",
                            rank++, rs.getString("title"), rs.getString("author"), rs.getInt("borrow_count")));
                }
            }
        } catch (SQLException e) {
            System.err.println("Błąd podczas pobierania statystyk: " + e.getMessage());
        }
        return result;
    }

    @Override
    public List<String> getActiveUsers() {
        List<String> result = new ArrayList<>();
        String sql = """
            SELECT u.username, COUNT(br.id) AS borrow_count
            FROM users u
            JOIN borrowings br ON u.id = br.user_id
            WHERE br.returned = FALSE
            GROUP BY u.id, u.username
            ORDER BY borrow_count DESC
        """;
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                result.add(String.format("%s (%d aktywnych wyp.)",
                        rs.getString("username"), rs.getInt("borrow_count")));
            }
        } catch (SQLException e) {
            System.err.println("Błąd podczas pobierania statystyk: " + e.getMessage());
        }
        return result;
    }
}
