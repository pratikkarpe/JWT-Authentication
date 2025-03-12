package com.pratik.productservice.service;

import com.pratik.productservice.model.AuthenticatedUserDto;

import com.pratik.productservice.security.CustomAuthenticationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class AuthenticationService {


    @Autowired
    CustomAuthenticationProvider authenticationProvider;

    @Value("${authentication.service.url}")
    private String authenticationUrl;


    public void authenticate(String token){
        RestClient restClient = RestClient.create();
        Map<String,String> req = new HashMap<>();
        req.put("token",token);
        ResponseEntity<AuthenticatedUserDto> authenticatedUserDto = restClient
                                                        .post()
                                                        .uri(getAuthenticationUrl())
                                                        .body(req)
                                                        .retrieve()
                                                        .toEntity(AuthenticatedUserDto.class);
        if(authenticatedUserDto.getStatusCode().is2xxSuccessful()){
            AuthenticatedUserDto userDto = authenticatedUserDto.getBody();
            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    userDto.getUserId(),null,setGrantedAuthorities(userDto.getPrivileges(),userDto.getRoles())
            );
            authenticationProvider.authenticate(authentication);
        }
    }

    private List<GrantedAuthority> setGrantedAuthorities(List<String> privileges,List<String> roles) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (String privilege : privileges) {
            authorities.add(new SimpleGrantedAuthority(privilege));
        }
        for(String role:roles){
            authorities.add(new SimpleGrantedAuthority(role));
        }
        return authorities;
    }

    public String getAuthenticationUrl() {
        return authenticationUrl;
    }
}

