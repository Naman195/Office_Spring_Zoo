package com.example.naman.DTOS;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class ZooResponseDTO {

	private Long zooId;
	private String zooName;
	
	private ZooAddressDTO address;
	
}
 