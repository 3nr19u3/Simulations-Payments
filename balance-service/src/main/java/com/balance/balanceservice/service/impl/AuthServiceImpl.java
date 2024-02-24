package com.balance.balanceservice.service.impl;



import com.balance.balanceservice.entity.User;
import com.balance.balanceservice.exception.APIException;
import com.balance.balanceservice.payload.LoginDto;
import com.balance.balanceservice.payload.RegisterDto;
import com.balance.balanceservice.repository.UserRepository;
import com.balance.balanceservice.security.JwtTokenProvider;
import com.balance.balanceservice.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private AuthenticationManager authenticationManager;

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private JwtTokenProvider jwtTokenProvider;

    public AuthServiceImpl(AuthenticationManager authenticationManager,
                           UserRepository userRepository,
                           PasswordEncoder passwordEncoder,
                           JwtTokenProvider jwtTokenProvider) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public String login(LoginDto loginDto) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginDto.getUsernameOrEmail(), loginDto.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        return jwtTokenProvider.generateToken(authentication);
    }

    @Override
    public String register(RegisterDto registerDto) {
        //Add check for username exists in database
        if(userRepository.existsByUsername(registerDto.getUsername()))
            throw new APIException(HttpStatus.BAD_REQUEST,"Username is already exist!");

        if(userRepository.existsByEmail(registerDto.getEmail()))
            throw new APIException(HttpStatus.BAD_REQUEST,"Email is already exist!");

        //Set<Role> roles = new HashSet<>();
        //Role userRole = roleRepository.findByName("ROLE_USER").get();
        //roles.add(userRole);

        User user = new User(null,
                             registerDto.getName(),
                             registerDto.getUsername(),
                             registerDto.getEmail(),
                             passwordEncoder.encode(registerDto.getPassword()));

        userRepository.save(user);

        return "User registered successfully";
    }
}
