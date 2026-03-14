package com.tushar.split.Service;


import com.tushar.split.DTO.verifyOTPDto;
import com.tushar.split.Model.OtpVerification;
import com.tushar.split.Model.Users;
import com.tushar.split.Repo.OtpRepo;
import com.tushar.split.Repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;


@Service
public class OtpService {


    @Autowired
    OtpRepo otpRepo;


    @Autowired
    UserRepo userRepo;

    public String generateOtp(String email){

        String otp = java.lang.String.valueOf(
                (int)(Math.random() * 900000) + 100000
        );

        OtpVerification entity=new OtpVerification();
        entity.setOtp(otp);
        entity.setEmail(email);
        entity.setExpireAt(LocalDateTime.now().plusMinutes(5));
        otpRepo.save(entity);
        return otp;
    }

    public boolean OptValidation(String email,String otp){
        OtpVerification record=otpRepo.findByEmail(email).orElse(null);
        if(record==null) return false;
        if(otp.equals(record.getOtp()) && !record.getExpireAt().isBefore(LocalDateTime.now())){
            otpRepo.delete(record);
            return true;
        }
        return false;
    }

    public String otpVerfiy(verifyOTPDto request){
        if(OptValidation(request.getEmail(),request.getOtp())){

            Users user=userRepo.findByEmail(request.getEmail()).orElse(null);
            user.setVerified(true);
            userRepo.save(user);
            return "User Verified";
        }
        else{
            throw new RuntimeException("Invalid OTP");
        }
    }


}
