package com.security.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtUtils;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/hello")
    public String hello(){
        return "Hello World";
    }


    @GetMapping("/admin/hello")
    public String adminHello(){
        return "Hello Admin";
    }

    @GetMapping("/user/hello")
    public String userHello(){
        return "Hello User";
    }



    @PostMapping("/signin")
    public String login(@RequestBody LoginRequest loginRequest){
        Authentication authentication = null;
        try {
            System.out.println("Username received: "
                    + loginRequest.getUsername());

            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword()
                    )
            );
        } catch (AuthenticationException e) {
            e.printStackTrace();
            return "Authentication failed: " + e.getMessage();
        }

        SecurityContext securityContext =
                SecurityContextHolder.getContext();
                UserDetails  userDetails = (UserDetails) authentication.getPrincipal();
                String jwtToken = jwtUtils.generateTokenFromUsername(userDetails .getUsername());

                return jwtToken;
    }
}
