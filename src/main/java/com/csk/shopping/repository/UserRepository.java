package com.csk.shopping.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.csk.shopping.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	Optional<User> findByEmailAndPasswordAndUsertype(String email, String password, String usertype);

	Optional<User> findByUsername(String username);

}
