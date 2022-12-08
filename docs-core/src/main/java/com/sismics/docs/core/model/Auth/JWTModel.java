package com.sismics.docs.core.model.Auth;

import java.util.Date;

public class JWTModel {
    public String accessToken;
    public Date expiresAt;
    public long expiresInSeconds;

    public JWTModel(String accessToken, Date expiresAt, long expiresInSeconds) {
        this.accessToken = accessToken;
        this.expiresAt = expiresAt;
        this.expiresInSeconds = expiresInSeconds;
    }
}
