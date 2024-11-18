package com.example.naman.services;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.naman.DTOS.CategoryRepositoryDTO;
import com.example.naman.entities.CategoryTypes;
import com.example.naman.repositories.CategoryRepository;

@Service
public class CategoryService {
	
	@Autowired
	private CategoryRepository categoryRepository;
	
	@Autowired
	private ModelMapper modelMapper;
	
	public List<CategoryRepositoryDTO> getAllcategory()
	{
		List<CategoryTypes> categories =  categoryRepository.findAll();
		return categories.stream().map(category -> modelMapper.map(category, CategoryRepositoryDTO.class)).collect(Collectors.toList());
	}

}
