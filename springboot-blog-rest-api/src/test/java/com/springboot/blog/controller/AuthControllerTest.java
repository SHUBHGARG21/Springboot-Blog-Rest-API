package com.springboot.blog.controller;

import com.springboot.blog.payload.JWTAuthResponse;
import com.springboot.blog.payload.LoginDto;
import com.springboot.blog.payload.RegisterDto;
import com.springboot.blog.service.AuthService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthControllerTest {
    @Mock
    private AuthService authService;

    @InjectMocks
    private AuthController authController;

    @Test
    public void testLogin(){
        LoginDto loginDto = new LoginDto();
        loginDto.setUsernameOrEmail("shubh@gmail.com");
        loginDto.setPassword("shubh");
        String token = "fake-jwt-token";

        when(authService.login(loginDto)).thenReturn(token);

        ResponseEntity<JWTAuthResponse> response=authController.login(loginDto);

        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals(token,response.getBody().getAccessToken());
        verify(authService,times(1)).login(loginDto);
    }

    @Test
    public void testRegister(){
        RegisterDto registerDto = new RegisterDto();
        registerDto.setEmail("shubh@gmail.com");
        registerDto.setPassword("shubh");
        registerDto.setName("Shubh");
        registerDto.setUsername("Shubh");

        String str="User registered successfully!.";

        when(authService.register(registerDto)).thenReturn(str);

        ResponseEntity<String> response = authController.register(registerDto);

        assertEquals(HttpStatus.CREATED,response.getStatusCode());
        assertEquals(str,response.getBody());
        verify(authService,times(1)).register(registerDto);
    }
}
