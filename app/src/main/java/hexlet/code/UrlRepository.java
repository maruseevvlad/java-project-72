package hexlet.code;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class UrlRepository extends BaseRepository {
    public UrlRepository(){
        super(DataSourceProvider.getDataSource());
    }

    public void save(Url url) throws SQLException {
        String sql = "INSERT INTO urls (name, created_at) VALUES (?, ?)";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, url.getName());
            LocalDateTime createdAt = url.getCreatedAt();
            if (createdAt != null) {
                createdAt = LocalDateTime.now();
                url.setCreatedAt(createdAt);
            }
            stmt.setTimestamp(2, Timestamp.valueOf(createdAt));
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    url.setId(rs.getLong(1));
                }
            }
        }
    }

    public List<Url> findAll() throws SQLException {
        String sql = "SELECT * FROM urls ORDER BY id";
        List<Url> urls = new ArrayList<>();

        try (Connection connection = dataSource.getConnection();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Url url = new Url();
                url.setId(rs.getLong("id"));
                url.setName(rs.getString("name"));
                Timestamp timestamp = rs.getTimestamp("created_at");
                if (timestamp != null) {
                    url.setCreatedAt(timestamp.toLocalDateTime());
                }
                urls.add(url);
            }
        }
        return urls;
    }

}
