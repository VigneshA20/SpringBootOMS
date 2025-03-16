package com.example.demo.ServiceImpl;

import java.io.File;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.model.Product;
import com.example.demo.repository.ProductRepository;
import com.example.demo.service.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@Service
public class ProductServiceImpl implements ProductService{

	@Autowired
	private ProductRepository productRepository;
	
	@Override
	public Product saveProduct(Product product) {
		return productRepository.save(product);
	}

	@Override
	public List<Product> getAllProducts() {
		return productRepository.findAll();
	}

	@Override
	public Boolean deleteProduct(Integer id) {
		
		
		Product product = productRepository.findById(id).orElse(null);
		
		if(!ObjectUtils.isEmpty(product))
		{
			productRepository.delete(product);
			return true;
		}
		
		return false;
	}

	@Override
	public Product getProductById(Integer id) {
		Product product = productRepository.findById(id).orElse(null);
		
		return product;
	}

	@Override
	public Product updateProduct(Product product, MultipartFile image) {
		
		
		Product dbProduct = getProductById(product.getId());
		
		String imageName = image.isEmpty() ? dbProduct.getImage() : image.getOriginalFilename();
		
		
		
		dbProduct.setName(product.getName());
		dbProduct.setDescription(product.getDescription());
		dbProduct.setCategory(product.getCategory());
		dbProduct.setPrice(product.getPrice());
		dbProduct.setQuantity(product.getQuantity());
		dbProduct.setImage(imageName);
		dbProduct.setIsActive(product.getIsActive());
		
		
		Product updateProduct = productRepository.save(dbProduct);
		
		
		if(!ObjectUtils.isEmpty(updateProduct))
		{
			if(!image.isEmpty())
			{
				try 
				{
					File saveFile = new ClassPathResource("static/images").getFile();
				} 
				catch (Exception e) 
				{
					e.printStackTrace();
				}
			}
			return product;
		}
		
		return null;
		
	}
	
	
	
	@Override
	public List<Product> getAllActiveProducts() {
//		List<Product> products = null;
//		if (ObjectUtils.isEmpty(category)) {
//			products = productRepository.findByisActiveTrue();
//		} else {
//			products = productRepository.findByCategory(category);
//		}
//
		List<Product> products = productRepository.findByisActiveTrue();
		return products;
	}
//
//	@Override
//	public List<Product> searchProduct(String ch) {
//		return productRepository.findByNameContainingIgnoreCaseOrCategoryContainingIgnoreCase(ch, ch);
//	}
//
//	@Override
//	public Page<Product> searchProductPagination(Integer pageNo, Integer pageSize, String ch) {
//		Pageable pageable = PageRequest.of(pageNo, pageSize);
//		return productRepository.findByNameContainingIgnoreCaseOrCategoryContainingIgnoreCase(ch, ch, pageable);
//	}
//
//	@Override
//	public Page<Product> getAllActiveProductPagination(Integer pageNo, Integer pageSize, String category) {
//
//		Pageable pageable = PageRequest.of(pageNo, pageSize);
//		Page<Product> pageProduct = null;
//
//		if (ObjectUtils.isEmpty(category)) {
//			pageProduct = productRepository.findByisActiveTrue(pageable);
//		} else {
//			pageProduct = productRepository.findByCategory(pageable, category);
//		}
//		return pageProduct;
//	}
//
//	@Override
//	public Page<Product> searchActiveProductPagination(Integer pageNo, Integer pageSize, String category, String ch) {
//
//		Page<Product> pageProduct = null;
//		Pageable pageable = PageRequest.of(pageNo, pageSize);
//
//		pageProduct = productRepository.findByisActiveTrueAndNameContainingIgnoreCaseOrCategoryContainingIgnoreCase(ch,
//				ch, pageable);
//
//		if (ObjectUtils.isEmpty(category)) {
//			pageProduct = productRepository.findByIsActiveTrue(pageable);
//		} else {
//			pageProduct = productRepository.findByCategory(pageable, category);
//		}
//		return pageProduct;
//	}
//
	@Override
	public Page<Product> getAllProductsPagination(Integer pageNo, Integer pageSize) {
	    Pageable pageable = PageRequest.of(pageNo, pageSize);
	    Page<Product> page = productRepository.findAll(pageable);
	    
	    if (page.isEmpty()) {
	        return Page.empty(); // Avoid null issues
	    }

	    return page;
	}




}
