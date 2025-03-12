package com.pratik.authentication.service;


import com.pratik.authentication.model.AuthenticatedUserDto;
import com.pratik.authentication.model.LoginDto;
import com.pratik.authentication.security.CustomAuthenticationProvider;
import com.pratik.authentication.util.Constants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.security.sasl.AuthenticationException;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.List;

@Service
public class AuthServiceImpl  {

    @Autowired
    private JwtService jwtService;

    @Autowired
    CustomAuthenticationProvider authenticationProvider;

    public String login(LoginDto loginDto) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        Authentication authentication = authenticationProvider.authentication(loginDto.getUsername(),loginDto.getPassword());
        if(authentication.isAuthenticated()) {
            AuthenticatedUserDto authenticatedUserDto = new AuthenticatedUserDto();
            authenticatedUserDto.setUserId(authentication.getPrincipal().toString());
            authenticatedUserDto.setRoles(authentication.getAuthorities().stream().map(Object::toString).filter(e-> e.contains(Constants.ROLE_AUTHORITY)).toList());
            authenticatedUserDto.setPrivileges(authentication.getAuthorities().stream().map(Object::toString).filter(e-> !e.contains(Constants.ROLE_AUTHORITY)).toList());
            return jwtService.generateToken(authenticatedUserDto);
        }
            throw new AuthenticationException("Invalid Username and password");
    }

    public AuthenticatedUserDto validateToken(String token) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        if(jwtService.validateToken(token)){
            String username = jwtService.getUsername(token);
            Claims claims = jwtService.extractClaims(token);
            AuthenticatedUserDto authenticatedUserDto = new AuthenticatedUserDto();
            authenticatedUserDto.setUserId(username);
            authenticatedUserDto.setRoles(claims.get(Constants.ROLE, List.class));
            authenticatedUserDto.setPrivileges(claims.get(Constants.PRIVILEGE, List.class));
            return authenticatedUserDto;
        }else{
            throw new JwtException("JWT validation failed exception");
        }
    }
}