package com.example.naman.entities;

import java.time.Instant;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter

@NoArgsConstructor
@AllArgsConstructor



@Entity
@Table(name = "animal")
public class Animal {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name  = "animal_id")
	private Long animalId;
	
	@Column(name = "animal_name", nullable = false)
	private String animalName;
	
	
	@Column(name = "animal_type", nullable = false)
	private String animalType;
	
	@ManyToOne
	@JoinColumn(name = "zoo_id", nullable = false)
	private Zoo zoo;
	
	
	
	@Column(name = "created_by", nullable = false)
	private String createdBy;
	
	@Column(name = "updated_by", nullable = true)
	private String updatedBy;
	
	 @CreationTimestamp
	 @Column(name = "created_at", updatable = false)
	 private Instant createdAt;

	 @UpdateTimestamp
	 @Column(name = "updated_at", updatable = true)
	 private Instant updatedAt;

	

}
