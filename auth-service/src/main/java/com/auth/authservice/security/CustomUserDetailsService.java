package com.auth.authservice.security;



import com.auth.authservice.repository.UserRepository;
import com.auth.authservice.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {

        User user = userRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail)
                                  .orElseThrow(()-> new UsernameNotFoundException("User not found with the email or username :"+usernameOrEmail));

        List<GrantedAuthority> authorities = new ArrayList<>();
                                                authorities.add(new SimpleGrantedAuthority("STANDARD_USER"));


        return new org.springframework.security.core.userdetails.User(user.getEmail(),user.getPassword(),authorities);
    }

}
