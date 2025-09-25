DROP TABLE IF EXISTS urls, url_checks;

CREATE TABLE IF NOT EXISTS urls (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    created_at TIMESTAMP
);

CREATE TABLE url_checks (
    id SERIAL PRIMARY KEY,
    url_id BIGINT NOT NULL,
    status_code INTEGER,
    h1 TEXT,
    title TEXT,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_url
      FOREIGN KEY(url_id)
      REFERENCES urls(id)
      ON DELETE CASCADE
);