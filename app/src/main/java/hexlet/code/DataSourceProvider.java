package hexlet.code;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

public class DataSourceProvider {

    private static HikariDataSource dataSource;

    public static DataSource getDataSource() {
        if (dataSource == null) {
            HikariConfig config = new HikariConfig();

            String rawUrl = System.getenv("JDBC_DATABASE_URL");

            try {
                if (rawUrl == null || rawUrl.isBlank()) {
                    // локально — H2
                    String jdbcUrl = "jdbc:h2:mem:project;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE";
                    config.setJdbcUrl(jdbcUrl);
                    config.setDriverClassName("org.h2.Driver");
                    config.setUsername("sa");
                    config.setPassword("");
                    System.out.println("Using H2 in-memory database");
                } else {

                    if (rawUrl.startsWith("postgres://")) {
                        URI uri = new URI(rawUrl);
                        String userInfo = uri.getUserInfo(); // user:pass
                        String user = null;
                        String pass = null;
                        if (userInfo != null) {
                            String[] up = userInfo.split(":", 2);
                            user = URLDecoder.decode(up[0], StandardCharsets.UTF_8);
                            if (up.length > 1) pass = URLDecoder.decode(up[1], StandardCharsets.UTF_8);
                        }
                        String host = uri.getHost();
                        int port = uri.getPort();
                        String path = uri.getPath(); // /dbname
                        String db = (path != null && path.startsWith("/")) ? path.substring(1) : path;

                        String jdbcUrl = String.format("jdbc:postgresql://%s:%d/%s", host, port, db);

                        config.setJdbcUrl(jdbcUrl);
                        if (user != null) config.setUsername(user);
                        if (pass != null) config.setPassword(pass);
                        config.setDriverClassName("org.postgresql.Driver");
                        System.out.println("Using Postgres (converted) at " + host + ":" + port + "/" + db);
                    } else {

                        config.setJdbcUrl(rawUrl);
                        config.setDriverClassName("org.postgresql.Driver");
                        System.out.println("Using JDBC URL from JDBC_DATABASE_URL");
                    }
                }

                config.setMaximumPoolSize(10);
                config.setAutoCommit(true);

                dataSource = new HikariDataSource(config);
            } catch (URISyntaxException e) {
                throw new RuntimeException("Invalid JDBC_DATABASE_URL format", e);
            }
        }
        return dataSource;
    }

    public static void close() {
        if (dataSource != null) {
            dataSource.close();
        }
    }
}
