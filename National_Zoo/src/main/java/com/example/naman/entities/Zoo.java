package com.example.naman.entities;

import java.time.Instant;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter

@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "zoo")
public class Zoo {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name  = "zoo_id")
	private Long zooId;
	
	@Column(name = "zoo_name", nullable = false)
	private String zooName;
	
	@OneToOne(cascade = CascadeType.ALL) 	
	@JoinColumn(name = "address_id", nullable = true)
	private Address address;
	
	
	@CreationTimestamp
	@Column(name = "created_at", updatable = false)
	private Instant createdAt;

	@UpdateTimestamp
	@Column(name = "updated_at")
	private Instant updatedAt;
	
	@Column(name = "created_by",nullable = false , updatable = false)
	private String createdBy;
	
	@Column(name = "updated_by", nullable = true, updatable = true)
	private String updatedBy;
	
	
	
	
	
	
	

}
