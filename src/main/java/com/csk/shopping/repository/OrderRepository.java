package com.csk.shopping.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.csk.shopping.model.Orders;

@Repository
@Transactional
public interface OrderRepository extends JpaRepository<Orders, Long> {

	Orders findByOrderId(int orderId);

	List<Orders> findAll();

}
