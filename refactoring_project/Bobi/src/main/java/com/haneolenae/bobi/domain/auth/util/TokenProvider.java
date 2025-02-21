package com.haneolenae.bobi.domain.auth.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Date;

public interface TokenProvider {
    public String createAccessToken(Long id) ;

    public String createRefreshToken(Long id);

    public String getTokenFromHeader(String accessHeader);

    public boolean validateToken(String token) ;;

    public Long getIdFromToken(String token);

    public Long getIdFromRefreshToken(String refreshToken);

    public Long getExpiration(String token);
}
