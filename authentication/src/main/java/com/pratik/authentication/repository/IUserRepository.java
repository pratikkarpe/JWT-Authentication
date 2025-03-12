package com.pratik.authentication.repository;

import com.pratik.authentication.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface IUserRepository extends JpaRepository<User,String> {

    @Query(name = "User.findByUsername")
    public User findByUsername(@Param(value = "username") String username);
}
