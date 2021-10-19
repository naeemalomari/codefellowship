package com.codefellowship.restap.Repository;


import com.codefellowship.restap.model.ApplicationUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApplicationUserRepository extends JpaRepository<ApplicationUser, Long> {


     ApplicationUser findApplicationUserByUsername(String username);
     ApplicationUser findApplicationUserById(Long id);




}
