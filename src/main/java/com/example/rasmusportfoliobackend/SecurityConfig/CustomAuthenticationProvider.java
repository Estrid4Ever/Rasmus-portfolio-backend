package com.example.rasmusportfoliobackend.SecurityConfig;

import com.example.rasmusportfoliobackend.entity.User;
import com.example.rasmusportfoliobackend.service.UserService;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Optional;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    UserService usersService;

    private User user;

    public User getUser() {
        return user;
    }

    @Override
    public Authentication authenticate(Authentication auth)
            throws AuthenticationException {
        String username = auth.getName();
        String password = DigestUtils.sha256Hex(auth.getCredentials().toString());

        Optional<User> optionalUser = usersService.fetchOptionalUser(username);

        if (optionalUser.isPresent()) {
            user = optionalUser.get();

            if (user.getEmail().equals(username) && user.getPassword().equals(password)) {
                usersService.setUser(user);

                ArrayList<SimpleGrantedAuthority> roles = new ArrayList<>();
                roles.add(new SimpleGrantedAuthority(String.valueOf(user.isAdmin())));

                return new UsernamePasswordAuthenticationToken
                        (username, password, roles);
            } else {
                usersService.setUser(null);
                throw new
                        BadCredentialsException("Authentication failed");
            }
        } else {
            throw new
                    BadCredentialsException("Email not registered");
        }
    }

    @Override
    public boolean supports(Class<?> auth) {
        return auth.equals(UsernamePasswordAuthenticationToken.class);
    }
}

