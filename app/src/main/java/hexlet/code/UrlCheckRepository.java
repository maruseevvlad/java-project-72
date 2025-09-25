package hexlet.code;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class UrlCheckRepository {
    private final DataSource dataSource;

    public UrlCheckRepository() {
        this(DataSourceProvider.getDataSource());
    }

    public UrlCheckRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public UrlCheck save(UrlCheck check) {
        String sql = "INSERT INTO url_checks (url_id, status_code, h1, title, description, created_at) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            if (check.getCreatedAt() == null) {
                check.setCreatedAt(LocalDateTime.now());
            }

            stmt.setLong(1, check.getUrlId());
            if (check.getStatusCode() == null) stmt.setNull(2, Types.INTEGER);
            else stmt.setInt(2, check.getStatusCode());
            stmt.setString(3, check.getH1());
            stmt.setString(4, check.getTitle());
            stmt.setString(5, check.getDescription());
            stmt.setTimestamp(6, Timestamp.valueOf(check.getCreatedAt()));

            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    check.setId(rs.getLong(1));
                }
            }
            return check;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<UrlCheck> findAllByUrlId(Long urlId) {
        String sql = "SELECT * FROM url_checks WHERE url_id = ? ORDER BY created_at DESC";
        List<UrlCheck> list = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, urlId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    UrlCheck ch = mapRow(rs);
                    list.add(ch);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    public UrlCheck findLastByUrlId(Long urlId) {
        String sql = "SELECT * FROM url_checks WHERE url_id = ? ORDER BY created_at DESC LIMIT 1";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, urlId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    private UrlCheck mapRow(ResultSet rs) throws SQLException {
        UrlCheck ch = new UrlCheck();
        ch.setId(rs.getLong("id"));
        ch.setUrlId(rs.getLong("url_id"));
        int code = rs.getInt("status_code");
        if (!rs.wasNull()) ch.setStatusCode(code);
        ch.setH1(rs.getString("h1"));
        ch.setTitle(rs.getString("title"));
        ch.setDescription(rs.getString("description"));
        Timestamp t = rs.getTimestamp("created_at");
        if (t != null) ch.setCreatedAt(t.toLocalDateTime());
        return ch;
    }
}
