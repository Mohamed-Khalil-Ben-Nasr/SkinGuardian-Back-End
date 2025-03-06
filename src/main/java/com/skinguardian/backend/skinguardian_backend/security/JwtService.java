/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.skinguardian.backend.skinguardian_backend.security;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Service;

import io.jsonwebtoken.Jwts;

@Service
public class JwtService {
    SecretKey key = Jwts.SIG.HS256.key().build();

    public boolean isValid(String token) {
            try {
                    return Jwts.parser()
                                    .verifyWith(key)
                                    .build()
                                    .parseSignedClaims(token)
                                    .getPayload()
                                    .getExpiration()
                                    .after(new Date(System.currentTimeMillis()));
            } catch (Exception ex) {

            }
            return false;
    }

    public String getSubject(String token) {
            return Jwts.parser()
                            .verifyWith(key)
                            .build()
                            .parseSignedClaims(token)
                            .getPayload()
                            .getSubject();
    }

    public String makeJwt(String userid) {
            return Jwts.builder()
                            .subject(userid)
                            .issuedAt(new Date(System.currentTimeMillis()))
                            .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 30))
                            .signWith(key)
                            .compact();
    }
}
