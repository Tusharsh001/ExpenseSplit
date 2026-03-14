package com.tushar.split.config;

import com.tushar.split.Service.JwtService;
import com.tushar.split.Service.UserDetailServiceImp;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    JwtService jwtService;

    @Autowired
    UserDetailServiceImp userDetailService;




    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
      String authHeader=request.getHeader("Authorization");
      System.out.println("auth Header "+ authHeader);
      if(authHeader==null || !authHeader.startsWith("Bearer ")){
          filterChain.doFilter(request,response);
          return;
      }
      String token=authHeader.substring(7);
      System.out.println(token);
      String username=jwtService.extractUsername(token);
      if(username!=null && SecurityContextHolder.getContext().getAuthentication()==null){
          UserDetails userDetails= userDetailService.loadUserByUsername(username);
          if(jwtService.isTokenValid(token,username) && !jwtService.isTokenExpire(token)){
              UsernamePasswordAuthenticationToken userToken=new UsernamePasswordAuthenticationToken(
                      userDetails,
                      null,
                      userDetails.getAuthorities()
              );

              userToken.setDetails(
                      new WebAuthenticationDetailsSource().buildDetails(request)
              );
              SecurityContextHolder.getContext().setAuthentication(userToken);
          }

      }
      filterChain.doFilter(request,response);
    }
}
