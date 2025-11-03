package com.socialmedia.mssspost.service;

import com.socialmedia.mssspost.dto.CursorPayload;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;

@Service
public class CursorService {
    private static final DateTimeFormatter FMT = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    public String encode(LocalDateTime createdAt, Integer id) {
        String raw = createdAt.format(FMT) + "|" + id;
        return Base64.getUrlEncoder().withoutPadding()
                .encodeToString(raw.getBytes(StandardCharsets.UTF_8));
    }

    public CursorPayload decode(String cursor) {
        var raw = new String(Base64.getUrlDecoder().decode(cursor), StandardCharsets.UTF_8);
        var parts = raw.split("\\|", 2);
        if (parts.length != 2) throw new IllegalArgumentException("Bad cursor");
        return new CursorPayload(LocalDateTime.parse(parts[0], FMT), Integer.parseInt(parts[1]));
    }
}
