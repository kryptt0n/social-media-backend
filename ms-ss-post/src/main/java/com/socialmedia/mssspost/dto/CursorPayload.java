package com.socialmedia.mssspost.dto;

import java.time.LocalDateTime;

public record CursorPayload(LocalDateTime createdAt, Integer id) {
}
