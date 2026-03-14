package com.tushar.split.Controllers;


import com.tushar.split.DTO.LoginRequestDto;
import com.tushar.split.DTO.RegisterRequestDto;
import com.tushar.split.DTO.verifyOTPDto;
import com.tushar.split.Model.Users;
import com.tushar.split.Repo.UserRepo;
import com.tushar.split.Service.OtpService;
import com.tushar.split.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/auth")
@RestController
public class AuthController {

    @Autowired
    UserService userService;

    @Autowired
    UserRepo userRepo;

    @Autowired
    OtpService otpService;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser( @RequestBody RegisterRequestDto request) {
       String email=userService.register(request);
       return new ResponseEntity<>(email,HttpStatus.CREATED);
    }

//     Verify OTP
    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp( @RequestBody verifyOTPDto request) {
        String response=otpService.otpVerfiy(request);
        return ResponseEntity.ok(request);
    }
//
//     Login
    @PostMapping("/login")
    public String loginUser( @RequestBody LoginRequestDto request) {

        return userService.logIn(request);
    }
//
//    // Resend OTP
//    @PostMapping("/resend-otp")
//    public ResponseEntity<?> resendOtp( @RequestParam String email) {
//
//
//        return ResponseEntity.ok(email);
//    }
}
