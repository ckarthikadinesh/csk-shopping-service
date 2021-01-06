package com.csk.shopping.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.csk.shopping.model.Address;
import com.csk.shopping.model.User;

@Repository
@Transactional
public interface AddressRepository extends JpaRepository<Address, Long> {

	Address findByUser(User user);
}
