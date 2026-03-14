package com.tushar.split.Service;


import com.tushar.split.DTO.LoginRequestDto;
import com.tushar.split.DTO.RegisterRequestDto;
import com.tushar.split.Model.Users;
import com.tushar.split.Repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserService {

    @Autowired
    UserRepo userRepo;

    @Autowired
    JwtService jwtService;

    @Autowired
    OtpService otpService;

    @Autowired
    EmailService emailService;

    private BCryptPasswordEncoder encoder=new BCryptPasswordEncoder(12);

    public String  register(RegisterRequestDto request) {
        if(userRepo.findByUsername(request.getUsername()).isPresent()){
            throw new RuntimeException("username Already Taken");

        }
        if(userRepo.findByEmail(request.getEmail()).isPresent()){
            throw new RuntimeException("Email Already Exist");

        }
        Users users =new Users();
        users.setEmail(request.getEmail());
        users.setRegisterAt(LocalDateTime.now());
        users.setUsername(request.getUsername());
        users.setPassword(encoder.encode(request.getPassword()));
        userRepo.save(users);
        String otp=otpService.generateOtp(users.getEmail());
        emailService.sendMail(otp,users.getEmail());
        return users.getEmail();
    }


    public String logIn(LoginRequestDto request){

        Users users =userRepo.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("Enter Correct Username or Password"));
        if(!users.getVerified()) throw new RuntimeException("Email Not verified");
        if(!encoder.matches(request.getPassword(),users.getPassword())){
            throw new RuntimeException("Enter Correct Username or Password");
        }

        return jwtService.GenerateToken(users.getUsername());

    }


}
