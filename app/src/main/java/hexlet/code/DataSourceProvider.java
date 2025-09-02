package hexlet.code;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;

public class DataSourceProvider {
    private static HikariDataSource dataSource;

    public static DataSource getDataSource() {
        if (dataSource == null) {
            HikariConfig config = new HikariConfig();

            String jdbcUrl = System.getenv("JDBC_DATABASE_URL");

            if (jdbcUrl == null || jdbcUrl.isBlank()) {

                config.setJdbcUrl("jdbc:h2:mem:project;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE");
                config.setUsername("sa");
                config.setPassword("");
                config.setDriverClassName("org.h2.Driver");
            } else {
                config.setJdbcUrl(jdbcUrl);
                config.setDriverClassName("org.postgresql.Driver");
            }

            config.setJdbcUrl(jdbcUrl);
            config.setMaximumPoolSize(30);
            config.setAutoCommit(true);

            dataSource = new HikariDataSource(config);
        }
        return dataSource;
    }

    public static void closeDataSource() {
        if (dataSource != null) {
            dataSource.close();
        }
    }
}
