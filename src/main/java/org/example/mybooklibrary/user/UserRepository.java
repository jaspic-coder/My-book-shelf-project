package org.example.mybooklibrary.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    //Optional<User> findByRegNo(String regNo);
    //Optional<User> findByCollegeRegNo(String collegeRegNo);

    // Add this method to find user by password reset token
    Optional<User> findByPasswordResetToken(String token);
}
