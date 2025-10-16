package hexlet.code.util;

import hexlet.code.model.Url;
import hexlet.code.model.UrlCheck;

import java.time.format.DateTimeFormatter;

public class DateUtils {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public static String getFormattedUrlDate(Url url) {
        return url.getCreatedAt().toLocalDateTime().format(FORMATTER);
    }

    public static String getFormattedCheckDate(UrlCheck urlCheck) {
        return urlCheck.getCreatedAt().toLocalDateTime().format(FORMATTER);
    }
}
