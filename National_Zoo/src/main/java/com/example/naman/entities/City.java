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

/**
 * Documentation
 *
 * @author Naman Arora
 * @version 1.0.0
 * @since 14-Feb-2025
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class City {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name  = "city_id")
	private Long cityId;

	@Column(name = "city_name", nullable = false)
	private String cityName;

	@ManyToOne
	@JoinColumn(name = "state_id", nullable = false)
	private State state;
}
