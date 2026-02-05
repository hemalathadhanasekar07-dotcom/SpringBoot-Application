package org.example.demo2.Security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.demo2.Services.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.ManagedProperties;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtFilter extends OncePerRequestFilter {
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private CustomUserDetailsService customUserDetailsService;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
       String authHeader= request.getHeader("Authorization");
       if (authHeader==null|| !authHeader.startsWith("Bearer ")){
           filterChain.doFilter(request,response);
           return;

       }
       String token=authHeader.substring(7);
       try {
           String username= jwtUtil.extractUsername(token);
           if(username!=null && SecurityContextHolder.getContext().getAuthentication()==null){
               UserDetails userDetails=customUserDetailsService.loadUserByUsername(username);
               if (jwtUtil.validateToken(token,userDetails)){
                   UsernamePasswordAuthenticationToken authenticationToken=
                           new UsernamePasswordAuthenticationToken(userDetails.getUsername(),userDetails.getPassword(),userDetails.getAuthorities());
                   authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                   SecurityContextHolder.getContext().setAuthentication(authenticationToken);
               }
           }
       } catch (Exception e) {
           Map<String,String> responseMap=new HashMap<>();
           responseMap.put("error","Invalid Token");
           ObjectMapper objectMapper=new ObjectMapper();
          String jsonString = objectMapper.writeValueAsString(responseMap);
           response.getWriter().write(jsonString);
           response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
           return;

       }
        filterChain.doFilter(request,response);


    }
}
