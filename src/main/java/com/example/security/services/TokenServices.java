package com.example.security.services;


import com.example.exception.HttpError;
import com.example.model.entity.Account;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
@Transactional
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class TokenServices {

    private final UserDetailsServiceImpl userDetailsService;

    @Value("${security.jwt.token.prefix}")
    private String TOKEN_PREFIX;

    @Value("${security.jwt.token.secret-key}")
    private String SECRET_KEY;

    @Value("${security.jwt.token.expire-length}")
    private int TOKEN_EXPIRE_TIME;


    @PostConstruct
    protected void init() {
        SECRET_KEY = Base64.getEncoder().encodeToString(SECRET_KEY.getBytes());
    }

    public String createAccessToken(Account account) {
        Map<String, Object> payloads = new HashMap<>();
        payloads.put("jti", account.getId());
        payloads.put("role", account.getRole());
        payloads.put("name", account.getName());

        Date now = new Date();
        Date expireTime = new Date(now.getTime() + TOKEN_EXPIRE_TIME);

        return Jwts.builder()
                .setClaims(payloads)
                .setIssuedAt(now)
                .setExpiration(expireTime)
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    public Authentication getAuthentication(String token) {
        String username = getUserName(token);
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public String getUserName(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(SECRET_KEY)
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();

        } catch (ExpiredJwtException e) {
            return e.getClaims().getSubject();
        }
    }

    public String getAccessToken(HttpServletRequest req) {
        String bearerToken = req.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith(TOKEN_PREFIX + " ")) {
            return bearerToken.replace(TOKEN_PREFIX + " ", "");
        }
        return null;
    }

    public boolean isValidToken(String token) {
        try {
            Jwts.parser()
                    .setSigningKey(SECRET_KEY)
                    .parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException e) {
            return false;
        } catch (JwtException | IllegalArgumentException e) {
            throw new HttpError("Invalid JWT Token", HttpStatus.FORBIDDEN);
        }
    }

}