package com.springboot.blog.service;

import com.springboot.blog.entity.Role;
import com.springboot.blog.entity.User;
import com.springboot.blog.exception.BlogAPIException;
import com.springboot.blog.payload.LoginDto;
import com.springboot.blog.payload.RegisterDto;
import com.springboot.blog.repository.RoleRepository;
import com.springboot.blog.repository.UserRepository;
import com.springboot.blog.security.JwtTokenProvider;
import com.springboot.blog.service.impl.AuthServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceImpTest {
    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @InjectMocks
    private AuthServiceImpl authServiceImp;

    @BeforeEach
    public void setUp(){

    }

    @Test
    public void testLoginSuccess(){
        LoginDto loginDto= new LoginDto("user","password"); // To remove the error we will add the @AllArgConstructor in its Dto class
        Authentication authentication=mock(Authentication.class);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(jwtTokenProvider.generateToken(authentication)).thenReturn("token");

        String token= authServiceImp.login(loginDto);

        assertEquals("token", token);
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtTokenProvider).generateToken(authentication);

    }

    @Test
    public void testRegisterSuccess(){
        RegisterDto registerDto= new RegisterDto("Name","Name21","shubh@gmail.com","password");
        Role role = new Role();
        role.setName("ROLE_USER");

        when(userRepository.existsByEmail(registerDto.getEmail())).thenReturn(false);
        when(userRepository.existsByUsername(registerDto.getUsername())).thenReturn(false);
        when(roleRepository.findByName("ROLE_USER")).thenReturn(Optional.of(role));
        when(passwordEncoder.encode(registerDto.getPassword())).thenReturn("encodedPassword");

        String result = authServiceImp.register(registerDto);

        assertEquals("User registered successfully!.", result);
        verify(userRepository).save(any(User.class));

    }

    @Test
    public void testRegisterEmailExist(){
        RegisterDto registerDto = new RegisterDto("Name", "Name21", "shubh@gmail.com", "password");
        when(userRepository.existsByEmail(registerDto.getEmail())).thenReturn(true);

        BlogAPIException apiException = assertThrows(BlogAPIException.class, () -> {
            authServiceImp.register(registerDto);
        });

        assertEquals(HttpStatus.BAD_REQUEST, apiException.getStatus());
        assertEquals("Email is already exist!", apiException.getMessage());
    }

    @Test
    public void testRegisterUsernameExist(){
        RegisterDto registerDto= new RegisterDto("Name", "Name21", "shubh@gmail.com", "password");

        when(userRepository.existsByUsername(registerDto.getUsername())).thenReturn(true);
        BlogAPIException apiException=assertThrows(BlogAPIException.class,()->{
            authServiceImp.register(registerDto);
        });
        assertEquals(HttpStatus.BAD_REQUEST,apiException.getStatus());
        assertEquals("Username is already exist!",apiException.getMessage());
    }






}
