package com.example.naman.services;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.naman.DTOS.CategoryRepositoryDTO;
import com.example.naman.entities.CategoryTypes;
import com.example.naman.repositories.CategoryRepository;
/**
 * Category Service
 * @author Naman Arora
 *
 * @since 30-dec-2024
 * */

@Service
public class CategoryService {
	
	@Autowired
	private CategoryRepository categoryRepository;
	
	@Autowired
	private ModelMapper modelMapper;
	
	/**
	 * fetch All categories.
	 * @return Get All categories.
	 * 
	 * @author Naman Arora
	 * */
	
	public List<CategoryRepositoryDTO> getAllcategory()
	{
		List<CategoryTypes> categories =  categoryRepository.findAll();
		return categories.stream().map(category -> modelMapper.map(category, CategoryRepositoryDTO.class)).collect(Collectors.toList());
	}

}
