package hexlet.code;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.Statement;

public class DbInitializer {

    public static void init() {
        try (Connection conn = DataSourceProvider.getDataSource().getConnection()) {
            InputStream is = DbInitializer.class.getClassLoader().getResourceAsStream("schema.sql");
            if (is == null) {
                System.out.println("schema.sql not found in resources");
                return;
            }
            String sql = new String(is.readAllBytes(), StandardCharsets.UTF_8);
            // Разбиваем по ';' чтобы выполнить несколько команд
            for (String stmt : sql.split(";")) {
                String s = stmt.trim();
                if (!s.isEmpty()) {
                    try (Statement statement = conn.createStatement()) {
                        statement.execute(s);
                    }
                }
            }
            System.out.println("DB schema initialized");
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize DB schema", e);
        }
    }
}
