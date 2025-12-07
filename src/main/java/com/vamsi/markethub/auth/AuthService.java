package com.vamsi.markethub.auth;

import com.vamsi.markethub.security.JwtService;
import com.vamsi.markethub.user.Role;
import com.vamsi.markethub.user.User;
import com.vamsi.markethub.user.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public void registerUser(RegistrationRequest request){
        if(userRepository.existsByUsername(request.getUsername())){
            throw new IllegalArgumentException("Username already Exists");
        }

        Role role;
        try{
            role = Role.valueOf(request.getRole().toUpperCase());
        }
        catch (Exception e){
            throw new IllegalArgumentException("Invalid Role: use CUSTOMER or SELLER");
        }

        String hashedPassword = passwordEncoder.encode(request.getPassword());

        User user = new User(request.getUsername(), hashedPassword, role);
        userRepository.save(user);
    }

    public LoginResponse login(LoginRequest request){
        User user = userRepository.findByUsername(request.getUsername()).orElseThrow(
                () -> new IllegalArgumentException("Invalid Username or Password")
        );

        if(!passwordEncoder.matches(request.getPassword(), user.getPassword())){
            throw new IllegalArgumentException("Invalid password");
        }

        String token = jwtService.generateToken(user);

        return new LoginResponse(token,user.getRole().name());
    }
}
