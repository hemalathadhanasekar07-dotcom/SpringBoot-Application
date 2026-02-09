package org.example.demo2.Services;

import org.example.demo2.entities.Student;
import org.example.demo2.repositories.StudentRepository;
import org.springframework.beans.factory.BeanRegistrarDslMarker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.security.autoconfigure.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
@Component
@Service
public  class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private StudentRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //fetch user from database
        Student user= userRepository.findByUsername(username).
                orElseThrow( ()->new UsernameNotFoundException("user Not Found!"));
        return new User(user.getUsername(),user.getPassword(), Collections.singleton(new SimpleGrantedAuthority("USER_ROLE")));

    }
}