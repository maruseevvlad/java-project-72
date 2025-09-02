package hexlet.code;

import javax.sql.DataSource;

public abstract class  BaseRepository {
    protected final DataSource dataSource;

    protected BaseRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }
}
