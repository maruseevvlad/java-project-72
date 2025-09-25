package hexlet.code;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UrlCheckRepository {
    public void save(UrlCheck check) throws SQLException {
        try (Connection conn = DataSourceProvider.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "INSERT INTO url_checks (url_id, status_code, h1, title, description, created_at) VALUES (?, ?, ?, ?, ?, ?)")) {
            stmt.setLong(1, check.getUrlId());
            stmt.setInt(2, check.getStatusCode());
            stmt.setString(3, check.getH1());
            stmt.setString(4, check.getTitle());
            stmt.setString(5, check.getDescription());
            stmt.setTimestamp(6, Timestamp.valueOf(check.getCreatedAt()));
            stmt.executeUpdate();
        }
    }

    public List<UrlCheck> findAllByUrlId(Long urlId) throws SQLException {
        List<UrlCheck> checks = new ArrayList<>();
        try (Connection conn = DataSourceProvider.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT * FROM url_checks WHERE url_id = ? ORDER BY created_at DESC")) {
            stmt.setLong(1, urlId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                UrlCheck check = new UrlCheck();
                check.setId(rs.getLong("id"));
                check.setUrlId(rs.getLong("url_id"));
                check.setStatusCode(rs.getInt("status_code"));
                check.setTitle(rs.getString("title"));
                check.setH1(rs.getString("h1"));
                check.setDescription(rs.getString("description"));
                check.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                checks.add(check);
            }
        }
        return checks;
    }

    public UrlCheck findLastByUrlId(Long urlId) throws SQLException {
        try (Connection conn = DataSourceProvider.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT * FROM url_checks WHERE url_id = ? ORDER BY created_at DESC LIMIT 1")) {
            stmt.setLong(1, urlId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                UrlCheck check = new UrlCheck();
                check.setId(rs.getLong("id"));
                check.setUrlId(rs.getLong("url_id"));
                check.setStatusCode(rs.getInt("status_code"));
                check.setTitle(rs.getString("title"));
                check.setH1(rs.getString("h1"));
                check.setDescription(rs.getString("description"));
                check.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                return check;
            }
            return null;
        }
    }
}
