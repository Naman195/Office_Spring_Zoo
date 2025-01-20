package com.example.naman.configs;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.naman.DTOS.CreateUserDTO;
import com.example.naman.entities.User;

@Configuration
public class ModelMapperConfig {

	@Bean
	ModelMapper modelMapper() {
		ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setSkipNullEnabled(true);
//        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        return modelMapper;

	}
	
	@Bean
	ModelMapper userModelMapper() {
		
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.typeMap(CreateUserDTO.class,User.class).addMappings(mp -> {
		    mp.skip(User::setUserId);
		});
        return modelMapper;

	}
}
