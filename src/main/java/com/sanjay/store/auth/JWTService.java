package com.sanjay.store.auth;

import com.sanjay.store.Users.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import lombok.AllArgsConstructor;

import lombok.var;
import org.springframework.stereotype.Service;

import java.util.Date;

@AllArgsConstructor
@Service
public class JWTService {

    private final JwtConfig jwtConfig;

    public Jwt generateAccessToken(User user)
    {


        return generateToken(user, jwtConfig.getAccessTokenExpiration());

    }
    public Jwt generateRefreshToken(User user)
    {


        return generateToken(user, jwtConfig.getRefreshTokenExpiration());

    }
    public Jwt generateToken(User user,long expiration)
    {
        var claims=Jwts.claims().subject(user.getId().toString())
                .add("email",user.getEmail())
                .add("Name",user.getName())
                .add("Role",user.getRole())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis()+1000*expiration))
                .build();

        return new Jwt(claims,Keys.hmacShaKeyFor((jwtConfig.getSecret().getBytes())));

        /*return Jwts.builder().subject(user.getId().toString())
                .claim("Email",user.getEmail())
                .claim("Name",user.getName())
                .claim("Role",user.getRole())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis()+1000*expiration))
                .signWith(Keys.hmacShaKeyFor(jwtConfig.getSecret().getBytes())).compact();*/
    }

    public Claims getClaims(String token)
    {
        return Jwts.parser().verifyWith(Keys.hmacShaKeyFor(jwtConfig.getSecret().getBytes())).build()
                .parseSignedClaims(token).getPayload();
    }
    public Jwt parse(String token)
    {
        try
        {
            var claims=getClaims(token);
            return new Jwt(claims,Keys.hmacShaKeyFor((jwtConfig.getSecret().getBytes())));
        }
        catch (Exception e)
        {
            return null;
        }
    }
}
