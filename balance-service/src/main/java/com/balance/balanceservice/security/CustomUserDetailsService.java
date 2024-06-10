package com.balance.balanceservice.security;

import com.balance.balanceservice.payload.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDto userDto = getUserDto(username);

        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("STANDARD_USER"));

        return new org.springframework.security.core.userdetails.User(userDto.getEmail(),userDto.getPassword(),authorities);
    }

    private UserDto getUserDto(String email) {
        String url = "http://localhost:8082/api/user?username=";
        ResponseEntity<UserDto> responseEntity = restTemplate.getForEntity(url.concat(email), UserDto.class);
        return responseEntity.getBody();
    }
}
