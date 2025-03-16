package com.example.demo.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.model.Category;
import com.example.demo.model.Product;
import com.example.demo.model.ProductOrder;
import com.example.demo.model.User;
import com.example.demo.service.CartService;
import com.example.demo.service.CategoryService;
import com.example.demo.service.OrderService;
import com.example.demo.service.ProductService;
import com.example.demo.service.UserService;
import com.example.demo.util.CommonUtil;
import com.example.demo.util.OrderStatus;

import org.springframework.data.domain.Page;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")
public class AdminController {

	@Autowired
	private CategoryService categoryService;

	@Autowired
	private ProductService productService;
	
	
	@Autowired
	private UserService userService;

	
	@Autowired
	private CartService cartService;

	
	
	@Autowired
	private OrderService orderService;

	
	
	
	@Autowired
	private CommonUtil commonUtil;

	
	
	
	
	@ModelAttribute
	public void getUserDetails(Principal p, Model m)
	{
		if(p!=null)
		{
			String email = p.getName();
			User user = userService.getUserByEmail(email);
			m.addAttribute("user", user);
			Integer countCart = cartService.getCountCart(user.getId());
			m.addAttribute("countCart", countCart);
		}
		
		List<Category> allActiveCategory = categoryService.getAllActiveCategory();
		m.addAttribute("categorys", allActiveCategory);
	}
	
	
	
	

	@GetMapping("/")
	public String index() {
		return "admin/index";
	}

	@GetMapping("/add_product")
	public String add_product(Model m) {
		List<Category> categories = categoryService.getAllCategory();
		m.addAttribute("categories", categories);
		return "admin/add_product";
	}

	@GetMapping("/category")
	public String category(Model m) {
		m.addAttribute("categorys", categoryService.getAllCategory());
		return "admin/category";
	}

	@PostMapping("/saveCategory")
	public String saveCategory(@ModelAttribute Category category, @RequestParam MultipartFile file,
			HttpSession session) {
		Boolean existsCategory = categoryService.existsCategory(category.getName());

		String imageName = file != null ? file.getOriginalFilename() : "default.jpg";
		category.setImageName(imageName);

		if (existsCategory) {
			session.setAttribute("errorMsg", "Category Name Already Exists");
		} else {
			Category saveCategory = categoryService.saveCategory(category);

			if (ObjectUtils.isEmpty(saveCategory)) {
				session.setAttribute("errorMsg", "Not Saved! Internal Serror Error");
			} else {
				session.setAttribute("successMsg", "Category Saved Successfully");
			}
		}

		return "redirect:/admin/category";

	}

	@GetMapping("/deleteCategory/{id}")
	public String deleteCategory(@PathVariable int id, HttpSession session) {
		Boolean deleteCategory = categoryService.deleteCategory(id);

		if (deleteCategory) {
			session.setAttribute("successMsg", "Category Deleted Successfully");
		} else {
			session.setAttribute("errorMsg", "Internal Server Error");

		}
		return "redirect:/admin/category";
	}

	@GetMapping("/editCategory/{id}")
	public String editCategory(@PathVariable int id, Model m) {
		m.addAttribute("category", categoryService.getCategoryById(id));
		return "/admin/edit_category";
	}

	@PostMapping("/updateCategory")
	public String updateCategory(@ModelAttribute Category category, @RequestParam("file") MultipartFile file,
			HttpSession session) throws IOException {

		Category oldCategory = categoryService.getCategoryById(category.getId());
		String imageName = file.isEmpty() ? oldCategory.getImageName() : file.getOriginalFilename();

		if (!ObjectUtils.isEmpty(category)) {

			oldCategory.setName(category.getName());
			oldCategory.setIsActive(category.getIsActive());
			oldCategory.setImageName(imageName);
		}

		Category updateCategory = categoryService.saveCategory(oldCategory);

		if (!ObjectUtils.isEmpty(updateCategory)) {

			if (!file.isEmpty()) {
				File saveFile = new ClassPathResource("static/img").getFile();

				Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + "category_img" + File.separator
						+ file.getOriginalFilename());

				// System.out.println(path);
				Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
			}

			session.setAttribute("successMsg", "Category updated successfully");
		} else {
			session.setAttribute("errorMsg", "something wrong on server");
		}

		return "redirect:/admin/editCategory/" + category.getId();
	}

	@PostMapping("/saveProduct")
	public String saveProduct(@ModelAttribute Product product, @RequestParam("file") MultipartFile image,
			HttpSession session) throws IOException {

		String imageName = image.isEmpty() ? "default.jpg" : image.getOriginalFilename();

		product.setImage(imageName);
		product.setPrice(product.getPrice());
		Product saveProduct = productService.saveProduct(product);

		if (!ObjectUtils.isEmpty(saveProduct)) {
			
			session.setAttribute("successMsg", "Product Saved Successfully");
		} 
		else 
		{
			session.setAttribute("errorMsg", "Internal Server Error");
		}

		return "redirect:/admin/add_product";
	}

	@GetMapping("/products")
	public String viewProduct(Model m,
	        @RequestParam(name = "pageNo", defaultValue = "0") Integer pageNo,
	        @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {

	    Page<Product> page = productService.getAllProductsPagination(pageNo, pageSize);

	    m.addAttribute("products", page.getContent());
	    m.addAttribute("pageNo", page.getNumber());
	    m.addAttribute("pageSize", pageSize);
	    m.addAttribute("totalElements", page.getTotalElements());
	    m.addAttribute("totalPages", page.getTotalPages());
	    m.addAttribute("isFirst", page.isFirst());
	    m.addAttribute("isLast", page.isLast());

	    return "admin/products";
	}


	@GetMapping("/deleteProduct/{id}")
	public String deleteProduct(@PathVariable int id, HttpSession session) {

		Boolean deleteProduct = productService.deleteProduct(id);

		if (deleteProduct) {
			session.setAttribute("successMsg", "Product Deleted Successfully");
		} else {
			session.setAttribute("errorMsg", "Internal Server Error");
		}
		return "redirect:/admin/products";
	}

	@GetMapping("/editProduct/{id}")
	public String editProduct(@PathVariable int id, Model m) {
		m.addAttribute("product", productService.getProductById(id));
		m.addAttribute("categories", categoryService.getAllCategory());
		return "/admin/edit_product";
	}

	@PostMapping("/updateProduct")
	public String updateProduct(@ModelAttribute Product product, @RequestParam("file") MultipartFile image,
			HttpSession session, Model m) {

		Product updateProduct = productService.updateProduct(product, image);

		if (!ObjectUtils.isEmpty(updateProduct)) {
			session.setAttribute("successMsg", "Product Update successfully");
		} else {
			session.setAttribute("errorMsg", "Internal Server Error");
		}

		return "redirect:/admin/editProduct/" + product.getId();
	}
	
	
	
	
	
	@GetMapping("/orders")
	public String getAllOrders(Model m)
	{
		List<ProductOrder> allOrders = orderService.getAllOrders();
		m.addAttribute("orders", allOrders);
		return "/admin/orders";
	}
	
	
	
	
	
	
	
	
	

	@PostMapping("/update-order-status")
	public String updateOrderStatus(@RequestParam Integer id, @RequestParam Integer st, HttpSession session)
	{
		
		OrderStatus[] values = OrderStatus.values();
		String status = null;
		
		for(OrderStatus ordertSt : values)
		{
			if(ordertSt.getId().equals(st))
			{
				status = ordertSt.getName();
			}
		}
		
		
		ProductOrder updateOrder = orderService.updateOrderStatus(id, status);
		try {
			commonUtil.sendMailForProductOrder(updateOrder, status);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		
		if(!ObjectUtils.isEmpty(updateOrder))
		{
			session.setAttribute("successMsg", "Your Order is Cancelled");
		}
		else
		{
			session.setAttribute("errorMsg", "Internal Server Error");
		}
		
		return "redirect:/admin/orders";

	}
	

}
