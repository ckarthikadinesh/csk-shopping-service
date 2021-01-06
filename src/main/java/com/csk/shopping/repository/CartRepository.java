package com.csk.shopping.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.csk.shopping.model.Cart;

@Repository
@Transactional
public interface CartRepository extends JpaRepository<Cart, Long> {

	List<Cart> findByEmail(String email);

	Cart findByCartIdAndEmail(int cartId, String email);

	void deleteByCartIdAndEmail(int cartId, String email);

	List<Cart> findAllByEmail(String email);

	List<Cart> findAllByOrderId(int orderId);
}
