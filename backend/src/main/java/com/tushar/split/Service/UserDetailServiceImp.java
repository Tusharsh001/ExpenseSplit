package com.tushar.split.Service;

import com.tushar.split.Model.Users;
import com.tushar.split.Repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class UserDetailServiceImp implements UserDetailsService {

    @Autowired
    UserRepo userRepo;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users user=userRepo.findByUsername(username).orElse(null);
        if(user!=null){
            return User.builder()
                    .password(user.getUsername())
                    .username(user.getUsername())
                    .build();
        }
        return null;
    }
}
