package com.example.demo.repository;

import java.util.List;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer>{
	
	List<Product> findByisActiveTrue();

//	Page<Product> findByisActiveTrue(Pageable pageable);
//
	List<Product> findByCategory(String category);
//
//	List<Product> findByNameContainingIgnoreCaseOrCategoryContainingIgnoreCase(String ch, String ch2);
//
//	Page<Product> findByCategory(Pageable pageable, String category);
//
//	Page<Product> findByNameContainingIgnoreCaseOrCategoryContainingIgnoreCase(String ch, String ch2,
//			Pageable pageable);
//
//	Page<Product> findByisActiveTrueAndNameContainingIgnoreCaseOrCategoryContainingIgnoreCase(String ch, String ch2,
//			Pageable pageable);


}
