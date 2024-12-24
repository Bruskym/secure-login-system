package com.example.securitystudy.dtos.responses;

import java.time.Instant;

public record PostResponse(String content, String username, Instant creationDate) {}
