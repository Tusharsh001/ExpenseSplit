package com.tushar.split.Repo;

import com.tushar.split.Model.OtpVerification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OtpRepo extends JpaRepository<OtpVerification,Integer> {

    Optional<OtpVerification> findByEmail(String email);
}
