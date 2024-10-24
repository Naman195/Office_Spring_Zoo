package com.example.naman.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter

@NoArgsConstructor
@AllArgsConstructor



@Entity
public class State {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name  = "state_id")
	private Long stateId;
	
	@Column(name = "state_name", nullable = false)
	private String stateName;
	
	
	@ManyToOne
	@JoinColumn(name = "country_id", nullable = false)
	private Country country;

	
	
	
	
	
}
