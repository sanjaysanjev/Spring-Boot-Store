package com.sanjay.store.auth;

import com.sanjay.store.Users.UserMapper;
import com.sanjay.store.Users.userDto;
import com.sanjay.store.Users.UserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

import lombok.var;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {

    private AuthenticationManager authenticationManager;
    private JWTService jwtService;
    private JwtConfig jwtConfig;
    private UserRepository userRepository;
    private UserMapper userMapper;
    @PostMapping("/login")

    public ResponseEntity<JWTResponse> login(@Valid @RequestBody LoginRequest request, HttpServletResponse response)
    {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(),request.getPassword()));

        var user=userRepository.findByemail(request.getEmail()).orElseThrow(null);
        var acessToken=jwtService.generateAccessToken(user);
        var refreshToken= jwtService.generateRefreshToken(user);
        var cookie=new Cookie("refreshToken",refreshToken.toString());
        cookie.setHttpOnly(true);
        cookie.setPath("/auth");
        cookie.setMaxAge((int)jwtConfig.getRefreshTokenExpiration());
        cookie.setSecure(true);
        response.addCookie(cookie);

        return ResponseEntity.ok(new JWTResponse(acessToken.toString()));

    }

    @PostMapping("/refresh")
    public ResponseEntity<JWTResponse>refresh(@CookieValue(value="refreshToken") String refreshToken)
    {
        var jwt=jwtService.parse(refreshToken);
        if(jwt==null || jwt.isExpired())
        {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        var user=userRepository.findById(jwt.getUserId()).orElseThrow(null);
        var accessToken=jwtService.generateAccessToken(user);
        return ResponseEntity.ok(new JWTResponse(accessToken.toString()));
    }
    @GetMapping("/me")
    public ResponseEntity<userDto> me()
    {
        var authentication= SecurityContextHolder.getContext().getAuthentication();
        var userId=(Long)authentication.getPrincipal();
        var user=userRepository.findById(userId).orElse(null);
        if(user==null)
        {
            return ResponseEntity.notFound().build();
        }
        var userDto=userMapper.toDto(user);
        return ResponseEntity.ok(userDto);

    }
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Void> badhandleException()
    {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}
