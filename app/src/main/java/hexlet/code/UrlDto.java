package hexlet.code;

import java.time.LocalDateTime;

public class UrlDto {
    private Long id;
    private String name;
    private LocalDateTime createdAt;
    private UrlCheck lastCheck;

    public UrlDto(Url url, UrlCheck lastCheck) {
        this.id = url.getId();
        this.name = url.getName();
        this.createdAt = url.getCreatedAt();
        this.lastCheck = lastCheck;
    }

    public Long getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    public UrlCheck getLastCheck() {
        return lastCheck;
    }
}
