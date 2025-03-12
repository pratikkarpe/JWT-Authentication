package com.pratik.authentication.controller;

import com.pratik.authentication.model.AuthResponseDto;
import com.pratik.authentication.model.AuthenticatedUserDto;
import com.pratik.authentication.model.LoginDto;
import com.pratik.authentication.service.AuthServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.security.sasl.AuthenticationException;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Map;


@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthServiceImpl authService;

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody LoginDto loginDto) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {

        String token = "";
        try {
             token = authService.login(loginDto);
        } catch (AuthenticationException e) {
            return new ResponseEntity<>("Wrong Username or Password",HttpStatus.UNAUTHORIZED);
        }
        AuthResponseDto authResponseDto = new AuthResponseDto();
        authResponseDto.setAccessToken(token);
        return new ResponseEntity<>(authResponseDto, HttpStatus.OK);
    }

    @PostMapping("/validatejwt")
    public ResponseEntity<AuthenticatedUserDto> validateJwtToken(@RequestBody Map<String,String> jwtToken) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        AuthenticatedUserDto authenticatedUserDto = authService.validateToken(jwtToken.get("token"));
        return new ResponseEntity<>(authenticatedUserDto, HttpStatus.OK);
    }
}