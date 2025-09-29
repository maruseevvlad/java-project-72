package hexlet.code;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class Url {
    private Long id;
    private String name;
    private LocalDateTime createdAt;

    public Url() { }

    public Url(Long id, String name, LocalDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Url{"
                + "id=" + id
                + ", name='" + name + '\''
                + ", createdAt=" + createdAt
                + '}';
    }

}
