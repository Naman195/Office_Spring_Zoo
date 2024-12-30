package com.example.naman.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.naman.DTOS.CategoryRepositoryDTO;
import com.example.naman.services.CategoryService;


/**
 * Category Controller
 * @author Naman Arora
 *
 * @since 30-dec-2024
 * */

@RestController
@RequestMapping("/category")
public class CategoryController {

	@Autowired
	private CategoryService categoryService;
	
	/**
	 * fetch All categories.
	 * @return Get All categories.
	 * 
	 * @author Naman Arora
	 * */
	
	@GetMapping("/all")
	public ResponseEntity<List<CategoryRepositoryDTO>>  getAllCategory()
	{
		return ResponseEntity.ok(categoryService.getAllcategory());
	}
	
}
