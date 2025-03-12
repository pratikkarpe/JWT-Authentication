package com.pratik.authentication.service;

import com.pratik.authentication.model.AuthenticatedUserDto;
import com.pratik.authentication.util.Constants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;

import java.io.FileReader;
import java.io.IOException;

import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Date;

import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

@Component
public class JwtService {

    private static final Logger LOGGER = LoggerFactory.getLogger(JwtService.class);

    @Value("${jwtexpiry}")
    private long jwtExpiration;

    @Value("${private.key.path}")
    private String privateKeyFilePath;

    @Value("${public.key.path}")
    private String publicKeyFilePath;

    public String generateToken(AuthenticatedUserDto authentication){


        String username = authentication.getUserId();
        Date currentDate = new Date();
        Date expireDate = new Date(currentDate.getTime() + getJwtExpiration());

        return Jwts.builder()
                .subject(username)
                .claim(Constants.PRIVILEGE,authentication.getPrivileges())
                .claim(Constants.ROLE,authentication.getRoles())
                .issuedAt(new Date())
                .expiration(expireDate)
                .signWith(loadPrivateKey(getPrivateKeyFilePath()))
                .compact();

    }

    private static PrivateKey loadPrivateKey(String filePath) {
        try {
            Resource resource = new ClassPathResource(filePath);
            PemReader reader = new PemReader(new FileReader(resource.getFile()));
            PemObject pemObject = reader.readPemObject();
            byte[] content = pemObject.getContent();
            KeyFactory keyFactory = KeyFactory.getInstance(Constants.RSA_ALGORITHM);
            return keyFactory.generatePrivate(new PKCS8EncodedKeySpec(content));
        } catch (IOException | NoSuchAlgorithmException | InvalidKeySpecException exception) {
            LOGGER.error("Error occurred while loading private key {}", exception.getMessage());
        }
        return null;
    }

    private static PublicKey loadPublicKey(String filePath){
        try {
            Resource resource = new ClassPathResource(filePath);
            PemReader reader = new PemReader(new FileReader(resource.getFile()));
            PemObject pemObject = reader.readPemObject();
            byte[] content = pemObject.getContent();
            KeyFactory keyFactory = KeyFactory.getInstance(Constants.RSA_ALGORITHM);
            return keyFactory.generatePublic(new X509EncodedKeySpec(content));
        } catch (IOException | NoSuchAlgorithmException | InvalidKeySpecException exception) {
            LOGGER.error("Error occurred while loading public key {}", exception.getMessage());
        }
        return null;
    }

    public Claims extractClaims(String token){
        JwtParser parser = Jwts.parser()
                .verifyWith(loadPublicKey(getPublicKeyFilePath()))
                .build();
        return parser.parseSignedClaims(token).getPayload();
    }


    public String getUsername(String token)  {
        return Jwts.parser()
                .verifyWith(loadPublicKey(getPublicKeyFilePath()))
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    public boolean validateToken(String token) {
        Jwts.parser()
                .verifyWith(loadPublicKey(getPublicKeyFilePath()))
                .build()
                .parse(token);
        return true;

    }

    public long getJwtExpiration() {
        return jwtExpiration;
    }

    public String getPrivateKeyFilePath() {
        return privateKeyFilePath;
    }

    public String getPublicKeyFilePath() {
        return publicKeyFilePath;
    }
}