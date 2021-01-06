package com.csk.shopping.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.csk.shopping.model.Product;

@Repository
@Transactional
public interface ProductRepository extends JpaRepository<Product, Long> {

	Product findByProductid(int productid);

	void deleteByProductid(int productid);

	List<Product> findAll();
}
